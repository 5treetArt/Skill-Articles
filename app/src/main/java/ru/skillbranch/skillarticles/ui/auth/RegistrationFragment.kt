package ru.skillbranch.skillarticles.ui.auth

import androidx.annotation.VisibleForTesting
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.android.synthetic.main.fragment_registration.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.extensions.toggleError
import ru.skillbranch.skillarticles.ui.RootActivity
import ru.skillbranch.skillarticles.ui.base.BaseFragment
import ru.skillbranch.skillarticles.ui.base.Binding
import ru.skillbranch.skillarticles.ui.delegates.RenderProp
import ru.skillbranch.skillarticles.viewmodels.auth.AuthState
import ru.skillbranch.skillarticles.viewmodels.auth.AuthViewModel
import ru.skillbranch.skillarticles.viewmodels.base.IViewModelState

class RegistrationFragment() : BaseFragment<AuthViewModel>() {

    // for testing
    var _mockFactory: ((SavedStateRegistryOwner) -> ViewModelProvider.Factory)? = null

    override val viewModel: AuthViewModel by viewModels {
        _mockFactory?.invoke(this) ?: defaultViewModelProviderFactory
    }
    override val layout: Int = R.layout.fragment_registration

    override val binding by lazy { RegistrationBinding() }

    // testing constructors
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    constructor(
        mockRoot: RootActivity,
        mockFactory: ((SavedStateRegistryOwner) -> ViewModelProvider.Factory)? = null
    ) : this() {
        _mockRoot = mockRoot
        _mockFactory = mockFactory
    }

    private val args: RegistrationFragmentArgs by navArgs()

    override fun setupViews() {
        et_name.doAfterTextChanged { viewModel.handleRegisterName(it.toString()) }
        et_login.doAfterTextChanged { viewModel.handleRegisterLogin(it.toString()) }
        et_password.doAfterTextChanged { viewModel.handleRegisterPassword(it.toString()) }

        btn_register.setOnClickListener {
            viewModel.handleRegister(
                et_name.text.toString(),//.trim(),
                et_login.text.toString(),//.trim(),
                et_password.text.toString(),//.trim(),
                if (args.privateDestination == -1) null else args.privateDestination
            )
        }

    }

    inner class RegistrationBinding : Binding() {

        private var isNameCorrect by RenderProp(true) {
            wrap_name.toggleError(
                """The name must be at least 3 characters long and contain only letters and numbers and can also contain the characters "-" and "_""""
                    .takeIf { _ -> !it }
            )
        }

        private var isLoginCorrect by RenderProp(true) {
            wrap_login.toggleError("Incorrect Email entered".takeIf { _ -> !it })
        }

        private var isPasswordCorrect by RenderProp(true) {
            wrap_password.toggleError(
                "Password must be at least 8 characters long and contain only letters and numbers"
                    .takeIf { _ -> !it }
            )
        }

        override fun bind(data: IViewModelState) {
            data as AuthState
            isNameCorrect = data.isNameCorrect
            isLoginCorrect = data.isLoginCorrect
            isPasswordCorrect = data.isPasswordCorrect
        }
    }
}