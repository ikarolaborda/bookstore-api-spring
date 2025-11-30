package tech.aerolambda.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tech.aerolambda.domain.entity.User;
import tech.aerolambda.domain.enums.UserRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(UserRole role);

    List<User> findByNameContainingIgnoreCase(String name);

    boolean existsByEmail(String email);
}
