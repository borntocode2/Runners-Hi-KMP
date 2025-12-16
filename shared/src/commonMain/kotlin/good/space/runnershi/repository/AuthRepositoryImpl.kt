package good.space.runnershi.repository

import good.space.runnershi.model.dto.auth.LoginRequest
import good.space.runnershi.model.dto.auth.LoginResponse
import good.space.runnershi.model.dto.auth.SignUpRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : AuthRepository {

    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = httpClient.post("$baseUrl/api/v1/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            
            if (response.status == HttpStatusCode.OK) {
                val tokenResponse = response.body<LoginResponse>()
                Result.success(tokenResponse)
            } else {
                Result.failure(Exception("로그인 실패: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(request: SignUpRequest): Result<LoginResponse> {
        return try {
            // 1. 회원가입 요청
            val signUpResponse = httpClient.post("$baseUrl/api/v1/auth/signup") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            
            if (signUpResponse.status != HttpStatusCode.Created) {
                return Result.failure(Exception("회원가입 실패: ${signUpResponse.status}"))
            }
            
            // 2. 회원가입 성공 후 자동 로그인 (UX 개선)
            val loginRequest = LoginRequest(
                email = request.email,
                password = request.password
            )
            
            val loginResponse = httpClient.post("$baseUrl/api/v1/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(loginRequest)
            }
            
            if (loginResponse.status == HttpStatusCode.OK) {
                val tokenResponse = loginResponse.body<LoginResponse>()
                Result.success(tokenResponse)
            } else {
                Result.failure(Exception("회원가입은 성공했으나 자동 로그인 실패: ${loginResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

