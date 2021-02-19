package ru.skillbranch.skillarticles.ui.auth

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_registration.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.ui.base.BaseFragment
import ru.skillbranch.skillarticles.viewmodels.auth.AuthViewModel

@AndroidEntryPoint
class RegistrationFragment() : BaseFragment<AuthViewModel>() {

    override val viewModel: AuthViewModel by activityViewModels()
    override val layout: Int = R.layout.fragment_registration

    //override val binding by lazy { RegistrationBinding() }

    private val args: RegistrationFragmentArgs by navArgs()

    override fun setupViews() {
        //et_name.doAfterTextChanged { viewModel.handleRegisterName(it.toString()) }
        //et_login.doAfterTextChanged { viewModel.handleRegisterLogin(it.toString()) }
        //et_password.doAfterTextChanged { viewModel.handleRegisterPassword(it.toString()) }

        btn_register.setOnClickListener {
            viewModel.handleRegister(
                et_name.text.toString(),//.trim(),
                et_login.text.toString(),//.trim(),
                et_password.text.toString(),//.trim(),
                if (args.privateDestination == -1) null else args.privateDestination
            )
        }

    }
/*
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
    }*/
}