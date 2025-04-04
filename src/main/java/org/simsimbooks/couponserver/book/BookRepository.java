package org.simsimbooks.couponserver.book;

import org.simsimbooks.couponserver.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {
}
