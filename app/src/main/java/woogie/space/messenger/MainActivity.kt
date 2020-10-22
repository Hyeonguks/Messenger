package woogie.space.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import woogie.space.messenger.sign.SignActivity

class MainActivity : AppCompatActivity() {
    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var mGoogleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainPagerAdapter = MainPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = mainPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.text = null
        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_camera_alt_24)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_chatbubble)
        tabs.getTabAt(2)!!.setIcon(R.drawable.people)
        tabs.getTabAt(3)!!.setIcon(R.drawable.settings)

        val layout = (tabs.getChildAt(0) as LinearLayout).getChildAt(0) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0.5f // e.g. 0.5f
        layout.layoutParams = layoutParams


        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        tabs.selectTab(tabs.getTabAt(1))
        tabs.getTabAt(1)!!.orCreateBadge.number = 43
//        tabLayout.getTabAt(0).showBadge().setNumber(1);
    }

    override fun onStart() {
        super.onStart()
//        이미 로그인한 사용자일 경우
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account == null) {
            startActivity(Intent(this@MainActivity, SignActivity::class.java))
            finish()
        } else {

        }
    }
}