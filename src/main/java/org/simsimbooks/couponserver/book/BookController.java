package org.simsimbooks.couponserver.book;

import lombok.RequiredArgsConstructor;
import org.simsimbooks.couponserver.book.dto.BookRequestDto;
import org.simsimbooks.couponserver.book.dto.BookResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping // Create
    public ResponseEntity<BookResponseDto> createBook(@RequestBody BookRequestDto requestDto) {
        BookResponseDto response = bookService.createBook(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{bookId}") // Read
    public ResponseEntity<BookResponseDto> getBook(@PathVariable Long bookId) {
        BookResponseDto response = bookService.getBook(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{bookId}") //Update
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable Long bookId,
                                                      @RequestBody BookRequestDto requestDto) {
        BookResponseDto response = bookService.updateBook(bookId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{bookId}") //Delete
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
