package com.alhasid.taker;

public record TakerRegistrationRequest(String name, String email, Integer age, Gender gender, Long senderId) {
}
