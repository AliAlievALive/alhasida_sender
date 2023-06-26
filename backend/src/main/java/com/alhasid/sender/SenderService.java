package com.alhasid.sender;

import com.alhasid.exception.DuplicateResourceException;
import com.alhasid.exception.RequestValidationException;
import com.alhasid.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.alhasid.Util.getMessage;

@Service
public class SenderService {
    private final SenderDao senderDao;
    private final PasswordEncoder passwordEncoder;
    private final SenderDTOMapper senderDTOMapper;

    public SenderService(SenderDao senderDao, PasswordEncoder passwordEncoder, SenderDTOMapper senderDTOMapper) {
        this.senderDao = senderDao;
        this.passwordEncoder = passwordEncoder;
        this.senderDTOMapper = senderDTOMapper;
    }

    public void addSender(SenderRegistrationRequest request) {
        String email = request.email();
        if (senderDao.existsSenderWithEmail(email)) {
            throw new DuplicateResourceException("email already taken");
        }
        senderDao.insertSender(new Sender(request.email(), passwordEncoder.encode(request.password())));
    }

    public List<SenderDTO> getAllSenders() {
        return senderDao.selectAllSenders()
                .stream()
                .map(senderDTOMapper)
                .toList();
    }

    public SenderDTO getSender(Long id) {
        return senderDao.selectSenderById(id)
                .map(senderDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage(id)));
    }

    public void deleteSender(Long id) {
        if (!senderDao.existsSenderWithId(id)) {
            throw new ResourceNotFoundException(getMessage(id));
        }
        senderDao.deleteSender(id);
    }

    public void updateSender(Long id, SenderUpdateRequest updateRequest) {
        Sender sender = senderDao.selectSenderById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage(id)));

        boolean changed = false;
        if (updateRequest.email() != null && !updateRequest.email().equals(sender.getEmail())) {
            if (senderDao.existsSenderWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException("email already taken");
            }
            sender.setEmail(updateRequest.email());
            changed = true;
        }

        if (updateRequest.password() != null &&
                !passwordEncoder.encode(updateRequest.password())
                        .equals(passwordEncoder.encode(sender.getPassword()))) {
            sender.setPassword(passwordEncoder.encode(updateRequest.password()));
            changed = true;
        }

        if (!changed) {
            throw new RequestValidationException("no data changes found");
        }

        senderDao.updateSender(sender);
    }
}
