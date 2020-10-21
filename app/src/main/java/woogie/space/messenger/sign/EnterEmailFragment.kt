package woogie.space.messenger.sign

import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_enter_email.view.*
import woogie.space.messenger.R
import woogie.space.messenger.base.BaseSignFragment
import woogie.space.messenger.databinding.FragmentEnterEmailBinding

class EnterEmailFragment: BaseSignFragment<FragmentEnterEmailBinding, SignViewModel>(R.layout.fragment_enter_email), View.OnClickListener {
    override val viewModel: SignViewModel by activityViewModels()

    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun Init() {
        BindInit()
        KeyboardActionDone()

        bind.root.Btn_Next.setOnClickListener(this)

        viewModel.eMail.observe(requireActivity(), Observer {
            viewModel.eMailButtonEnable.value = Patterns.EMAIL_ADDRESS.matcher(it).matches()
        })

        viewModel.eMailButtonEnable.observe(requireActivity(), Observer {
            viewModel.onChangedEmailBoolean()
        })
    }

    override fun BindInit() {
        bind.apply {
            email = viewModel
            lifecycleOwner = requireActivity()
            executePendingBindings()
        }
    }

    override fun InitToolbar() { }

    override fun KeyboardActionDone() {
        // (2).소프프키보드 완료버튼 클릭 이벤트 처리
        bind.root.editText_id.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.Navigator_Sign_OR_Login()
                return@OnEditorActionListener true
            } else {
                false
            }
        })
    }

    override fun Next() {
        mAuth.signInWithEmailAndPassword(viewModel.eMail.value.toString(),viewModel.passWord.value.toString())
            .addOnCompleteListener(requireActivity()) { task->
                if(task.isSuccessful){
                    //아이디 생성이 완료 되었을 때
                    val user = mAuth.currentUser
                    Log.e("EnterNameFragment", "createUserWithEmail:Success")
                }else{
                    Log.e("EnterNameFragment", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(requireContext(), task.exception!!.message,
                        Toast.LENGTH_SHORT).show()
                }
            }
        if (true) {
            // 회원가입
            Navigation.findNavController(bind.root).navigate(R.id.action_from_default_to_pw)
        } else {
            Navigation.findNavController(bind.root).navigate(R.id.action_navigation_signup_login_navigator_to_navigation_login)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Btn_Next -> { Next() }
        }
    }
}