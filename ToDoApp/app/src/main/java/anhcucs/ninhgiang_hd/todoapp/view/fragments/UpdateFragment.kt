package anhcucs.ninhgiang_hd.todoapp.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import anhcucs.ninhgiang_hd.todoapp.R
import anhcucs.ninhgiang_hd.todoapp.model.Priority
import anhcucs.ninhgiang_hd.todoapp.model.TodoData
import anhcucs.ninhgiang_hd.todoapp.viewmodel.SharedViewModel
import anhcucs.ninhgiang_hd.todoapp.viewmodel.TodoViewModel

class UpdateFragment : Fragment() {
    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel by viewModels<SharedViewModel>()
    private val todoViewModel by viewModels<TodoViewModel>()

    private var currentTitleET: EditText? = null
    private var currentPrioritiesSpinner: Spinner? = null
    private var currentDescriptionEt: EditText? = null

    private var tempResult: TodoData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update, container, false)
        return view
    }

    private fun initView() {
        currentTitleET = requireView().findViewById(R.id.current_title_et)
        currentPrioritiesSpinner = requireView().findViewById(R.id.current_priorities_spinner)
        currentDescriptionEt = requireView().findViewById(R.id.current_description_et)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.munu_update, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        tempResult = args.resultData
        currentTitleET?.setText(tempResult?.title)
        currentDescriptionEt?.setText(tempResult?.description)
        currentPrioritiesSpinner?.setSelection(parsePriority(tempResult!!.priority))
        currentPrioritiesSpinner?.onItemSelectedListener = sharedViewModel.listener
    }

    private fun updateDataToDB() {
        val txtTitle = currentTitleET?.text.toString()
        val txtDes = currentDescriptionEt?.text.toString()
        val currentPriority = currentPrioritiesSpinner?.selectedItem.toString()
        val validate = sharedViewModel.checkInput(txtTitle, txtDes)
        if (validate) {
            val updateData = tempResult?.id?.let {
                TodoData(
                    it,
                    txtTitle,
                    sharedViewModel.parsePriority(currentPriority),
                    txtDes
                )
            }
            if (updateData != null) {
                todoViewModel.update(updateData)
            }
            Toast.makeText(requireContext(), "Update thành công", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Update thất bại", Toast.LENGTH_SHORT).show()
        }
    }


    private fun parsePriority(priority: Priority): Int {
        return when (priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_update -> {
                updateDataToDB()
            }
            R.id.menu_delete -> {
                confirmDeleteItem()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDeleteItem() {
        val builder = AlertDialog.Builder(
            requireContext(),
        ).setPositiveButton("Có") { _, _ ->
            tempResult?.let { todoViewModel.delete(it) }
            Toast.makeText(requireContext(), "Xóa thành công '${tempResult?.title}'", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("Không") { _, _ ->
        }
        builder.setTitle("Bạn có muốn xóa '${tempResult?.title}'")
        builder.create().show()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UpdateFragment()
    }
}