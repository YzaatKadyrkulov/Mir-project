package kg.mir.Mirproject.api;

import kg.mir.Mirproject.dto.mirDto.response.MirRangeResponse;
import kg.mir.Mirproject.dto.mirDto.response.MirResponse;
import kg.mir.Mirproject.entities.User;
import kg.mir.Mirproject.service.MirService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mir")
@RequiredArgsConstructor
public class MirApi {

    private final MirService service;

    @GetMapping("/ranges")
    public ResponseEntity<List<MirRangeResponse>> getRangeList() {
        List<MirRangeResponse> ranges = service.getRanges();
        return new ResponseEntity<>(ranges, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<MirResponse>> getUsersByTotalSum(
            @RequestParam Integer minTotalSum,
            @RequestParam Integer maxTotalSum
    ) {
        List<MirResponse> users = service.getUsersByTotalSumRange(minTotalSum, maxTotalSum);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
