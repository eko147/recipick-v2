package kr.co.recipick.api.order;

import lombok.Data;

@Data
public class OrderRequestVO {
	private int member_id;
    private String apiKey;
}
