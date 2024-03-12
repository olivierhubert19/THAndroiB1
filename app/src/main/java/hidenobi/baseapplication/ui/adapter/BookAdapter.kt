package hidenobi.baseapplication.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import hidenobi.baseapplication.R
import hidenobi.baseapplication.base.BaseRecyclerAdapter
import hidenobi.baseapplication.base.BaseViewHolder
import hidenobi.baseapplication.databinding.ItemBookBinding
import hidenobi.baseapplication.models.Book

class BookAdapter(private val list: ArrayList<Book>, private val onClickBook: OnClickBook) :
    BaseRecyclerAdapter<Book>(list) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Book> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBookBinding.inflate(inflater, parent, false)
        return BookViewHolder(binding)
    }

    inner class BookViewHolder(private val binding: ItemBookBinding) :
        BaseViewHolder<Book>(binding) {
        override fun bind(item: Book) {
            val resources = binding.root.resources
            val context = binding.root.context
            binding.apply {
                cardItemBook.setCardBackgroundColor(resources.getColor(item.color, context.theme))
                tvNameBook.text = context.getString(R.string.name_s, item.name)
                tvAuthor.text = context.getString(R.string.author_s, item.author)
                tvYear.text = context.getString(R.string.publication_date_s, item.publishedDate)
                cbScience.isChecked = item.isScience
                cbNovel.isChecked = item.isNovel
                cbKid.isChecked = item.isKid
            }
            binding.cardItemBook.setOnClickListener {
                onClickBook.update(item)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: ArrayList<Book>) {
        this.list.clear()
        this.list.addAll(newList)
        this.notifyDataSetChanged()
    }
}

interface OnClickBook {
    fun update(item: Book)
}
