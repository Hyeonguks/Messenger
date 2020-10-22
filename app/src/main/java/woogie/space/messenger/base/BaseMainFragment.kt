package woogie.space.messenger.base

import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseMainFragment<B : ViewDataBinding,V : ViewModel>(): Fragment() {
    protected lateinit var bind: B
    abstract val viewModel: V

    abstract fun BindInit()

    protected fun showToast(msg: String) =
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}