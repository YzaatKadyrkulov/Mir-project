package kg.mir.Mirproject.service;

import kg.mir.Mirproject.dto.aboutUsDto.request.AboutUsRequest;
import kg.mir.Mirproject.dto.aboutUsDto.response.AboutUsResponse;

public interface AboutUsService {

    Long save(AboutUsRequest request);

    AboutUsResponse findById(Long id);

    void update(Long id, AboutUsRequest request);

    AboutUsResponse findLastInformation();
}
