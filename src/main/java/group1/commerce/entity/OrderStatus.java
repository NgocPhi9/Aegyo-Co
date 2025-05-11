package group1.commerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idStatus;

    @ManyToOne
    @JoinColumn(name = "idOrder", nullable = false)
    @JsonIgnore
    private Orders order;

    @Enumerated(EnumType.STRING)
    private OrderStage status;

    private LocalDateTime statusTime = LocalDateTime.now();

}
