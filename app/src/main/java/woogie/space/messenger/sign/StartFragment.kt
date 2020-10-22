package woogie.space.messenger.sign

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_start.view.*
import woogie.space.messenger.MainActivity
import woogie.space.messenger.R
import woogie.space.messenger.base.BaseSignFragment
import woogie.space.messenger.databinding.FragmentStartBinding

class StartFragment: BaseSignFragment<FragmentStartBinding, SignViewModel>(R.layout.fragment_start), View.OnClickListener {
    override val viewModel: SignViewModel by activityViewModels()

    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var mGoogleSignInClient : GoogleSignInClient

//    이미 로그인한 사용자일 경우
//    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//    updateUI(account);

    override fun Init() {
        BindInit()
        KeyboardActionDone()

        bind.root.Btn_Next.setOnClickListener(this)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    override fun BindInit() {
        bind.apply {
            start = viewModel
            lifecycleOwner = requireActivity()
            executePendingBindings()
        }
    }

    override fun InitToolbar() { }

    override fun KeyboardActionDone() {}

    override fun Next() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, 1)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Btn_Next -> {
                Next()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.e("StartFragment", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.e("StartFragment", "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e("StartFragment", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    startActivity(Intent(requireActivity(),MainActivity::class.java))
                    requireActivity().finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e("StartFragment", "signInWithCredential:failure", task.exception)
                    Snackbar.make(bind.root, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }
            }
    }
}