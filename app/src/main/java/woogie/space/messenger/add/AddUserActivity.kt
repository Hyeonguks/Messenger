package woogie.space.messenger.add

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.firestore.ktx.toObject
import woogie.space.messenger.R
import woogie.space.messenger.databinding.ActivityAddUserBinding
import woogie.space.messenger.main.MainActivity
import java.util.*
import kotlin.collections.ArrayList

//https://material.io/develop/android/components/text-fields#outlined-text-field
class AddUserActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var binding : ActivityAddUserBinding
    lateinit var userObject : DocumentSnapshot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_user)
        binding.apply {
            AddToolbar.title = resources.getString(R.string.UserSearch)
            setSupportActionBar(AddToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            textInputLayout.editText!!.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchingUser(textInputLayout.editText!!.text.toString())
                    return@OnEditorActionListener true
                } else {
                    false
                }
            })

            BtnAdd.setOnClickListener { addFriends() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_user_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_ok -> {
                searchingUser(binding.textInputLayout.editText!!.text.toString())
            }
            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun searchingUser(email: String) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this,"이메일을 입력해주세요.",Toast.LENGTH_SHORT).show()
        } else if (mAuth.currentUser!!.email == email) {
            binding.NothingResult.visibility = View.VISIBLE
            binding.ConProfile.visibility = View.GONE
        } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            db.collection(resources.getString(R.string.users))
                .document(email)
                .get()
                .addOnSuccessListener {
                    if (it.get("email").toString() == "null") {
                        Log.e("onQueryTextSubmit",it.get("email").toString())
                        binding.NothingResult.visibility = View.VISIBLE
                        binding.ConProfile.visibility = View.GONE
                    } else {
                        userObject = it
                        binding.NothingResult.visibility = View.GONE
                        binding.ConProfile.visibility = View.VISIBLE

                        Glide.with(this)
                            .load(it.get("photoUrl").toString())
                            .centerCrop()
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(binding.UserImage)

                        binding.UserName.text = it.get("displayName").toString()
                    }
                }
                .addOnFailureListener {
                    Log.e("onQueryTextSubmit", it.stackTraceToString())
                    Toast.makeText(this,it.stackTraceToString(),Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this,"이메일 형식이 아닙니다.",Toast.LENGTH_SHORT).show()
        }
    }


    fun addFriends() {
//        https://firebase.google.com/docs/firestore/manage-data/add-data#kotlin+ktx
        val myRef = db.collection(resources.getString(R.string.friends)).document(mAuth.currentUser!!.email.toString())
        val userRef = db.collection(resources.getString(R.string.friends)).document(userObject.get("email").toString())

        db.runTransaction { transaction ->

            transaction.update(myRef,"friendsList",FieldValue.arrayUnion(userObject["email"]))
            transaction.update(userRef,"received",FieldValue.arrayUnion(mAuth.currentUser!!.email))

//            transaction.update(myRef, "friends",FieldValue.arrayRemove("asdasd@gmail.com"))
        }.addOnSuccessListener { result ->
            Log.e("addFriends", "Transaction success: $result")
        }.addOnFailureListener { e ->
            Log.e("addFriends", "Transaction failure.", e)
        }

    }


}