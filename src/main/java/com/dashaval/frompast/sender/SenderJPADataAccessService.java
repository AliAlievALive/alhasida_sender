package com.dashaval.frompast.sender;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
@AllArgsConstructor
public class SenderJPADataAccessService implements SenderDao {

    private final SenderRepository sendersRepo;
    @Override
    public List<Sender> selectAllSenders() {
        return sendersRepo.findAll();
    }

    @Override
    public Optional<Sender> selectSenderById(Long id) {
        return sendersRepo.findById(id);
    }

    @Override
    public void insertSender(Sender sender) {
        sendersRepo.save(sender);
    }

    @Override
    public boolean existsSenderWithEmail(String email) {
        return sendersRepo.existsSenderByEmail(email);
    }
}
