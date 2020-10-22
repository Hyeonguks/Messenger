package woogie.space.messenger.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import woogie.space.messenger.model.MemoModel

/*
https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin#5
https://developer.android.com/reference/androidx/room/OnConflictStrategy.html

onConflict = OnConflictStrategy.IGNORE: 선택한 onConflict 전략은 이미 목록에있는 단어와 정확히 동일한 경우 새 단어를 무시합니다.
suspend = 일시중지
Room에서 coroutine을 지원하면서 DAO에서 suspend function을 만들어 선언해 놓으면 해당 function들은 main thread에서 동작하지 않습니다.

따라서 DB 접근할때 따로 background thread를 만들어서 접근할 필요가 없어졌습니다.
 */
@Dao
interface MemoDao {

    @Query("SELECT * FROM memo ORDER BY Date DESC")
    fun getAllMemo(): LiveData<List<MemoModel>>

    @Query("DELETE FROM memo")
    fun clearAll()

    //해당 데이터를 추가합니다.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg memo : MemoModel)

    //해당 데이터를 업데이트 합니다.
    @Update
    fun update(vararg memo : MemoModel)

    @Query("DELETE FROM memo")
    suspend fun deleteAll()


}