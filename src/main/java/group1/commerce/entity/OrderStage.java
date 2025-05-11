package group1.commerce.entity;

import lombok.Getter;

@Getter
public enum OrderStage {
    PLACED("Đã đặt hàng"),
    CONFIRMED("Đã xác nhận"),
    SHIPPED("Đang giao"),
    DELIVERED("Đã giao"),
    REVIEWED("Đã đánh giá"),
    CANCELLED("Đã hủy");

    private final String vietnameseLabel;

    OrderStage(String label) {
        this.vietnameseLabel = label;
    }
}
