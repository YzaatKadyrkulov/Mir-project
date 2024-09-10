package kg.mir.Mirproject.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kg.mir.Mirproject.dto.aboutUsDto.request.AboutUsRequest;
import kg.mir.Mirproject.dto.aboutUsDto.response.AboutUsResponse;
import kg.mir.Mirproject.entities.AboutUs;
import kg.mir.Mirproject.mapper.AboutUsMapper;
import kg.mir.Mirproject.repository.AboutUsRepository;
import kg.mir.Mirproject.service.AboutUsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AboutUsServiceImpl implements AboutUsService {

    private final AboutUsRepository aboutUsRepository;

    private final AboutUsMapper mapper;

    @Override
    public Long save(AboutUsRequest request) {
        return aboutUsRepository.save(mapper.toEntity(request)).getId();
    }

    @Override
    public AboutUsResponse findById(Long id) {
        return aboutUsRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("About Us Not Found"));
    }

    @Override
    public void update(AboutUsRequest request) {
        AboutUs aboutUs = aboutUsRepository.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("About Us Not Found"));

        mapper.updateEntityFromRequest(request, aboutUs);

        aboutUsRepository.save(aboutUs);
    }
}
