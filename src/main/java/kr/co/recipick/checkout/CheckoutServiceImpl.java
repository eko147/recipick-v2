package kr.co.recipick.checkout;

import kr.co.recipick.cart.CartVO;
import kr.co.recipick.cart.CartMapper;
import kr.co.recipick.cart.CartService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

@Service
public class CheckoutServiceImpl implements CheckoutService {

	@Value("${imp.key}")
	private String IMP_KEY;

	@Value("${imp.secret}")
	private String IMP_SECRET;

	@Autowired
	private CheckoutMapper checkoutMapper;

	@Autowired
	private CartMapper cartMapper;

	@Autowired
	private CartService cartService;

//	@Override
//	public int createOrder(OrderVO orderHistory, int check) {
//		// 장바구니 데이터를 가져오는 로직 (필요 시 활용)
//		List<CartVO> cartItems = cartService.getCartItems(orderHistory.getMemberId());
//
//		if (cartItems == null || cartItems.isEmpty()) {
//			throw new IllegalArgumentException("장바구니가 비어 있습니다.");
//		}
//		
//		int lastOrderId = 0;
//
//		for (CartVO cartItem : cartItems) {
//
//			if (cartItem.getCategory() == 1) {
//				orderHistory.setCategory("r");
//				orderHistory.setTitle(cartItem.getRcp_title());
//
//			} else {
//				orderHistory.setCategory("i");
//				orderHistory.setTitle(cartItem.getIng_name());
//			}
//			orderHistory.setQty(cartItem.getQty());
//			orderHistory.setRecipeId(cartItem.getRecipe_id());
//			orderHistory.setIngId(cartItem.getIng_id());
//			// 프라이
//			orderHistory.setAddress(orderHistory.getAddress()); // 서버에서 가져온 주소
//
//			checkoutMapper.insertOrderHistory(orderHistory);
//			
//			lastOrderId = orderHistory.getOh_id();
//			
//
//			System.out.println();
//			System.out.println(orderHistory);
//			System.out.println();
//
//		}
//
//	if (check == 1) {
//		cartMapper.deleteCartByMemberId(orderHistory.getMemberId());
//	}
//	
//	return lastOrderId;
//	
//	}
	
	
	
//	@Override
//	public int createOrder(OrderVO orderHistory, int check) {
//	    List<CartVO> cartItems = cartService.getCartItems(orderHistory.getMemberId());
//
//	    if (cartItems == null || cartItems.isEmpty()) {
//	        throw new IllegalArgumentException("장바구니가 비어 있습니다.");
//	    }
//	    
//	    int lastOrderId = 0;
//
//	    for (CartVO cartItem : cartItems) {
//	        // 첫 번째 아이템일 때만 주문 생성
//	        if (cartItem == cartItems.get(0)) {
//	            if (cartItem.getCategory() == 1) {
//	                orderHistory.setCategory("r");
//	                orderHistory.setTitle(cartItem.getRcp_title());
//	            } else {
//	                orderHistory.setCategory("i");
//	                orderHistory.setTitle(cartItem.getIng_name());
//	            }
//	            orderHistory.setQty(cartItem.getQty());
//	            orderHistory.setRecipeId(cartItem.getRecipe_id());
//	            orderHistory.setIngId(cartItem.getIng_id());
//
//	            // 주문 생성
//	            checkoutMapper.insertOrderHistory(orderHistory);
//	            
//	            // 생성된 주문 ID 저장
//	            lastOrderId = orderHistory.getOh_id();
//	            
//	            System.out.println("첫 번째 주문 생성 - OrderVO: " + orderHistory);
//	            System.out.println("생성된 주문 ID: " + lastOrderId);
//	        }
//	    }
//
//	    // 장바구니 삭제 처리
//	    if (check == 1) {
//	        cartMapper.deleteCartByMemberId(orderHistory.getMemberId());
//	    }
//	    
//	    return lastOrderId;
//	}
	
	
	@Override
    public int createOrder(OrderVO orderHistory, int check) {
        List<CartVO> cartItems = cartService.getCartItems(orderHistory.getMemberId());

        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어 있습니다.");
        }
        
        int lastOrderId = 0;

        for (CartVO cartItem : cartItems) {
            // 각 카트 아이템마다 주문 생성
            OrderVO itemOrder = new OrderVO();
            
            // 기존 주문 정보 복사
            itemOrder.setMemberId(orderHistory.getMemberId());
            itemOrder.setOrderState(orderHistory.getOrderState());
            itemOrder.setDeliveryStatus(orderHistory.getDeliveryStatus());
            itemOrder.setDeliveryDate(orderHistory.getDeliveryDate());
            itemOrder.setOrderDate(orderHistory.getOrderDate());
            itemOrder.setPaymentMethod(orderHistory.getPaymentMethod());
            itemOrder.setAddress(orderHistory.getAddress());
            itemOrder.setDiscount(orderHistory.getDiscount());
            
            // 카트 아이템 정보 설정
            if (cartItem.getCategory() == 1) {
                itemOrder.setCategory("r");
                itemOrder.setTitle(cartItem.getRcp_title());
                itemOrder.setRecipeId(cartItem.getRecipe_id());
                itemOrder.setIngId(0); // 레시피는 식재료 ID 없음
            } else {
                itemOrder.setCategory("i");
                itemOrder.setTitle(cartItem.getIng_name());
                itemOrder.setRecipeId(0); // 식재료는 레시피 ID 없음
                itemOrder.setIngId(cartItem.getIng_id());
            }
            
            itemOrder.setQty(cartItem.getQty());
            
            // 가격 설정 (필요하다면)
            if (cartItem.getCategory() == 1) {
                itemOrder.setPrice(cartItem.getRcp_discount_price() * cartItem.getQty());
            } else {
                itemOrder.setPrice(cartItem.getIng_discount_price() * cartItem.getQty());
            }
            
            // 주문 생성
            checkoutMapper.insertOrderHistory(itemOrder);
            
            // 마지막 주문 ID 저장
            lastOrderId = itemOrder.getOh_id();
        }

        // 장바구니 삭제 처리
        if (check == 1) {
            cartMapper.deleteCartByMemberId(orderHistory.getMemberId());
        }
        
        return lastOrderId;
    }
	

	@Override
	public OrdererVO getOrdererInfo(int memberId) {
		return checkoutMapper.getOrdererInfo(memberId);
	}

	@Override
	public boolean verifyPayment(String impUid, int requestedAmount) {
		try {

			String accessToken = getAccessToken();

			// 아임포트 API 호출
			RestTemplate restTemplate = new RestTemplate();
			String url = "https://api.iamport.kr/payments/" + impUid;

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", accessToken);
			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
			Map<String, Object> body = (Map<String, Object>) response.getBody().get("response");

			int paidAmount = (int) body.get("amount");
			return paidAmount == requestedAmount;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("결제 검증 중 오류 발생");
		}
	}

	private String getAccessToken() {

		try {
			RestTemplate restTemplate = new RestTemplate();

			// 헤더 설정
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			// JSON 요청 데이터
			Map<String, String> request = new HashMap<>();
			request.put("imp_key", IMP_KEY); // API Key
			request.put("imp_secret", IMP_SECRET); // API Secret

			HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

			// 아임포트 API 호출
			ResponseEntity<Map> response = restTemplate.postForEntity("https://api.iamport.kr/users/getToken", entity,
					Map.class);

			// 응답 처리
			Map<String, Object> responseBody = (Map<String, Object>) response.getBody().get("response");
			return (String) responseBody.get("access_token");
		} catch (Exception e) {
			System.out.println("발급실패");
			System.out.println();
			e.printStackTrace();
			throw new RuntimeException("Access Token 발급 실패: " + e.getMessage());
		}
	}
}
