package kr.co.recipick.external.solpick;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolpickVerifyCardResponseDTO {
    private boolean success;
    private String message;
    private boolean isValid;
}