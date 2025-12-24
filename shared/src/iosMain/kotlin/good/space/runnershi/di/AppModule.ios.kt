package good.space.runnershi.di

import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    // TODO: iOS용 구현체 구현
    // single<TokenStorage> { IosTokenStorage() } 
}
