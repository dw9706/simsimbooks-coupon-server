package org.simsimbooks.couponserver.coupons.coupontype.entity;


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
@Table(name = "coupon_types")
public class CouponType {
    public static final Long WELCOME_COUPON_TYPE_ID = 1L;
    public static final Long BIRTHDAY_COUPON_TYPE_ID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_type_id")
    private Long id;

    @Column(name = "coupon_type_name", length = 40, nullable = false)
    private String name;

    @Column(name = "period")
    private int period;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_policy_id", nullable = false)
    private CouponPolicy couponPolicy;
}
