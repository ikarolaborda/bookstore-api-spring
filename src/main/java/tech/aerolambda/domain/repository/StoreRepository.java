package tech.aerolambda.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tech.aerolambda.domain.entity.Store;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>, JpaSpecificationExecutor<Store> {

    Optional<Store> findByNameIgnoreCase(String name);

    List<Store> findByNameContainingIgnoreCase(String name);

    List<Store> findByAddressContainingIgnoreCase(String address);

    boolean existsByNameIgnoreCase(String name);

    Page<Store> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Store> findByAddressContainingIgnoreCase(String address, Pageable pageable);
}
