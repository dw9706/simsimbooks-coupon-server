package org.simsimbooks.couponserver.coupons.usercoupon.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.simsimbooks.couponserver.coupons.couponpolicy.entity.DiscountType;

@Getter
public enum UserCouponStatus {
    USED("사용"),
    EXPIRED("만료"),
    UNUSED("미사용");

    private final String name;

    UserCouponStatus(String name) {
        this.name = name;
    }

    // 역직렬화 : JSON -> Enum
    @JsonCreator
    public static DiscountType from(String value) {
        return DiscountType.valueOf(value.toUpperCase());
    }
    // 직렬화 : Enum -> JSON
    @JsonValue
    public String getName() {
        return name;
    }
}
