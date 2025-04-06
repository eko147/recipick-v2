package kr.co.recipick.api.refrigerator;

import java.util.List;

public interface IngredientApiService {
    List<IngredientResponseVO> getIngredients(int memberId);
}