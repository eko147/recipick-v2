package kr.co.recipick.api.refrigerator;

import lombok.Data;

@Data
public class IngredientResponseVO {
    private int userId;
    private int ohId;
    private String name;
    private String emoji;
    private String image;
    private int quantity;
    private String expiryDate;
    private String mainCategory;
    private String subCategory;
    private String detailCategory;
}