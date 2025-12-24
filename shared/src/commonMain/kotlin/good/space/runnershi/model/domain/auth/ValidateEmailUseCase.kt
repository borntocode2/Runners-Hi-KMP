package good.space.runnershi.model.domain.auth

class ValidateEmailUseCase {
    // KMP 공통 정규식 (Java/Swift 공통 동작)
    private val emailRegex = Regex(
        """^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$"""
    )

    operator fun invoke(email: String): AuthValidationResult {
        if (email.isBlank()) {
            return AuthValidationResult.Error.Blank
        }
        if (!emailRegex.matches(email)) {
            return AuthValidationResult.Error.InvalidFormat
        }
        return AuthValidationResult.Success
    }
}