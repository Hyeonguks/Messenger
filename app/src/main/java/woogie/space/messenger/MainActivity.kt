package woogie.space.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import woogie.space.messenger.base.BaseMainActivity
import woogie.space.messenger.databinding.ActivityMainBinding
import woogie.space.messenger.sign.SignActivity

class MainActivity : BaseMainActivity<ActivityMainBinding,MainViewModel>(R.layout.activity_main), View.OnClickListener {
    override val viewModel: MainViewModel by viewModels()
    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var mGoogleSignInClient : GoogleSignInClient
    val mainPagerAdapter = MainPagerAdapter(this, supportFragmentManager)

    override fun bindInit() {
        binding.apply {
            main = viewModel
            lifecycleOwner = this@MainActivity
            executePendingBindings()

            fab.setOnClickListener(this@MainActivity)

            viewPager.adapter = mainPagerAdapter
            tabs.setupWithViewPager(viewPager)
            tabs.getTabAt(0)!!.text = null
            tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_camera_alt_24)
            tabs.getTabAt(1)!!.setIcon(R.drawable.people)
            tabs.getTabAt(2)!!.setIcon(R.drawable.ic_chatbubble)
            tabs.getTabAt(3)!!.setIcon(R.drawable.settings)

            val layout = (tabs.getChildAt(0) as LinearLayout).getChildAt(0) as LinearLayout
            val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 0.5f // e.g. 0.5f
            layout.layoutParams = layoutParams

            tabs.selectTab(tabs.getTabAt(1))
            tabs.getTabAt(2)!!.orCreateBadge.number = 43
//        tabLayout.getTabAt(0).showBadge().setNumber(1);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindInit()
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account == null) {
            startActivity(Intent(this@MainActivity, SignActivity::class.java))
            finish()
        } else {
//        이미 로그인한 사용자일 경우
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }
            else -> {

            }
        }
    }
}