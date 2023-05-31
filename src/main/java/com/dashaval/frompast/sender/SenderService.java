package com.dashaval.frompast.sender;

import com.dashaval.frompast.exception.DuplicateResourceException;
import com.dashaval.frompast.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SenderService {
    private final SenderDao senderDao;

    public SenderService(@Qualifier("jpa") SenderDao senderDao) {
        this.senderDao = senderDao;
    }

    public List<Sender> getAllSenders() {
        return senderDao.selectAllSenders();
    }

    public Sender getSender(Long id) {
        return senderDao.selectSenderById(id)
                .orElseThrow(() -> new ResourceNotFoundException("sender with id [%s] not found".formatted(id)));
    }

    public void addSender(SenderRegistrationRequest senderRegistrationRequest) {
        String email = senderRegistrationRequest.email();
        if (senderDao.existsSenderWithEmail(email)) {
            throw new DuplicateResourceException("email already taken");
        }
        Sender sender = new Sender(
                senderRegistrationRequest.name(),
                senderRegistrationRequest.email(),
                senderRegistrationRequest.age());
        senderDao.insertSender(sender);
    }
}
