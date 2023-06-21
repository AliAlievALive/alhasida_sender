package com.alhasid.taker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TakerRepository extends JpaRepository<Taker, Long> {
    boolean existsTakerByEmail(String email);

    List<Taker> getTakersBySenderId(Long id);
}
