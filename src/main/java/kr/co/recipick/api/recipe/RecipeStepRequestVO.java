package kr.co.recipick.api.recipe;

import lombok.Data;

@Data
public class RecipeStepRequestVO {
    private int recipe_id;
    private String apiKey;
}