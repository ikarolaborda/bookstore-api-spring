package tech.aerolambda.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import tech.aerolambda.domain.entity.Author;
import tech.aerolambda.domain.entity.Book;
import tech.aerolambda.domain.entity.Store;
import tech.aerolambda.domain.entity.User;
import tech.aerolambda.domain.enums.UserRole;
import tech.aerolambda.domain.repository.AuthorRepository;
import tech.aerolambda.domain.repository.BookRepository;
import tech.aerolambda.domain.repository.StoreRepository;
import tech.aerolambda.domain.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final StoreRepository storeRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;
    private final DataGenerator dataGenerator;

    @Value("${app.seed.enabled:true}")
    private boolean seedEnabled;

    @Value("${app.seed.authors:100}")
    private int authorCount;

    @Value("${app.seed.stores:50}")
    private int storeCount;

    @Value("${app.seed.books:5000}")
    private int bookCount;

    @Value("${app.seed.users:10}")
    private int userCount;

    @Bean
    CommandLineRunner seedDatabase() {
        return args -> {
            seedAdminUser();
            if (seedEnabled) {
                seedData();
            }
        };
    }

    private void seedAdminUser() {
        if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
            User admin = User.builder()
                    .name("Super Admin")
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(UserRole.ADMIN)
                    .enabled(true)
                    .build();

            userRepository.save(admin);
            log.info("Default admin user created: admin@admin.com / admin123");
        }
    }

    @Transactional
    public void seedData() {
        if (bookRepository.count() > 0) {
            log.info("Database already contains data, skipping seed");
            return;
        }

        log.info("Starting data seed: {} authors, {} stores, {} books, {} users",
                authorCount, storeCount, bookCount, userCount);

        long startTime = System.currentTimeMillis();

        List<Author> authors = seedAuthors();
        List<Store> stores = seedStores();
        seedUsers();
        seedBooks(authors, stores);

        long duration = System.currentTimeMillis() - startTime;
        log.info("Data seed completed in {} ms", duration);
    }

    private List<Author> seedAuthors() {
        log.info("Seeding {} authors...", authorCount);
        List<Author> authors = new ArrayList<>();
        Set<String> usedNames = new HashSet<>();

        for (int i = 0; i < authorCount; i++) {
            String name;
            do {
                name = dataGenerator.generateAuthorName();
            } while (usedNames.contains(name));
            usedNames.add(name);

            Author author = Author.builder()
                    .name(name)
                    .bio(dataGenerator.generateAuthorBio(name))
                    .build();
            authors.add(author);
        }

        List<Author> savedAuthors = authorRepository.saveAll(authors);
        log.info("Created {} authors", savedAuthors.size());
        return savedAuthors;
    }

    private List<Store> seedStores() {
        log.info("Seeding {} stores...", storeCount);
        List<Store> stores = new ArrayList<>();
        Set<String> usedNames = new HashSet<>();

        for (int i = 0; i < storeCount; i++) {
            String name;
            do {
                name = dataGenerator.generateStoreName();
            } while (usedNames.contains(name));
            usedNames.add(name);

            Store store = Store.builder()
                    .name(name)
                    .address(dataGenerator.generateAddress())
                    .phone(dataGenerator.generatePhone())
                    .email(dataGenerator.generateEmail(name))
                    .build();
            stores.add(store);
        }

        List<Store> savedStores = storeRepository.saveAll(stores);
        log.info("Created {} stores", savedStores.size());
        return savedStores;
    }

    private void seedUsers() {
        log.info("Seeding {} users...", userCount);
        List<User> users = new ArrayList<>();
        Set<String> usedEmails = new HashSet<>();
        usedEmails.add("admin@admin.com");

        for (int i = 0; i < userCount; i++) {
            String name = dataGenerator.generateUserName();
            String email;
            do {
                email = dataGenerator.generateUserEmail(name);
            } while (usedEmails.contains(email));
            usedEmails.add(email);

            UserRole role = i < 2 ? UserRole.ADMIN : UserRole.USER;

            User user = User.builder()
                    .name(name)
                    .email(email)
                    .password(passwordEncoder.encode("password123"))
                    .role(role)
                    .enabled(true)
                    .build();
            users.add(user);
        }

        userRepository.saveAll(users);
        log.info("Created {} users", users.size());
    }

    private void seedBooks(List<Author> authors, List<Store> stores) {
        log.info("Seeding {} books...", bookCount);
        List<Book> books = new ArrayList<>();
        Set<String> usedIsbns = new HashSet<>();
        Set<String> usedTitles = new HashSet<>();
        int batchSize = 500;

        for (int i = 0; i < bookCount; i++) {
            String title;
            do {
                title = dataGenerator.generateBookTitle();
            } while (usedTitles.contains(title));
            usedTitles.add(title);

            String isbn;
            do {
                isbn = dataGenerator.generateIsbn();
            } while (usedIsbns.contains(isbn));
            usedIsbns.add(isbn);

            Author author = authors.get(dataGenerator.nextInt(authors.size()));
            Store store = stores.get(dataGenerator.nextInt(stores.size()));

            Book book = Book.builder()
                    .title(title)
                    .isbn(isbn)
                    .price(dataGenerator.generatePrice())
                    .publicationYear(dataGenerator.generatePublicationYear())
                    .description(dataGenerator.generateBookDescription(author.getName()))
                    .author(author)
                    .store(store)
                    .build();
            books.add(book);

            if (books.size() >= batchSize) {
                bookRepository.saveAll(books);
                books.clear();
                log.info("Saved {} books so far...", i + 1);
            }
        }

        if (!books.isEmpty()) {
            bookRepository.saveAll(books);
        }

        log.info("Created {} books", bookCount);
    }
}
