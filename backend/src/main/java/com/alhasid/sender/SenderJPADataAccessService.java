package com.alhasid.sender;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class SenderJPADataAccessService implements SenderDao {
    private final SenderRepository senderRepository;

    @Override
    public List<Sender> selectAllSenders() {
        return senderRepository.findAll();
    }

    @Override
    public Optional<Sender> selectSenderById(Long id) {
        return senderRepository.findById(id);
    }

    @Override
    public boolean existsSenderWithEmail(String email) {
        return senderRepository.existsSenderByEmail(email);
    }

    @Override
    public void insertSender(Sender sender) {
        senderRepository.save(sender);
    }

    @Override
    public boolean existsSenderWithId(Long id) {
        return senderRepository.existsById(id);
    }

    @Override
    public void deleteSender(Long id) {
        senderRepository.deleteById(id);
    }

    @Override
    public void updateSender(Sender sender) {
        senderRepository.save(sender);
    }
}
