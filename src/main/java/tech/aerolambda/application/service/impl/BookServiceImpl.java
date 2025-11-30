package tech.aerolambda.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.aerolambda.application.dto.PageResponse;
import tech.aerolambda.application.dto.book.BookRequest;
import tech.aerolambda.application.dto.book.BookResponse;
import tech.aerolambda.application.mapper.BookMapper;
import tech.aerolambda.application.service.BookService;
import tech.aerolambda.domain.entity.Author;
import tech.aerolambda.domain.entity.Book;
import tech.aerolambda.domain.entity.Store;
import tech.aerolambda.domain.repository.AuthorRepository;
import tech.aerolambda.domain.repository.BookRepository;
import tech.aerolambda.domain.repository.StoreRepository;
import tech.aerolambda.infrastructure.exception.DuplicateResourceException;
import tech.aerolambda.infrastructure.exception.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final StoreRepository storeRepository;
    private final BookMapper bookMapper;

    @Override
    @Transactional
    public BookResponse create(BookRequest request) {
        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new DuplicateResourceException("Book", "ISBN", request.isbn());
        }

        Book book = bookMapper.toEntity(request);
        setRelations(book, request);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponse(savedBook);
    }

    @Override
    public BookResponse findById(Long id) {
        Book book = bookRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
        return bookMapper.toResponse(book);
    }

    @Override
    public List<BookResponse> findAll() {
        return bookMapper.toResponseList(bookRepository.findAll());
    }

    @Override
    public PageResponse<BookResponse> findAll(Pageable pageable) {
        Page<Book> page = bookRepository.findAll(pageable);
        List<BookResponse> content = bookMapper.toResponseList(page.getContent());
        return PageResponse.from(page, content);
    }

    @Override
    public List<BookResponse> searchByTitle(String title) {
        return bookMapper.toResponseList(bookRepository.findByTitleContainingIgnoreCase(title));
    }

    @Override
    public PageResponse<BookResponse> searchByTitle(String title, Pageable pageable) {
        Page<Book> page = bookRepository.findByTitleContainingIgnoreCase(title, pageable);
        List<BookResponse> content = bookMapper.toResponseList(page.getContent());
        return PageResponse.from(page, content);
    }

    @Override
    public List<BookResponse> findByAuthorId(Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("Author", authorId);
        }
        return bookMapper.toResponseList(bookRepository.findByAuthorId(authorId));
    }

    @Override
    public PageResponse<BookResponse> findByAuthorId(Long authorId, Pageable pageable) {
        if (!authorRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("Author", authorId);
        }
        Page<Book> page = bookRepository.findByAuthorId(authorId, pageable);
        List<BookResponse> content = bookMapper.toResponseList(page.getContent());
        return PageResponse.from(page, content);
    }

    @Override
    public List<BookResponse> findByStoreId(Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new ResourceNotFoundException("Store", storeId);
        }
        return bookMapper.toResponseList(bookRepository.findByStoreId(storeId));
    }

    @Override
    public PageResponse<BookResponse> findByStoreId(Long storeId, Pageable pageable) {
        if (!storeRepository.existsById(storeId)) {
            throw new ResourceNotFoundException("Store", storeId);
        }
        Page<Book> page = bookRepository.findByStoreId(storeId, pageable);
        List<BookResponse> content = bookMapper.toResponseList(page.getContent());
        return PageResponse.from(page, content);
    }

    @Override
    public List<BookResponse> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return bookMapper.toResponseList(bookRepository.findByPriceBetween(minPrice, maxPrice));
    }

    @Override
    public PageResponse<BookResponse> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Page<Book> page = bookRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        List<BookResponse> content = bookMapper.toResponseList(page.getContent());
        return PageResponse.from(page, content);
    }

    @Override
    @Transactional
    public BookResponse update(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));

        if (!book.getIsbn().equals(request.isbn()) && bookRepository.existsByIsbn(request.isbn())) {
            throw new DuplicateResourceException("Book", "ISBN", request.isbn());
        }

        bookMapper.updateEntity(request, book);
        setRelations(book, request);
        Book updatedBook = bookRepository.save(book);
        return bookMapper.toResponse(updatedBook);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book", id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> findAllEntities() {
        return bookRepository.findAll();
    }

    private void setRelations(Book book, BookRequest request) {
        if (request.authorId() != null) {
            Author author = authorRepository.findById(request.authorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Author", request.authorId()));
            book.setAuthor(author);
        }

        if (request.storeId() != null) {
            Store store = storeRepository.findById(request.storeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store", request.storeId()));
            book.setStore(store);
        }
    }
}
