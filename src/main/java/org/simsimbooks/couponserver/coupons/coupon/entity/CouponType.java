package org.simsimbooks.couponserver.coupons.coupon.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum CouponType {
     ALL("전체"),
     CATEGORY("카테고리"),
     BOOK("책");

     private final String name;

     CouponType(String name) {
          this.name = name;
     }

     // 역직렬화 : JSON -> Enum
     @JsonCreator
     public static CouponType from(String value) {
          for (CouponType type : values()) {
               if (type.name.equals(value)) {
                    return type;
               }
          }
          return CouponType.ALL;
     }
     // 직렬화 : Enum -> JSON
     @JsonValue
     public String getName() {
          return name;
     }
}
