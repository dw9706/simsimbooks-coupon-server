package org.simsimbooks.couponserver.coupons.couponpolicy.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.NoSuchElementException;

public enum DisCountType {
    RATE("정률"),
    FIX("정액");

    private final String name;

    DisCountType(String name) {this.name = name;}

    // 역직렬화 : JSON -> Enum
    @JsonCreator
    public static DisCountType from(String value) {
        for (DisCountType disCountType : values()) {
            if (disCountType.name.equals(value)) {
                return disCountType;
            }
        }
        throw new NoSuchElementException("잘못된 DiscountType입니다. '정률'과'정액'중 하나를 선택하세요");
    }
    // 직렬화 : Enum -> JSON
    @JsonValue
    public String getName() {
        return name;
    }
}
