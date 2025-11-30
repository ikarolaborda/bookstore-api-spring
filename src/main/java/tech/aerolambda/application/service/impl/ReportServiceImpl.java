package tech.aerolambda.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.aerolambda.application.service.BookService;
import tech.aerolambda.application.service.ReportService;
import tech.aerolambda.domain.entity.Author;
import tech.aerolambda.domain.entity.Book;
import tech.aerolambda.domain.entity.Store;
import tech.aerolambda.domain.entity.User;
import tech.aerolambda.domain.enums.ReportFormat;
import tech.aerolambda.domain.enums.ReportType;
import tech.aerolambda.domain.repository.AuthorRepository;
import tech.aerolambda.domain.repository.BookRepository;
import tech.aerolambda.domain.repository.StoreRepository;
import tech.aerolambda.domain.repository.UserRepository;
import tech.aerolambda.presentation.dto.report.ReportRequest;
import tech.aerolambda.report.ReportData;
import tech.aerolambda.report.factory.ReportFactory;
import tech.aerolambda.report.strategy.ReportStrategy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final ReportFactory reportFactory;
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Override
    public byte[] generateBooksReport(ReportFormat format, Integer limit) {
        ReportStrategy strategy = reportFactory.getStrategy(format);
        List<Book> books = bookService.findAllEntities();
        if (limit != null && limit > 0 && limit < books.size()) {
            books = books.subList(0, limit);
        }
        return strategy.generate(books);
    }

    @Override
    public byte[] generateReport(ReportType type, ReportFormat format, ReportRequest request) {
        ReportStrategy strategy = reportFactory.getStrategy(format);

        return switch (type) {
            case BOOKS -> generateBooksReportData(strategy, request);
            case AUTHORS -> generateAuthorsReportData(strategy, request);
            case USERS -> generateUsersReportData(strategy, request);
            case STORES -> generateStoresReportData(strategy, request);
            case BOOKS_BY_AUTHOR -> generateBooksByAuthorReportData(strategy, request);
        };
    }

    private byte[] generateBooksReportData(ReportStrategy strategy, ReportRequest request) {
        List<Book> books = bookRepository.findAll();

        books = filterByDate(books, request);

        if (request.getLimit() != null && request.getLimit() > 0 && request.getLimit() < books.size()) {
            books = books.subList(0, request.getLimit());
        }

        ReportData<Book> reportData = ReportData.<Book>builder()
                .reportType(ReportType.BOOKS)
                .title("Books Report")
                .headers(List.of("ID", "Title", "ISBN", "Price", "Author", "Store", "Created At"))
                .data(books)
                .rowMapper(book -> List.of(
                        String.valueOf(book.getId()),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getPrice() != null ? "$" + book.getPrice().toString() : "N/A",
                        book.getAuthor() != null ? book.getAuthor().getName() : "N/A",
                        book.getStore() != null ? book.getStore().getName() : "N/A",
                        book.getCreatedAt() != null ? book.getCreatedAt().toLocalDate().toString() : "N/A"
                ))
                .build();

        return strategy.generate(reportData);
    }

    private byte[] generateAuthorsReportData(ReportStrategy strategy, ReportRequest request) {
        List<Author> authors = authorRepository.findAll();

        if (request.getStartDate() != null || request.getEndDate() != null) {
            authors = authors.stream()
                    .filter(a -> filterByCreatedAt(a.getCreatedAt(), request))
                    .collect(Collectors.toList());
        }

        if (request.getLimit() != null && request.getLimit() > 0 && request.getLimit() < authors.size()) {
            authors = authors.subList(0, request.getLimit());
        }

        ReportData<Author> reportData = ReportData.<Author>builder()
                .reportType(ReportType.AUTHORS)
                .title("Authors Report")
                .headers(List.of("ID", "Name", "Bio", "Books Count", "Created At"))
                .data(authors)
                .rowMapper(author -> List.of(
                        String.valueOf(author.getId()),
                        author.getName(),
                        author.getBio() != null ? truncate(author.getBio(), 50) : "N/A",
                        String.valueOf(author.getBooks() != null ? author.getBooks().size() : 0),
                        author.getCreatedAt() != null ? author.getCreatedAt().toLocalDate().toString() : "N/A"
                ))
                .build();

        return strategy.generate(reportData);
    }

    private byte[] generateUsersReportData(ReportStrategy strategy, ReportRequest request) {
        List<User> users = userRepository.findAll();

        if (request.getStartDate() != null || request.getEndDate() != null) {
            users = users.stream()
                    .filter(u -> filterByCreatedAt(u.getCreatedAt(), request))
                    .collect(Collectors.toList());
        }

        if (request.getLimit() != null && request.getLimit() > 0 && request.getLimit() < users.size()) {
            users = users.subList(0, request.getLimit());
        }

        ReportData<User> reportData = ReportData.<User>builder()
                .reportType(ReportType.USERS)
                .title("System Users Report")
                .headers(List.of("ID", "Name", "Email", "Role", "Enabled", "Created At"))
                .data(users)
                .rowMapper(user -> List.of(
                        String.valueOf(user.getId()),
                        user.getName(),
                        user.getEmail(),
                        user.getRole().name(),
                        user.isEnabled() ? "Yes" : "No",
                        user.getCreatedAt() != null ? user.getCreatedAt().toLocalDate().toString() : "N/A"
                ))
                .build();

        return strategy.generate(reportData);
    }

    private byte[] generateStoresReportData(ReportStrategy strategy, ReportRequest request) {
        List<Store> stores = storeRepository.findAll();

        if (request.getStartDate() != null || request.getEndDate() != null) {
            stores = stores.stream()
                    .filter(s -> filterByCreatedAt(s.getCreatedAt(), request))
                    .collect(Collectors.toList());
        }

        if (request.getLimit() != null && request.getLimit() > 0 && request.getLimit() < stores.size()) {
            stores = stores.subList(0, request.getLimit());
        }

        ReportData<Store> reportData = ReportData.<Store>builder()
                .reportType(ReportType.STORES)
                .title("Stores Report")
                .headers(List.of("ID", "Name", "Address", "Phone", "Email", "Books Count", "Created At"))
                .data(stores)
                .rowMapper(store -> List.of(
                        String.valueOf(store.getId()),
                        store.getName(),
                        store.getAddress() != null ? store.getAddress() : "N/A",
                        store.getPhone() != null ? store.getPhone() : "N/A",
                        store.getEmail() != null ? store.getEmail() : "N/A",
                        String.valueOf(store.getBooks() != null ? store.getBooks().size() : 0),
                        store.getCreatedAt() != null ? store.getCreatedAt().toLocalDate().toString() : "N/A"
                ))
                .build();

        return strategy.generate(reportData);
    }

    private byte[] generateBooksByAuthorReportData(ReportStrategy strategy, ReportRequest request) {
        if (request.getAuthorId() == null) {
            throw new IllegalArgumentException("Author ID is required for Books by Author report");
        }

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("Author not found with ID: " + request.getAuthorId()));

        List<Book> books = bookRepository.findByAuthorId(request.getAuthorId());

        books = filterByDate(books, request);

        if (request.getLimit() != null && request.getLimit() > 0 && request.getLimit() < books.size()) {
            books = books.subList(0, request.getLimit());
        }

        ReportData<Book> reportData = ReportData.<Book>builder()
                .reportType(ReportType.BOOKS_BY_AUTHOR)
                .title("Books by Author: " + author.getName())
                .headers(List.of("ID", "Title", "ISBN", "Price", "Publication Year", "Store", "Created At"))
                .data(books)
                .rowMapper(book -> List.of(
                        String.valueOf(book.getId()),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getPrice() != null ? "$" + book.getPrice().toString() : "N/A",
                        book.getPublicationYear() != null ? String.valueOf(book.getPublicationYear()) : "N/A",
                        book.getStore() != null ? book.getStore().getName() : "N/A",
                        book.getCreatedAt() != null ? book.getCreatedAt().toLocalDate().toString() : "N/A"
                ))
                .build();

        return strategy.generate(reportData);
    }

    private List<Book> filterByDate(List<Book> books, ReportRequest request) {
        if (request.getStartDate() != null || request.getEndDate() != null) {
            return books.stream()
                    .filter(b -> filterByCreatedAt(b.getCreatedAt(), request))
                    .collect(Collectors.toList());
        }
        return books;
    }

    private boolean filterByCreatedAt(LocalDateTime createdAt, ReportRequest request) {
        if (createdAt == null) return true;

        if (request.getStartDate() != null && createdAt.toLocalDate().isBefore(request.getStartDate())) {
            return false;
        }
        if (request.getEndDate() != null && createdAt.toLocalDate().isAfter(request.getEndDate())) {
            return false;
        }
        return true;
    }

    private String truncate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }

    @Override
    public String getContentType(ReportFormat format) {
        return reportFactory.getStrategy(format).getContentType();
    }

    @Override
    public String getFileExtension(ReportFormat format) {
        return reportFactory.getStrategy(format).getFileExtension();
    }

    @Override
    public ReportType[] getAvailableReportTypes(boolean isAdmin) {
        if (isAdmin) {
            return ReportType.values();
        }
        return Arrays.stream(ReportType.values())
                .filter(type -> type != ReportType.USERS)
                .toArray(ReportType[]::new);
    }
}
