package good.space.runnershi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import good.space.runnershi.ui.home.HomeRoute
import good.space.runnershi.ui.login.LoginRoute
import good.space.runnershi.ui.navigation.Screen
import good.space.runnershi.ui.result.ResultDataHolder
import good.space.runnershi.ui.result.ResultRoute
import good.space.runnershi.ui.running.RunningRoute
import good.space.runnershi.ui.signup.SignUpRoute
import good.space.runnershi.ui.splash.SplashRoute
import good.space.runnershi.ui.splash.SplashViewModel
import good.space.runnershi.ui.theme.RunnersHiTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    viewModel: SplashViewModel = koinViewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    RunnersHiTheme {
        val navController = rememberNavController()

        // 로그인 여부가 결정되지 않았다면 스플래시 UI 렌더링
        if (isLoggedIn == null) {
            SplashRoute()
            return@RunnersHiTheme
        }

        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn == true) Screen.Home.name else Screen.Login.name
        ) {
            // 로그인 화면
            composable(route = Screen.Login.name) {
                LoginRoute(
                    navigateToHome = {
                        navController.navigate(Screen.Home.name) {
                            popUpTo(Screen.Login.name) { inclusive = true }
                        }
                    },
                    navigateToSignUp = {
                        navController.navigate(Screen.SignUp.name)
                    }
                )
            }

            // 회원가입 화면
            composable(route = Screen.SignUp.name) {
                SignUpRoute(
                    navigateBack = {
                        navController.popBackStack() // 뒤로 가기
                    },
                    navigateToHome = {
                        navController.navigate(Screen.Home.name)
                    }
                )
            }

            // 홈 화면
            composable(route = Screen.Home.name) {
                HomeRoute(
                    navigateToRunning = {
                        navController.navigate(Screen.Running.name)
                    },
                    navigateToLogin = {
                        navController.navigate(Screen.Login.name) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // 러닝 화면
            composable(route = Screen.Running.name) {
                RunningRoute(
                    navigateToResult = { userInfo, runResult ->
                        // 싱글톤 홀더에 데이터 직접 주입
                        ResultDataHolder.userInfo = userInfo
                        ResultDataHolder.runResult = runResult

                        // 결과 화면으로 이동
                        navController.navigate(Screen.Result.name)
                    }
                )
            }

            // 결과 화면
            composable(route = Screen.Result.name) {
                ResultRoute(
                    onCloseClick = {
                        // 홈 화면까지 모든 화면을 제거하고 홈으로 이동
                        if (!navController.popBackStack(Screen.Home.name, inclusive = false)) {
                            // Home이 백스택에 없으면 navigate로 이동
                            navController.navigate(Screen.Home.name) {
                                popUpTo(Screen.Home.name) { inclusive = false }
                            }
                        }
                    },
                    onDataMissing = {
                        // 데이터가 없을 경우에 대한 안전장치
                        if (!navController.popBackStack(Screen.Home.name, inclusive = false)) {
                            navController.navigate(Screen.Home.name) {
                                popUpTo(Screen.Home.name) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
    }
}
