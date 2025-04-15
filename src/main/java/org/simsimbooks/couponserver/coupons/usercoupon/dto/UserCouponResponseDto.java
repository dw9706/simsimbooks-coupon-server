package org.simsimbooks.couponserver.coupons.usercoupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.simsimbooks.couponserver.coupons.couponpolicy.entity.DiscountType;
import org.simsimbooks.couponserver.coupons.usercoupon.entity.UserCouponStatus;
import org.simsimbooks.couponserver.coupons.coupon.entity.CouponType;

import java.time.LocalDateTime;
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public abstract class UserCouponResponseDto {
    //쿠폰 Id
    private Long id;
    //쿠폰 발급 날짜
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime issueDate;
    // 쿠폰 마감일
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime deadline;
    // 쿠폰 상태 -> USED, EXPIRED, UNUSED
    private UserCouponStatus userCouponStatus;
    // 쿠폰 정책 이름
    private String couponTypeName;
    // 쿠폰 적용 대상 -> ALL, CATEGORY, BOOK
    private CouponType couponType;
    // 쿠폰 적용 대상의 Id
    private Long couponTargetId; //추후 고민

    private DiscountType discountType;

    private Long userId;




}
