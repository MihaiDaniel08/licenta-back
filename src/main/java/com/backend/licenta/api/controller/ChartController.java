package com.backend.licenta.api.controller;

import com.backend.licenta.util.MultipartInputStreamFileResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chart")
public class ChartController {

    @Value("${python.service.url}")
    private String pythonServiceUrl; //http://localhost:8000/analyze
    @Value("${python.streamlit.url}")
    private String pythonStreamlitUrl; //http://localhost:8000/start-streamlit


    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/generate")
    public ResponseEntity<?> generateChart(@RequestParam("file") MultipartFile file,
                                           @RequestParam("prompt") String prompt) {
        System.out.println("PYTHON URL FOLOSIT: " + pythonServiceUrl);
        try {
            //Creează cererea cu fișier + prompt
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            var body = new LinkedMultiValueMap<String, Object>();
            body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
            body.add("prompt", prompt);

            var requestEntity = new HttpEntity<>(body, headers);

            //Trimite la serverul Python
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


    @PostMapping("/start-streamlit")
    public ResponseEntity<?> startStreamlitWithFile(@RequestParam("file") MultipartFile file) {
        try {
            // Construim requestul către FastAPI
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            var body = new LinkedMultiValueMap<String, Object>();
            body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));

            var requestEntity = new HttpEntity<>(body, headers);

            // Trimitem la FastAPI pe /start-streamlit-with-file
            ResponseEntity<String> response = restTemplate.postForEntity(
                    pythonStreamlitUrl, requestEntity, String.class
            );

            return ResponseEntity.ok(response.getBody());

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Eroare la trimiterea fișierului.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Eroare: " + e.getMessage());
        }
    }

    @PostMapping("/rag")
    public ResponseEntity<String> getRagSummary(@RequestBody Map<String, String> body) {
        String prompt = body.get("prompt");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> pythonPayload = new HashMap<>();
        pythonPayload.put("prompt", prompt);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(pythonPayload, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8000/rag", request, String.class);
        return ResponseEntity.ok(response.getBody());
    }
}
