package woogie.space.messenger.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseChatActivity<B : ViewDataBinding,V : ViewModel> : AppCompatActivity() {

    protected lateinit var binding: B
    abstract val viewModel: V
    abstract fun bindInit()
}