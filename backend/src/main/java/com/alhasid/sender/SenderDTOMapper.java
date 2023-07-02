package com.alhasid.sender;

import com.alhasid.taker.TakerDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class SenderDTOMapper implements Function<Sender, SenderDTO> {
    @Override
    public SenderDTO apply(Sender sender) {
        return new SenderDTO(
                sender.getId(),
                sender.getEmail(),
                sender.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList(),
                sender.getTakers().stream()
                        .map(taker -> new TakerDTO(
                                taker.getId(),
                                taker.getName(),
                                taker.getEmail(),
                                taker.getAge(), taker.getGender()))
                        .toList(),
                sender.getUsername()
        );
    }
}
