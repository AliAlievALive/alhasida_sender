package com.alhasid.taker;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/takers")
public class TakerController {
    private final TakerService takerService;

    @GetMapping
    public List<Taker> getTakers() {
        return takerService.getAllTakers();
    }

    @GetMapping("{id}")
    public Taker takers(@PathVariable Long id) {
        return takerService.getTaker(id);
    }

    @PostMapping
    public void registrationTaker(@RequestBody TakerRegistrationRequest takerRegistrationRequest) {
        takerService.addTaker(takerRegistrationRequest);
    }

    @DeleteMapping("{id}")
    public void deleteTaker(@PathVariable Long id) {
        takerService.deleteTaker(id);
    }

    @PutMapping("{id}")
    public void registrationTaker(@PathVariable Long id,
                                   @RequestBody TakerUpdateRequest updateRequest) {
        takerService.updateTaker(id, updateRequest);
    }
}
