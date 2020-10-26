package woogie.space.messenger.search

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.*
import woogie.space.messenger.database.AppDatabase
import woogie.space.messenger.model.SearchUserHistory
import woogie.space.messenger.repo.SearchUserHistoryRepository

class SearchUserViewModel(application: Application): AndroidViewModel(application) {
    private val repository : SearchUserHistoryRepository

    init {
        val dao = AppDatabase.getDatabase(application).searchUserHistoryDao()
        repository = SearchUserHistoryRepository(dao)
    }

    fun insertSearchedText(searchText :String) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertSearchUserHistory(searchText)
    }

    fun deleteSearchedText(searchText :String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteSearchUserHistory(searchText)
    }

    fun deleteAllSearchedText() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllSearchUserHistory()
    }

    fun getSearchedHistoryList() : LiveData<List<SearchUserHistory>> {
        return repository.allHistory
    }
}