package woogie.space.messenger.sign

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_enter_pass_word.view.*
import woogie.space.messenger.R
import woogie.space.messenger.base.BaseSignFragment
import woogie.space.messenger.databinding.FragmentEnterPassWordBinding
import java.util.regex.Pattern

/*
    참고 사이트 : Navigation + Fragment + Toolbar + 뒤로가기 버튼 적용
    https://stackoverflow.com/questions/26651602/display-back-arrow-on-toolbar
    http://susemi99.kr/5438/
 */
class EnterPassWordFragment : BaseSignFragment<FragmentEnterPassWordBinding,SignViewModel>(R.layout.fragment_enter_pass_word), View.OnClickListener {
    override val viewModel: SignViewModel by activityViewModels()

    override fun Init() {
        BindInit()
        InitToolbar()
        KeyboardActionDone()
        InitCheckBox()

        bind.root.Btn_Next.setOnClickListener(this)

        viewModel.passWord.observe(requireActivity(), Observer {
            viewModel.passWordButtonEnable.value = Pattern.matches("^[a-zA-Z0-9]{5,12}$", it)
        })
    }

    override fun BindInit() {
        bind.apply {
            pw = viewModel
            lifecycleOwner = requireActivity()
            executePendingBindings()
        }
    }

    override fun InitToolbar() {
        (activity as AppCompatActivity).run {
            setSupportActionBar(bind.root.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        bind.root.toolbar.setNavigationOnClickListener {
            Navigation.findNavController(bind.root).navigateUp()
        }
    }

    override fun KeyboardActionDone() {
        bind.root.editText_pw.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.Navigator_Sign_OR_Login()
                return@OnEditorActionListener true
            } else {
                false
            }
        })
    }

    override fun Next() {
        Log.e("SignActivity-PW",viewModel.eMail.value.toString())
        Log.e("SignActivity-PW",viewModel.passWord.value.toString())
        Log.e("SignActivity-PW",viewModel.name.value.toString())
        if (true) {
            // 회원가입
            Navigation.findNavController(bind.root).navigate(R.id.action_navigation_pw_to_navigation_name)
        } else {
            Navigation.findNavController(bind.root).navigate(R.id.action_navigation_signup_login_navigator_to_navigation_login)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Btn_Next -> { Next() }
        }
    }

    fun InitCheckBox() {
        bind.root.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                bind.root.editText_pw.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                bind.root.editText_pw.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            bind.root.editText_pw.setSelection(bind.root.editText_pw.text.length)
        }
    }
}