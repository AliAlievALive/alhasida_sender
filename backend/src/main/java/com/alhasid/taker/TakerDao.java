package com.alhasid.taker;

import java.util.List;
import java.util.Optional;

public interface TakerDao {
    List<Taker> selectAllTakers();
    Optional<Taker> selectTakerById(Long id);
    void insertTaker(Taker taker);
    boolean existsTakerWithEmail(String email);
    void deleteTaker(Long id);
    boolean existsTakerWithId(Long takerId);
    void updateTaker(Taker taker);
    List<Taker> selectTakersForSender(Long id);
}
