package _2024.winter.demoemailauth.controller;

import _2024.winter.demoemailauth.dto.CheckAuthEmailRequest;
import _2024.winter.demoemailauth.dto.SendAuthEmailRequest;
import _2024.winter.demoemailauth.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendAuthEmail(@RequestBody SendAuthEmailRequest request) {
        return emailService.sendAuthEmail(request.getEmail());
    }

    @PostMapping("/check")
    public ResponseEntity<String> checkAuthEmail(@RequestBody CheckAuthEmailRequest request) {
        return emailService.checkAuthEmail(request.getEmail(), request.getAuthNum());
    }
}