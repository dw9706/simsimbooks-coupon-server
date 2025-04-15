package org.simsimbooks.couponserver.coupons.coupon.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.simsimbooks.couponserver.coupons.couponpolicy.entity.CouponPolicy;

import java.time.LocalDateTime;


@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "coupon_type")
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(name = "coupon_name", length = 40, nullable = false)
    private String name;

    @Column(name = "period")
    private int period;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_policy_id", nullable = false)
    private CouponPolicy couponPolicy;
}
