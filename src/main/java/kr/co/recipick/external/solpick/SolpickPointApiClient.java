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
public class SolpickPointApiClient {
	
    @Value("${solpick.api.base-url}")
    private String baseUrl;

    @Value("${solpick.api.key}")
    private String apiKey;
    
    private final RestTemplate restTemplate;
    
    public SolpickPointApiClient() {
        this.restTemplate = new RestTemplate();
    }
    
	public SolpickPointResponseDTO getUserPoints(int memberId) {
	    try {
	        String url = baseUrl + "/api/points";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        // 추가 디버깅용 로깅
	        log.info("전체 요청 URL: {}", url);

	        SolpickRequestDTO requestDTO = new SolpickRequestDTO();
	        requestDTO.setMemberId(memberId);
	        requestDTO.setApiKey(apiKey);

	        HttpEntity<SolpickRequestDTO> requestEntity = new HttpEntity<>(requestDTO, headers);

	        log.info("요청 DTO: {}", requestDTO);
	        log.info("요청 헤더: {}", headers);

	        ResponseEntity<SolpickPointResponseDTO> response = restTemplate.postForEntity(
	                url,
	                requestEntity,
	                SolpickPointResponseDTO.class);

	        log.info("API 응답 상태코드: {}", response.getStatusCode());
	        log.info("API 응답 바디: {}", response.getBody());

	        return response.getBody();
	    } catch (Exception e) {
	        log.error("API 호출 예외 발생", e);
	        
	        // 예외 상세 로깅
	        log.error("예외 상세: ", e);
	        
	        SolpickPointResponseDTO errorResponse = new SolpickPointResponseDTO();
	        errorResponse.setSuccess(false);
	        errorResponse.setMessage("솔픽 API 호출 중 오류 발생: " + e.getMessage());
	        errorResponse.setPoints(0);
	        
	        return errorResponse;
	    }
	}
	
	
}