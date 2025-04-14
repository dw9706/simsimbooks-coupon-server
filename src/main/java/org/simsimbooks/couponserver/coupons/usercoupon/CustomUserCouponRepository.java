package org.simsimbooks.couponserver.coupons.usercoupon;



import org.simsimbooks.couponserver.coupons.usercoupon.entity.UserCoupon;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CustomUserCouponRepository {
    List<UserCoupon> findEligibleCouponToBook(Long userId, Long bookId, BigDecimal orderAmount);

    Optional<UserCoupon> findUnusedCouponByUserAndType(Long userId, Long couponTypeId);
}
