package com.alhasid.taker;

import com.alhasid.jwt.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/takers")
public class TakerController {
    private final TakerService takerService;
    private JWTUtil jwtUtil;

    @GetMapping
    public List<Taker> getTakers() {
        return takerService.getAllTakers();
    }

    @GetMapping("{id}")
    public Taker takers(@PathVariable Long id) {
        return takerService.getTaker(id);
    }

    @PostMapping
    public ResponseEntity<?> registrationTaker(@RequestBody TakerRegistrationRequest request) {
        takerService.addTaker(request);
        String jwtToken = jwtUtil.issueToken(request.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
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
