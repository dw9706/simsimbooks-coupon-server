package org.simsimbooks.couponserver.coupons.coupon.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.simsimbooks.couponserver.coupons.couponpolicy.entity.DisCountType;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
public class RateCouponResponseDto extends CouponResponseDto{

    // 할인 형태
    private final DisCountType disCountType = DisCountType.RATE;
    // 할인율
    private BigDecimal discountRate;
    // 최대 할인 금액
    private BigDecimal maxDiscountAmount;
    // 최소 주문 금액
    private BigDecimal minOrderAmount;


}
