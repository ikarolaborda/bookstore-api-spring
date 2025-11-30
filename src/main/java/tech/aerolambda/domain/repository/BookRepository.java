package tech.aerolambda.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.aerolambda.domain.entity.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorId(Long authorId);

    List<Book> findByStoreId(Long storeId);

    List<Book> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Book> findByPublicationYear(Integer year);

    @Query("SELECT b FROM Book b WHERE b.author.name LIKE %:authorName%")
    List<Book> findByAuthorNameContaining(@Param("authorName") String authorName);

    @Query("SELECT b FROM Book b JOIN FETCH b.author JOIN FETCH b.store WHERE b.id = :id")
    Optional<Book> findByIdWithRelations(@Param("id") Long id);

    boolean existsByIsbn(String isbn);

    @EntityGraph(attributePaths = {"author", "store"})
    Page<Book> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"author", "store"})
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "store"})
    Page<Book> findByAuthorId(Long authorId, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "store"})
    Page<Book> findByStoreId(Long storeId, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "store"})
    Page<Book> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "store"})
    @Query("SELECT b FROM Book b WHERE b.author.name LIKE %:authorName%")
    Page<Book> findByAuthorNameContaining(@Param("authorName") String authorName, Pageable pageable);
}
