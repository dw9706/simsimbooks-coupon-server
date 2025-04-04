package org.simsimbooks.couponserver.coupons.bookcoupon;

import org.simsimbooks.couponserver.coupons.bookcoupon.entity.BookCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCouponRepository extends JpaRepository<BookCoupon,Long> {
}
