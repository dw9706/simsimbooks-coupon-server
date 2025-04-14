package org.simsimbooks.couponserver.coupons.usercoupon;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.simsimbooks.couponserver.coupons.usercoupon.dto.IssueUserCouponsRequestDto;
import org.simsimbooks.couponserver.coupons.usercoupon.dto.UserCouponResponseDto;
import org.simsimbooks.couponserver.coupons.usercoupon.dto.DiscountAmountResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/user-coupons")
public class UserCouponController {
    private final UserCouponService userCouponService;
//    private final CouponDiscountService couponDiscountService;

//    @GetMapping("/admin/coupons")
//    public ResponseEntity<Page<CouponResponseDto>> getTotalCoupons(Pageable pageable) {
//        Page<CouponResponseDto> totalCoupons = couponService.getTotalCoupons(setPageable(pageable,null));
//        return ResponseEntity.status(HttpStatus.OK).body(totalCoupons);
//    }
    /**
     * 특정 쿠폰(couponId) 하나를 응답한다.
     * @param userCouponId
     * @return 쿠폰
     */
    @GetMapping("/{userCouponId}")
    public ResponseEntity<UserCouponResponseDto> getUserCoupon(@PathVariable Long userCouponId) {
        UserCouponResponseDto userCouponById = userCouponService.getCouponById(userCouponId);
        return ResponseEntity.status(HttpStatus.OK).body(userCouponById);
    }

    /**
     * 유저가 가진 쿠폰 중 특정(couponTypeId) 쿠폰 타입인 미사용 쿠폰을 응답한다.
     * @param userId
     * @param couponTypeId
     * @return 미사용된 쿠폰
     */
    //이거 Get매핑 바꾸기 userId RequestParam으로
    @GetMapping(value = "/shop/users/{userId}/userCoupons/unused",params = "couponTypeId")
    public ResponseEntity<UserCouponResponseDto> getUnusedUserCouponByCouponType(@PathVariable Long userId,
                                                                             @RequestParam Long couponTypeId) {
        UserCouponResponseDto unusedUserCoupon = userCouponService.getUnusedUserCouponByCouponType(userId, couponTypeId);
        return ResponseEntity.status(HttpStatus.OK).body(unusedUserCoupon);
    }

    /**
     * 유저가 사용한 쿠폰 할인 내역을 Page로 가져온다.
     * @param userId
     * @param pageable
     * @return
     */
//    @GetMapping("/shop/users/{user-id}/coupon-discounts")
//    public ResponseEntity<Page<UserCouponDiscountResponseDto>> getUserCouponDiscount(@PathVariable(name = "user-id") Long userId,
//                                                                                     Pageable pageable) {
//        Page<UserCouponDiscountResponseDto> userCouponDiscountPage = couponDiscountService.getUserCouponDiscount(userId, pageable);
//        return ResponseEntity.status(HttpStatus.OK).body(userCouponDiscountPage);
//    }

    /**
     * 유저가 가진 쿠폰을 Page로 응답한다.
     * 만료, 미사용, 사용된 쿠폰 상관없이 가지고 와서 응답한다.
     * @param userId
     * @param sortField 쿠폰 정렬기준 (null일 경우 발급일(issueDate)를 기준으로 정렬함)
     * @param pageable
     * @return 유저의 쿠폰 Page
     */
    @GetMapping("/admin/users/{userId}/userCoupons")
    public ResponseEntity<Page<UserCouponResponseDto>> getUserCoupons(@PathVariable Long userId,
                                                                  @RequestParam(required = false) String sortField,
                                                                  Pageable pageable) {
        Page<UserCouponResponseDto> userCoupons = userCouponService.getCoupons(setPageable(pageable, sortField), userId);
        return ResponseEntity.status(HttpStatus.OK).body(userCoupons);
    }

    /**
     * 유저의 쿠폰 중 미사용된 쿠폰을 Page로 응답한다..
     * @param userId
     * @param sortField 쿠폰 정렬기준 (null일 경우 발급일(issueDate)를 기준으로 정렬함)
     * @param pageable
     * @return 유저의 미사용 쿠폰 Page
     */
    @GetMapping(value ="/shop/users/{userId}/userCoupons/unused", params = {"!bookId", "!couponTypeId"})
    public ResponseEntity<Page<UserCouponResponseDto>> getUnusedUserCoupons(@PathVariable Long userId,
                                                                        @RequestParam(required = false) String sortField,
                                                                        Pageable pageable) {
        Page<UserCouponResponseDto> unusedUserCoupons = userCouponService.getUnusedCoupons(setPageable(pageable, sortField), userId);
        return ResponseEntity.status(HttpStatus.OK).body(unusedUserCoupons);
    }

    /**
     * 유저가 가지고 있는 쿠폰 중 특정 책(bookId)에 적용 가능한 쿠폰을 Page로 응답한다.
     * @param userId
     * @param bookId
     * @return 적용가능한 쿠폰 Page
     */
    @GetMapping(value = "/shop/users/{userId}/userCoupons/unused",params = {"bookId","quantity"})
    public ResponseEntity<List<UserCouponResponseDto>> getEligibleUserCouponsToBook(@PathVariable Long userId,
                                                                                @RequestParam Long bookId,
                                                                                @RequestParam int quantity) {
        List<UserCouponResponseDto> eligibleUserCoupons = userCouponService.getEligibleCoupons(userId, bookId, quantity);
        return ResponseEntity.status(HttpStatus.OK).body(eligibleUserCoupons);

    }

    /**
     * 유저들에게 쿠폰을 발행한다.
     * @param requestDto 여러 유저id, 쿠폰 타입 id
     * @return 발행된 쿠폰들
     */
    @PostMapping("/admin/coupons/issue")
    public ResponseEntity<Void> issueUserCoupons(@Valid @RequestBody IssueUserCouponsRequestDto requestDto) {
        userCouponService.issueCoupons(requestDto.getUserIds(), requestDto.getCouponTypeId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 유저의 쿠폰을 만료시킵니다.
     * @param userId
     * @param userCouponId
     * @return 만료된 쿠폰
     */
    @PostMapping("/admin/users/{userId}/coupons/expired")
    public ResponseEntity<UserCouponResponseDto> expiredUserCoupon(@PathVariable Long userId,
                                                               @RequestParam Long userCouponId) {
        UserCouponResponseDto expireUserCoupon = userCouponService.expireCoupon(userId, userCouponId);
        return ResponseEntity.status(HttpStatus.OK).body(expireUserCoupon);
    }

    /**
     * 쿠폰을 사용합니다.
     * @param userId
     * @param userCouponId
     * @return 사용한 쿠폰
     */
    @PostMapping("/shop/users/{userId}/coupons/use")
    public ResponseEntity<UserCouponResponseDto> useUserCoupon(@PathVariable Long userId,
                                                           @RequestParam Long userCouponId) {
        UserCouponResponseDto useUserCoupon = userCouponService.useCoupon(userId, userCouponId);
        return ResponseEntity.status(HttpStatus.OK).body(useUserCoupon);
    }

    /**
     * 유저의 쿠폰을 삭제합니다.
     * @param userId
     * @param userCouponId
     * @return
     */
    @DeleteMapping("/admin/users/{userId}/userCoupons/{userCouponId}")
    public ResponseEntity<Void> deleteUserCoupon(@PathVariable  Long userId,
                                       @PathVariable Long userCouponId) {
        userCouponService.deleteCoupon(userId, userCouponId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 쿠폰의 할인액을 계산합니다.
     * @param userCouponId
     * @param bookId
     * @param quantity
     * @return
     */
    @GetMapping("/shop/coupons/{userCouponId}/calculate")
    public ResponseEntity<DiscountAmountResponseDto> calDiscountAmount(@PathVariable Long userCouponId,
                                                                       @RequestParam Long bookId,
                                                                       @RequestParam @PositiveOrZero(message = "수량은 0 이상이어야 합니다.") Integer quantity) {
        DiscountAmountResponseDto discountAmountResponseDto = userCouponService.calDiscountAmount(bookId, quantity, userCouponId);
        return ResponseEntity.status(HttpStatus.OK).body(discountAmountResponseDto);
    }

    /**
     * 만료된 쿠폰을 모두 가져온다.
     * @return
     */
    @GetMapping("/admin/coupons/expired")
    public ResponseEntity<List<UserCouponResponseDto>> getExpiredCoupons() {
        List<UserCouponResponseDto> userCoupons = userCouponService.getExpiredCoupons();
        return ResponseEntity.status(HttpStatus.OK).body(userCoupons);
    }

    /**
     * 쿠폰의 상태는 UNUSED상태이지만 deadline이 끝난 쿠폰들을 모두 가져온다.
     * @return
     */
    @GetMapping("/admin/coupons/unused/deadline-pass")
    public ResponseEntity<List<UserCouponResponseDto>> getUnusedButDeadlinePassedCoupon() {
        List<UserCouponResponseDto> userCoupons = userCouponService.getUnusedButDeadlinePassedCoupon();
        return ResponseEntity.status(HttpStatus.OK).body(userCoupons);
    }




    private Pageable setPageable(Pageable pageable, String sortField) {
        String sortBy = (StringUtils.isEmpty(sortField) && StringUtils.hasText(sortField) ? "issueDate" : sortField);
        return PageRequest.of(
                pageable.getPageNumber(),
                10, //페이지 사이즈를 10으로 고정
                Sort.by(Sort.Direction.DESC, sortBy)
        );
    }
}
