package com.alhasid;

public class Util {
    private Util() {}

    public static String getMessage(Long id) {
        return "sender with id [%s] not found".formatted(id);
    }
}
