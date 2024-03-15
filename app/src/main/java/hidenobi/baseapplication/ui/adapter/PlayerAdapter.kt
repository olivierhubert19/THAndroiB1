package hidenobi.baseapplication.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import hidenobi.baseapplication.R
import hidenobi.baseapplication.base.BaseRecyclerAdapter
import hidenobi.baseapplication.base.BaseViewHolder
import hidenobi.baseapplication.databinding.ItemPlayerBinding
import hidenobi.baseapplication.models.Gender
import hidenobi.baseapplication.models.Player

class BookAdapter(private val list: ArrayList<Player>, private val onClickBook: OnClickBook) :
    BaseRecyclerAdapter<Player>(list) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Player> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPlayerBinding.inflate(inflater, parent, false)
        return BookViewHolder(binding)
    }

    inner class BookViewHolder(private val binding: ItemPlayerBinding) :
        BaseViewHolder<Player>(binding) {
        override fun bind(item: Player) {
            val resources = binding.root.resources
            val context = binding.root.context
            binding.apply {
                cardItemBook.setCardBackgroundColor(resources.getColor(item.color, context.theme))
                tvName.text = context.getString(R.string.name_s, item.name)
                if (item.gender == Gender.MALE) {
                    ivAvt.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_man
                        )
                    )
                } else {
                    ivAvt.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_women
                        )
                    )
                }
                tvYear.text = context.getString(R.string.birthday_s, item.birthday)
                cbDefender.isChecked = item.isDefender
                cbMidfielder.isChecked = item.isMidfielder
                cbStriker.isChecked = item.isStriker
            }
            binding.cardItemBook.setOnClickListener {
                onClickBook.update(item)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: ArrayList<Player>) {
        this.list.clear()
        this.list.addAll(newList)
        this.notifyDataSetChanged()
    }
}

interface OnClickBook {
    fun update(item: Player)
}
