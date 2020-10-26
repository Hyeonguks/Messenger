package woogie.space.messenger.repo

import androidx.lifecycle.LiveData
import woogie.space.messenger.dao.SearchUserHistoryDao
import woogie.space.messenger.model.SearchUserHistory

/*
리포지토리를 사용하는 이유는 무엇입니까?
리포지토리는 쿼리를 관리하고 여러 백엔드를 사용할 수 있도록합니다. 가장 일반적인 예에서 리포지토리는 네트워크에서 데이터를 가져올 지 아니면 로컬 데이터베이스에 캐시 된 결과를 사용할지 결정하는 논리를 구현합니다.
 */

class SearchUserHistoryRepository(private val searchUserHistoryDao: SearchUserHistoryDao) {

    suspend fun insertSearchUserHistory(searchUserHistoryText : String) {
        searchUserHistoryDao.insert(SearchUserHistory(searchUserHistoryText))
    }

    suspend fun deleteSearchUserHistory(searchedText: String) {
        searchUserHistoryDao.deleteBySearchText(searchedText)
    }

    suspend fun deleteAllSearchUserHistory() {
        searchUserHistoryDao.deleteAll()
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allHistory: LiveData<List<SearchUserHistory>> = searchUserHistoryDao.getAll()
}