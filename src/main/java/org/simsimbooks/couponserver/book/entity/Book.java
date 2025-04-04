package org.simsimbooks.couponserver.book.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "books")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Book {

    @Builder
    public Book(String title, BigDecimal salePrice) {
        this.title = title;
        this.salePrice = salePrice;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "book_id")
    private Long id;

    private String title;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

}
