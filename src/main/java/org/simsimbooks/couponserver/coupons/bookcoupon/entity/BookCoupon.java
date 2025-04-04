package org.simsimbooks.couponserver.coupons.bookcoupon.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.simsimbooks.couponserver.book.entity.Book;
import org.simsimbooks.couponserver.coupons.coupontype.entity.CouponType;


@Entity
@Table(name = "book_coupons")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DiscriminatorValue("BookCoupon")
public class BookCoupon extends CouponType {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

}
