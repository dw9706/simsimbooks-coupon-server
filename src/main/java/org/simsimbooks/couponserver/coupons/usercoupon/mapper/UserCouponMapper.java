package org.simsimbooks.couponserver.coupons.usercoupon.mapper;


import org.simsimbooks.couponserver.coupons.allcoupon.entity.AllCoupon;
import org.simsimbooks.couponserver.coupons.bookcoupon.entity.BookCoupon;
import org.simsimbooks.couponserver.coupons.categorycoupon.entity.CategoryCoupon;
import org.simsimbooks.couponserver.coupons.coupon.entity.Coupon;
import org.simsimbooks.couponserver.coupons.usercoupon.dto.UserCouponResponseDto;
import org.simsimbooks.couponserver.coupons.usercoupon.dto.FixUserCouponResponseDto;
import org.simsimbooks.couponserver.coupons.usercoupon.dto.RateUserCouponResponseDto;
import org.simsimbooks.couponserver.coupons.couponpolicy.entity.CouponPolicy;
import org.simsimbooks.couponserver.coupons.coupon.entity.CouponType;
import org.simsimbooks.couponserver.coupons.usercoupon.entity.UserCoupon;

import java.util.Objects;

public class UserCouponMapper {
    public static UserCouponResponseDto toResponse(UserCoupon userCoupon) {
        if (Objects.isNull(userCoupon)) {
            throw new IllegalArgumentException("userCoupon is null");
        }

        Coupon coupon = userCoupon.getCoupon();
        CouponPolicy couponPolicy = ((Coupon) coupon).getCouponPolicy();
        CouponDetails couponDetails = extractCouponDetails(coupon);

        return switch (couponPolicy.getDiscountType()) {
            case FIX -> toFixCouponResponse(userCoupon, coupon, couponPolicy, couponDetails);
            case RATE -> toRateCouponResponse(userCoupon, coupon, couponPolicy, couponDetails);
        };
    }

    private static CouponDetails extractCouponDetails(Coupon coupon) {
        if (coupon instanceof BookCoupon bookCoupon) {
            return new CouponDetails(CouponType.BOOK, bookCoupon.getBook().getId());
        } else if (coupon instanceof CategoryCoupon categoryCoupon) {
            return new CouponDetails(CouponType.CATEGORY, categoryCoupon.getCategory().getId());
        } else if (coupon instanceof AllCoupon) {
            return new CouponDetails(CouponType.ALL, null);
        } else {
            throw new IllegalArgumentException("Unknown Coupon: " + coupon.getClass().getSimpleName());
        }
    }

    private static FixUserCouponResponseDto toFixCouponResponse(UserCoupon userCoupon, Coupon coupon, CouponPolicy couponPolicy, CouponDetails couponDetails) {

        return FixUserCouponResponseDto.builder()
                .userId(userCoupon.getUser().getId())
                .id(userCoupon.getId())
                .issueDate(userCoupon.getIssueDate())
                .deadline(userCoupon.getDeadline())
                .couponTypeName(coupon.getName())
                .userCouponStatus(userCoupon.getUserCouponStatus())
                .couponType(couponDetails.couponTargetType())
                .couponTargetId(couponDetails.targetId())
                .discountPrice(couponPolicy.getDiscountPrice())
                .minOrderAmount(couponPolicy.getMinOrderAmount())
                .build();
    }

    private static RateUserCouponResponseDto toRateCouponResponse(UserCoupon userCoupon, Coupon coupon, CouponPolicy couponPolicy, CouponDetails couponDetails) {

        return RateUserCouponResponseDto.builder()
                .userId(userCoupon.getUser().getId())
                .id(userCoupon.getId())
                .issueDate(userCoupon.getIssueDate())
                .deadline(userCoupon.getDeadline())
                .couponTypeName(coupon.getName())
                .userCouponStatus(userCoupon.getUserCouponStatus())
                .couponType(couponDetails.couponTargetType())
                .couponTargetId(couponDetails.targetId())
                .discountRate(couponPolicy.getDiscountRate())
                .maxDiscountAmount(couponPolicy.getMaxDiscountAmount())
                .minOrderAmount(couponPolicy.getMinOrderAmount())
                .build();
    }


    private static class CouponDetails {
        private final CouponType couponType;
        private final Long targetId;

        private CouponDetails(CouponType couponType, Long targetId) {
            this.couponType = couponType;
            this.targetId = targetId;
        }
        public CouponType couponTargetType() {
            return couponType;
        }

        public Long targetId() {
            return targetId;
        }

    }
}
