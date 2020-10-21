package woogie.space.messenger.sign

import android.os.Bundle
import androidx.activity.viewModels
import woogie.space.messenger.R
import woogie.space.messenger.base.BaseSignActivity
import woogie.space.messenger.databinding.ActivitySignBinding

class SignActivity : BaseSignActivity<ActivitySignBinding, SignViewModel>(R.layout.activity_sign) {
    override val viewModel: SignViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            sign = viewModel
            lifecycleOwner = this@SignActivity
            executePendingBindings()
        }
    }
}