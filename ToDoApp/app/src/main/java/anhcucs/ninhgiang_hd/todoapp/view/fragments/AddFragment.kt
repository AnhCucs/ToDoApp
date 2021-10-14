package anhcucs.ninhgiang_hd.todoapp.view.fragments

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import anhcucs.ninhgiang_hd.todoapp.R
import anhcucs.ninhgiang_hd.todoapp.model.TodoData
import anhcucs.ninhgiang_hd.todoapp.viewmodel.SharedViewModel
import anhcucs.ninhgiang_hd.todoapp.viewmodel.TodoViewModel


class AddFragment : Fragment() {
    private val todoViewModel: TodoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private var prioritiesSpinner: Spinner? = null
    private var titleET: EditText? = null
    private var descriptionEt: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        prioritiesSpinner?.onItemSelectedListener = sharedViewModel.listener
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                insertDataToDB()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView(){
        prioritiesSpinner = requireView().findViewById(R.id.priorities_spinner)
        titleET = requireView().findViewById(R.id.title_et)
        descriptionEt = requireView().findViewById(R.id.description_et)
    }

    private fun insertDataToDB() {
        val titleData = titleET?.text.toString()
        val priority = prioritiesSpinner?.selectedItem.toString()
        val description = descriptionEt?.text.toString()

        val validation = sharedViewModel.checkInput(titleData, description)
        if (validation) {
            val newData = TodoData(
                0,
                titleData,
                sharedViewModel.parsePriority(priority),
                description
            )
            todoViewModel.insert(newData)
            Toast.makeText(requireContext(), "Add Thanh công", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(
                requireContext(),
                "Vui lòng nhập đủ thông tin các trường",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddFragment()
    }
}