package tech.aerolambda.application.service;

import org.springframework.data.domain.Pageable;
import tech.aerolambda.application.dto.PageResponse;
import tech.aerolambda.application.dto.author.AuthorRequest;
import tech.aerolambda.application.dto.author.AuthorResponse;

import java.util.List;

public interface AuthorService {

    AuthorResponse create(AuthorRequest request);

    AuthorResponse findById(Long id);

    List<AuthorResponse> findAll();

    PageResponse<AuthorResponse> findAll(Pageable pageable);

    List<AuthorResponse> searchByName(String name);

    PageResponse<AuthorResponse> searchByName(String name, Pageable pageable);

    AuthorResponse update(Long id, AuthorRequest request);

    void delete(Long id);
}
