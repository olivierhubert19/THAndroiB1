package hidenobi.baseapplication.ui.fragment

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
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
    private var listBook = ArrayList<Book>()
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
        listBook[index] = newBook
        bookAdapter.setData(listBook)
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
        bookAdapter = BookAdapter(listBook, object : OnClickBook {
            override fun update(item: Book) {
                openAddBottomDialog(item)
            }

            override fun deleteBook(item: Book) {
                listBook.remove(item)
                bookAdapter.setData(listBook)
            }

        })
        binding.rvTasks.adapter = bookAdapter
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