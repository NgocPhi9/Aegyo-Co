package group1.commerce.dto;

import group1.commerce.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private String idUser;
    private String userName;
    private String email;
    private String phoneNumber;
    private String address;
    private Role role;
}
