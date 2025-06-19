package group1.commerce.entity;

import lombok.Getter;

@Getter
public enum OrderStage {
    PLACED("Chờ xác nhận", "Đặt hàng"),
    CONFIRMED("Đã xác nhận", "Xác nhận đơn hàng"),
    SHIPPED("Đang giao", "Giao cho đơn vị vận chuyển"),
    DELIVERED("Đã giao", "Người mua nhận hàng"),
    REVIEWED("Đã đánh giá", "Người mua đánh giá"),
    CANCELLED("Đã hủy", "Hủy đơn hàng");

    private final String vietnameseLabel;
    private final String vietnameseStatus;

    OrderStage(String label, String vietnameseStatus) {
        this.vietnameseLabel = label;
        this.vietnameseStatus = vietnameseStatus;
    }

    public static OrderStage nextStage(OrderStage current) {
        return switch (current) {
            case PLACED -> CONFIRMED;
            case CONFIRMED -> SHIPPED;
            case SHIPPED -> DELIVERED;
            case DELIVERED -> REVIEWED;
            default -> null;
        };
    }
}
