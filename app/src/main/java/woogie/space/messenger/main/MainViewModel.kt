package woogie.space.messenger.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import woogie.space.messenger.database.AppDatabase
import woogie.space.messenger.model.Friends
import woogie.space.messenger.model.SearchUserHistory
import woogie.space.messenger.repo.FriendsRepository

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val friendsRepository : FriendsRepository

    init {
        val friendsDao = AppDatabase.getDatabase(application).friendsDao()
        friendsRepository = FriendsRepository(friendsDao)
    }

    fun insertFriend(friends: Friends) = viewModelScope.launch(Dispatchers.IO) {
        friendsRepository.insertFriend(friends)
    }

    fun deleteFriendEmail(email :String) = viewModelScope.launch(Dispatchers.IO) {
        friendsRepository.deleteFriendByEmail(email)
    }

    fun getFriendsList() : LiveData<List<Friends>> {
        // 올 히스토리라니 변수명 변경
        // 전부 삭제하는 메소드가 있어야하나?
        return friendsRepository.friendsList
    }
}