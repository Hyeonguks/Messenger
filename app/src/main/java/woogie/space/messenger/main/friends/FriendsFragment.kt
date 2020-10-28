package woogie.space.messenger.main.friends

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import woogie.space.messenger.main.MainViewModel
import woogie.space.messenger.R
import woogie.space.messenger.add.AddUserActivity
import woogie.space.messenger.base.BaseMainFragment
import woogie.space.messenger.databinding.FragmentFriendsBinding
import woogie.space.messenger.main.MainActivity
import woogie.space.messenger.model.FireStoreFriends
import woogie.space.messenger.model.Friends
import woogie.space.messenger.model.SearchUserHistory
import woogie.space.messenger.search.SearchUserActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FriendsFragment : BaseMainFragment<FragmentFriendsBinding, MainViewModel>(), View.OnClickListener{
    private var param1: String? = null
    private var param2: String? = null

    override val viewModel: MainViewModel by activityViewModels()

    private lateinit var friendsAdapter: FriendsAdapter

    val db = FirebaseFirestore.getInstance()
    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()


    override fun BindInit() {
        bind.apply {
            friends = viewModel
            lifecycleOwner = requireActivity()
            executePendingBindings()

            (activity as AppCompatActivity).setSupportActionBar(FriendsToolbar)
            // https://stackoverflow.com/a/51047037
            setHasOptionsMenu(true)

            BtnAddFriends.setOnClickListener(this@FriendsFragment)

            friendsAdapter = FriendsAdapter(requireActivity())
            friendsAdapter.itemClick = object : FriendsAdapter.ItemClick {
                override fun onClick(view: View, position: Int, friend: Friends, requestCode: Int) {

                }

            }

            FriendsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
            FriendsRecyclerView.adapter = friendsAdapter
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DataBindingUtil.inflate(inflater,R.layout.fragment_friends,container,false)
        BindInit()
        observeUI()
        getFriendsList()
        return bind.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FriendsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Btn_Add_Friends -> {
//                startActivity(Intent(requireActivity(),SearchUserActivity::class.java))
                startActivity(Intent(requireActivity(), AddUserActivity::class.java))
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.friends_toolbar_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_user_search -> startActivity(Intent(requireActivity(),SearchUserActivity::class.java))
            R.id.action_user_add -> startActivity(Intent(requireActivity(), AddUserActivity::class.java))
            R.id.action_user_setting -> startActivity(Intent(requireActivity(),SearchUserActivity::class.java))
            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun setVisibility (it : List<Friends>) {
        if (it.isEmpty()) {
            Log.e("FriendsFragment","FriendsList is Empty")
            bind.FriendsRecyclerView.visibility = View.GONE
            bind.ConNothing.visibility = View.VISIBLE
            bind.ConLoading.visibility = View.GONE
        } else {
            Log.e("FriendsFragment","FriendsList is Not Empty")
            bind.FriendsRecyclerView.visibility = View.VISIBLE
            bind.ConNothing.visibility = View.GONE
            bind.ConLoading.visibility = View.GONE
        }
    }

    fun observeUI () {
        viewModel.getFriendsList().observe(requireActivity(), Observer {
            friendsAdapter.setList(it)
            setVisibility(it)
        })
    }

    fun getFriendsList() {
        db.collection(resources.getString(R.string.friends))
            .document(mAuth.currentUser!!.email.toString())
            .get()
            .addOnSuccessListener {
                val map = it.toObject(FireStoreFriends::class.java)

                db.runTransaction { transaction ->
                    for (i in map!!.friendsList) {
                        Log.e("getFriendsList",i)
                        db.collection(resources.getString(R.string.users))
                            .document(i)
                            .get()
                            .addOnSuccessListener { friendsInfo ->
                                viewModel.insertFriend(Friends(1,
                                    friendsInfo["displayName"].toString(),
                                    friendsInfo["email"].toString(),
                                    friendsInfo["photoUrl"].toString(),
                                    friendsInfo["uid"].toString()))
                            }.addOnFailureListener { }
                    }
                }.addOnSuccessListener {
//                    friendsAdapter.setList(it.data!!["friendsList"] as List<Friends>)
//                    setVisibility(it.data!!["friendsList"] as List<Friends>)
                }.addOnFailureListener {

                }
            }
            .addOnFailureListener {
                Log.e("FriendsFragment",it.stackTraceToString())
            }
    }


}