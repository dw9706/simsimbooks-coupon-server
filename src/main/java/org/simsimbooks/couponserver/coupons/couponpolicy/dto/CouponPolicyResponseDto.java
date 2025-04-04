package org.simsimbooks.couponserver.coupons.couponpolicy.dto;

import lombok.Builder;
import lombok.Data;
import org.simsimbooks.couponserver.coupons.couponpolicy.entity.DisCountType;

import java.math.BigDecimal;

@Data
@Builder
public class CouponPolicyResponseDto {
    private Long couponPolicyId;

    private String couponPolicyName;

    private DisCountType discountType; // RATE, FIX

    private BigDecimal discountRate;

    private BigDecimal discountPrice;

    private BigDecimal maxDiscountAmount;

    private BigDecimal minOrderAMount;

    private String policyDescription;
}
