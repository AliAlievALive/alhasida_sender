package com.alhasid.taker;

import com.alhasid.exception.DuplicateResourceException;
import com.alhasid.exception.RequestValidationException;
import com.alhasid.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TakerService {
    private final TakerDao takerDao;

    public TakerService(@Qualifier("jdbc") TakerDao takerDao) {
        this.takerDao = takerDao;
    }

    public List<Taker> getAllTakers() {
        return takerDao.selectAllTakers();
    }

    public Taker getTaker(Long id) {
        return takerDao.selectTakerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("taker with id [%s] not found".formatted(id)));
    }

    public void addTaker(TakerRegistrationRequest takerRegistrationRequest) {
        String email = takerRegistrationRequest.email();
        if (takerDao.existsTakerWithEmail(email)) {
            throw new DuplicateResourceException("email already taken");
        }

        Taker taker = new Taker(
                takerRegistrationRequest.name(),
                takerRegistrationRequest.email(),
                takerRegistrationRequest.age(),
                takerRegistrationRequest.gender(),
                takerRegistrationRequest.sender()
                );
        takerDao.insertTaker(taker);
    }

    public void deleteTaker(Long takerId) {
        if (!takerDao.existsTakerWithId(takerId)) {
            throw new ResourceNotFoundException("taker with id [%s] not found".formatted(takerId));
        }
        takerDao.deleteTaker(takerId);
    }


    public void updateTaker(Long id, TakerUpdateRequest updateRequest) {
        Taker taker = getTaker(id);

        boolean changed = false;
        if (updateRequest.name() != null && !updateRequest.name().equals(taker.getName())) {
            taker.setName(updateRequest.name());
            changed = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(taker.getAge())) {
            taker.setAge(updateRequest.age());
            changed = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(taker.getEmail())) {
            if (takerDao.existsTakerWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException("email already taken");
            }
            taker.setEmail(updateRequest.email());
            changed = true;
        }

        if (!changed) {
            throw new RequestValidationException("no data changes found");
        }

        takerDao.updateTaker(taker);
    }
}
