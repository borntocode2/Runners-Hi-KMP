package good.space.runnershi.percentile.controller

import good.space.runnershi.model.dto.running.percentile.RunPercentileRequest
import good.space.runnershi.model.dto.running.percentile.RunPercentileResponse
import good.space.runnershi.percentile.service.RunResultPercentileService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Percentile", description = "달리기 결과 백분위 표시 API")
@RestController
@RequestMapping("/api/v1/running")
class RunResultPercentileController (
    private val runningPercentileService: RunResultPercentileService
){
    @Operation(summary = "달리기 결과 백분위 계산", description = "주어진 달리기 거리(미터)와 시간(분)에 대해 상위 N% 를 계산하여 반환합니다.")
    @PostMapping("/percentile")
    fun percentile(@RequestBody req: RunPercentileRequest): RunPercentileResponse {
        return runningPercentileService.calculate(req)
    }
}
