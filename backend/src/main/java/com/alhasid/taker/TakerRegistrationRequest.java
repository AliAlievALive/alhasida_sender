package com.alhasid.taker;

import com.alhasid.sender.Sender;

public record TakerRegistrationRequest(String name, String email, Integer age, Gender gender, Sender sender) {
}
