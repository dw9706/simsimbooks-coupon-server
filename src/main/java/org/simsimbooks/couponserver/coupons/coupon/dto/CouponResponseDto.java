package org.simsimbooks.couponserver.coupons.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.simsimbooks.couponserver.coupons.coupon.entity.CouponStatus;
import org.simsimbooks.couponserver.coupons.couponpolicy.entity.DisCountType;
import org.simsimbooks.couponserver.coupons.coupontype.entity.CouponTargetType;

import java.time.LocalDateTime;
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public abstract class CouponResponseDto {
    //쿠폰 Id
    private Long couponId;
    //쿠폰 발급 날짜
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime issueDate;
    // 쿠폰 마감일
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime deadline;
    // 쿠폰 상태 -> USED, EXPIRED, UNUSED
    private CouponStatus couponStatus;
    // 쿠폰 정책 이름
    private String couponTypeName;
    // 중복여부
    private boolean isStacking;
    // 쿠폰 적용 대상 -> ALL, CATEGORY, BOOK
    private CouponTargetType couponTargetType;
    // 쿠폰 적용 대상의 Id
    private Long couponTargetId; //추후 고민

    private DisCountType disCountType;

    private Long userId;




}
