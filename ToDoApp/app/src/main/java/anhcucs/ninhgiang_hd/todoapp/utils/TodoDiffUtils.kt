package anhcucs.ninhgiang_hd.todoapp.utils

import androidx.recyclerview.widget.DiffUtil
import anhcucs.ninhgiang_hd.todoapp.model.TodoData

class TodoDiffUtils(
    private val oldList: List<TodoData>,
    private val newList: List<TodoData>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

}