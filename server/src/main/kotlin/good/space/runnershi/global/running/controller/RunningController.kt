package good.space.runnershi.global.running.controller

import good.space.runnershi.global.running.service.RunningService
import good.space.runnershi.model.dto.running.LongestDistance
import good.space.runnershi.model.dto.running.RunCreateRequest
import good.space.runnershi.model.dto.running.UpdatedUserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Running", description = "러닝 데이터 관련 API")
@RestController
@RequestMapping("/api/v1/running")
class RunningController (
    private val runningService: RunningService
){
    @Operation(summary = "러닝 데이터 저장", description = "클라이언트가 러닝 종료를 눌렀을 때, 서버에 러닝데이터가 저장되는 API입니다.")
    @PostMapping("/run-records")
    fun applyRunningStats(
        @AuthenticationPrincipal userId: Long,
        @RequestBody runCreateRequest: RunCreateRequest
    ): ResponseEntity<UpdatedUserResponse> {

        return ResponseEntity.ok(runningService.saveRunningStats(userId, runCreateRequest))
    }

    @Operation(summary = "가장 긴 거리 조회", description = "사용자의 러닝 화면에서 사용자의 최고기록(거리)를 조회하는 API입니다.")
    @GetMapping("/LongestDistance")
    fun getLongestDistance(
        @AuthenticationPrincipal userId: Long,
    ): ResponseEntity<LongestDistance> {

        return ResponseEntity.ok(runningService.getLongestDistance(userId))
    }
}
