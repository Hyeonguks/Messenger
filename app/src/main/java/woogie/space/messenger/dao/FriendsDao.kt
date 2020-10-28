package woogie.space.messenger.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import woogie.space.messenger.model.Friends
import woogie.space.messenger.model.SearchUserHistory

@Dao
interface FriendsDao {

    @Query("SELECT * FROM friends ORDER BY email DESC")
    fun getAll(): LiveData<List<Friends>>

    //해당 데이터를 추가합니다.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg friends: Friends)

    //해당 데이터를 업데이트 합니다.
    @Update
    suspend fun updateFriendByEmail(vararg friends: Friends)

    @Query("DELETE FROM friends")
    suspend fun deleteAll()

    @Query("DELETE FROM friends WHERE email = :email")
    suspend fun deleteFriendByEmail(email: String)
}