-- 1. 쿠폰할인정책 (coupon_policies)
CREATE TABLE IF NOT EXISTS coupon_policies (
    coupon_policy_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    coupon_policy_name VARCHAR(100) NOT NULL,               -- 쿠폰 정책 이름
    discount_type VARCHAR(10) NOT NULL CHECK(discount_type IN ('RATE','FIX')),  -- 할인 형태 (비율 / 고정)
    discount_price DECIMAL(10,2) NULL,                      -- 할인 금액 또는 할인율
    discount_rate DECIMAL(10,2) NULL,
    max_discount_amount DECIMAL(10,2) NULL,                 -- 최대 할인 금액
    min_order_amount DECIMAL(10,2) NULL,                    -- 최소 주문 금액
    policy_description TEXT                                 -- 정책 상세 설명
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 쿠폰종류 (coupon_types)
CREATE TABLE IF NOT EXISTS coupon_types (
    coupon_type_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    coupon_type_name VARCHAR(40) NOT NULL,                -- 쿠폰종류명
    period INT NULL,                                      -- 기간 (예: 7, 30)
    deadline TIMESTAMP NULL,                              -- 마감일
    coupon_policy_id BIGINT NOT NULL,                     -- coupon_policies 테이블과 연결
    DTYPE VARCHAR(50) NOT NULL,                           -- 쿠폰 적용 대상 (예: AllCoupon, BookCoupon, CategoryCoupon 등)
    CONSTRAINT fk_coupon_types_policy
    FOREIGN KEY (coupon_policy_id)
    REFERENCES coupon_policies(coupon_policy_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 회원 (users)
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,                       -- 이름
    email VARCHAR(255),                                   -- 이메일
    gender VARCHAR(10) NOT NULL CHECK(gender IN ('MALE','FEMALE'))
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 쿠폰 (coupons)
CREATE TABLE IF NOT EXISTS coupons (
   coupon_id BIGINT AUTO_INCREMENT PRIMARY KEY,
   issue_date TIMESTAMP NOT NULL,                       -- 발급일
   deadline TIMESTAMP NOT NULL,                         -- 마감일
   coupon_status VARCHAR(10) NOT NULL CHECK(coupon_status IN ('USED','EXPIRED','UNUSED')),
    use_date TIMESTAMP,                                  -- 사용일 (NULL 가능)
    coupon_type_id BIGINT NOT NULL,                      -- coupon_types 테이블과 연결
    user_id BIGINT NOT NULL,                             -- users 테이블과 연결
    CONSTRAINT fk_coupons_coupon_type
    FOREIGN KEY (coupon_type_id)
    REFERENCES coupon_types(coupon_type_id),
    CONSTRAINT fk_coupons_user
    FOREIGN KEY (user_id)
    REFERENCES users(user_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. 도서 (books)
CREATE TABLE IF NOT EXISTS books (
     book_id BIGINT AUTO_INCREMENT PRIMARY KEY,
     title TEXT NOT NULL,                                 -- 제목
     sale_price DECIMAL(10,2) NOT NULL,-- 판매가
     category_id BIGINT NOT NULL,
     CONSTRAINT books_categories__fk
     FOREIGN KEY(category_id)
     REFERENCES categories(category_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. 카테고리 (categories)
CREATE TABLE IF NOT EXISTS categories (
  category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  category_name VARCHAR(30) NOT NULL,                  -- 카테고리명
    parent_id BIGINT,                                    -- 상위 카테고리 (NULL 가능)
    CONSTRAINT fk_categories_self
    FOREIGN KEY (parent_id)
    REFERENCES categories(category_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 8. 책쿠폰 (book_coupons)
CREATE TABLE IF NOT EXISTS book_coupons (
    coupon_type_id BIGINT NOT NULL,                      -- coupon_types 테이블과 연결
    book_id BIGINT NOT NULL,                             -- books 테이블과 연결
    PRIMARY KEY (coupon_type_id),
    CONSTRAINT fk_book_coupons_coupon_type
    FOREIGN KEY (coupon_type_id)
    REFERENCES coupon_types(coupon_type_id),
    CONSTRAINT fk_book_coupons_book
    FOREIGN KEY (book_id)
    REFERENCES books(book_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9. 카테고리쿠폰 (category_coupons)
CREATE TABLE IF NOT EXISTS category_coupons (
    coupon_type_id BIGINT NOT NULL,                      -- coupon_types 테이블과 연결
    category_id BIGINT NOT NULL,                         -- categories 테이블과 연결
    PRIMARY KEY (coupon_type_id),
    CONSTRAINT fk_category_coupons_coupon_type
    FOREIGN KEY (coupon_type_id)
    REFERENCES coupon_types(coupon_type_id),
    CONSTRAINT fk_category_coupons_category
    FOREIGN KEY (category_id)
    REFERENCES categories(category_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10. 전체쿠폰 (all_coupons)
CREATE TABLE IF NOT EXISTS all_coupons (
   coupon_type_id BIGINT NOT NULL,                      -- coupon_types 테이블과 연결
   PRIMARY KEY (coupon_type_id),
    CONSTRAINT fk_all_coupons_coupon_type
    FOREIGN KEY (coupon_type_id)
    REFERENCES coupon_types(coupon_type_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
