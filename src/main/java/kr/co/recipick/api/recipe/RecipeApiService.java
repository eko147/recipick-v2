package kr.co.recipick.api.recipe;

import java.util.List;

public interface RecipeApiService {
    List<RecipeLikeResponseVO> getLikedRecipes(int memberId);
    List<RecipeStepResponseVO> getRecipeSteps(int recipeId);
}
