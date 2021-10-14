package anhcucs.ninhgiang_hd.todoapp.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import anhcucs.ninhgiang_hd.todoapp.R
import anhcucs.ninhgiang_hd.todoapp.adapter.ListAdapter
import anhcucs.ninhgiang_hd.todoapp.databinding.FragmentListBinding
import anhcucs.ninhgiang_hd.todoapp.model.TodoData
import anhcucs.ninhgiang_hd.todoapp.utils.SwipeToDelete
import anhcucs.ninhgiang_hd.todoapp.viewmodel.SharedViewModel
import anhcucs.ninhgiang_hd.todoapp.viewmodel.TodoViewModel
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.LandingAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {
    private val todoViewModel: TodoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val myAdapter: ListAdapter by lazy {
        ListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observerList()
        initAction()
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.rcvList
        recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            adapter = myAdapter
        }
        recyclerView.itemAnimator = LandingAnimator().apply {
            addDuration = 300

        }
        swipeItem(recyclerView)
    }

    private fun swipeItem(recyclerView: RecyclerView) {
        val swipeItem = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = myAdapter.dataList[viewHolder.adapterPosition]

                todoViewModel.delete(itemToDelete)
                myAdapter.notifyItemChanged(viewHolder.adapterPosition)

                restoreItemDelete(viewHolder.itemView, itemToDelete, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeItem)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreItemDelete(view: View, itemDelete: TodoData, position: Int) {
        val snackBar = Snackbar.make(
            view, "Deleted ${itemDelete.title}",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            todoViewModel.insert(itemDelete)
        }
        snackBar.show()
    }

    private fun initAction() {
        myAdapter.onItemClick = { data ->
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToUpdateFragment(
                    data
                )
            )
        }

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
    }

    private fun observerList() {
        todoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            sharedViewModel.checkListData(data)
            myAdapter.setData(data)
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> {
                confirmDeleteAll()
            }
            R.id.menu_priority_high -> {
                todoViewModel.listSortByHigh.observe(viewLifecycleOwner, Observer {
                    myAdapter.setData(it)
                })
            }
            R.id.menu_priority_low -> {
                todoViewModel.listSortByLow.observe(viewLifecycleOwner, Observer {
                    myAdapter.setData(it)
                })
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun confirmDeleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Có") { _, _ ->
            todoViewModel.deleteAll()
            Toast.makeText(requireContext(), "Xóa tất cả Data", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Không") { _, _ ->
        }
        builder.setTitle("Bạn có muốn xóa toàn bộ dữ liệu")
        builder.create().show()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ListFragment()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }


    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchDatabase(newText)
        }
        return true
    }

    private fun searchDatabase(query: String) {
        var searchQuery = query
        searchQuery = "%$searchQuery%"
        todoViewModel.search(searchQuery).observe(viewLifecycleOwner, Observer {
            myAdapter.setData(it)
        })
    }
}