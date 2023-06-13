package com.alhasid.taker;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
@AllArgsConstructor
public class TakerJPADataAccessService implements TakerDao {

    private final TakerRepository takersRepo;
    @Override
    public List<Taker> selectAllTakers() {
        return takersRepo.findAll();
    }

    @Override
    public Optional<Taker> selectTakerById(Long id) {
        return takersRepo.findById(id);
    }

    @Override
    public void insertTaker(Taker taker) {
        takersRepo.save(taker);
    }

    @Override
    public boolean existsTakerWithEmail(String email) {
        return takersRepo.existsTakerByEmail(email);
    }

    @Override
    public void deleteTaker(Long id) {
        takersRepo.deleteById(id);
    }

    @Override
    public boolean existsTakerWithId(Long takerId) {
        return takersRepo.existsById(takerId);
    }

    @Override
    public void updateTaker(Taker taker) {
        takersRepo.save(taker);
    }
}
