package org.simsimbooks.couponserver.coupons.allcoupon.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.simsimbooks.couponserver.coupons.coupon.entity.Coupon;

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "all_coupons")
@DiscriminatorValue("AllCoupon")
public class AllCoupon extends Coupon {
}
