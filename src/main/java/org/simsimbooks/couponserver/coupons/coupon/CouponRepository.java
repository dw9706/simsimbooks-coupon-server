package org.simsimbooks.couponserver.coupons.coupon;

import org.simsimbooks.couponserver.coupons.coupon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon,Long> {
    Page<Coupon> findByCouponPolicyId(Pageable pageable, Long couponPolicyId);

    List<Coupon> findByCouponPolicyId(Long couponPolicyId);
}
