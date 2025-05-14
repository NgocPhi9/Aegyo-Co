package group1.commerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reviews {

    @Id
    @Column(name = "idProduct")
    private String idProduct;

    @Id
    @Column(name = "idUser")
    private String idUser;

    private int rating;
    private String comment;
    private LocalDateTime createdAt = LocalDateTime.now();


    @OneToOne
    @MapsId
    @JoinColumn(name = "idProduct")
    private Product product;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idUser")
    private User user;
}
