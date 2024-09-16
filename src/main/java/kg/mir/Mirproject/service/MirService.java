package kg.mir.Mirproject.service;

import kg.mir.Mirproject.dto.mirDto.response.MirRangeResponse;
import kg.mir.Mirproject.dto.mirDto.response.MirResponse;

import java.util.List;

public interface MirService {

    List<MirResponse> getUsersByTotalSumRange(Integer minTotalSum, Integer maxTotalSum);

    List<MirRangeResponse> getRanges();
}
