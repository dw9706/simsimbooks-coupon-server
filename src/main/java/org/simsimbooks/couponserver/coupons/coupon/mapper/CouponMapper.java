package org.simsimbooks.couponserver.coupons.coupon.mapper;


import org.simsimbooks.couponserver.coupons.allcoupon.entity.AllCoupon;
import org.simsimbooks.couponserver.coupons.bookcoupon.entity.BookCoupon;
import org.simsimbooks.couponserver.coupons.categorycoupon.entity.CategoryCoupon;
import org.simsimbooks.couponserver.coupons.coupon.dto.CouponResponseDto;
import org.simsimbooks.couponserver.coupons.coupon.dto.FixCouponResponseDto;
import org.simsimbooks.couponserver.coupons.coupon.dto.RateCouponResponseDto;
import org.simsimbooks.couponserver.coupons.coupon.entity.Coupon;
import org.simsimbooks.couponserver.coupons.couponpolicy.entity.CouponPolicy;
import org.simsimbooks.couponserver.coupons.coupontype.entity.CouponTargetType;
import org.simsimbooks.couponserver.coupons.coupontype.entity.CouponType;

import java.util.Objects;

public class CouponMapper {
    public static CouponResponseDto toResponse(Coupon coupon) {
        if (Objects.isNull(coupon)) {
            throw new IllegalArgumentException("coupon is null");
        }

        CouponType couponType = coupon.getCouponType();
        CouponPolicy couponPolicy = ((org.simsimbooks.couponserver.coupons.coupontype.entity.CouponType) couponType).getCouponPolicy();
        CouponDetails couponDetails = extractCouponDetails(couponType);

        return switch (couponPolicy.getDiscountType()) {
            case FIX -> toFixCouponResponse(coupon, couponType, couponPolicy, couponDetails);
            case RATE -> toRateCouponResponse(coupon, couponType, couponPolicy, couponDetails);
        };
    }

    private static CouponDetails extractCouponDetails(CouponType couponType) {
        if (couponType instanceof BookCoupon bookCoupon) {
            return new CouponDetails(CouponTargetType.BOOK, bookCoupon.getBook().getId());
        } else if (couponType instanceof CategoryCoupon categoryCoupon) {
            return new CouponDetails(CouponTargetType.CATEGORY, categoryCoupon.getCategory().getId());
        } else if (couponType instanceof AllCoupon) {
            return new CouponDetails(CouponTargetType.ALL, null);
        } else {
            throw new IllegalArgumentException("Unknown CouponType: " + couponType.getClass().getSimpleName());
        }
    }

    private static FixCouponResponseDto toFixCouponResponse(Coupon coupon, CouponType couponType, CouponPolicy couponPolicy, CouponDetails couponDetails) {

        return FixCouponResponseDto.builder()
                .userId(coupon.getUser().getId())
                .couponId(coupon.getCouponId())
                .issueDate(coupon.getIssueDate())
                .deadline(coupon.getDeadline())
                .couponTypeName(couponType.getName())
                .couponStatus(coupon.getCouponStatus())
                .couponTargetType(couponDetails.couponTargetType())
                .couponTargetId(couponDetails.targetId())
                .discountPrice(couponPolicy.getDiscountPrice())
                .minOrderAmount(couponPolicy.getMinOrderAmount())
                .build();
    }

    private static RateCouponResponseDto toRateCouponResponse(Coupon coupon, CouponType couponType, CouponPolicy couponPolicy, CouponDetails couponDetails) {

        return RateCouponResponseDto.builder()
                .userId(coupon.getUser().getId())
                .couponId(coupon.getCouponId())
                .issueDate(coupon.getIssueDate())
                .deadline(coupon.getDeadline())
                .couponTypeName(couponType.getName())
                .couponStatus(coupon.getCouponStatus())
                .couponTargetType(couponDetails.couponTargetType())
                .couponTargetId(couponDetails.targetId())
                .discountRate(couponPolicy.getDiscountRate())
                .maxDiscountAmount(couponPolicy.getMaxDiscountAmount())
                .minOrderAmount(couponPolicy.getMinOrderAmount())
                .build();
    }


    private static class CouponDetails {
        private final CouponTargetType couponTargetType;
        private final Long targetId;

        private CouponDetails(CouponTargetType couponTargetType, Long targetId) {
            this.couponTargetType = couponTargetType;
            this.targetId = targetId;
        }
        public CouponTargetType couponTargetType() {
            return couponTargetType;
        }

        public Long targetId() {
            return targetId;
        }

    }
}
