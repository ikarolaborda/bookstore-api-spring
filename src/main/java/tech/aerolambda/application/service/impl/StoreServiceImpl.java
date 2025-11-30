package tech.aerolambda.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.aerolambda.application.dto.PageResponse;
import tech.aerolambda.application.dto.store.StoreRequest;
import tech.aerolambda.application.dto.store.StoreResponse;
import tech.aerolambda.application.mapper.StoreMapper;
import tech.aerolambda.application.service.StoreService;
import tech.aerolambda.domain.entity.Store;
import tech.aerolambda.domain.repository.StoreRepository;
import tech.aerolambda.infrastructure.exception.DuplicateResourceException;
import tech.aerolambda.infrastructure.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    @Override
    @Transactional
    public StoreResponse create(StoreRequest request) {
        if (storeRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException("Store", "name", request.name());
        }
        Store store = storeMapper.toEntity(request);
        Store savedStore = storeRepository.save(store);
        return storeMapper.toResponse(savedStore);
    }

    @Override
    public StoreResponse findById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store", id));
        return storeMapper.toResponse(store);
    }

    @Override
    public List<StoreResponse> findAll() {
        return storeMapper.toResponseList(storeRepository.findAll());
    }

    @Override
    public PageResponse<StoreResponse> findAll(Pageable pageable) {
        Page<Store> page = storeRepository.findAll(pageable);
        List<StoreResponse> content = storeMapper.toResponseList(page.getContent());
        return PageResponse.from(page, content);
    }

    @Override
    public List<StoreResponse> searchByName(String name) {
        return storeMapper.toResponseList(storeRepository.findByNameContainingIgnoreCase(name));
    }

    @Override
    public PageResponse<StoreResponse> searchByName(String name, Pageable pageable) {
        Page<Store> page = storeRepository.findByNameContainingIgnoreCase(name, pageable);
        List<StoreResponse> content = storeMapper.toResponseList(page.getContent());
        return PageResponse.from(page, content);
    }

    @Override
    @Transactional
    public StoreResponse update(Long id, StoreRequest request) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store", id));

        if (!store.getName().equalsIgnoreCase(request.name())
                && storeRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException("Store", "name", request.name());
        }

        storeMapper.updateEntity(request, store);
        Store updatedStore = storeRepository.save(store);
        return storeMapper.toResponse(updatedStore);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!storeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Store", id);
        }
        storeRepository.deleteById(id);
    }
}
