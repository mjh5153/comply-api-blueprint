package com.init_spring_bean_mvn.demo.dataset.api;

import com.init_spring_bean_mvn.demo.dataset.domain.DatasetAnalysisRequest;
import com.init_spring_bean_mvn.demo.dataset.domain.DatasetAnalysisResponse;
import com.init_spring_bean_mvn.demo.dataset.service.DatasetAnalysisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/datasets")
public class DatasetAnalysisController {

    private final DatasetAnalysisService analysisService;

    public DatasetAnalysisController(DatasetAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<DatasetAnalysisResponse> analyze(
            @Valid @RequestBody DatasetAnalysisRequest request,
            HttpServletRequest httpRequest) {
        String correlationId = (String) httpRequest.getAttribute(CorrelationIdFilter.ATTRIBUTE);
        return ResponseEntity.ok(analysisService.analyze(request, correlationId));
    }
}
