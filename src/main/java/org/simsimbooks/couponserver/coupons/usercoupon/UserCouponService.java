package org.simsimbooks.couponserver.coupons.usercoupon;

import lombok.RequiredArgsConstructor;
import org.simsimbooks.couponserver.book.BookRepository;
import org.simsimbooks.couponserver.book.entity.Book;
import org.simsimbooks.couponserver.category.entity.Category;
import org.simsimbooks.couponserver.coupons.bookcoupon.entity.BookCoupon;
import org.simsimbooks.couponserver.coupons.categorycoupon.entity.CategoryCoupon;
import org.simsimbooks.couponserver.coupons.couponpolicy.entity.DiscountType;
import org.simsimbooks.couponserver.coupons.usercoupon.dto.UserCouponResponseDto;
import org.simsimbooks.couponserver.coupons.usercoupon.dto.DiscountAmountResponseDto;
import org.simsimbooks.couponserver.coupons.usercoupon.dto.EmptyUserCouponResponseDto;
import org.simsimbooks.couponserver.coupons.usercoupon.entity.UserCoupon;
import org.simsimbooks.couponserver.coupons.usercoupon.entity.UserCouponStatus;
import org.simsimbooks.couponserver.coupons.usercoupon.mapper.UserCouponMapper;
import org.simsimbooks.couponserver.coupons.couponpolicy.entity.CouponPolicy;
import org.simsimbooks.couponserver.coupons.coupontype.CouponTypeRepository;
import org.simsimbooks.couponserver.coupons.coupontype.entity.CouponType;
import org.simsimbooks.couponserver.user.UserRepository;
import org.simsimbooks.couponserver.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCouponService {
    private final UserCouponRepository couponRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CouponTypeRepository couponTypeRepository;

    public Page<UserCouponResponseDto> getTotalCoupons(Pageable pageable) {
        Page<UserCoupon> totalCoupons = couponRepository.findAll(pageable);
        return totalCoupons.map(UserCouponMapper::toResponse);
    }
    /**
     * couponId로 쿠폰을 가져온다.
     * @param userCouponId
     * @throws IllegalArgumentException id가 0이거나 null일 경우
     * @return 쿠폰
     */
    public UserCouponResponseDto getUserCouponById(Long userCouponId) {
        //couponId null 체크
        validateId(userCouponId);

        UserCoupon userCoupon = couponRepository.findById(userCouponId).orElseThrow(() -> new NoSuchElementException("쿠폰(id:"+userCouponId+")이 존재하지 않습니다."));


        return UserCouponMapper.toResponse(userCoupon);

    }

    /**
     * 유저가 가지고 있는 쿠폰 중 특정 쿠폰 타입이고 아직 미사용인 쿠폰을 가지고 온다.
     * 각 유저는 미사용된 특정 쿠폰 타입의 쿠폰을 하나씩만 가지고 있을 수 있음.
     * @param userId
     * @param couponTypeId
     * @throws IllegalArgumentException id가 0이거나 null일 경우
     * @throws NoSuchElementException 쿠폰, 회원이 존재하지 않을 경우
     * @return 미사용된 쿠폰
     */
    public UserCouponResponseDto getUnusedUserCouponByCouponType(Long userId, Long couponTypeId) {
        validateId(userId);
        validateId(couponTypeId);
        userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("회원(id:" + userId + ")이 존재하지 않습니다."));
        couponTypeRepository.findById(couponTypeId).orElseThrow(() -> new NoSuchElementException("쿠폰 정책(id:" + couponTypeId + ")이 존재하지 않습니다."));
        Optional<UserCoupon> unusedUserCoupon = couponRepository.findUnusedCouponByUserAndType(userId, couponTypeId);
        if (unusedUserCoupon.isPresent()) {
            return UserCouponMapper.toResponse(unusedUserCoupon.get());
        }
        return EmptyUserCouponResponseDto.builder().build();

    }

    /**
     * 유저가 가진 쿠폰을 Page로 가지고 온다
     * 만료,미사용,사용된 쿠폰 상관없이 가져온다.
     * @param pageable
     * @param userId
     * @throws IllegalArgumentException id가 0이거나 null일 경우
     * @throws NoSuchElementException 회원이 존재하지 않을 경우
     * @return 유저의 쿠폰 페이지
     */
    public Page<UserCouponResponseDto> getUserCoupons(Pageable pageable, Long userId) {
        //userId null 체크
        validateId(userId);
        //유저 확인
        userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("회원(id:"+userId+")이 존재하지 않습니다."));

        Page<UserCoupon> userCouponPage = couponRepository.findByUserId(pageable, userId);

        return userCouponPage.map(UserCouponMapper::toResponse);
    }

    /**
     * 유저의 쿠폰 중 미사용된 쿠폰을 Page로 가져온다.
     * @param pageable
     * @param userId
     * @throws IllegalArgumentException id가 0이거나 null일 경우
     * @throws NoSuchElementException 회원이 존재하지 않을 경우

     * @return 미사용된 쿠폰 페이지
     */
    public Page<UserCouponResponseDto> getUnusedUserCoupons(Pageable pageable, Long userId) {
        //userId null 체크
        validateId(userId);
        //유저 확인
        userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("회원(id:"+userId+")이 존재하지 않습니다."));
        Page<UserCoupon> userCouponPage = couponRepository.findByUserUserIdAndCouponStatusAndDeadlineAfterNow(userId, UserCouponStatus.UNUSED, pageable);
        return userCouponPage.map(UserCouponMapper::toResponse);
    }
    public List<UserCouponResponseDto> getExpiredUserCoupons() {
        List<UserCoupon> userCoupons = couponRepository.findByUserCouponStatus(UserCouponStatus.EXPIRED);
        return userCoupons.stream().map(UserCouponMapper::toResponse).toList();
    }

    public List<UserCouponResponseDto> getUnusedButDeadlinePassedUserCoupon() {
        List<UserCoupon> userCoupons = couponRepository.findUnusedAndExpiredCoupons();
        return userCoupons.stream().map(UserCouponMapper::toResponse).toList();
    }

    /**
     * 유저가 가지고 있는 쿠폰 중 특정 책에 적용 가능한 쿠폰을 Page로 가져온다.
     * @param userId
     * @param bookId
     * @throws IllegalArgumentException id가 0이거나 null일 경우
     * @throws NoSuchElementException 회원, 도서가 존재하지 않을 경우
     * @return 적용가능한 쿠폰 패이지
     */
    @Transactional
    public List<UserCouponResponseDto> getEligibleUserCoupons(Long userId, Long bookId, int quantity) {
        //userId null 체크
        validateId(userId);
        //bookId null 체크
        validateId(bookId);

        userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("회원(id:" + userId + ")이 존재하지 않습니다."));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NoSuchElementException("도서(id:" + bookId + ")이 존재하지 않습니다."));
        BigDecimal orderAmount = book.getSalePrice().multiply(BigDecimal.valueOf(quantity));
        List<UserCoupon> userCoupons = couponRepository.findEligibleCouponToBook(userId, book.getId(),orderAmount); //유저의 모든 전체쿠폰, 모든 카테고리 쿠폰, 책에 적용가능한 책 쿠폰을 가져온다.

        //책의 정보를 가지고 와서 책이 속한 카테고리 Id를 모두 가지고 온다.
//        BookResponseDto bookDetail = bookGetService.getBookDetail(null, book.getId());
//        List<Long> categoryIdList = bookDetail.getCategoryList().stream()
//                .flatMap(List::stream)
//                .map(CategoryResponseDto::getId) // 각 CategoryResponseDto에서 ID 추출
//                .toList();
        List<Long> categoryIdList = new ArrayList<>();

        List<UserCoupon> eligibleUserCoupons = new ArrayList<>();

        // 유저의 카테고리 쿠폰이 책에 적용 가능한지 확인한다.
        for (UserCoupon userCoupon : userCoupons) {
            //유효기간이 지났는지 확인하고 지났으면 만료처리
            if (userCoupon.getDeadline().isBefore(LocalDateTime.now())) {
                userCoupon.expire();
                continue;
            }
            if (userCoupon.getCouponType() instanceof CategoryCoupon categoryCoupon) {
                Long targetId = categoryCoupon.getCategory().getId();
                for (Long categoryId : categoryIdList) {
                    //카테고리 쿠폰의 targetId와 일치하면 List에 저장
                    if (targetId.equals(categoryId)) {
                        eligibleUserCoupons.add(userCoupon);
                        break;
                    }
                }
                // 카테고리 쿠폰이 아니라면 List에 저장
            } else {
                eligibleUserCoupons.add(userCoupon);
            }
        }


        return eligibleUserCoupons.stream().map(UserCouponMapper::toResponse).toList();
    }


    /**
     * 유저들에게 쿠폰을 발행한다.
     * @param userIds
     * @param couponTypeId
     * @throws IllegalArgumentException id가 0이거나 null일 경우
     * @throws NoSuchElementException 회원,쿠폰타입이 존재하지 않을 경우
     * @return 발행된 쿠폰들
     */
    @Transactional
    public void issueUserCoupons(List<Long> userIds, Long couponTypeId) {
        List<Long> result = new ArrayList<>();
        validateId(couponTypeId);
        CouponType couponType = couponTypeRepository.findById(couponTypeId).orElseThrow(() -> new NoSuchElementException("쿠폰 정책(id:" + couponTypeId + ")이 존재하지 않습니다.1"));

        // 회원 존재 확인
        for (Long userId : userIds) {
            validateId(userId);
            User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("회원(id:" + userId + ")이 존재하지 않습니다."));

            Optional<UserCoupon> unusedUserCoupon = couponRepository.findUnusedCouponByUserAndType(userId, couponTypeId);

            //회원이 해당 쿠폰타입의 UNUSED쿠폰을 가지고 있는지 확인 후 있으면 삭제 후 새로 발급
            unusedUserCoupon.ifPresent(couponRepository::delete);

            UserCoupon userCoupon = UserCoupon.builder()
                    .issueDate(LocalDateTime.now())
                    .deadline(calUserCouponDeadline(couponType))
                    .userCouponStatus(UserCouponStatus.UNUSED)
                    .couponType(couponType)
                    .user(user)
                    .build();

            UserCoupon savedUserCoupon = couponRepository.save(userCoupon);
            result.add(savedUserCoupon.getId());

        }
    }

    /**
     * 쿠폰을 사용합니다.
     * @param userId
     * @param couponId
     * @throws IllegalArgumentException id가 0이거나 null일 경우
     * @throws NoSuchElementException 회원,쿠폰이 존재하지 않을 경우
     * @return 사용한 쿠폰
     */
    @Transactional
    public UserCouponResponseDto useUserCoupon(Long userId, Long couponId) {
        validateId(userId);
        validateId(couponId);

        userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("회원(id:" + userId + ")이 존재하지 않습니다."));
        UserCoupon coupon = couponRepository.findByUserIdAndUserCouponId(userId, couponId).orElseThrow(() -> new NoSuchElementException("회원(id:" + userId + ")은 쿠폰(id:" + couponId + ")을 가지고 있지 않습니다."));
        if (coupon.getUserCouponStatus() != UserCouponStatus.UNUSED) {
            throw new IllegalStateException("회원(id:" + userId + ")의 쿠폰(id:" + couponId + ")은 이미 사용된 쿠폰입니다.");
        }
        if (coupon.getDeadline().isBefore(LocalDateTime.now())) {
            coupon.expire();
            throw new IllegalStateException("쿠폰(id:" + couponId + ")은 이미 사용기간이 지났습니다. 쿠폰을 만료 처리합니다.");
        }
        coupon.use();

        return UserCouponMapper.toResponse(coupon);
    }

    /**
     * 쿠폰을 만료시킨다.
     * @param userId
     * @param userCouponId
     * @throws IllegalArgumentException id가 0이거나 null일 경우
     * @throws NoSuchElementException 회원,쿠폰이 존재하지 않을 경우
     * @return 만료된 쿠폰
     */
    @Transactional
    public UserCouponResponseDto expireUserCoupon(Long userId, Long userCouponId) {
        validateId(userId);
        validateId(userCouponId);

        userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("회원(id:" + userId + ")이 존재하지 않습니다."));
        UserCoupon userCoupon = couponRepository.findByUserIdAndUserCouponId(userId, userCouponId).orElseThrow(() -> new NoSuchElementException("회원(id:" + userId + ")은 쿠폰(id:" + userCouponId + ")을 가지고 있지 않습니다."));

        userCoupon.expire();

        return UserCouponMapper.toResponse(userCoupon);
    }

    /**
     * 쿠폰을 삭제합니다. (쿠폰 사용과 다름)
     * @param userId
     * @param userCouponId
     * @throws IllegalArgumentException id가 0이거나 null일 경우
     * @throws NoSuchElementException 회원,쿠폰이 존재하지 않을 경우
     */
    @Transactional
    public void deleteUserCoupon(Long userId, Long userCouponId) {
        validateId(userId);
        validateId(userCouponId);

        userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("회원(id:" + userId + ")이 존재하지 않습니다."));
        UserCoupon userCoupon = couponRepository.findByUserIdAndUserCouponId(userId, userCouponId).orElseThrow(() -> new NoSuchElementException("회원(id:" + userId + ")은 쿠폰(id:" + userCouponId + ")을 가지고 있지 않습니다."));

        couponRepository.delete(userCoupon);
    }

    /**
     * 주문 금액에 대해 적용된 쿠폰 할인 금액을 반환한다.
     * @param bookId
     * @param quantity
     * @param userCouponId
     * @throws IllegalArgumentException id,quantity가 0이거나 null일 경우
     * @throws NoSuchElementException 책,쿠폰이 존재하지 않을 경우
     * @return 할인금액, 할인 전 금액, 할인 후 금액
     */
    @Transactional
    public DiscountAmountResponseDto calDiscountAmount(Long bookId, Integer quantity, Long userCouponId) {
        if (quantity < 1) {
            throw new IllegalArgumentException("책의 수량은 0보다 많아야합니다.");
        }
        validateId(bookId);
        validateId(userCouponId);
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NoSuchElementException("책(id:" + bookId + ")이 존재하지 않습니다."));
        UserCoupon userCoupon = couponRepository.findById(userCouponId).orElseThrow(() -> new NoSuchElementException("쿠폰(id:" + userCouponId + ")이 존재하지 않습니다."));
//        BookResponseDto bookDetail = bookRepository.getBookDetail(null, book.getBookId());
//        List<List<CategoryResponseDto>> categoryList = bookDetail.getCategoryList();
//        List<BookCategory> bookCategoryList = bookCategoryRepository.findByBookId(book.getId());

        List<Category> bookCategoryList = new ArrayList<>();

        // 쿠폰 적용 가능한지 확인
        if (userCoupon.getCouponType() instanceof BookCoupon bookCoupon) {
            if (!bookCoupon.getBook().getId().equals(bookId)) {
                throw new IllegalArgumentException("책 쿠폰(id:" + userCouponId + ")은 책(id:" + bookId + ")에 적용 불가능합니다.");
            }
        } else if (userCoupon.getCouponType() instanceof CategoryCoupon categoryCoupon) {
            boolean flag = true;
//            for (List<CategoryResponseDto> categoryResponseDtos : categoryList) {
//                for (CategoryResponseDto categoryResponseDto : categoryResponseDtos) {
//                    if (categoryResponseDto.getCategoryId().equals(categoryCoupon.getCategory().getCategoryId())) {
//                        flag = false;
//                    }
//                }
//            }
            Long couponCategoryId = categoryCoupon.getCategory().getId();
            for (Category bookCategory : bookCategoryList) {
                Category catagory = bookCategory;
                // 책의 가장 하위 카테고리와 카테고리 쿠폰의 id가 같은지 확인
                if (couponCategoryId.equals(catagory.getId())) {
                    flag = false;
                    break;
                }

                Category parent = catagory.getParent();
                while (Objects.nonNull(parent)) {
                    catagory = parent;
                    Long bookCategoryId = catagory.getId();
                    //상위 카테고리로 올라가면서 카테고리 쿠폰과 같은 Id인지 검증
                    if (bookCategoryId.equals(couponCategoryId)) {
                        flag = false;
                        break;
                    }

                    parent = catagory.getParent();
                }
            }
            if (flag) {
                throw new IllegalArgumentException("카테고리 쿠폰(id:" + userCouponId + ")은 책(id:" + bookId + ")에 적용 불가능합니다.");
            }
        }

        //책 주문 금액 -> 책 판매가 * 개수
        BigDecimal bookOrderPrice = book.getSalePrice().multiply(new BigDecimal(quantity));

        // 쿠폰 정책
        CouponPolicy couponPolicy = userCoupon.getCouponType().getCouponPolicy();
        // 최소 주문 금액에 못미치면
        if (couponPolicy.getMinOrderAmount().compareTo(bookOrderPrice) > 0) {
            throw new IllegalArgumentException("주문 금액(" + bookOrderPrice + ")이 쿠폰 최소 주문 금액(" + couponPolicy.getMinOrderAmount() + ")에 못미칩니다.");
        }


        if (couponPolicy.getDiscountType() == DiscountType.FIX) {
            return DiscountAmountResponseDto.builder()
                    .bookId(bookId)
                    .quantity(quantity)
                    .discountAmount(couponPolicy.getDiscountPrice())
                    .beforeCouponDiscount(bookOrderPrice)
                    .afterCouponDiscount(bookOrderPrice.subtract(couponPolicy.getDiscountPrice()))
                    .build();
        } else {
            // 소수점 첫번째 자리에서 반올림
            BigDecimal discountAmount = bookOrderPrice.multiply(couponPolicy.getDiscountRate()).divide(new BigDecimal(100),1, RoundingMode.HALF_UP);
            BigDecimal afterDiscount = bookOrderPrice.subtract(discountAmount);

            //만약 쿠폰을 적용한 할인금액이 최대 할인 금액보다 높다면
            if (couponPolicy.getMaxDiscountAmount().compareTo(discountAmount) < 0) {
                discountAmount = couponPolicy.getMaxDiscountAmount();
                afterDiscount = bookOrderPrice.subtract(discountAmount);
            }

            return DiscountAmountResponseDto.builder()
                    .bookId(bookId)
                    .quantity(quantity)
                    .discountAmount(discountAmount)
                    .beforeCouponDiscount(bookOrderPrice)
                    .afterCouponDiscount(afterDiscount)
                    .build();
        }

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

    /**
     * 쿠폰의 마감일을 계산해 반환한다.
     * @param couponType
     * @return
     */
    private LocalDateTime calUserCouponDeadline(CouponType couponType) {
        if (Objects.nonNull(couponType.getDeadline())) {
            return couponType.getDeadline();
        } else {
            return LocalDateTime.now().plusDays(couponType.getPeriod());
        }


    }
}
