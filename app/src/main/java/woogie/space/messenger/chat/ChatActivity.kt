package woogie.space.messenger.chat

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import woogie.space.messenger.R
import woogie.space.messenger.base.BaseChatActivity
import woogie.space.messenger.databinding.ActivityChatBinding
import woogie.space.messenger.util.OnKeyboardVisibilityListener


class ChatActivity : BaseChatActivity<ActivityChatBinding, ChatViewModel>(), View.OnClickListener, OnKeyboardVisibilityListener{
    override val viewModel: ChatViewModel by viewModels()

    // 더보기의 아이콘이 플러스냐 닫기냐
    private var moreBoolean = true
    var keyboardHeight = 0
    var rootHeight = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        bindInit()

        setKeyboardVisibilityListener(this)
    }

    override fun bindInit() {
        binding.apply {
            chat = viewModel
            lifecycleOwner = this@ChatActivity
            executePendingBindings()

            ChatEditText.setOnClickListener(this@ChatActivity)
            ConMore.setOnClickListener(this@ChatActivity)
            ConSend.setOnClickListener(this@ChatActivity)
        }
    }

    fun View.showKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        this.requestFocus()
        imm.showSoftInput(this, 0)
    }

    fun View.hideKeyboard(): Boolean {
        try {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        } catch (ignored: RuntimeException) { }
        return false
    }

    fun View.setHeight(value: Int) {
        val lp = layoutParams
        lp?.let {
            lp.height = value
            layoutParams = lp
        }
    }

    override fun onPause() {
        super.onPause()
        // 다시 액티비티가 전면으로 돌아왔을때 키보드가 안올라옴.
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }


    private fun setKeyboardVisibilityListener(onKeyboardVisibilityListener: OnKeyboardVisibilityListener) {
        binding.ConRoot.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (rootHeight == -1) rootHeight = binding.ConRoot.height // 매번 호출되기 때문에, 처음 한 번만 값을 할당해준다.
                val visibleFrameSize = Rect()
                binding.ConRoot.getWindowVisibleDisplayFrame(visibleFrameSize)
                val heightExceptKeyboard = visibleFrameSize.bottom - visibleFrameSize.top
                // 키보드를 제외한 높이가 디바이스 root_view보다 높거나 같다면, 키보드가 올라왔을 때가 아니므로 거른다.
                if (heightExceptKeyboard < rootHeight) {
                    if (keyboardHeight == 0) {
                        keyboardHeight = rootHeight - heightExceptKeyboard
                        binding.ConMenu.setHeight(keyboardHeight)
                        binding.ConRoot.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                    Log.e("keyboardHeight", "opned -> ${rootHeight - heightExceptKeyboard}")
                    return
                } else {
                    Log.e("keyboardHeight", "hide -> ${rootHeight - heightExceptKeyboard}")
                    return
                }
            }
        })
    }

    override fun onVisibilityChanged(visible: Boolean) {}

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ChatEditText -> {
                if (moreBoolean) {

                } else {
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                    moreBoolean = !moreBoolean
                    binding.ImageMore.setImageResource(R.drawable.ic_baseline_add_24)

                    binding.ChatEditText.showKeyboard()
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.ConMenu.visibility = View.GONE
                        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    }, 100)
                }
            }
            R.id.Con_More -> {
                if (keyboardHeight == 0) {
                    Log.e("Con_More","keyboardHeight == 0")
                    binding.ChatEditText.showKeyboard()
                } else if (moreBoolean) {
                    // 메뉴 열림, 키보드 숨김
                    Log.e("Con_More",moreBoolean.toString())
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                    moreBoolean = !moreBoolean
                    binding.ConMenu.visibility = View.VISIBLE
                    binding.ImageMore.setImageResource(R.drawable.ic_baseline_close_24)
                    binding.ChatEditText.hideKeyboard()
                    Handler(Looper.getMainLooper()).postDelayed({
                        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    }, 100)
                } else {
                    // 메뉴 닫음, 키보드 열림
                    Log.e("Con_More",moreBoolean.toString())
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                    moreBoolean = !moreBoolean
                    binding.ImageMore.setImageResource(R.drawable.ic_baseline_add_24)
                    binding.ChatEditText.showKeyboard()
                    Handler(Looper.getMainLooper()).postDelayed({
                        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                        binding.ConMenu.visibility = View.GONE
                    }, 100)
                }
            }
            R.id.Con_Send -> {

            }
        }
    }
}