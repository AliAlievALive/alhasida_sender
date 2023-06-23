package com.alhasid.sender;

import com.alhasid.taker.TakerDTO;

import java.util.List;

public record SenderDTO(
        Long id,
        String email,
        List<String> roles,
        List<TakerDTO> takers,
        String username
) {
}

