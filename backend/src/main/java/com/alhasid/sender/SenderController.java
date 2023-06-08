package com.alhasid.sender;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/senders")
public class SenderController {
    private final SenderService senderService;

    @GetMapping
    public List<Sender> getSenders() {
        return senderService.getAllSenders();
    }

    @GetMapping("{id}")
    public Sender senders(@PathVariable Long id) {
        return senderService.getSender(id);
    }

    @PostMapping
    public void registrationSender(@RequestBody SenderRegistrationRequest senderRegistrationRequest) {
        senderService.addSender(senderRegistrationRequest);
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
