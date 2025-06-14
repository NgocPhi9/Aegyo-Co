package group1.commerce.entity;

import lombok.Getter;

import java.util.Set;

@Getter
public enum CancelReason {
    CHANGE_OF_MIND("Đổi ý", Set.of(Role.CUSTOMER)),
    FOUND_BETTER_PRICE("Tìm được giá tốt hơn", Set.of(Role.CUSTOMER)),
    ORDERED_BY_MISTAKE("Đặt nhầm", Set.of(Role.CUSTOMER)),
    CUSTOMER_REJECTED("Khách từ chối nhận hàng", Set.of(Role.ADMIN)),
    OUT_OF_STOCK("Hết hàng", Set.of(Role.ADMIN)),
    OTHER("Khác", Set.of(Role.CUSTOMER, Role.ADMIN)),;

    private final String label;
    private final Set<Role> allowedRoles;

    CancelReason(String label, Set<Role> role) {
        this.label = label;
        this.allowedRoles = role;
    }
}
