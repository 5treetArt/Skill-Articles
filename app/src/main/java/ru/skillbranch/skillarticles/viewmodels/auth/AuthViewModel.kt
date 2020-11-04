package ru.skillbranch.skillarticles.viewmodels.auth

import androidx.lifecycle.SavedStateHandle
import kotlinx.android.synthetic.main.fragment_registration.*
import ru.skillbranch.skillarticles.data.repositories.RootRepository
import ru.skillbranch.skillarticles.extensions.toggleError
import ru.skillbranch.skillarticles.viewmodels.base.BaseViewModel
import ru.skillbranch.skillarticles.viewmodels.base.IViewModelState
import ru.skillbranch.skillarticles.viewmodels.base.NavigationCommand

class AuthViewModel(handle: SavedStateHandle) :
    BaseViewModel<AuthState>(handle, AuthState()), IAuthViewModel {

    private val repository = RootRepository

    init {
        subscribeOnDataSource(repository.isAuth()) {isAuth, state ->
            state.copy(isAuth = isAuth)
        }
    }

    private val nameRegex = Regex("^[\\w\\d-_]{3,}$")
    private val passRegex = Regex("^[\\w\\d]{8,}$")

    override fun handleLogin(login: String, pass: String, dest: Int?) {
        launchSafely {
            repository.login(login, pass)
            navigate(NavigationCommand.FinishLogin(dest))
        }
    }

    fun handleRegisterName(name: String) {
        updateState { it.copy(isNameCorrect = nameRegex.matches(name)) }
    }

    fun handleRegisterLogin(login: String) {
        updateState { it.copy(isLoginCorrect = login.isNotBlank()) }
    }

    fun handleRegisterPassword(password: String) {
        updateState { it.copy(isPasswordCorrect = passRegex.matches(password)) }
    }

    fun handleRegister(name: String, login: String, password: String, dest: Int?) {
        with(currentState) {
            if (isNameCorrect && isLoginCorrect && isPasswordCorrect) {
                launchSafely {
                    repository.register(name, login, password)
                    navigate(NavigationCommand.FinishLogin(dest))
                }
            }
        }
    }
}

data class AuthState(
    val isAuth: Boolean = false,
    val isNameCorrect: Boolean = true,
    val isLoginCorrect: Boolean = true,
    val isPasswordCorrect: Boolean = true
) : IViewModelState