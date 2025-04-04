package org.simsimbooks.couponserver.coupons.coupon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.simsimbooks.couponserver.coupons.coupontype.entity.CouponType;
import org.simsimbooks.couponserver.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "coupon_status")
    private CouponStatus couponStatus;

    @Column(name = "use_date")
    private LocalDateTime useDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coupon_type_id", nullable = false)
    private CouponType couponType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void use() {
        couponStatus = CouponStatus.USED;
        useDate = LocalDateTime.now();
    }

    public void expire() {
        couponStatus = CouponStatus.EXPIRED;
    }
}
