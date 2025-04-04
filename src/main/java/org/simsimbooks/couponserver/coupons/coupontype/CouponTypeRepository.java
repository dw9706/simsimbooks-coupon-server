package org.simsimbooks.couponserver.coupons.coupontype;

import org.simsimbooks.couponserver.coupons.coupontype.entity.CouponType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponTypeRepository extends JpaRepository<CouponType,Long> {
    Page<CouponType> findByCouponPolicyId(Pageable pageable, Long couponPolicyId);

    List<CouponType> findByCouponPolicyId(Long couponPolicyId);
}
