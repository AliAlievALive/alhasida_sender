package com.alhasid.taker;

public record TakerDTO(
        String name,
        String email,
        Integer age,
        Gender gender
) {}
