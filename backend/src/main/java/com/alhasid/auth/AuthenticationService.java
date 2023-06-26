package com.alhasid.auth;

import com.alhasid.jwt.JWTUtil;
import com.alhasid.sender.Sender;
import com.alhasid.sender.SenderDTO;
import com.alhasid.sender.SenderDTOMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final SenderDTOMapper senderDTOMapper;
    private final JWTUtil jwtUtil;

    public AuthenticationService(AuthenticationManager authenticationManager, SenderDTOMapper senderDTOMapper, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.senderDTOMapper = senderDTOMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        Sender principal = (Sender) authentication.getPrincipal();
        SenderDTO senderDTO = senderDTOMapper.apply(principal);
        String token = jwtUtil.issueToken(senderDTO.username(), senderDTO.roles());
        return new AuthenticationResponse(senderDTO, token);
    }
}
