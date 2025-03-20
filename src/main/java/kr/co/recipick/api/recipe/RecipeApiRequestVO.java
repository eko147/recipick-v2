package kr.co.recipick.api.recipe;

import lombok.Data;

@Data
public class RecipeApiRequestVO {
	private int member_id;
    private String apiKey;
}
