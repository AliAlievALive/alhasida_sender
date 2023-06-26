package com.alhasid.auth;

import com.alhasid.sender.SenderDTO;

public record AuthenticationResponse(SenderDTO senderDTO, String token) {
}
