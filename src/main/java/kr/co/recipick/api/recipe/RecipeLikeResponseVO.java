package kr.co.recipick.api.recipe;

import lombok.Data;

@Data
public class RecipeLikeResponseVO {
    private int recipe_id;
    private String recipe_name;
    private String thumbnail_url;
}