package com.library.managementprojectjava.repository.user;

import com.library.managementprojectjava.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = 'MEMBER'")
    long countMembers();
}
