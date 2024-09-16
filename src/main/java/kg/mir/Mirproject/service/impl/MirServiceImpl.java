package kg.mir.Mirproject.service.impl;

import kg.mir.Mirproject.dto.mirDto.response.MirRangeResponse;
import kg.mir.Mirproject.dto.mirDto.response.MirResponse;
import kg.mir.Mirproject.entities.User;
import kg.mir.Mirproject.mapper.MirMapper;
import kg.mir.Mirproject.repository.UserRepository;
import kg.mir.Mirproject.service.MirService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MirServiceImpl implements MirService {

    private final UserRepository userRepository;

    private final MirMapper mapper;

    public Integer getMaxTotalSum() {
        return userRepository.findMaxTotalSum();
    }

    public List<MirResponse> getUsersByTotalSumRange(Integer minTotalSum, Integer maxTotalSum) {
        return userRepository.findByTotalSumBetween(minTotalSum, maxTotalSum)
                .stream().map(mapper::toMirResponse).toList();
    }

    public List<MirRangeResponse> getRanges() {
        Integer maxTotalSum = getMaxTotalSum();

        int step = (int) (maxTotalSum * 0.20);

        List<MirRangeResponse> ranges = new ArrayList<>();

        int currentMax = maxTotalSum;
        int currentMin = maxTotalSum - step;

        while (currentMin >= 0) {
            ranges.add(new MirRangeResponse(currentMin, currentMax));
            currentMax = currentMin;
            currentMin -= step;
        }

        ranges.add(new MirRangeResponse(0, currentMax));

        return ranges;
    }
}
