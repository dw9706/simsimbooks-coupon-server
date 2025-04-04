package org.simsimbooks.couponserver.coupons.allcoupon;

import org.simsimbooks.couponserver.coupons.allcoupon.entity.AllCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllCouponRepository extends JpaRepository<AllCoupon,Long> {
}
