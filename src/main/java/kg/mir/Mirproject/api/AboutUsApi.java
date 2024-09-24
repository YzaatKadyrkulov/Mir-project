package kg.mir.Mirproject.api;

import kg.mir.Mirproject.dto.aboutUsDto.request.AboutUsRequest;
import kg.mir.Mirproject.dto.aboutUsDto.response.AboutUsResponse;
import kg.mir.Mirproject.service.AboutUsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/about-us")
@RequiredArgsConstructor
public class AboutUsApi {
    private final AboutUsService service;

    @GetMapping("/{id}")
    public ResponseEntity<AboutUsResponse> getAboutUs(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/last")
    public ResponseEntity<AboutUsResponse> getLastAboutUs() {
        return ResponseEntity.ok(service.findLastInformation());
    }

    @PostMapping
    public ResponseEntity<Long> createAboutUs(@RequestBody AboutUsRequest request) {
        return ResponseEntity.ok(service.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAboutUs(@PathVariable("id") Long id, @RequestBody AboutUsRequest request) {
        service.update(id, request);
        return ResponseEntity.accepted().build();
    }
}
