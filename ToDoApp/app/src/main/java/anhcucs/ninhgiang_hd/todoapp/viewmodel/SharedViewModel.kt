package anhcucs.ninhgiang_hd.todoapp.viewmodel

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import anhcucs.ninhgiang_hd.todoapp.R
import anhcucs.ninhgiang_hd.todoapp.model.Priority
import anhcucs.ninhgiang_hd.todoapp.model.TodoData

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val checkData = MutableLiveData<Boolean>()

    val listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                0 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.red
                        )
                    )
                }
                1 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.yellow
                        )
                    )
                }
                2 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.green
                        )
                    )
                }
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }
    }

    fun checkListData(listData: List<TodoData>) {
        checkData.value = listData.isEmpty()
    }

    fun parsePriority(priority: String): Priority {
        return when (priority) {
            "High Priority" -> Priority.HIGH
            "Medium Priority" -> Priority.MEDIUM
            "Low Priority" -> Priority.LOW
            else -> {
                Priority.LOW
            }
        }
    }

    fun checkInput(title: String, description: String): Boolean {
        return !(title.isEmpty() || description.isEmpty())
    }
}