package good.space.runnershi.model.domain.auth

class ValidatePasswordUseCase {
    operator fun invoke(password: String): AuthValidationResult {
        if (password.isBlank()) {
            return AuthValidationResult.Error.Blank
        }
        if (password.length < 6) {
            return AuthValidationResult.Error.TooShort(6)
        }

        return AuthValidationResult.Success
    }
}
