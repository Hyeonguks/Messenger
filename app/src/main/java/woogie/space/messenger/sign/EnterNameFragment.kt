package woogie.space.messenger.sign

import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_enter_name.view.*
import woogie.space.messenger.BR
import woogie.space.messenger.R
import woogie.space.messenger.base.BaseSignFragment
import woogie.space.messenger.databinding.FragmentEnterNameBinding
import java.util.regex.Pattern

class EnterNameFragment : BaseSignFragment<FragmentEnterNameBinding,SignViewModel>(R.layout.fragment_enter_name), View.OnClickListener {
    override val viewModel: SignViewModel by activityViewModels()
    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun Init() {
        BindInit()
        InitToolbar()
        KeyboardActionDone()

        bind.root.Btn_Sign.setOnClickListener(this)

        viewModel.name.observe(requireActivity(), Observer {
            viewModel.nameButtonEnable.value = Pattern.matches("^[a-zA-Z0-9가-힣]{2,10}$", it)
        })
    }

    override fun BindInit() {
        bind.apply {
            name = viewModel
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
        bind.root.editText_name.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.Navigator_Sign_OR_Login()
                return@OnEditorActionListener true
            } else {
                false
            }
        })
    }

    override fun Next() {
        Log.e("SignActivity-Name",viewModel.eMail.value.toString())
        Log.e("SignActivity-Name",viewModel.passWord.value.toString())
        Log.e("SignActivity-Name",viewModel.name.value.toString())
        mAuth.createUserWithEmailAndPassword(viewModel.eMail.value.toString(),viewModel.passWord.value.toString())
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
//        if (true) {
//            // 회원가입
//            Navigation.findNavController(bind.root).navigate(R.id.action_navigation_pw_to_navigation_name)
//        } else {
//            Navigation.findNavController(bind.root).navigate(R.id.action_navigation_signup_login_navigator_to_navigation_login)
//        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Btn_Sign -> { Next() }
        }
    }
}