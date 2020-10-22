package woogie.space.messenger

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import woogie.space.messenger.camera.CameraFragment
import woogie.space.messenger.friends.FriendsFragment
import woogie.space.messenger.rooms.ChattingRoomsFragment

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class MainPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return when(position) {
            1 -> {
                CameraFragment.newInstance("","")
            }
            2 -> {
                FriendsFragment.newInstance("","")
            }
            3 -> {
                ChattingRoomsFragment.newInstance("","")
            }
            else -> {
                ChattingRoomsFragment.newInstance("","")
            }
        }
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 4
    }
}