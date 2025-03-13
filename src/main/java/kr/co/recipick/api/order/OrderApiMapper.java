package kr.co.recipick.api.order;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface OrderApiMapper {
	List<OrderResponseVO> getOrderHistory(int memberId);
}