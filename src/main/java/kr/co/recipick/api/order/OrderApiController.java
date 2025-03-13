package kr.co.recipick.api.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.recipick.api.auth.AuthService;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderApiController {
    private static final Logger logger = LoggerFactory.getLogger(OrderApiController.class);
    
    @Autowired
    private OrderApiService orderApiService;
    
    @Autowired
    private AuthService authService;
    
    @ResponseBody // key 노출 때문에 포스트
    @PostMapping("/history")
    public ResponseEntity<List<OrderResponseVO>> getOrderHistory(@RequestBody OrderRequestVO requestVO) {
        logger.info("주문 내역 조회 요청: " + requestVO.getMember_id());
        
        try {
            // API 키 검증
            if (!authService.validateApiKey(requestVO.getApiKey())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403
            }
            
            // 주문 내역 조회
            List<OrderResponseVO> orderHistory = orderApiService.getOrderHistory(requestVO.getMember_id());
            
            // 주문 내역 없으면 NO_CONTENT 반환
            if (orderHistory == null || orderHistory.isEmpty()) {
                logger.warn("주문 내역 없음: " + requestVO.getMember_id());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
            }
            
            // 주문 내역 있으면 데이터 반환
            logger.info("주문 내역 조회 성공: " + requestVO.getMember_id());
            return new ResponseEntity<>(orderHistory, HttpStatus.OK); // 200
        } catch (Exception e) {
            logger.error("주문 내역 조회 중 오류 발생", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }
}