package gomdoliro.gomdol.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gomdoliro.gomdol.controller.dto.SummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OpenAiService {


    @Value("${GPT_KEY}")
    private String openAiApiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SummaryResponse getSummary(String content,String title) {
        String prompt = String.format("다음 게시글을 요약해 주세요 하지만 4줄 이하로 요약하는데 구분하지 말고 마지막에 끊기지 않게 자연스럽게 요약해 주세요: %s 제목은 %s", content,title);
        prompt = prompt.replace("\n"," ").replace("\r"," ").replace("\n\r"," ").replace("\t"," ");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = String.format(
                "{\"model\": \"gpt-4\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}], \"max_tokens\": 200}",
                prompt);

        HttpEntity<String> entity = new HttpEntity<>(requestBody,headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://api.openai.com/v1/chat/completions",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode node = objectMapper.readTree(response.getBody());
                String summary = node.get("choices").get(0).get("message").get("content").asText();
                return new SummaryResponse(summary);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "GPT API로부터 예상하지 못한 응답");
            }
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "응답이 Json이 아닙니다.",e);
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,"GPT API를 호출하는 중 HTTP 오류 발생",e);
        }
        catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"GPT API 연결 불가",e);
        }
    }
}
