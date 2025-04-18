package org.simsimbooks.couponserver.coupons.coupon.mapper;


import org.simsimbooks.couponserver.coupons.allcoupon.entity.AllCoupon;
import org.simsimbooks.couponserver.coupons.bookcoupon.entity.BookCoupon;
import org.simsimbooks.couponserver.coupons.categorycoupon.entity.CategoryCoupon;
import org.simsimbooks.couponserver.coupons.coupon.dto.CouponRequestDto;
import org.simsimbooks.couponserver.coupons.coupon.dto.CouponResponseDto;
import org.simsimbooks.couponserver.coupons.coupon.entity.Coupon;
import org.simsimbooks.couponserver.coupons.coupon.entity.CouponType;

import java.util.Objects;

public class CouponMapper {
    public static CouponResponseDto toResponse(Coupon coupon) {
        CouponResponseDto responseDto = CouponResponseDto.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .period(coupon.getPeriod())
                .deadline(coupon.getDeadline())
                .couponPolicyId(coupon.getCouponPolicy().getId())
                .build();

        if (coupon instanceof BookCoupon) {
            responseDto.setCouponTypes(CouponType.BOOK);
            responseDto.setCouponTargetId(((BookCoupon) coupon).getBook().getId());
            responseDto.setCouponTargetName(((BookCoupon) coupon).getBook().getTitle());
        } else if (coupon instanceof CategoryCoupon) {
            responseDto.setCouponTypes(CouponType.CATEGORY);
            responseDto.setCouponTargetId(((CategoryCoupon) coupon).getCategory().getId());
            responseDto.setCouponTargetName(((CategoryCoupon) coupon).getCategory().getName());
        } else {
            responseDto.setCouponTypes(CouponType.ALL);
        }

        return responseDto;
    }

    public static Coupon toCoupon(CouponRequestDto requestDto) {
        if (requestDto.getCouponType() == CouponType.BOOK) {
            BookCoupon bookCoupon = BookCoupon.builder()
                    .name(requestDto.getName())
                    .build();
            return setPeriodOrDeadLine(bookCoupon, requestDto);
        } else if (requestDto.getCouponType() == CouponType.CATEGORY) {
            CategoryCoupon categoryCoupon = CategoryCoupon.builder()
                    .name(requestDto.getName())
                    .build();
            return setPeriodOrDeadLine(categoryCoupon, requestDto);
        } else {
            AllCoupon allCoupon = AllCoupon.builder()
                    .name(requestDto.getName())
                    .build();
            return setPeriodOrDeadLine(allCoupon, requestDto);
        }

    }

    private static Coupon setPeriodOrDeadLine(Coupon coupon, CouponRequestDto requestDto) {
        if (Objects.isNull(requestDto.getPeriod())) {
            coupon.setDeadline(requestDto.getDeadline());
        } else {
            coupon.setPeriod(requestDto.getPeriod());
        }
        return coupon;
    }
}
