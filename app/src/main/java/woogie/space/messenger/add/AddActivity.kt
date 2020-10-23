package woogie.space.messenger.add

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_enter_name.view.*
import woogie.space.messenger.R
import woogie.space.messenger.base.BaseAddActivity
import woogie.space.messenger.databinding.ActivityAddBinding

class AddActivity : BaseAddActivity<ActivityAddBinding, AddViewModel>(R.layout.activity_add), View.OnClickListener {

    override val viewModel: AddViewModel by viewModels()

    val addPagerAdapter = AddPagerAdapter(this, supportFragmentManager)


    override fun bindInit() {
        binding.apply {
            add = viewModel
            lifecycleOwner = this@AddActivity
            executePendingBindings()
            setSupportActionBar(AddToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)

            AddToolbar.setNavigationOnClickListener {
                finish()
            }

            viewPager.adapter = addPagerAdapter
            tabs.setupWithViewPager(viewPager)
            tabs.getTabAt(0)!!.text = null
            tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_camera_alt_24)
            tabs.getTabAt(1)!!.setIcon(R.drawable.people)
            tabs.getTabAt(2)!!.setIcon(R.drawable.ic_chatbubble)
            tabs.getTabAt(3)!!.setIcon(R.drawable.ic_baseline_settings_24)

            val layout = (tabs.getChildAt(0) as LinearLayout).getChildAt(0) as LinearLayout
            val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 0.5f // e.g. 0.5f
            layout.layoutParams = layoutParams

            tabs.selectTab(tabs.getTabAt(1))
            tabs.getTabAt(2)!!.orCreateBadge.number = 43
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindInit()
    }

    override fun onClick(v: View?) {
        when(v?.id) {

        }
    }
}