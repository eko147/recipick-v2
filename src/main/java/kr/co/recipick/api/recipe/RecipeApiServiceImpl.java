package kr.co.recipick.api.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeApiServiceImpl implements RecipeApiService {

    @Autowired
    private RecipeApiMapper recipeApiMapper;

    @Override
    public List<RecipeLikeResponseVO> getLikedRecipes(int memberId) {
        return recipeApiMapper.getLikedRecipes(memberId);
    }
    
    @Override
    public List<RecipeStepResponseVO> getRecipeSteps(int recipeId) {
        return recipeApiMapper.getRecipeSteps(recipeId);
    }
}