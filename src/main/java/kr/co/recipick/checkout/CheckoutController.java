package kr.co.recipick.checkout;

import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.recipick.cart.CartService;
import kr.co.recipick.cart.CartVO;
import kr.co.recipick.external.solpick.SolpickCardService;
import kr.co.recipick.external.solpick.SolpickPointService;
import kr.co.recipick.external.solpick.SolpickVerifyCardResponseDTO;
import kr.co.recipick.member.MemberVO;

@Controller
public class CheckoutController {

	@Autowired
	private CartService cartService;

	@Autowired
	private CheckoutService checkoutService;
	
	@Autowired
	private SolpickCardService solpickCardService;

	@GetMapping("/shop-checkout")
	public String shopCheckout(Model model, HttpSession session) {

		MemberVO login = (MemberVO) session.getAttribute("login");
		if (login == null) {
			return "redirect:/signin.do";
		}
		Integer memberId = login.getMember_id();

//    	model.addAttribute("isRecipePage", true);  // 레시피 페이지일 때만 true

		List<CartVO> cartItems = cartService.getCartItems(memberId);
		model.addAttribute("cartItems", cartItems);

		@SuppressWarnings("unchecked")
		Map<String, String> checkoutData = (Map<String, String>) session.getAttribute("checkoutData");
		OrdererVO orderer = checkoutService.getOrdererInfo(memberId); // 멤버아이디 주석
		model.addAttribute("orderer", orderer);

		model.addAttribute("totalPrice", checkoutData.get("totalPayablePrice"));
		model.addAttribute("totalDiscountPrice", checkoutData.get("totalDiscountPrice"));
		model.addAttribute("totalOriginalPrice", checkoutData.get("totalOriginalPrice"));

		return "checkout/shop-checkout";
	}

	@PostMapping("/shop-checkout/session-data")
	@ResponseBody
	public Map<String, Object> saveSessionData(@RequestBody Map<String, String> orderData, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		try {
			session.setAttribute("checkoutData", orderData);
			response.put("success", true);
			return response;
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "세션 데이터 저장 실패");
			return response;
		}
	}

	@GetMapping("/checkout/payment-success")
	public String paymentSuccess(@RequestParam String merchantUid, Model model) {
		model.addAttribute("merchantUid", merchantUid);
		return "checkout/payment-success";
	}

	@GetMapping("/checkout/payment-failed")
	public String paymentFailed(@RequestParam String merchantUid, Model model) {
		model.addAttribute("merchantUid", merchantUid);
		return "checkout/payment-failed";
	}

//	@PostMapping("/checkout/success")
//	@ResponseBody
//	public Map<String, Object> handlePaymentSuccess(@RequestBody Map<String, Object> successData, HttpSession session) {
//
//		Map<String, Object> response = new HashMap<>();
//
//        MemberVO login = (MemberVO) session.getAttribute("login");
//        Integer memberId = login.getMember_id();
//
//		try {
//
//			int totalPrice = (int) successData.get("totalPrice");
//			String deliveryDate = (String) successData.get("deliveryDate");
//			String orderDate = (String) successData.get("orderDate");
//			String paymentMethod = (String) successData.get("paymentMethod");
//			String orderStatus = (String) successData.get("orderStatus");
////            String impUid = (String) successData.get("impUid");
//			String address = (String) successData.get("address");
//
//			// 디테일 안씀??
//			OrderVO orderHistory = new OrderVO();
//			List<CartVO> cartItems = cartService.getCartItems(memberId);
////            
//			orderHistory.setMemberId(memberId);
//			orderHistory.setOrderState(orderStatus);
//			orderHistory.setDeliveryStatus("배송준비");
//			orderHistory.setDeliveryDate(deliveryDate);
//			orderHistory.setOrderDate(orderDate);
//			orderHistory.setPaymentMethod(paymentMethod);
//			orderHistory.setAddress(address);
//
//			System.out.println("Member ID: " + orderHistory.getMemberId());
//			System.out.println("Order State: " + orderHistory.getOrderState());
//			System.out.println("Delivery Status: " + orderHistory.getDeliveryStatus());
//			System.out.println("Payment Method: " + orderHistory.getPaymentMethod());
//			System.out.println("Delivery Date: " + orderHistory.getDeliveryDate());
//			System.out.println("Order Date: " + orderHistory.getOrderDate());
//			System.out.println("Address: " + orderHistory.getAddress());
//
//			int orderId = checkoutService.createOrder(orderHistory, 1);
//
//			response.put("success", true);
//			response.put("message", "결제 성공 데이터가 처리되었습니다.");
//			response.put("orderId", orderId);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			response.put("success", false);
//			response.put("message", "결제 성공 데이터 처리 중 오류가 발생했습니다.");
//		}
//		return response;
//	}
	
	
	@PostMapping("/checkout/success")
	@ResponseBody
	public Map<String, Object> handlePaymentSuccess(@RequestBody Map<String, Object> successData, HttpSession session) {
	    Map<String, Object> response = new HashMap<>();

	    MemberVO login = (MemberVO) session.getAttribute("login");
	    Integer memberId = login.getMember_id();

	    try {
	        int totalPrice = (int) successData.get("totalPrice");
	        String deliveryDate = (String) successData.get("deliveryDate");
	        String orderDate = (String) successData.get("orderDate");
	        String paymentMethod = (String) successData.get("paymentMethod");
	        String orderStatus = (String) successData.get("orderStatus");
	        String address = (String) successData.get("address");

	        OrderVO orderHistory = new OrderVO();
	        List<CartVO> cartItems = cartService.getCartItems(memberId);

	        orderHistory.setMemberId(memberId);
	        orderHistory.setOrderState(orderStatus);
	        orderHistory.setDeliveryStatus("배송준비");
	        orderHistory.setDeliveryDate(deliveryDate);
	        orderHistory.setOrderDate(orderDate);
	        orderHistory.setPaymentMethod(paymentMethod);
	        orderHistory.setAddress(address);

	        // 디버그 로그 추가
	        System.out.println("주문 생성 전 OrderVO: " + orderHistory);

	        int orderId = checkoutService.createOrder(orderHistory, 1);

	        // 디버그 로그 추가
	        System.out.println("생성된 주문 ID: " + orderId);

	        response.put("success", true);
	        response.put("message", "결제 성공 데이터가 처리되었습니다.");
	        response.put("orderId", orderId);  // 주문 ID 명시적으로 설정
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("success", false);
	        response.put("message", "결제 성공 데이터 처리 중 오류가 발생했습니다.");
	    }
	    return response;
	}
	

	@PostMapping("/checkout/failure")
	@ResponseBody
	public Map<String, Object> handlePaymentFailure(@RequestBody Map<String, Object> data, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		
		MemberVO login = (MemberVO) session.getAttribute("login");
        Integer memberId = login.getMember_id();
		

		try {
			int totalPrice = (int) data.get("totalPrice");
			String deliveryDate = (String) data.get("deliveryDate");
			String orderDate = (String) data.get("orderDate");
			String paymentMethod = (String) data.get("paymentMethod");
			String orderStatus = (String) data.get("orderStatus");
			String address = (String) data.get("address");

			OrderVO orderHistory = new OrderVO();
			List<CartVO> cartItems = cartService.getCartItems(memberId);

			orderHistory.setMemberId(memberId);
			orderHistory.setOrderState(orderStatus);
			orderHistory.setDeliveryStatus("");
			orderHistory.setPaymentMethod("");
			orderHistory.setDeliveryDate("0000-00-00");
			orderHistory.setOrderDate(orderDate);
			orderHistory.setAddress(address);

			checkoutService.createOrder(orderHistory, 0);

			response.put("success", true);
			response.put("message", "결제 실패 데이터가 처리되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "결제 실패 데이터 처리 중 오류가 발생했습니다.");
		}
		return response;
	}

	@PostMapping("/checkout/verify-card")
	@ResponseBody
	public Map<String, Object> verifyCard(@RequestBody Map<String, Object> request, HttpSession session) {
	    Map<String, Object> response = new HashMap<>();
	    
	    
	    System.out.println("=====================================");
	    System.out.println("카드 검증 요청 수신: " + request);
	    System.out.println("=====================================");

	    
	    try {
	        MemberVO login = (MemberVO) session.getAttribute("login");
	        if (login == null) {
	            response.put("success", false);
	            response.put("message", "로그인이 필요합니다.");
	            response.put("isValid", false);
	            return response;
	        }
	        
	        int memberId = login.getMember_id();
	        String cardNumber = (String) request.get("cardNumber");
	        String cardExpiry = (String) request.get("cardExpiry");
	        
	        // 솔픽 카드 검증 서비스 호출 (포인트 사용량 제외)
	        SolpickVerifyCardResponseDTO result = solpickCardService.verifyCard(
	            memberId, cardNumber, cardExpiry
	        );
	        
	        // 솔픽 API 응답을 프론트엔드용 응답으로 변환 (기본 정보만)
	        response.put("success", result.isSuccess());
	        response.put("message", result.getMessage());
	        response.put("isValid", result.isValid());
	        
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "서버 오류: " + e.getMessage());
	        response.put("isValid", false);
	    }
	    
	    return response;
	}
	
	/*point 조회*/
	@Autowired
	private SolpickPointService solpickPointService;

	// 포인트 조회 API 엔드포인트
	@PostMapping("/checkout/get-points")
	@ResponseBody
	public Map<String, Object> getPoints(HttpSession session) {
	    Map<String, Object> response = new HashMap<>();
	    
	    try {
	        MemberVO login = (MemberVO) session.getAttribute("login");
	        if (login == null) {
	            response.put("success", false);
	            response.put("message", "로그인이 필요합니다.");
	            return response;
	        }
	        
	        int memberId = login.getMember_id();
	        
	        int points = solpickPointService.getUserPoints(memberId);
	        
	        response.put("success", true);
	        response.put("points", points);
	        
	        return response;
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "포인트 조회 중 오류 발생: " + e.getMessage());
	        return response;
	    }
	}
	
	
	@PostMapping("/checkout/update-points")
	@ResponseBody
	public Map<String, Object> updatePoints(@RequestBody Map<String, Object> request, HttpSession session) {
	    Map<String, Object> response = new HashMap<>();
	    
	    System.out.println("=====================================");
	    System.out.println("포인트 업데이트 요청 상세 정보:");
	    System.out.println("요청 전체 데이터: " + request);
	    System.out.println("주문 ID 타입: " + (request.get("orderId") != null ? request.get("orderId").getClass() : "null"));
	    System.out.println("주문 ID: " + request.get("orderId"));
	    System.out.println("포인트 사용량: " + request.get("pointsUsed"));
	    System.out.println("총 가격: " + request.get("totalPrice"));
	    System.out.println("=====================================");
	    	    
	    try {
	        MemberVO login = (MemberVO) session.getAttribute("login");
	        if (login == null) {
	            response.put("success", false);
	            response.put("message", "로그인이 필요합니다.");
	            return response;
	        }
	        
	        int memberId = login.getMember_id();
	        int orderId = request.get("orderId") != null ? Integer.parseInt(request.get("orderId").toString()) : 0;
	        int pointsUsed = request.get("pointsUsed") != null ? Integer.parseInt(request.get("pointsUsed").toString()) : 0;
	        int totalPrice = request.get("totalPrice") != null ? Integer.parseInt(request.get("totalPrice").toString()) : 0;
	        
	        
	        if (orderId <= 0) {
	            response.put("success", false);
	            response.put("message", "유효하지 않은 주문 ID입니다.");
	            return response;
	        }
	        
	        if (pointsUsed <= 0) {
	            response.put("success", true);
	            response.put("message", "포인트 사용이 없습니다.");
	            return response;
	        }
	        
	        // 솔픽 포인트 API 호출
	        boolean result = solpickPointService.updatePointsUsage(memberId, orderId, pointsUsed, totalPrice);
	        
	        response.put("success", result);
	        response.put("message", result ? "포인트 업데이트 성공" : "포인트 업데이트 실패");
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("success", false);
	        response.put("message", "포인트 업데이트 중 오류가 발생했습니다: " + e.getMessage());
	    }
	    
	    return response;
	}
	
	

}