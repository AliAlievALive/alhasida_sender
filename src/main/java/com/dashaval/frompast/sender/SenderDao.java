package com.dashaval.frompast.sender;

import java.util.List;
import java.util.Optional;

public interface SenderDao {
    List<Sender> selectAllSenders();
    Optional<Sender> selectSenderById(Long id);
    void insertSender(Sender sender);
    boolean existsSenderWithEmail(String email);
    void deleteSender(Long id);
    boolean existsSenderWithId(Long senderId);
    void updateSender(Sender sender);
}
