package kr.co.recipick.api.order;

import java.util.List;

public interface OrderApiService {
    List<OrderResponseVO> getOrderHistory(int memberId);
}