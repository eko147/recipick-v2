package kr.co.recipick.external.solpick;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SolpickCardApiClient {
    
    @Value("${solpick.api.base-url}")
    private String baseUrl;

    @Value("${solpick.api.key}")
    private String apiKey;
    
    private final RestTemplate restTemplate;
    
    public SolpickCardApiClient() {
        this.restTemplate = new RestTemplate();
    }
    
    public SolpickVerifyCardResponseDTO verifyCard(int recipickUserId, String cardNumber, String cardExpiry) {
        try {
            String url = baseUrl + "/api/payment/verify-card";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            log.info("전체 요청 URL: {}", url);

            SolpickVerifyCardRequestDTO requestDTO = new SolpickVerifyCardRequestDTO();
            requestDTO.setApiKey(apiKey);
            requestDTO.setRecipickUserId(recipickUserId);
            requestDTO.setCardNumber(cardNumber);
            requestDTO.setCardExpiry(cardExpiry);

            HttpEntity<SolpickVerifyCardRequestDTO> requestEntity = new HttpEntity<>(requestDTO, headers);

            log.info("요청 DTO: {}", requestDTO);
            log.info("요청 헤더: {}", headers);

            ResponseEntity<SolpickVerifyCardResponseDTO> response = restTemplate.postForEntity(
                    url,
                    requestEntity,
                    SolpickVerifyCardResponseDTO.class);

            log.info("API 응답 상태코드: {}", response.getStatusCode());
            log.info("API 응답 바디: {}", response.getBody());

            return response.getBody();
        } catch (Exception e) {
            log.error("API 호출 예외 발생", e);
            
            log.error("예외 상세: ", e);
            
            SolpickVerifyCardResponseDTO errorResponse = new SolpickVerifyCardResponseDTO();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("솔픽 카드 검증 API 호출 중 오류 발생: " + e.getMessage());
            errorResponse.setValid(false);
            
            return errorResponse;
        }
    }
}