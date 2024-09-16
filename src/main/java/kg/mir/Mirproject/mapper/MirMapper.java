package kg.mir.Mirproject.mapper;

import kg.mir.Mirproject.dto.mirDto.response.MirResponse;
import kg.mir.Mirproject.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MirMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "photoUrl", target = "photoUrl")
    MirResponse toMirResponse(User user);
}
