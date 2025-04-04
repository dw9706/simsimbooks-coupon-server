package org.simsimbooks.couponserver.coupons.categorycoupon;

import org.simsimbooks.couponserver.coupons.categorycoupon.entity.CategoryCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryCouponRepository extends JpaRepository<CategoryCoupon,Long> {
}
