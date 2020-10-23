package woogie.space.messenger.search

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import woogie.space.messenger.R
import woogie.space.messenger.base.BaseSearchUserActivity
import woogie.space.messenger.databinding.ActivitySearchUserBinding
import woogie.space.messenger.main.friends.FriendsAdapter
import woogie.space.messenger.model.Friends

class SearchUserActivity : BaseSearchUserActivity<ActivitySearchUserBinding, SearchUserViewModel>(R.layout.activity_search_user), View.OnClickListener {
    override val viewModel: SearchUserViewModel by viewModels()
    val db = FirebaseFirestore.getInstance()

    var lastSearchedUserName : String = ""

    private lateinit var friendsAdapter: FriendsAdapter
    lateinit var lManager: LinearLayoutManager
    var friendsList = arrayListOf<Friends>()

//            friendsAdapter = FriendsAdapter(requireActivity(), fakeList())
//            lManager = LinearLayoutManager(requireActivity())
//            lManager.orientation = LinearLayoutManager.VERTICAL
//            FriendsRecyclerView.layoutManager = lManager
//            FriendsRecyclerView.adapter = friendsAdapter

    override fun bindInit() {
        binding.apply {
            userSearch = viewModel
            lifecycleOwner = this@SearchUserActivity
            executePendingBindings()

            setSupportActionBar(SearchToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_search_menu, menu)
        // Define the listener
        val expandListener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                // Do something when action item collapses
                return true // Return true to collapse action view
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                // Do something when expanded
                return true // Return true to expand action view
            }
        }

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "사용자 이메일 검색.."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                db.collection(resources.getString(R.string.users))
                    .document(query!!)
                    .get()
                    .addOnSuccessListener {
                        if (it.get("email").toString() == "null") {
                            Log.e("onQueryTextSubmit",it.get("email").toString())
                            binding.ConResult.visibility = View.GONE
                            binding.ConResultNothing.visibility = View.VISIBLE
                            binding.ConNothing.visibility = View.GONE
                            binding.SearchHistoryRecyclerView.visibility = View.GONE
                        } else {
                            lastSearchedUserName = query
                            Glide.with(this@SearchUserActivity)
                                .load(it.get("photoUrl").toString())
                                .centerCrop()
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(binding.UserPhoto)
                            binding.UserName.text = it.get("displayName").toString()
                            binding.UserEmail.text = it.get("email").toString()

                            binding.ConResult.visibility = View.VISIBLE
                            binding.ConResultNothing.visibility = View.GONE
                            binding.ConNothing.visibility = View.GONE
                            binding.SearchHistoryRecyclerView.visibility = View.GONE
                        }
                    }
                    .addOnFailureListener {
                        Log.e("onQueryTextSubmit", it.stackTraceToString())
                        Toast.makeText(this@SearchUserActivity,it.stackTraceToString(),Toast.LENGTH_SHORT).show()
                    }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isEmpty()) {
                    binding.ConNothing.visibility = View.VISIBLE
                    binding.ConResult.visibility = View.GONE
                    binding.SearchHistoryRecyclerView.visibility = View.GONE
                    binding.ConResultNothing.visibility = View.GONE
                } else if(newText == lastSearchedUserName) {
                    binding.ConNothing.visibility = View.GONE
                    binding.ConResult.visibility = View.VISIBLE
                    binding.SearchHistoryRecyclerView.visibility = View.GONE
                    binding.ConResultNothing.visibility = View.GONE
                } else {
                    binding.ConNothing.visibility = View.GONE
                    binding.ConResult.visibility = View.GONE
                    binding.SearchHistoryRecyclerView.visibility = View.GONE
                    binding.ConResultNothing.visibility = View.GONE
                }
                return true
            }

        })

        // Assign the listener to that action item
        searchItem.setOnActionExpandListener(expandListener)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_search -> {
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.Con_Result -> {

            }
        }
    }
}