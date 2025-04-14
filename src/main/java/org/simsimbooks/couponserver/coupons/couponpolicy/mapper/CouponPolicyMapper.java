package org.simsimbooks.couponserver.coupons.couponpolicy.mapper;


import org.simsimbooks.couponserver.coupons.couponpolicy.dto.CouponPolicyRequestDto;
import org.simsimbooks.couponserver.coupons.couponpolicy.dto.CouponPolicyResponseDto;
import org.simsimbooks.couponserver.coupons.couponpolicy.entity.CouponPolicy;

public class CouponPolicyMapper {
    public static CouponPolicyResponseDto toResponse(CouponPolicy couponPolicy) {
        return CouponPolicyResponseDto.builder()
                .id(couponPolicy.getId())
                .name(couponPolicy.getName())
                .discountType(couponPolicy.getDiscountType())
                .discountRate(couponPolicy.getDiscountRate())
                .discountPrice(couponPolicy.getDiscountPrice())
                .maxDiscountAmount(couponPolicy.getMaxDiscountAmount())
                .minOrderAMount(couponPolicy.getMinOrderAmount())
                .policyDescription(couponPolicy.getPolicyDescription())
                .build();
    }

    public static CouponPolicy toCouponPolicy(CouponPolicyRequestDto requestDto) {
        return CouponPolicy.builder()
                .name(requestDto.getCouponPolicyName())
                .discountType(requestDto.getDiscountType())
                .discountPrice(requestDto.getDiscountPrice())
                .discountRate(requestDto.getDiscountRate())
                .maxDiscountAmount(requestDto.getMaxDiscountAmount())
                .minOrderAmount(requestDto.getMinOrderAmount())
                .policyDescription(requestDto.getPolicyDescription())
                .build();
    }
}
