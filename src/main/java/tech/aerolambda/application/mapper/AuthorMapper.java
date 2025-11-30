package tech.aerolambda.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import tech.aerolambda.application.dto.author.AuthorRequest;
import tech.aerolambda.application.dto.author.AuthorResponse;
import tech.aerolambda.domain.entity.Author;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        builder = @org.mapstruct.Builder(disableBuilder = true))
public interface AuthorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "books", ignore = true)
    Author toEntity(AuthorRequest request);

    @Mapping(target = "bookCount", expression = "java(author.getBooks() != null ? author.getBooks().size() : 0)")
    AuthorResponse toResponse(Author author);

    List<AuthorResponse> toResponseList(List<Author> authors);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "books", ignore = true)
    void updateEntity(AuthorRequest request, @MappingTarget Author author);
}
