package kr.co.recipick.api.recipe;

import lombok.Data;

@Data
public class RecipeStepResponseVO {
    private int step_id;
    private int sort;
    private String description;
    private String img_url;
    private int time;
}