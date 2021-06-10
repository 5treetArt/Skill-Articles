package ru.skillbranch.skillarticles.ui.auth

import android.text.Spannable
import android.view.WindowManager
import androidx.core.text.set
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_auth.*
import ru.skillbranch.skillarticles.ui.base.BaseFragment
import ru.skillbranch.skillarticles.viewmodels.auth.AuthViewModel
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.extensions.attrValue
import ru.skillbranch.skillarticles.ui.custom.spans.UnderlineSpan
import ru.skillbranch.skillarticles.viewmodels.base.NavigationCommand

@AndroidEntryPoint
class AuthFragment : BaseFragment<AuthViewModel>() {

    override val viewModel: AuthViewModel by activityViewModels()
    override val layout: Int = R.layout.fragment_auth
    private val args: AuthFragmentArgs by navArgs()

    override fun setupViews() {

        tv_privacy.setOnClickListener {
            val action = AuthFragmentDirections.actionNavAuthToPagePrivacyPolicy()
            viewModel.navigate(NavigationCommand.To(action.actionId, action.arguments))
        }

        tv_register.setOnClickListener {
            val action = AuthFragmentDirections.actionNavAuthToNavRegister(args.privateDestination)
            viewModel.navigate(NavigationCommand.To(action.actionId, action.arguments))
        }

        btn_login.setOnClickListener {
            viewModel.handleLogin(
                et_login.text.toString(),
                et_password.text.toString(),
                if (args.privateDestination == -1) null else args.privateDestination
            )
        }

        val color = root.attrValue(R.attr.colorPrimary)

        setOf(tv_access_code, tv_privacy, tv_register).forEach { tv ->
            (tv.text as Spannable).let { it[0..it.length] = UnderlineSpan(color) }
        }

    }
}