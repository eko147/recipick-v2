package kr.co.recipick.external.solpick;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolpickVerifyCardRequestDTO {
    private String apiKey;
    private int recipickUserId;
    private String cardNumber;
    private String cardExpiry;
    
}