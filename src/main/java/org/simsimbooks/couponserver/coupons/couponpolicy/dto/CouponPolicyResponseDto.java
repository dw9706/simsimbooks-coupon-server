package org.simsimbooks.couponserver.coupons.couponpolicy.dto;

import lombok.Builder;
import lombok.Data;
import org.simsimbooks.couponserver.coupons.couponpolicy.entity.DiscountType;

import java.math.BigDecimal;

@Data
@Builder
public class CouponPolicyResponseDto {
    private Long id;

    private String name;

    private DiscountType discountType; // RATE, FIX

    private BigDecimal discountRate;

    private BigDecimal discountPrice;

    private BigDecimal maxDiscountAmount;

    private BigDecimal minOrderAMount;

    private String policyDescription;
}
