package org.simsimbooks.couponserver.book.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookRequestDto {
    private String title;
    private BigDecimal salePrice;
}
