package com.alhasid.taker;

import com.alhasid.exception.DuplicateResourceException;
import com.alhasid.exception.RequestValidationException;
import com.alhasid.exception.ResourceNotFoundException;
import com.alhasid.sender.Sender;
import com.alhasid.sender.SenderDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TakerService {
    private final TakerDao takerDao;
    private final SenderDao senderDao;

    public TakerService(@Qualifier("jpa") TakerDao takerDao, SenderDao senderDao) {
        this.takerDao = takerDao;
        this.senderDao = senderDao;
    }

    public List<Taker> getAllTakers() {
        return takerDao.selectAllTakers();
    }

    public Taker getTaker(Long id) {
        return takerDao.selectTakerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("taker with id [%s] not found".formatted(id)));
    }

    public void addTaker(TakerRegistrationRequest takerRegistrationRequest) {
        Sender sender = senderDao.selectSenderById(takerRegistrationRequest.senderId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));

        Optional<Taker> takers = sender.getTakers()
                .stream().filter(
                        taker -> taker.getEmail()
                                .equals(takerRegistrationRequest.email())
                )
                .findFirst();

        if (takers.isPresent()) {
            throw new DuplicateResourceException("email already taken");
        }

        Taker taker = new Taker(
                takerRegistrationRequest.name(),
                takerRegistrationRequest.email(),
                takerRegistrationRequest.age(),
                takerRegistrationRequest.gender(),
                sender
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

    public List<TakerDTO> getTakersForSender(Long id) {
        return takerDao.selectTakersForSender(id)
                .stream()
                .map(taker -> new TakerDTO(taker.getName(), taker.getEmail(), taker.getAge(), taker.getGender()))
                .toList();
    }
}
