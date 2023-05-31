package com.dashaval.frompast.sender;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SenderRepository extends JpaRepository<Sender, Long> {
    boolean existsSenderByEmail(String email);
}
