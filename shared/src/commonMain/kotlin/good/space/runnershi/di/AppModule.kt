package good.space.runnershi.di

import good.space.runnershi.model.domain.auth.ValidateEmailUseCase
import good.space.runnershi.model.domain.auth.ValidatePasswordUseCase
import good.space.runnershi.ui.home.HomeViewModel
import good.space.runnershi.ui.login.LoginViewModel
import good.space.runnershi.ui.result.ResultViewModel
import good.space.runnershi.ui.running.RunningViewModel
import good.space.runnershi.ui.signup.SignUpViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val appModule = module {
    includes(platformModule, networkModule, repositoryModule)

    // ViewModels
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModel {
        HomeViewModel(
            questRepository = get(),
            settingsRepository = getOrNull(),
            authRepository = get(),
            tokenStorage = get(),
            runningDataSource = getOrNull()
        )
    }
    viewModelOf(::RunningViewModel)
    viewModelOf(::ResultViewModel)

    // UseCases
    single { ValidateEmailUseCase() }
    single { ValidatePasswordUseCase() }
}
