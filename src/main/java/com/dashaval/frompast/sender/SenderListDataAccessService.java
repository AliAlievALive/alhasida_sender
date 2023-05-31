package com.dashaval.frompast.sender;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class SenderListDataAccessService implements SenderDao {
    public static List<Sender> senders;

    {
        senders = new ArrayList<>();
        Sender alex = new Sender(1L, "Alex", "alex@gamil.com", 32);
        senders.add(alex);
        Sender alim = new Sender(2L, "Alim", "alim@gamil.com", 9);
        senders.add(alim);
        Sender hava = new Sender(3L, "Hava", "hava@gamil.com", 7);
        senders.add(hava);
    }

    @Override
    public List<Sender> selectAllSenders() {
        return senders;
    }

    @Override
    public Optional<Sender> selectSenderById(Long id) {
        return senders.stream()
                .filter(sender -> sender.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertSender(Sender sender) {
        senders.add(sender);
    }

    @Override
    public boolean existsSenderWithEmail(String email) {
        return senders.stream().anyMatch(s -> s.getEmail().equals(email));
    }

    @Override
    public void deleteSender(Long id) {
        senders.stream()
                .filter(sender -> sender.getId().equals(id))
                .findFirst()
                .ifPresent(sender -> senders.remove(sender));
    }

    @Override
    public boolean existsSenderWithId(Long senderId) {
        return senders.stream().anyMatch(sender -> sender.getId().equals(senderId));
    }


}
