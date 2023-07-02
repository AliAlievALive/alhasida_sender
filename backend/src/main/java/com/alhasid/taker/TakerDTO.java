package com.alhasid.taker;

public record TakerDTO(
        Long id,
        String name,
        String email,
        Integer age,
        Gender gender
) {}
