package org.simsimbooks.couponserver.book;

import lombok.RequiredArgsConstructor;
import org.simsimbooks.couponserver.book.dto.BookRequestDto;
import org.simsimbooks.couponserver.book.dto.BookResponseDto;
import org.simsimbooks.couponserver.book.entity.Book;
import org.simsimbooks.couponserver.book.mapper.BookMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    @Transactional
    public BookResponseDto createBook(BookRequestDto requestDto) {
        Book save = bookRepository.save(BookMapper.toBook(requestDto));
        return BookMapper.toResponse(save);
    }

    public BookResponseDto getBook(Long bookId) {
        if (Objects.isNull(bookId)) {
            throw new IllegalArgumentException("bookId is null");
        }

        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isEmpty()) {
            throw new NoSuchElementException("id : " + bookId + "인 book이 없습니다.");
        }

        return BookMapper.toResponse(optionalBook.get());
    }

    @Transactional
    public BookResponseDto updateBook(Long bookId, BookRequestDto requestDto) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isEmpty()) {
            throw new NoSuchElementException("id : " + bookId + "인 book이 없습니다.");
        }

        Book book = optionalBook.get();
        Optional.ofNullable(requestDto.getTitle()).ifPresent(book::setTitle);
        Optional.ofNullable(requestDto.getSalePrice()).ifPresent(book::setSalePrice);

        return BookMapper.toResponse(book);
    }

    @Transactional
    public void deleteBook(Long bookId) {
        if (Objects.isNull(bookId)) {
            throw new IllegalArgumentException("bookId is null");
        }

        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isEmpty()) {
            throw new NoSuchElementException("id : " + bookId + "인 book이 없습니다.");
        }

        bookRepository.delete(optionalBook.get());
    }
}
