package woogie.space.messenger.friends

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import woogie.space.messenger.MainViewModel
import woogie.space.messenger.R
import woogie.space.messenger.add.AddActivity
import woogie.space.messenger.base.BaseMainFragment
import woogie.space.messenger.databinding.FragmentFriendsBinding
import woogie.space.messenger.model.Friends
import woogie.space.messenger.sign.SignActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FriendsFragment : BaseMainFragment<FragmentFriendsBinding,MainViewModel>(), View.OnClickListener{
    private var param1: String? = null
    private var param2: String? = null
    override val viewModel: MainViewModel by activityViewModels()

    private lateinit var friendsAdapter: FriendsAdapter
    lateinit var lManager : LinearLayoutManager
    var friendsList = arrayListOf<Friends>()

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val database = Firebase.database

    val userRef = database.getReference("users/")

    override fun BindInit() {
        bind.apply {
            friends = viewModel
            lifecycleOwner = requireActivity()
            executePendingBindings()

            BtnAddFriends.setOnClickListener(this@FriendsFragment)

//            friendsAdapter = FriendsAdapter(requireActivity(), fakeList())
//            lManager = LinearLayoutManager(requireActivity())
//            lManager.orientation = LinearLayoutManager.VERTICAL
//            FriendsRecyclerView.layoutManager = lManager
//            FriendsRecyclerView.adapter = friendsAdapter

//            ConLoading.visibility = View.GONE
//            ConNothing.visibility = View.GONE
        }

    }

    fun fakeList() : ArrayList<Friends>{
        for (i in 1..100) {
            friendsList.add(Friends(1,"김형욱$i"))
        }
        return friendsList
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
                startActivity(Intent(requireActivity(),AddActivity::class.java).putExtra("Type",1))
            }

        }
    }
}