package org.simsimbooks.couponserver.coupons.coupon.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.simsimbooks.couponserver.coupons.couponpolicy.entity.DisCountType;

@Getter
public enum CouponStatus {
    USED("사용"),
    EXPIRED("만료"),
    UNUSED("미사용");

    private final String name;

    CouponStatus(String name) {
        this.name = name;
    }

    // 역직렬화 : JSON -> Enum
    @JsonCreator
    public static DisCountType from(String value) {
        return DisCountType.valueOf(value.toUpperCase());
    }
    // 직렬화 : Enum -> JSON
    @JsonValue
    public String getName() {
        return name;
    }
}
