package vn.iotstar.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Products")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer quantity;
    @Column(name = "description")
    private String desc;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @ManyToMany
    @JoinTable(
        name = "product_category",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonIgnore
    private Set<Category> categories = new HashSet<>();
}