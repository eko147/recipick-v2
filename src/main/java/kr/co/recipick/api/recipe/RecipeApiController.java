package kr.co.recipick.api.recipe;

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
@RequestMapping("/api/recipe")
public class RecipeApiController {
    private static final Logger logger = LoggerFactory.getLogger(RecipeApiController.class);
    
    @Autowired
    private RecipeApiService recipeApiService;

    @Autowired
    private AuthService authService;
    
    @ResponseBody
    @PostMapping("/likes")
    public ResponseEntity<List<RecipeLikeResponseVO>> getLikedRecipes(@RequestBody RecipeApiRequestVO requestVO) {
        logger.info("사용자 레시피 좋아요 목록 조회 요청: " + requestVO.getMember_id());
        
        try {
            if (!authService.validateApiKey(requestVO.getApiKey())) {
                logger.warn("API 키 인증 실패: " + requestVO.getApiKey());
                return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403
            }
            
            // 레시피 좋아요 목록 조회
            List<RecipeLikeResponseVO> likedRecipes = recipeApiService.getLikedRecipes(requestVO.getMember_id());
            
            // 좋아요한 레시피가 없는 경우
            if (likedRecipes == null || likedRecipes.isEmpty()) {
                logger.warn("좋아요한 레시피 없음: " + requestVO.getMember_id());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
            }
            logger.info("좋아요 목록 조회 성공: " + requestVO.getMember_id() + ", 개수: " + likedRecipes.size());
            return new ResponseEntity<>(likedRecipes, HttpStatus.OK); // 200
        } catch (Exception e) {
            logger.error("좋아요 목록 조회 중 오류 발생", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }
    
    @ResponseBody
    @PostMapping("/steps")
    public ResponseEntity<List<RecipeStepResponseVO>> getRecipeSteps(@RequestBody RecipeStepRequestVO requestVO) {
        logger.info("레시피 스텝 조회 요청: " + requestVO.getRecipe_id());
        
        try {
            if (!authService.validateApiKey(requestVO.getApiKey())) {
                logger.warn("API 키 인증 실패: " + requestVO.getApiKey());
                return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403
            }
            
            // 레시피 스텝 목록 조회
            List<RecipeStepResponseVO> recipeSteps = recipeApiService.getRecipeSteps(requestVO.getRecipe_id());
            
            // 레시피 스텝이 없는 경우
            if (recipeSteps == null || recipeSteps.isEmpty()) {
                logger.warn("레시피 스텝 없음: " + requestVO.getRecipe_id());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
            }
            
            logger.info("레시피 스텝 조회 성공: " + requestVO.getRecipe_id() + ", 개수: " + recipeSteps.size());
            return new ResponseEntity<>(recipeSteps, HttpStatus.OK); // 200
        } catch (Exception e) {
            logger.error("레시피 스텝 조회 중 오류 발생", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }
}