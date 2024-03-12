package hidenobi.baseapplication.ui.fragment

import android.app.DatePickerDialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import hidenobi.baseapplication.R
import hidenobi.baseapplication.base.BaseFragment
import hidenobi.baseapplication.databinding.AddBookBottomSheetBinding
import hidenobi.baseapplication.databinding.FragmentHomeBinding
import hidenobi.baseapplication.models.Book
import hidenobi.baseapplication.ui.activity.HomeViewModel
import hidenobi.baseapplication.ui.adapter.BookAdapter
import hidenobi.baseapplication.ui.adapter.OnClickBook
import java.util.Calendar


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    private val listBook = ArrayList<Book>()
    private lateinit var bookAdapter: BookAdapter
    override fun initView() {
        initFirstBook()
        initBookAdapter()
        initActions()
    }

    private fun initActions() {
        binding.apply {
            btnAdd.setOnClickListener {
                openAddBottomDialog(null)
            }

            svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    searchBookByName(p0)
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    if (p0 == null || p0 == "") {
                        searchBookByName(null)
                    }
                    return false
                }
            })
        }
    }

    private fun searchBookByName(p: String?) {
        if (p.isNullOrEmpty()) {
            bookAdapter.setData(listBook)
        } else {
            val searchList = listBook.filter {
                it.name.lowercase().contains(p.lowercase())
            } as ArrayList
            bookAdapter.setData(searchList)
        }
    }

    private fun openAddBottomDialog(book: Book?) {
        val bottomBinding = AddBookBottomSheetBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        bottomSheetDialog.setContentView(bottomBinding.root)
        bottomSheetDialog.setOnShowListener {
            val bsd = it as BottomSheetDialog
            val bs = bsd.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?: return@setOnShowListener
            bs.setBackgroundColor(Color.TRANSPARENT)
        }
        book?.let {
            bottomBinding.apply {
                edtNameBook.setText(it.name)
                tvDateTime.text = it.publishedDate
                edtAuthor.setText(it.author)
                cbScience.isChecked = it.isScience
                cbNovel.isChecked = it.isNovel
                cbKid.isChecked = it.isKid
            }
        }
        bottomBinding.apply {
            ivDone.setOnClickListener {
                val name = edtNameBook.text.toString()
                val author = edtAuthor.text.toString()
                val date = tvDateTime.text.toString()
                val isNovel = cbNovel.isChecked
                val isKid = cbKid.isChecked
                val isScience = cbScience.isChecked
                val id = book?.id ?: System.currentTimeMillis()
                val color = if (id % 2 == 0L) R.color.ColorBlur else R.color.ColorLightYellow
                if (name.isEmpty() || author.isEmpty() || date.isEmpty()) {
                    showToast(R.string.cannot_blank)
                } else {
                    val newBook = Book(name, author, date, isScience, isNovel, isKid, color, id)
                    if (book == null) {
                        saveBook(newBook)
                    } else {
                        updateBook(newBook)
                    }
                    bottomSheetDialog.dismiss()
                }
            }
            tvDateTime.setOnClickListener {
                val c: Calendar = Calendar.getInstance()
                val mYear = c.get(Calendar.YEAR)
                val mMonth = c.get(Calendar.MONTH)
                val mDay = c.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, year, monthOfYear, dayOfMonth ->
                        tvDateTime.text =
                            dayOfMonth.toString().plus("/").plus((monthOfYear + 1).toString())
                                .plus("/").plus(year.toString())
                    }, mYear, mMonth, mDay
                )
                datePickerDialog.show()
            }

        }
        bottomSheetDialog.show()
    }

    private fun saveBook(newBook: Book) {
        listBook.add(newBook)
        bookAdapter.setData(listBook)
    }

    private fun updateBook(newBook: Book) {
        val index = listBook.indexOf(newBook)
        if (index < 0) {
            showToast(R.string.update_fail)
        } else {
            listBook[index] = newBook
            bookAdapter.setData(listBook)
        }
    }

    private fun initFirstBook() {
        listBook.add(
            Book(
                name = "Alice in wonderland",
                author = "David",
                publishedDate = "12/07/2002",
                color = R.color.ColorLightYellow,
                isNovel = true,
                id = 1
            )
        )
        listBook.add(
            Book(
                name = "Harry potter",
                author = "JK",
                publishedDate = "12/07/1996",
                color = R.color.ColorBlur,
                isNovel = true,
                id = 2
            )
        )
    }

    private fun initBookAdapter() {
        val cacheList = ArrayList<Book>()
        cacheList.addAll(listBook)
        bookAdapter = BookAdapter(cacheList, object : OnClickBook {
            override fun update(item: Book) {
                openAddBottomDialog(item)
            }
        })
        binding.rvTasks.adapter = bookAdapter
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    ItemTouchHelper.END or ItemTouchHelper.START
                )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                listBook.removeAt(position)
                bookAdapter.setData(listBook)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvTasks)
    }

    private fun showToast(@StringRes stringId: Int) {
        Toast.makeText(requireContext(), stringId, Toast.LENGTH_SHORT).show()
    }

    override fun getClassViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

}