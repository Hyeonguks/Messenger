package woogie.space.messenger.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import woogie.space.messenger.sign.SignViewModel


//https://stackoverflow.com/questions/62591052/should-i-add-binding-lifecycleowner-this-when-i-use-viewmodel

abstract class BaseSignFragment<B : ViewDataBinding,V : ViewModel>(private val layoutID: Int): Fragment() {
    protected lateinit var bind: B
    abstract val viewModel: V

    abstract fun Init()
    abstract fun BindInit()
    abstract fun InitToolbar()
    abstract fun KeyboardActionDone()
    abstract fun Next()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DataBindingUtil.inflate(inflater,layoutID,container,false)
        Init()
        return bind.root
    }

    protected fun showToast(msg: String) =
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}