package kg.mir.Mirproject.mapper;

import kg.mir.Mirproject.dto.aboutUsDto.request.AboutUsRequest;
import kg.mir.Mirproject.dto.aboutUsDto.response.AboutUsResponse;
import kg.mir.Mirproject.entities.AboutUs;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AboutUsMapper {

    AboutUsResponse toResponse(AboutUs aboutUs);

    AboutUs toEntity(AboutUsRequest request);

    void updateEntityFromRequest(AboutUsRequest request, @MappingTarget AboutUs aboutUs);
}
