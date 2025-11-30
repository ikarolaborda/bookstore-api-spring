package tech.aerolambda.application.service;

import org.springframework.data.domain.Pageable;
import tech.aerolambda.application.dto.PageResponse;
import tech.aerolambda.application.dto.book.BookRequest;
import tech.aerolambda.application.dto.book.BookResponse;
import tech.aerolambda.domain.entity.Book;

import java.math.BigDecimal;
import java.util.List;

public interface BookService {

    BookResponse create(BookRequest request);

    BookResponse findById(Long id);

    List<BookResponse> findAll();

    PageResponse<BookResponse> findAll(Pageable pageable);

    List<BookResponse> searchByTitle(String title);

    PageResponse<BookResponse> searchByTitle(String title, Pageable pageable);

    List<BookResponse> findByAuthorId(Long authorId);

    PageResponse<BookResponse> findByAuthorId(Long authorId, Pageable pageable);

    List<BookResponse> findByStoreId(Long storeId);

    PageResponse<BookResponse> findByStoreId(Long storeId, Pageable pageable);

    List<BookResponse> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    PageResponse<BookResponse> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    BookResponse update(Long id, BookRequest request);

    void delete(Long id);

    List<Book> findAllEntities();
}
