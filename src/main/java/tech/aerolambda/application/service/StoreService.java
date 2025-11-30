package tech.aerolambda.application.service;

import org.springframework.data.domain.Pageable;
import tech.aerolambda.application.dto.PageResponse;
import tech.aerolambda.application.dto.store.StoreRequest;
import tech.aerolambda.application.dto.store.StoreResponse;

import java.util.List;

public interface StoreService {

    StoreResponse create(StoreRequest request);

    StoreResponse findById(Long id);

    List<StoreResponse> findAll();

    PageResponse<StoreResponse> findAll(Pageable pageable);

    List<StoreResponse> searchByName(String name);

    PageResponse<StoreResponse> searchByName(String name, Pageable pageable);

    StoreResponse update(Long id, StoreRequest request);

    void delete(Long id);
}
