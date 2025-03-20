package kr.co.recipick.external.solpick;

import lombok.Data;

@Data
public class SolpickPointUpdateDTO {
    private String apiKey;
    private int memberId;
    private int orderId;
    private int pointsUsed;
    private int totalPrice;
}