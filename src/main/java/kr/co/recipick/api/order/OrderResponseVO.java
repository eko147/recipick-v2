package kr.co.recipick.api.order;


import lombok.Data;

@Data
public class OrderResponseVO {
    private String orderDateTime; 
    private String itemName;             
    private double price;                
}