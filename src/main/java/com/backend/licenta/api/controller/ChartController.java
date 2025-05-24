package com.backend.licenta.api.controller;

import com.backend.licenta.util.MultipartInputStreamFileResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/chart")
public class ChartController {

    @Value("${python.service.url}")
    private String pythonServiceUrl; // e.g., http://localhost:8000/analyze

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/generate")
    public ResponseEntity<?> generateChart(@RequestParam("file") MultipartFile file,
                                           @RequestParam("prompt") String prompt) {
        System.out.println("PYTHON URL FOLOSIT: " + pythonServiceUrl);
        try {
            // 1. Creează cererea cu fișier + prompt
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            var body = new LinkedMultiValueMap<String, Object>();
            body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
            body.add("prompt", prompt);

            var requestEntity = new HttpEntity<>(body, headers);

            // 2. Trimite la serverul Python
            ResponseEntity<String> response = restTemplate.postForEntity(
                    pythonServiceUrl, requestEntity, String.class
            );

            return ResponseEntity.ok(response.getBody());

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Eroare la citirea fișierului.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Eroare: " + ex.getMessage());
        }
    }
}
