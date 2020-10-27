package woogie.space.messenger.search

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import woogie.space.messenger.R
import woogie.space.messenger.base.BaseSearchUserActivity
import woogie.space.messenger.databinding.ActivitySearchUserBinding
import woogie.space.messenger.model.SearchUserHistory
import woogie.space.messenger.userprofile.UserProfileActivity

class SearchUserActivity : BaseSearchUserActivity<ActivitySearchUserBinding, SearchUserViewModel>(R.layout.activity_search_user), View.OnClickListener {
    override val viewModel: SearchUserViewModel by viewModels()

    val db = FirebaseFirestore.getInstance()
    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()

    var lastSearchedUserName : String = ""
    var isSearching : Boolean = false
    private lateinit var historyAdapter: SearchUserHistoryAdapter

    /*
    1. 최근 검색이 가장 밑에있다 이거 해결해야함.
    2. 리사이클러 뷰 스크롤 바 표시.
    4. 아이템 클릭 시 해당 검색기록으로 검
     */

    override fun bindInit() {
        binding.apply {
            userSearch = viewModel
            lifecycleOwner = this@SearchUserActivity
            executePendingBindings()

            SearchToolbar.title = resources.getString(R.string.UserSearch)
            setSupportActionBar(SearchToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            ConResult.setOnClickListener(this@SearchUserActivity)

            historyAdapter = SearchUserHistoryAdapter(this@SearchUserActivity)
            historyAdapter.itemClick = object : SearchUserHistoryAdapter.ItemClick {
                override fun onClick(view: View, position: Int, searchUserHistory: SearchUserHistory, requestCode: Int) {
                    if (requestCode == historyAdapter.DEFAULT_ITEM_DELETE) {
                        viewModel.deleteSearchedText(searchUserHistory.index)
                    } else if (requestCode == historyAdapter.FIRST_ITEM_DELETE) {
                        viewModel.deleteSearchedText(searchUserHistory.index)
                    } else if (requestCode == historyAdapter.FIRST_ITEM_DELETE_ALL) {
                        viewModel.deleteAllSearchedText()
                    } else if (requestCode == historyAdapter.DEFAULT_ITEM_CLICK) {

                    } else if (requestCode == historyAdapter.FIRST_ITEM_CLICK) {

                    } else {

                    }
                }

            }
            SearchHistoryRecyclerView.layoutManager = LinearLayoutManager(this@SearchUserActivity)
            SearchHistoryRecyclerView.adapter = historyAdapter
        }
    }


    override fun observeUI() {
        viewModel.getSearchedHistoryList().observe(this@SearchUserActivity, Observer {
            historyAdapter.setList(it)
            if (it.isEmpty()) {
                Log.e("viewModel.allSearchedList.observe", "it.isEmpty()")
                binding.SearchHistoryRecyclerView.visibility = View.GONE
                binding.ConResult.visibility = View.GONE
                binding.ConNothing.visibility = View.VISIBLE
                binding.ConResultNothing.visibility = View.GONE
                binding.Loading.visibility = View.GONE
            } else if (isSearching) {
                Log.e("viewModel.allSearchedList.observe", "isSearching")
                binding.SearchHistoryRecyclerView.visibility = View.GONE
                binding.ConResult.visibility = View.GONE
                binding.ConNothing.visibility = View.GONE
                binding.ConResultNothing.visibility = View.GONE
                binding.Loading.visibility = View.VISIBLE
            } else {
                Log.e("viewModel.allSearchedList.observe", "it.isNotEmpty()")
                binding.SearchHistoryRecyclerView.visibility = View.VISIBLE
                binding.ConResult.visibility = View.GONE
                binding.ConNothing.visibility = View.GONE
                binding.ConResultNothing.visibility = View.GONE
                binding.Loading.visibility = View.GONE
            }
        })
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
                if (TextUtils.isEmpty(query)) {
                    Toast.makeText(this@SearchUserActivity,"내용을 입력해주세요.",Toast.LENGTH_SHORT).show()
                } else if (mAuth.currentUser!!.email.equals(query)) {
                    binding.ConResult.visibility = View.GONE
                    binding.ConResultNothing.visibility = View.VISIBLE
                    binding.ConNothing.visibility = View.GONE
                    binding.SearchHistoryRecyclerView.visibility = View.GONE
                    binding.Loading.visibility = View.GONE
                } else {
                    SearchingUser(query!!)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isEmpty() && viewModel.getSearchedHistoryList().value!!.isNotEmpty()) {
                    binding.ConNothing.visibility = View.GONE
                    binding.ConResult.visibility = View.GONE
                    binding.SearchHistoryRecyclerView.visibility = View.VISIBLE
                    binding.ConResultNothing.visibility = View.GONE
                    binding.Loading.visibility = View.GONE
                } else if (newText.isEmpty() && viewModel.getSearchedHistoryList().value!!.isEmpty()) {
                    binding.ConNothing.visibility = View.VISIBLE
                    binding.ConResult.visibility = View.GONE
                    binding.SearchHistoryRecyclerView.visibility = View.GONE
                    binding.ConResultNothing.visibility = View.GONE
                    binding.Loading.visibility = View.GONE
                } else if(newText == lastSearchedUserName) {
                    binding.ConNothing.visibility = View.GONE
                    binding.ConResult.visibility = View.VISIBLE
                    binding.SearchHistoryRecyclerView.visibility = View.GONE
                    binding.ConResultNothing.visibility = View.GONE
                    binding.Loading.visibility = View.GONE
                } else {
                    binding.ConNothing.visibility = View.GONE
                    binding.ConResult.visibility = View.GONE
                    binding.SearchHistoryRecyclerView.visibility = View.GONE
                    binding.ConResultNothing.visibility = View.GONE
                    binding.Loading.visibility = View.GONE
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
                startActivity(Intent(this@SearchUserActivity, UserProfileActivity::class.java))
            }
        }
    }

    fun SearchingUser(email: String){
        isSearching = true
        viewModel.insertSearchedText(email)
        db.collection(resources.getString(R.string.users))
            .document(email)
            .get()
            .addOnSuccessListener {
                if (it.get("email").toString() == "null") {
                    Log.e("onQueryTextSubmit",it.get("email").toString())
                    binding.ConResult.visibility = View.GONE
                    binding.ConResultNothing.visibility = View.VISIBLE
                    binding.ConNothing.visibility = View.GONE
                    binding.SearchHistoryRecyclerView.visibility = View.GONE
                    binding.Loading.visibility = View.GONE
                    isSearching = false
                } else {
                    lastSearchedUserName = email
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
                    binding.Loading.visibility = View.GONE
                    isSearching = false
                }
            }
            .addOnFailureListener {
                binding.Loading.visibility = View.GONE
                isSearching = false
                Log.e("onQueryTextSubmit", it.stackTraceToString())
                Toast.makeText(this@SearchUserActivity,it.stackTraceToString(),Toast.LENGTH_SHORT).show()
            }
    }
}