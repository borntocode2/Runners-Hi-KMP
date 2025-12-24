package good.space.runnershi.shared.di

import good.space.runnershi.di.initKoin

fun initKoinIOS() {
    initKoin(extraModules = listOf(iosPlatformModule))
}
