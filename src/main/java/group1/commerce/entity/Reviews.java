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
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idReview;

    @ManyToOne
    @JoinColumn(name = "idProduct")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    private int rating;
    private String comment;
    private LocalDateTime createdAt;

}
