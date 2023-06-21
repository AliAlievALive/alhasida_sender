package com.alhasid.sender;

import com.alhasid.exception.DuplicateResourceException;
import com.alhasid.exception.RequestValidationException;
import com.alhasid.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SenderService {
    private final SenderDao senderDao;

    public SenderService(SenderDao senderDao) {
        this.senderDao = senderDao;
    }

    public void addSender(SenderRegistrationRequest request) {
        String email = request.email();
        if (senderDao.existsSenderWithEmail(email)) {
            throw new DuplicateResourceException("email already taken");
        }
        senderDao.insertSender(new Sender(request.email()));
    }

    public List<Sender> getAllSenders() {
        return senderDao.selectAllSenders();
    }

    public Sender getSender(Long id) {
        return senderDao.selectSenderById(id)
                .orElseThrow(() -> new ResourceNotFoundException("sender with id [%s] not found".formatted(id)));
    }

    public void deleteSender(Long id) {
        if (!senderDao.existsSenderWithId(id)) {
            throw new ResourceNotFoundException("sender with id [%s] not found".formatted(id));
        }
        senderDao.deleteSender(id);
    }

    public void updateSender(Long id, SenderUpdateRequest updateRequest) {
        Sender sender = getSender(id);

        boolean changed = false;
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
