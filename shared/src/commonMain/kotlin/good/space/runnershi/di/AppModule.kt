package good.space.runnershi.di

import good.space.runnershi.model.domain.auth.ValidateEmailUseCase
import good.space.runnershi.model.domain.auth.ValidatePasswordUseCase
import good.space.runnershi.ui.login.LoginViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val appModule = module {
    includes(platformModule, networkModule, repositoryModule)

    viewModelOf(::LoginViewModel)

    single { ValidateEmailUseCase() }
    single { ValidatePasswordUseCase() }
}
