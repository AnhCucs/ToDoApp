package anhcucs.ninhgiang_hd.todoapp.repository

import androidx.lifecycle.LiveData
import anhcucs.ninhgiang_hd.todoapp.model.TodoDAO
import anhcucs.ninhgiang_hd.todoapp.model.TodoData

class TodoRepository(private val todoDAO: TodoDAO) {

    val getAllData = todoDAO.getAllData()

    val getListSortByHigh = todoDAO.sortByHighPriority()
    val getListSortByLow = todoDAO.sortByLowPriority()

    suspend fun insertTodo(todoData: TodoData) {
        todoDAO.insertData(todoData)
    }

    suspend fun updateToDB(todoData: TodoData) {
        todoDAO.updateData(todoData = todoData)
    }

    suspend fun deleteToDB(todoData: TodoData) {
        todoDAO.deleteData(todoData = todoData)
    }

    suspend fun deleteAll() {
        todoDAO.deleteAll()
    }

     fun search(searchQuery:String):LiveData<List<TodoData>>{
        return todoDAO.searchQuery(searchQuery)
    }
}