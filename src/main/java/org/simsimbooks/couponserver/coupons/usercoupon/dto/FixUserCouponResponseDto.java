package org.simsimbooks.couponserver.coupons.usercoupon.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.simsimbooks.couponserver.coupons.couponpolicy.entity.DiscountType;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
public class FixUserCouponResponseDto extends UserCouponResponseDto {

    // 할인 형태
    private final DiscountType discountType = DiscountType.FIX;
    // 할인액
    private BigDecimal discountPrice;
    // 최소 주문 금액
    private BigDecimal minOrderAmount;

}
