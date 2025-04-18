package org.simsimbooks.couponserver.book.mapper;

import org.simsimbooks.couponserver.book.dto.BookRequestDto;
import org.simsimbooks.couponserver.book.dto.BookResponseDto;
import org.simsimbooks.couponserver.book.entity.Book;

public class BookMapper {
    public static BookResponseDto toResponse(Book book) {
        BookResponseDto responseDto = new BookResponseDto();
        responseDto.setId(book.getId());
        responseDto.setTitle(book.getTitle());
        responseDto.setSalePrice(book.getSalePrice());
        return responseDto;
    }

    public static Book toBook(BookRequestDto requestDto) {
        return Book.builder()
                .title(requestDto.getTitle())
                .salePrice(requestDto.getSalePrice())
                .build();
    }
}
