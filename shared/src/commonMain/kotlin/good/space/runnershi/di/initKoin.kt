package good.space.runnershi.di

import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    appDeclaration: KoinAppDeclaration = {},
    extraModules: List<Module> = emptyList()
): Koin {
    return startKoin {
        appDeclaration()
        modules(
            appModule,
            *extraModules.toTypedArray()
        )
    }.koin
}
