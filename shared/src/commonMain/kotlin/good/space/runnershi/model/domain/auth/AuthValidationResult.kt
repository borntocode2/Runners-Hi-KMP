package good.space.runnershi.model.domain.auth

sealed interface AuthValidationResult {
    data object Success : AuthValidationResult

    sealed interface Error : AuthValidationResult {
        data object Blank : Error
        data object InvalidFormat: Error
        data class TooShort(val lowerBound: Int): Error
    }
}