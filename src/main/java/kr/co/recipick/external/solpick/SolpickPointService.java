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
    
    public boolean updatePointsUsage(int memberId, int orderId, int pointsUsed, int totalPrice) {
        try {
            // 솔픽 API 호출을 위한 DTO 생성
            SolpickPointUpdateDTO updateDTO = new SolpickPointUpdateDTO();
            updateDTO.setMemberId(memberId);
            updateDTO.setOrderId(orderId);
            updateDTO.setPointsUsed(pointsUsed);
            updateDTO.setTotalPrice(totalPrice);
            
            // API 호출
            SolpickPointResponseDTO response = solpickPointApiClient.updatePoints(updateDTO);
            
            return response != null && response.isSuccess();
        } catch (Exception e) {
            log.error("포인트 업데이트 오류", e);
            return false;
        }
    }
}