package shopping.slang.ui;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.product.dto.ProductRequest;
import shopping.product.dto.ProductResponse;
import shopping.slang.application.SlangService;
import shopping.slang.dto.SlangRequest;
import shopping.slang.dto.SlangResponse;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/slangs")
public class SlangController {

    private SlangService slangService;

    public SlangController(SlangService slangService) {
        this.slangService = slangService;
    }

    @PostMapping()
    public ResponseEntity<SlangResponse> registerSlangs(@RequestBody @Valid SlangRequest request) {
        SlangResponse slangResponse = slangService.registerSlangs(request);
        return ResponseEntity.created(URI.create("/slangs")).body(slangResponse);
    }
}
