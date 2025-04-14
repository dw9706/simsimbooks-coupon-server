package org.simsimbooks.couponserver.coupons.usercoupon.mapper;


import org.simsimbooks.couponserver.coupons.allcoupon.entity.AllCoupon;
import org.simsimbooks.couponserver.coupons.bookcoupon.entity.BookCoupon;
import org.simsimbooks.couponserver.coupons.categorycoupon.entity.CategoryCoupon;
import org.simsimbooks.couponserver.coupons.usercoupon.dto.UserCouponResponseDto;
import org.simsimbooks.couponserver.coupons.usercoupon.dto.FixUserCouponResponseDto;
import org.simsimbooks.couponserver.coupons.usercoupon.dto.RateUserCouponResponseDto;
import org.simsimbooks.couponserver.coupons.couponpolicy.entity.CouponPolicy;
import org.simsimbooks.couponserver.coupons.coupontype.entity.CouponTargetType;
import org.simsimbooks.couponserver.coupons.coupontype.entity.CouponType;
import org.simsimbooks.couponserver.coupons.usercoupon.entity.UserCoupon;

import java.util.Objects;

public class UserCouponMapper {
    public static UserCouponResponseDto toResponse(UserCoupon userCoupon) {
        if (Objects.isNull(userCoupon)) {
            throw new IllegalArgumentException("userCoupon is null");
        }

        CouponType couponType = userCoupon.getCouponType();
        CouponPolicy couponPolicy = ((org.simsimbooks.couponserver.coupons.coupontype.entity.CouponType) couponType).getCouponPolicy();
        CouponDetails couponDetails = extractCouponDetails(couponType);

        return switch (couponPolicy.getDiscountType()) {
            case FIX -> toFixCouponResponse(userCoupon, couponType, couponPolicy, couponDetails);
            case RATE -> toRateCouponResponse(userCoupon, couponType, couponPolicy, couponDetails);
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

    private static FixUserCouponResponseDto toFixCouponResponse(UserCoupon userCoupon, CouponType couponType, CouponPolicy couponPolicy, CouponDetails couponDetails) {

        return FixUserCouponResponseDto.builder()
                .userId(userCoupon.getUser().getId())
                .id(userCoupon.getId())
                .issueDate(userCoupon.getIssueDate())
                .deadline(userCoupon.getDeadline())
                .couponTypeName(couponType.getName())
                .userCouponStatus(userCoupon.getUserCouponStatus())
                .couponTargetType(couponDetails.couponTargetType())
                .couponTargetId(couponDetails.targetId())
                .discountPrice(couponPolicy.getDiscountPrice())
                .minOrderAmount(couponPolicy.getMinOrderAmount())
                .build();
    }

    private static RateUserCouponResponseDto toRateCouponResponse(UserCoupon userCoupon, CouponType couponType, CouponPolicy couponPolicy, CouponDetails couponDetails) {

        return RateUserCouponResponseDto.builder()
                .userId(userCoupon.getUser().getId())
                .id(userCoupon.getId())
                .issueDate(userCoupon.getIssueDate())
                .deadline(userCoupon.getDeadline())
                .couponTypeName(couponType.getName())
                .userCouponStatus(userCoupon.getUserCouponStatus())
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
