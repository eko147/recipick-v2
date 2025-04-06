package kr.co.recipick.api.refrigerator;

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
@RequestMapping("/api/ingredient")
public class IngredientApiController{
    private static final Logger logger = LoggerFactory.getLogger(IngredientApiController.class);
    
    @Autowired
    private IngredientApiService ingredientApiService;

    @Autowired
    private AuthService authService;
    
    @ResponseBody
    @PostMapping("/list")
    public ResponseEntity<List<IngredientResponseVO>> getIngredients(@RequestBody IngredientRequestVO requestVO) {
        logger.info("구매 재료 조회 요청: " + requestVO.getMember_id());
        
        try {
            // API 키 검증
            if (!authService.validateApiKey(requestVO.getApiKey())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403
            }
            
            // 구매 재료 조회
            List<IngredientResponseVO> ingredients = ingredientApiService.getIngredients(requestVO.getMember_id());
            
            // 구매 재료 없으면 NO_CONTENT 반환
            if (ingredients == null || ingredients.isEmpty()) {
                logger.warn("구매 재료 없음: " + requestVO.getMember_id());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
            }
            
            // 구매 재료 있으면 데이터 반환
            logger.info("구매 재료 조회 성공: " + requestVO.getMember_id());
            return new ResponseEntity<>(ingredients, HttpStatus.OK); // 200
        } catch (Exception e) {
            logger.error("구매 재료 조회 중 오류 발생: " + e.getMessage(), e);
            e.printStackTrace();
//            logger.error("구매 재료 조회 중 오류 발생", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }
}