package woogie.space.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import woogie.space.messenger.sign.SignActivity

class MainActivity : AppCompatActivity() {
    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var mGoogleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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