package anhcucs.ninhgiang_hd.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import anhcucs.ninhgiang_hd.todoapp.model.TodoData
import anhcucs.ninhgiang_hd.todoapp.model.TodoDatabase
import anhcucs.ninhgiang_hd.todoapp.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDAO = TodoDatabase.getDatabase(application).todoDao()
    private val repository: TodoRepository = TodoRepository(todoDAO = todoDAO)

    val getAllData: LiveData<List<TodoData>> = repository.getAllData

    val listSortByHigh: LiveData<List<TodoData>>
    val listSortByLow: LiveData<List<TodoData>>

    init {
        listSortByHigh = repository.getListSortByHigh
        listSortByLow = repository.getListSortByLow
    }


    fun insert(todoData: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTodo(todoData)
        }
    }

    fun update(todoData: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateToDB(todoData)
        }
    }

    fun delete(todoData: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteToDB(todoData)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun search(searchQuery: String): LiveData<List<TodoData>> {
        return repository.search(searchQuery)
    }
}