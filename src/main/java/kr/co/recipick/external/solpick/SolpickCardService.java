package kr.co.recipick.external.solpick;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolpickCardService {
    
    @Autowired
    private SolpickCardApiClient cardApiClient;
    
    public SolpickVerifyCardResponseDTO verifyCard(int recipickUserId, String cardNumber, String cardExpiry) {
        return cardApiClient.verifyCard(recipickUserId, cardNumber, cardExpiry);
    }
}