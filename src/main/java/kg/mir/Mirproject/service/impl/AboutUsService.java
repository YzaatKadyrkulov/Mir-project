package kg.mir.Mirproject.service.impl;

import kg.mir.Mirproject.entities.AboutUs;
import kg.mir.Mirproject.repository.AboutUsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AboutUsService {

    private final AboutUsRepository aboutUsRepository;

    public AboutUs save(AboutUs aboutUs) {
        return aboutUsRepository.save(aboutUs);
    }

    public Optional<AboutUs> findById(Long id) {
        return aboutUsRepository.findById(String.valueOf(id));
    }
}
