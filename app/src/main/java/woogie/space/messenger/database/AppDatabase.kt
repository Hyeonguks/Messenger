package woogie.space.messenger.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import woogie.space.messenger.dao.LoginDao
import woogie.space.messenger.dao.SearchUserHistoryDao
import woogie.space.messenger.model.SearchUserHistory

// https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin#7

@Database(version = 1, exportSchema = false,
        entities = [
            SearchUserHistory::class,

        ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun searchUserHistoryDao(): SearchUserHistoryDao
    abstract fun loginDao(): LoginDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "messenger_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }


    private class DBCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    // 디비가 열릴 때 필요한 작업 여기에 작성.
                }
            }
        }
    }
}