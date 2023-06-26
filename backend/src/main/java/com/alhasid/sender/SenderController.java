package com.alhasid.sender;

import com.alhasid.jwt.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/senders")
public class SenderController {
    private final SenderService senderService;
    private JWTUtil jwtUtil;

    @GetMapping
    public List<SenderDTO> getSenders() {
        return senderService.getAllSenders();
    }

    @GetMapping("{id}")
    public SenderDTO getSender(@PathVariable Long id) {
        return senderService.getSender(id);
    }
    
    @PostMapping
    public ResponseEntity<?> registrationSender(@RequestBody SenderRegistrationRequest request) {
        senderService.addSender(request);
        String jwtToken = jwtUtil.issueToken(request.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
    }

    @DeleteMapping("{id}")
    public void deleteSender(@PathVariable Long id) {
        senderService.deleteSender(id);
    }

    @PutMapping("{id}")
    public void registrationSender(@PathVariable Long id,
                                  @RequestBody SenderUpdateRequest updateRequest) {
        senderService.updateSender(id, updateRequest);
    }
}
