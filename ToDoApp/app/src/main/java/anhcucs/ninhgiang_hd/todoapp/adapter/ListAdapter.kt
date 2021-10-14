package anhcucs.ninhgiang_hd.todoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import anhcucs.ninhgiang_hd.todoapp.R
import anhcucs.ninhgiang_hd.todoapp.model.Priority
import anhcucs.ninhgiang_hd.todoapp.model.TodoData
import anhcucs.ninhgiang_hd.todoapp.utils.TodoDiffUtils

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var dataList = emptyList<TodoData>()
    var onItemClick: (todoData: TodoData) -> Unit = {}

    inner class MyViewHolder(onItemClick: (todoData: TodoData) -> Unit, itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val titleTxt = itemView.findViewById<TextView>(R.id.title_txt)
        private val descriptionTxt = itemView.findViewById<TextView>(R.id.description_txt)
        private val priorityIndicator = itemView.findViewById<CardView>(R.id.priority_indicator)

        init {
            itemView.setOnClickListener {
                onItemClick(dataList[adapterPosition])
            }
        }

        fun bind(todoData: TodoData) {
            titleTxt.text = todoData.title
            descriptionTxt.text = todoData.description
            when (dataList[adapterPosition].priority) {
                Priority.HIGH -> priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context, R.color.red
                    )
                )
                Priority.MEDIUM -> priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context, R.color.yellow
                    )
                )
                Priority.LOW -> priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context, R.color.green
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            onItemClick,
            LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val todoData = dataList[position]
        holder.bind(todoData)
    }

    override fun getItemCount(): Int = dataList.size

    fun setData(todoData: List<TodoData>) {
        val todoDiffUtil = TodoDiffUtils(dataList, todoData)
        val todoResult = DiffUtil.calculateDiff(todoDiffUtil)
        this.dataList = todoData
        todoResult.dispatchUpdatesTo(this)
    }
}