package tech.aerolambda.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import tech.aerolambda.application.dto.store.StoreRequest;
import tech.aerolambda.application.dto.store.StoreResponse;
import tech.aerolambda.domain.entity.Store;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        builder = @org.mapstruct.Builder(disableBuilder = true))
public interface StoreMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "books", ignore = true)
    Store toEntity(StoreRequest request);

    @Mapping(target = "bookCount", expression = "java(store.getBooks() != null ? store.getBooks().size() : 0)")
    StoreResponse toResponse(Store store);

    List<StoreResponse> toResponseList(List<Store> stores);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "books", ignore = true)
    void updateEntity(StoreRequest request, @MappingTarget Store store);
}
