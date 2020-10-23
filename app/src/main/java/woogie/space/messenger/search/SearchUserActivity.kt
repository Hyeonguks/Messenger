package woogie.space.messenger.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import woogie.space.messenger.R
import woogie.space.messenger.base.BaseSearchUserActivity
import woogie.space.messenger.databinding.ActivitySearchUserBinding

class SearchUserActivity : BaseSearchUserActivity<ActivitySearchUserBinding,SearchUserViewModel>(R.layout.activity_search_user) {
    override val viewModel: SearchUserViewModel by viewModels()

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
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isEmpty()) {
                    binding.ConNothing.visibility = View.VISIBLE
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
}