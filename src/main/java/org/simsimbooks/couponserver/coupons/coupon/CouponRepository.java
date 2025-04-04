package org.simsimbooks.couponserver.coupons.coupon;

import org.simsimbooks.couponserver.coupons.coupon.entity.Coupon;
import org.simsimbooks.couponserver.coupons.coupon.entity.CouponStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon,Long>, CustomCouponRepository {
    Page<Coupon> findByUserId(Pageable pageable, Long userId);

    @Query("SELECT c FROM Coupon c WHERE c.user.id = :userId AND c.couponStatus = :couponStatus AND c.deadline > CURRENT_TIMESTAMP")
    Page<Coupon> findByUserUserIdAndCouponStatusAndDeadlineAfterNow(
            @Param("userId") Long userId,
            @Param("couponStatus") CouponStatus couponStatus,
            Pageable pageable
    );

    Optional<Coupon> findByUserIdAndCouponId(Long userId, Long couponId);

    List<Coupon> findByCouponTypeId(Long couponTypeId);

    List<Coupon> findByCouponStatus(CouponStatus couponStatus);

    @Query("SELECT c FROM Coupon c WHERE c.couponStatus = 'UNUSED' AND c.deadline < CURRENT_TIMESTAMP")
    List<Coupon> findUnusedAndExpiredCoupons();
}

