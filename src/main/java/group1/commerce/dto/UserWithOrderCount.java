package group1.commerce.dto;

public interface UserWithOrderCount {
    String getIdUser();
    String getUserName();
    String getEmail();
    String getPhoneNumber();
    String getAddress();
    Boolean getPurchaseRestricted();
    Integer getBadCancelCount();
    Long getOrderCount();
    Long getTotalSpending();
}
