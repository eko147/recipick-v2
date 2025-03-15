package kr.co.recipick.external.solpick;

import lombok.Data;

@Data
public class SolpickRequestDTO {
    private int memberId;
    private String apiKey;
}