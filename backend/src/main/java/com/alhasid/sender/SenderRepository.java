package com.alhasid.sender;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SenderRepository extends JpaRepository<Sender, Long> {
    boolean existsSenderByEmail(String email);

    Optional<Sender> findUserByEmail(String email);
}
