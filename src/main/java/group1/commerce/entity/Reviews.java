package group1.commerce.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reviews {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idReview;

    @OneToOne(mappedBy = "review")
    private OrderItem orderItem;

    @ManyToOne
    @JoinColumn(name = "idProduct")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    private int rating;
    private String comment;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
