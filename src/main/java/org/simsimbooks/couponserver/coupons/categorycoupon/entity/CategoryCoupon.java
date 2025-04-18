package org.simsimbooks.couponserver.coupons.categorycoupon.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.simsimbooks.couponserver.category.entity.Category;
import org.simsimbooks.couponserver.coupons.coupon.entity.Coupon;

@Entity
@Table(name = "category_coupons")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DiscriminatorValue("CategoryCoupon")
public class CategoryCoupon extends Coupon {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
