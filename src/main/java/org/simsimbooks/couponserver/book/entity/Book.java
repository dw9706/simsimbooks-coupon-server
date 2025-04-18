package org.simsimbooks.couponserver.book.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Optional;

@Entity
@Table(name = "books")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Book {
    private Book(String title, BigDecimal salePrice) {
        this.title = title;
        this.salePrice = salePrice;
    }

    public static Book createBook(String title, BigDecimal salePrice) {
        return new Book(title, salePrice);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    private String title;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    // update method
    public void updateBookInfo(String title, BigDecimal salePrice) {
        Optional.ofNullable(title).ifPresent(t -> this.title = t);
        Optional.ofNullable(salePrice).ifPresent(p -> this.salePrice = p);
    }

}
