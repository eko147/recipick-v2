package kr.co.recipick.external.solpick;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolpickPointService {

    private final SolpickPointApiClient solpickPointApiClient;

    public int getUserPoints(int memberId) {
        SolpickPointResponseDTO response = solpickPointApiClient.getUserPoints(memberId);
        
        if (response != null && response.isSuccess()) {
            return response.getPoints();
        } else {
            log.warn("포인트 조회 실패: {}", response != null ? response.getMessage() : "응답 없음");
            return 0;
        }
    }
}