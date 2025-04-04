package org.simsimbooks.couponserver.book.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Builder
@Getter
@Setter
public class BookRequestDto {
    private String title;
    private BigDecimal salePrice;
}
