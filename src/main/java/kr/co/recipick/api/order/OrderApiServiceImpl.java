package kr.co.recipick.api.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderApiServiceImpl implements OrderApiService {

    @Autowired
    private OrderApiMapper orderApiMapper;

	@Override
	public List<OrderResponseVO> getOrderHistory(int memberId) {
		return orderApiMapper.getOrderHistory(memberId);
	}

}