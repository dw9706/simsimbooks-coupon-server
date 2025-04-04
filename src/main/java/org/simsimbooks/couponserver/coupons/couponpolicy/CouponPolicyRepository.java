package org.simsimbooks.couponserver.coupons.couponpolicy;

import org.simsimbooks.couponserver.coupons.couponpolicy.entity.CouponPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy,Long> {
}
