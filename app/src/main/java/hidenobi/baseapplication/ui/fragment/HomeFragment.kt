package hidenobi.baseapplication.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import hidenobi.baseapplication.R
import hidenobi.baseapplication.base.BaseFragment
import hidenobi.baseapplication.databinding.FragmentHomeBinding
import hidenobi.baseapplication.models.Book
import hidenobi.baseapplication.ui.activity.HomeViewModel
import hidenobi.baseapplication.ui.adapter.BookAdapter
import hidenobi.baseapplication.ui.adapter.OnClickBook

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    private val listBook = ArrayList<Book>()
    private lateinit var bookAdapter: BookAdapter
    override fun initView() {
        initFirstBook()
        initBookAdapter()
    }

    private fun initFirstBook() {
        listBook.add(
            Book(
                name = "Alice in wonderland",
                author = "David",
                publishedDate = "12/07/2002",
                color = R.color.ColorLightYellow,
                isNovel = true
            )
        )
        listBook.add(
            Book(
                name = "Harry potter",
                author = "JK",
                publishedDate = "12/07/1996",
                color = R.color.ColorBlur,
                isNovel = true
            )
        )
    }

    private fun initBookAdapter() {
        bookAdapter = BookAdapter(listBook, object : OnClickBook {
            override fun update(item: Book) {

            }

            override fun deleteBook(item: Book) {

            }

        })
        binding.rvTasks.adapter = bookAdapter
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