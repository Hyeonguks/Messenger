package woogie.space.messenger.add

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import woogie.space.messenger.camera.CameraFragment
import woogie.space.messenger.main.friends.FriendsFragment
import woogie.space.messenger.main.rooms.ChattingRoomsFragment
import woogie.space.messenger.main.settings.SettingsFragment

class AddPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return when(position) {
            0 -> {
                CameraFragment.newInstance("","")
            }
            1 -> {
                FriendsFragment.newInstance("","")
            }
            2 -> {
                ChattingRoomsFragment.newInstance("","")
            }
            else -> {
                SettingsFragment.newInstance("","")
            }
        }
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 4
    }
}