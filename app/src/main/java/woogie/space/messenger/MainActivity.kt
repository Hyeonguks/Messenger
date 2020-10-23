package woogie.space.messenger

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import woogie.space.messenger.base.BaseMainActivity
import woogie.space.messenger.databinding.ActivityMainBinding
import woogie.space.messenger.sign.SignActivity

class MainActivity : BaseMainActivity<ActivityMainBinding,MainViewModel>(R.layout.activity_main), View.OnClickListener {
    override val viewModel: MainViewModel by viewModels()

    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var mGoogleSignInClient : GoogleSignInClient

    override fun bindInit() {
        binding.apply {
            main = viewModel
            lifecycleOwner = this@MainActivity
            executePendingBindings()

            val navView: BottomNavigationView = findViewById(R.id.nav_view)

            val navController = findNavController(R.id.nav_host_fragment)
            navView.setupWithNavController(navController)
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
            else -> {
//                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }
        }
    }
}