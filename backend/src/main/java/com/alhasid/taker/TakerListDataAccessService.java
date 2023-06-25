package com.alhasid.taker;

import com.alhasid.sender.Sender;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class TakerListDataAccessService implements TakerDao {
    public static List<Taker> takers;

    {
        takers = new ArrayList<>();
        Taker alex = new Taker(1L, "Alex", "alex@gamil.com", 32, Gender.MALE, new Sender(1L, "test@t.com", "pass"));
        takers.add(alex);
        Taker alim = new Taker(2L, "Alim", "alim@gamil.com", 9, Gender.MALE, new Sender(1L, "test1@t.com", "pass"));
        takers.add(alim);
        Taker hava = new Taker(3L, "Hava", "hava@gamil.com", 7, Gender.FEMALE, new Sender(2L,"test2@t.com", "pass"));
        takers.add(hava);
    }

    @Override
    public List<Taker> selectAllTakers() {
        return takers;
    }

    @Override
    public Optional<Taker> selectTakerById(Long id) {
        return takers.stream()
                .filter(taker -> taker.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertTaker(Taker taker) {
        takers.add(taker);
    }

    @Override
    public boolean existsTakerWithEmail(String email) {
        return takers.stream().anyMatch(s -> s.getEmail().equals(email));
    }

    @Override
    public void deleteTaker(Long id) {
        takers.stream()
                .filter(taker -> taker.getId().equals(id))
                .findFirst()
                .ifPresent(taker -> takers.remove(taker));
    }

    @Override
    public boolean existsTakerWithId(Long takerId) {
        return takers.stream().anyMatch(taker -> taker.getId().equals(takerId));
    }

    @Override
    public void updateTaker(Taker taker) {
        takers.add(taker);
    }

    @Override
    public List<Taker> selectTakersForSender(Long id) {
        return takers.stream()
                .filter(taker -> taker.getSender().getId().equals(id))
                .toList();
    }
}
