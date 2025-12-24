package good.space.runnershi.di

import good.space.runnershi.auth.AndroidTokenStorage
import good.space.runnershi.auth.TokenStorage
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<TokenStorage> {
        AndroidTokenStorage(context = get())
    }
}
