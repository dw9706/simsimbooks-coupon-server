package org.simsimbooks.couponserver.coupons.usercoupon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.simsimbooks.couponserver.coupons.coupontype.entity.CouponType;
import org.simsimbooks.couponserver.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_coupons")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coupon_id")
    private Long id;

    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "coupon_status")
    private UserCouponStatus userCouponStatus;

    @Column(name = "use_date")
    private LocalDateTime useDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coupon_id", nullable = false)
    private CouponType couponType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void use() {
        userCouponStatus = UserCouponStatus.USED;
        useDate = LocalDateTime.now();
    }

    public void expire() {
        userCouponStatus = UserCouponStatus.EXPIRED;
    }
}
