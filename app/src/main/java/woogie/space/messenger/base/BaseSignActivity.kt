package woogie.space.messenger.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import woogie.space.messenger.sign.SignViewModel

abstract class BaseSignActivity<B : ViewDataBinding,V : ViewModel>(private val layoutId: Int) : AppCompatActivity() {

    protected lateinit var binding: B
    abstract val viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
    }
}