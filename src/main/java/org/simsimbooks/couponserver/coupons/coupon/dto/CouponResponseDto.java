package org.simsimbooks.couponserver.coupons.coupon.dto;

import lombok.Builder;
import lombok.Data;
import org.simsimbooks.couponserver.coupons.coupon.entity.CouponType;

import java.time.LocalDateTime;

@Builder
@Data
public class CouponResponseDto {
    private Long id;

    private String name;

    private Integer period;

    private LocalDateTime deadline;

    private boolean stacking;

    private Long couponPolicyId;

    private CouponType couponTypes; //ALL, CATEGORY, BOOK

    private Long couponTargetId;// 타겟의 Id

    private String couponTargetName; // 카테고리 이름, 책 이름

}
