package org.simsimbooks.couponserver.coupons.coupon;


import org.simsimbooks.couponserver.coupons.coupon.entity.Coupon;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CustomCouponRepository {
    List<Coupon> findEligibleCouponToBook(Long userId, Long bookId, BigDecimal orderAmount);

    Optional<Coupon> findUnusedCouponByUserAndType(Long userId, Long couponTypeId);
}
