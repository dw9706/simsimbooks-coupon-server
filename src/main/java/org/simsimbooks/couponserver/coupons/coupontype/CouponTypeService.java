package org.simsimbooks.couponserver.coupons.coupontype;

import lombok.RequiredArgsConstructor;
import org.simsimbooks.couponserver.book.BookRepository;
import org.simsimbooks.couponserver.book.entity.Book;
import org.simsimbooks.couponserver.category.CategoryRepository;
import org.simsimbooks.couponserver.category.entity.Category;
import org.simsimbooks.couponserver.coupons.bookcoupon.entity.BookCoupon;
import org.simsimbooks.couponserver.coupons.categorycoupon.entity.CategoryCoupon;
import org.simsimbooks.couponserver.coupons.usercoupon.UserCouponRepository;
import org.simsimbooks.couponserver.coupons.couponpolicy.CouponPolicyRepository;
import org.simsimbooks.couponserver.coupons.couponpolicy.entity.CouponPolicy;
import org.simsimbooks.couponserver.coupons.coupontype.dto.CouponTypeRequestDto;
import org.simsimbooks.couponserver.coupons.coupontype.dto.CouponTypeResponseDto;
import org.simsimbooks.couponserver.coupons.coupontype.entity.CouponType;
import org.simsimbooks.couponserver.coupons.coupontype.mapper.CouponTypeMapper;
import org.simsimbooks.couponserver.coupons.usercoupon.entity.UserCoupon;
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
public class CouponTypeService{
    private final CouponTypeRepository couponTypeRepository;
    private final UserCouponRepository couponRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final CouponPolicyRepository couponPolicyRepository;

    /**
     * 모든 쿠폰 타입을 가져온다.
     * @param pageable
     * @return 쿠폰타입 페이지
     */
    public Page<CouponTypeResponseDto> getAllCouponType(Pageable pageable) {
        Page<CouponType> couponTypes = couponTypeRepository.findAll(pageable);
        return couponTypes.map(CouponTypeMapper::toResponse);
    }

    /**
     * 특정 쿠폰타입을 가져온다.
     * @param couponTypeId
     * @throws NoSuchElementException 쿠폰타입이 존재하지 않을 경우
     * @throws IllegalArgumentException id가 0이거나 null일 경우
     * @return
     */
    public CouponTypeResponseDto getCouponType(Long couponTypeId) {
        validateId(couponTypeId);
        CouponType couponType = couponTypeRepository.findById(couponTypeId).orElseThrow(() -> new NoSuchElementException("쿠폰타입(id:" + couponTypeId + ")이 존재하지 않습니다."));
        return CouponTypeMapper.toResponse(couponType);

    }

    /**
     * 특정 쿠폰 정책과 연결된 쿠폰 타입들을 가져온다.
     * @param pageable
     * @throws IllegalArgumentException id가 0이거나 null일 경우
     * @param couponPolicyId
     * @return
     */
    public Page<CouponTypeResponseDto> getCouponByCouponPolicy(Pageable pageable, Long couponPolicyId) {
        validateId(couponPolicyId);
        Page<CouponType> couponTypePage = couponTypeRepository.findByCouponPolicyId(pageable, couponPolicyId);
        return couponTypePage.map(CouponTypeMapper::toResponse);
    }

    /**
     * 쿠폰 타입을 생성한다.
     * BookCoupon인지 CategoryCoupon인지 AllCoupon인지 확인 후 저장한다.
     * @param requestDto
     * @throws IllegalArgumentException id가 0이거나 null일 경우
     * @throws NoSuchElementException 책,쿠폰타입이 존재하지 않을 경우
     * @return 저장한 쿠폰 타입의 reponseDto
     */
    @Transactional
    public CouponTypeResponseDto createCouponType(CouponTypeRequestDto requestDto) {
        CouponType couponType = CouponTypeMapper.toCouponType(requestDto);
        if (couponType instanceof BookCoupon) {
            validateId(requestDto.getTargetId());
            Book book = bookRepository.findById(requestDto.getTargetId()).orElseThrow(() -> new NoSuchElementException("책(id:" + requestDto.getTargetId() + ")이 존재하지 않습니다."));
            ((BookCoupon) couponType).setBook(book);
        } else if (couponType instanceof CategoryCoupon) {
            validateId(requestDto.getTargetId());
            Category category = categoryRepository.findById(requestDto.getTargetId()).orElseThrow(() -> new NoSuchElementException("카테고리(id:" + requestDto.getTargetId() + ")이 존재하지 않습니다."));
            ((CategoryCoupon) couponType).setCategory(category);
        }
        validateId(requestDto.getCouponPolicyId());
        CouponPolicy couponPolicy = couponPolicyRepository.findById(requestDto.getCouponPolicyId()).orElseThrow(() -> new NoSuchElementException("쿠폰 정책(id:" + requestDto.getCouponPolicyId() + ")이 존재하지 않습니다."));
        couponType.setCouponPolicy(couponPolicy);
        CouponType save = couponTypeRepository.save(couponType);
        return CouponTypeMapper.toResponse(save);
    }

    /**
     * 특정 쿠폰 타입을 삭제한다.
     * 쿠폰 타입이 회원들에게 발급되면 삭제할 수 없다.
     * @param couponTypeId
     * @throws IllegalArgumentException id가 0이거나 null일 경우
     * @throws NoSuchElementException 책,쿠폰타입이 존재하지 않을 경우
     * @throws IllegalStateException 쿠폰 타입이 이미 회원들에게 발급되었을 때
     */
    @Transactional
    public void deleteCouponType(Long couponTypeId) {
        validateId(couponTypeId);
        CouponType couponType = couponTypeRepository.findById(couponTypeId).orElseThrow(() -> new NoSuchElementException("쿠폰타입(id:" + couponTypeId + ")이 존재하지 않습니다."));
        List<UserCoupon> coupons = couponRepository.findByCouponTypeId(couponTypeId);
        if (!coupons.isEmpty()) {
            throw new IllegalStateException("쿠폰 타입(id:" + couponTypeId + ")이 이미 회원에게 발급되었습니다.");
        }
        couponTypeRepository.delete(couponType);
    }

    /**
     * Id값의 유효성을 체크한다.
     * @param id
     * @throws IllegalArgumentException id가 0이거나 null일 경우
     */
    private void validateId(Long id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("ID가 null 입니다.");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("ID는 0보다 커야합니다.");
        }
    }
}
