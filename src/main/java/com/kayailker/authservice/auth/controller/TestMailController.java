package com.kayailker.authservice.auth.controller;

import com.kayailker.authservice.auth.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-mail")
public class TestMailController {
    private final MailService mailService;

    public TestMailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping
    public ResponseEntity<String> sendTestEmail(@RequestParam String to) {
        String body = """
    Merhaba Ortekay,

    Bu, sistemimiz tarafından gönderilen test amaçlı bir e-postadır.

    Eğer bu e-postayı beklemiyorsanız lütfen dikkate almayınız.

    İyi günler dileriz!
    
    - RestLocation Auth Team
    """;
        mailService.sendEmail(to, "Test Mail", body);

        return ResponseEntity.ok("Mail gönderildi!");
    }
}
