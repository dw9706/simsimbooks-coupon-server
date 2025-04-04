package org.simsimbooks.couponserver.coupons.coupontype.mapper;


import org.simsimbooks.couponserver.coupons.allcoupon.entity.AllCoupon;
import org.simsimbooks.couponserver.coupons.bookcoupon.entity.BookCoupon;
import org.simsimbooks.couponserver.coupons.categorycoupon.entity.CategoryCoupon;
import org.simsimbooks.couponserver.coupons.coupontype.dto.CouponTypeRequestDto;
import org.simsimbooks.couponserver.coupons.coupontype.dto.CouponTypeResponseDto;
import org.simsimbooks.couponserver.coupons.coupontype.entity.CouponTargetType;
import org.simsimbooks.couponserver.coupons.coupontype.entity.CouponType;

import java.util.Objects;

public class CouponTypeMapper {
    public static CouponTypeResponseDto toResponse(CouponType couponType) {
        CouponTypeResponseDto responseDto = CouponTypeResponseDto.builder()
                .couponTypeId(couponType.getId())
                .couponTypeName(couponType.getName())
                .period(couponType.getPeriod())
                .deadline(couponType.getDeadline())
                .couponPolicyId(couponType.getCouponPolicy().getId())
                .build();

        if (couponType instanceof BookCoupon) {
            responseDto.setCouponTypes(CouponTargetType.BOOK);
            responseDto.setCouponTargetId(((BookCoupon) couponType).getBook().getId());
            responseDto.setCouponTargetName(((BookCoupon) couponType).getBook().getTitle());
        } else if (couponType instanceof CategoryCoupon) {
            responseDto.setCouponTypes(CouponTargetType.CATEGORY);
            responseDto.setCouponTargetId(((CategoryCoupon) couponType).getCategory().getId());
            responseDto.setCouponTargetName(((CategoryCoupon) couponType).getCategory().getName());
        } else {
            responseDto.setCouponTypes(CouponTargetType.ALL);
        }

        return responseDto;
    }

    public static CouponType toCouponType(CouponTypeRequestDto requestDto) {
        if (requestDto.getCouponTargetType() == CouponTargetType.BOOK) {
            BookCoupon bookCoupon = BookCoupon.builder()
                    .name(requestDto.getCouponTypeName())
                    .build();
            return setPeriodOrDeadLine(bookCoupon, requestDto);
        } else if (requestDto.getCouponTargetType() == CouponTargetType.CATEGORY) {
            CategoryCoupon categoryCoupon = CategoryCoupon.builder()
                    .name(requestDto.getCouponTypeName())
                    .build();
            return setPeriodOrDeadLine(categoryCoupon, requestDto);
        } else {
            AllCoupon allCoupon = AllCoupon.builder()
                    .name(requestDto.getCouponTypeName())
                    .build();
            return setPeriodOrDeadLine(allCoupon, requestDto);
        }

    }

    private static CouponType setPeriodOrDeadLine(CouponType couponType, CouponTypeRequestDto requestDto) {
        if (Objects.isNull(requestDto.getPeriod())) {
            couponType.setDeadline(requestDto.getDeadline());
        } else {
            couponType.setPeriod(requestDto.getPeriod());
        }
        return couponType;
    }
}
