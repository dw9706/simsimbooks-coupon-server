package org.simsimbooks.couponserver.coupons.usercoupon;

import org.simsimbooks.couponserver.coupons.usercoupon.entity.UserCoupon;
import org.simsimbooks.couponserver.coupons.usercoupon.entity.UserCouponStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon,Long>, CustomUserCouponRepository {
    Page<UserCoupon> findByUserId(Pageable pageable, Long userId);

    @Query("SELECT uc FROM UserCoupon uc WHERE uc.user.id = :userId AND uc.userCouponStatus = :couponStatus AND uc.deadline > CURRENT_TIMESTAMP")
    Page<UserCoupon> findByUserUserIdAndCouponStatusAndDeadlineAfterNow(
            @Param("userId") Long userId,
            @Param("userCouponStatus") UserCouponStatus userCouponStatus,
            Pageable pageable
    );

    Optional<UserCoupon> findByUserIdAndUserCouponId(Long userId, Long userCouponId);

    List<UserCoupon> findByCouponTypeId(Long couponTypeId);

    List<UserCoupon> findByUserCouponStatus(UserCouponStatus userCouponStatus);

    @Query("SELECT uc FROM UserCoupon uc WHERE uc.userCouponStatus = 'UNUSED' AND uc.deadline < CURRENT_TIMESTAMP")
    List<UserCoupon> findUnusedAndExpiredCoupons();
}

