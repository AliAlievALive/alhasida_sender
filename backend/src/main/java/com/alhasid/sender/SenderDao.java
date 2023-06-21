package com.alhasid.sender;

import java.util.List;
import java.util.Optional;

public interface SenderDao {
    List<Sender> selectAllSenders();
    Optional<Sender> selectSenderById(Long id);
    boolean existsSenderWithEmail(String email);
    void insertSender(Sender sender);
    boolean existsSenderWithId(Long id);
    void deleteSender(Long id);
    void updateSender(Sender sender);
}
