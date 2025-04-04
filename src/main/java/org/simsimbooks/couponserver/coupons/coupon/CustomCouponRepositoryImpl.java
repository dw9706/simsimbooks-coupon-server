package org.simsimbooks.couponserver.coupons.coupon;

import lombok.RequiredArgsConstructor;
import org.simsimbooks.couponserver.coupons.coupon.entity.Coupon;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomCouponRepositoryImpl implements CustomCouponRepository {
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 유저가 가지고 있는 쿠폰 중에서 특정 책에 적용 가능한 쿠폰만 Page로 반환한다.
     * @param userId
     * @param bookId
     * @return 특정 책에 적용 가능한 쿠폰 페이지
     */
    @Override
    public List<Coupon> findEligibleCouponToBook(Long userId, Long bookId, BigDecimal orderAmount) {
        QCoupon coupon = QCoupon.coupon;
        QCouponType couponType = QCouponType.couponType;
        QCategoryCoupon categoryCoupon = QCategoryCoupon.categoryCoupon;
        QBookCoupon bookCoupon = QBookCoupon.bookCoupon;
        QAllCoupon allCoupon = QAllCoupon.allCoupon;
        QBookCategory bookCategory = QBookCategory.bookCategory;
        QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy; // CouponPolicy Q타입

        JPAQuery<Coupon> query = jpaQueryFactory.selectFrom(coupon)
                .join(coupon.couponType, couponType)
                .leftJoin(categoryCoupon).on(couponType.couponTypeId.eq(categoryCoupon.couponTypeId))
                .leftJoin(bookCoupon).on(couponType.couponTypeId.eq(bookCoupon.couponTypeId))
                .leftJoin(allCoupon).on(couponType.couponTypeId.eq(allCoupon.couponTypeId))
                .join(couponType.couponPolicy, couponPolicy) // CouponPolicy 조인 추가
                .where(
                        coupon.user.userId.eq(userId)
                                .and(coupon.couponStatus.eq(CouponStatus.UNUSED))
                                .and(couponPolicy.minOrderAmount.loe(orderAmount)) // orderAmount >= minOrderAmount 조건 추가
                                .and(
                                        couponType.instanceOf(CategoryCoupon.class)
                                                .or(
                                                        couponType.instanceOf(BookCoupon.class)
                                                                .and(bookCoupon.book.bookId.eq(bookId))
                                                )
                                                .or(
                                                        couponType.instanceOf(AllCoupon.class)
                                                )
                                )
                )
                .orderBy(coupon.issueDate.asc());

        return query.fetch();
    }



    /**
     * 유저가 가지고 있는 쿠폰 중에서 특정 쿠폰 타입이고 아직 사용되지 않은(UNUSED)쿠폰을 반환한다
     * 아직 사용하지 않은 특정 쿠폰 타입인 쿠폰은 사용자마다 1개씩만 가지고 있을 수 있음.
     * @param userId
     * @param couponTypeId
     * @return 사용되지 않은 쿠폰
     */
    @Override
    public Optional<Coupon> findUnusedCouponByUserAndType(Long userId, Long couponTypeId) {
        QCoupon coupon = QCoupon.coupon;
        return Optional.ofNullable(jpaQueryFactory.selectFrom(coupon)
                .where(
                        coupon.user.userId.eq(userId),
                        coupon.couponType.couponTypeId.eq(couponTypeId),
                        coupon.couponStatus.eq(CouponStatus.UNUSED)
                ).fetchOne());
    }
}
