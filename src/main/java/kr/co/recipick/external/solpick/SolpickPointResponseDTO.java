package kr.co.recipick.external.solpick;

import lombok.Data;

@Data
public class SolpickPointResponseDTO {
    private int points;
    private boolean success;
    private String message;
}