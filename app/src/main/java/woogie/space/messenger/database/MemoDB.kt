package woogie.space.messenger.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import woogie.space.messenger.dao.MemoDao
import woogie.space.messenger.model.MemoModel


@Database(entities = [MemoModel::class], version = 1, exportSchema = false)
abstract class MemoDB : RoomDatabase() {

    abstract fun memoDao(): MemoDao

    private class MemoDBCallback(
            private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
//                    var memoDao = database.memoDao()
//
//                    // Delete all content here.
//                    memoDao.deleteAll()
//
//                    // Add sample words.
//                    var momo = MemoModel(0,1,null,"안녕","메모는 처음이야?","",false,null)
//                    memoDao.insert(momo)
//                    momo = MemoModel(0,1,null,"안녕","메모는 처음이야?","",false,null)
//                    memoDao.insert(momo)
//
//                    // TODO: Add your own words!
//                    momo = MemoModel(0,1,null,"안녕","메모는 처음이야?","",false,null)
//                    memoDao.insert(momo)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MemoDB? = null

        fun getDatabase(
                context: Context,
                scope: CoroutineScope
        ): MemoDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MemoDB::class.java,
                        "memo_database"
                )
                        .addCallback(MemoDBCallback(scope))
                        .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}