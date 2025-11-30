package tech.aerolambda.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import tech.aerolambda.application.dto.book.BookRequest;
import tech.aerolambda.application.dto.book.BookResponse;
import tech.aerolambda.domain.entity.Author;
import tech.aerolambda.domain.entity.Book;
import tech.aerolambda.domain.entity.Store;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        builder = @org.mapstruct.Builder(disableBuilder = true))
public interface BookMapper {

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Book toEntity(BookRequest request);

    @Mapping(target = "author", source = "author")
    @Mapping(target = "store", source = "store")
    BookResponse toResponse(Book book);

    List<BookResponse> toResponseList(List<Book> books);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(BookRequest request, @MappingTarget Book book);

    default BookResponse.AuthorSummary toAuthorSummary(Author author) {
        if (author == null) {
            return null;
        }
        return new BookResponse.AuthorSummary(author.getId(), author.getName());
    }

    default BookResponse.StoreSummary toStoreSummary(Store store) {
        if (store == null) {
            return null;
        }
        return new BookResponse.StoreSummary(store.getId(), store.getName());
    }
}
