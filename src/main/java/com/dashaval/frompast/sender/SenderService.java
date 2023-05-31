package com.dashaval.frompast.sender;

import com.dashaval.frompast.exception.DuplicateResourceException;
import com.dashaval.frompast.exception.RequestValidationException;
import com.dashaval.frompast.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SenderService {
    private final SenderDao senderDao;

    public SenderService(@Qualifier("list") SenderDao senderDao) {
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

    public void deleteSender(Long senderId) {
        if (!senderDao.existsSenderWithId(senderId)) {
            throw new ResourceNotFoundException("sender with id [%s] not found".formatted(senderId));
        }
        senderDao.deleteSender(senderId);
    }


    public void updateSender(Long id, SenderUpdateRequest updateRequest) {
        Sender sender = getSender(id);

        boolean changed = false;
        if (updateRequest.name() != null && !updateRequest.name().equals(sender.getName())) {
            sender.setName(updateRequest.name());
            changed = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(sender.getAge())) {
            sender.setAge(updateRequest.age());
            changed = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(sender.getEmail())) {
            if (senderDao.existsSenderWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException("email already taken");
            }
            sender.setEmail(updateRequest.email());
            changed = true;
        }

        if (!changed) {
            throw new RequestValidationException("no data changes found");
        }

        senderDao.updateSender(sender);
    }
}
