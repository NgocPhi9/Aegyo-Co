package group1.commerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idUser", nullable = false)
    private String idUser;
    @Column(name = "userName", nullable = false)
    private String userName;
    @Column(name = "idProvided")
    private String idProvided;
    @Column(name = "password")
    private String password;
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Column(name = "email")
    private String email;
    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;
}
