package kr.co.recipick.api.recipe;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecipeApiMapper {
    List<RecipeLikeResponseVO> getLikedRecipes(int memberId);
    List<RecipeStepResponseVO> getRecipeSteps(int recipeId);
}