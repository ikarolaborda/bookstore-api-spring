package tech.aerolambda.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.aerolambda.application.dto.PageResponse;
import tech.aerolambda.application.dto.author.AuthorRequest;
import tech.aerolambda.application.dto.author.AuthorResponse;
import tech.aerolambda.application.mapper.AuthorMapper;
import tech.aerolambda.application.service.AuthorService;
import tech.aerolambda.domain.entity.Author;
import tech.aerolambda.domain.repository.AuthorRepository;
import tech.aerolambda.infrastructure.exception.DuplicateResourceException;
import tech.aerolambda.infrastructure.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    @Transactional
    public AuthorResponse create(AuthorRequest request) {
        if (authorRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException("Author", "name", request.name());
        }
        Author author = authorMapper.toEntity(request);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toResponse(savedAuthor);
    }

    @Override
    public AuthorResponse findById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
        return authorMapper.toResponse(author);
    }

    @Override
    public List<AuthorResponse> findAll() {
        return authorMapper.toResponseList(authorRepository.findAll());
    }

    @Override
    public PageResponse<AuthorResponse> findAll(Pageable pageable) {
        Page<Author> page = authorRepository.findAll(pageable);
        List<AuthorResponse> content = authorMapper.toResponseList(page.getContent());
        return PageResponse.from(page, content);
    }

    @Override
    public List<AuthorResponse> searchByName(String name) {
        return authorMapper.toResponseList(authorRepository.findByNameContainingIgnoreCase(name));
    }

    @Override
    public PageResponse<AuthorResponse> searchByName(String name, Pageable pageable) {
        Page<Author> page = authorRepository.findByNameContainingIgnoreCase(name, pageable);
        List<AuthorResponse> content = authorMapper.toResponseList(page.getContent());
        return PageResponse.from(page, content);
    }

    @Override
    @Transactional
    public AuthorResponse update(Long id, AuthorRequest request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));

        if (!author.getName().equalsIgnoreCase(request.name())
                && authorRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException("Author", "name", request.name());
        }

        authorMapper.updateEntity(request, author);
        Author updatedAuthor = authorRepository.save(author);
        return authorMapper.toResponse(updatedAuthor);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author", id);
        }
        authorRepository.deleteById(id);
    }
}
