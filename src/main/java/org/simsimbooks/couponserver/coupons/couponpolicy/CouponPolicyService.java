package org.simsimbooks.couponserver.coupons.couponpolicy;

import lombok.RequiredArgsConstructor;
import org.simsimbooks.couponserver.coupons.couponpolicy.dto.CouponPolicyRequestDto;
import org.simsimbooks.couponserver.coupons.couponpolicy.dto.CouponPolicyResponseDto;
import org.simsimbooks.couponserver.coupons.couponpolicy.entity.CouponPolicy;
import org.simsimbooks.couponserver.coupons.couponpolicy.mapper.CouponPolicyMapper;
import org.simsimbooks.couponserver.coupons.coupon.CouponTypeRepository;
import org.simsimbooks.couponserver.coupons.coupon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponPolicyService{
    private final CouponPolicyRepository couponPolicyRepository;
    private final CouponTypeRepository couponTypeRepository;

    /**
     * 모든 쿠폰 정책을 Page로 가지고온다.
     * @param pageable
     * @return
     */
    public Page<CouponPolicyResponseDto> getAllCouponPolicy(Pageable pageable) {
        Page<CouponPolicy> couponPolicies = couponPolicyRepository.findAll(pageable);
        return couponPolicies.map(CouponPolicyMapper::toResponse);
    }

    /**
     * 특정 쿠폰 정책(couponPolicyId)를 하나 가지고 온다.
     * @param couponPolicyId
     * @return
     */
    public CouponPolicyResponseDto getCouponPolicy(Long couponPolicyId) {
        validateId(couponPolicyId);
        CouponPolicy couponPolicy = couponPolicyRepository.findById(couponPolicyId).orElseThrow(() -> new NoSuchElementException("쿠폰정책(id:" + couponPolicyId + ")이 존재하지 않습니다."));
        return CouponPolicyMapper.toResponse(couponPolicy);

    }

    /**
     * 쿠폰 정책을 생성한다.
     * @param requestDto
     * @return
     */
    @Transactional
    public CouponPolicyResponseDto createCouponPolicy(CouponPolicyRequestDto requestDto) {
        CouponPolicy couponPolicy = CouponPolicyMapper.toCouponPolicy(requestDto);
        CouponPolicy save = couponPolicyRepository.save(couponPolicy);
        return CouponPolicyMapper.toResponse(save);
    }

    /**
     * 쿠폰 정책을 삭제한다.
     * 해당 쿠폰 정책으로 이미 쿠폰 타입이 만들어졌으면 삭제 불가능
     * @param couponPolicyId
     * @throws IllegalStateException
     */
    @Transactional
    public void deleteCouponPolicy(Long couponPolicyId) {
        validateId(couponPolicyId);
        CouponPolicy couponPolicy = couponPolicyRepository.findById(couponPolicyId).orElseThrow(() -> new NoSuchElementException("쿠폰정책(id:" + couponPolicyId + ")이 존재하지 않습니다."));
        List<Coupon> coupons = couponTypeRepository.findByCouponPolicyId(couponPolicyId);
        if (!coupons.isEmpty()) {
            throw new IllegalStateException("쿠폰정책(id:" + couponPolicyId + ")으로 쿠폰 타입이 생성되었습니다.");
        }
        couponPolicyRepository.delete(couponPolicy);
    }

    private void validateId(Long id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("ID가 null 입니다.");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("ID는 0보다 커야합니다.");
        }
    }

}
