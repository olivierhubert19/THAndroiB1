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
import hidenobi.baseapplication.databinding.AddPlayerBottomSheetBinding
import hidenobi.baseapplication.databinding.FragmentHomeBinding
import hidenobi.baseapplication.models.Gender
import hidenobi.baseapplication.models.Player
import hidenobi.baseapplication.ui.activity.HomeViewModel
import hidenobi.baseapplication.ui.adapter.BookAdapter
import hidenobi.baseapplication.ui.adapter.OnClickBook
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Calendar


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    private val listPlayer = ArrayList<Player>()
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
            bookAdapter.setData(listPlayer)
        } else {
            val searchList = listPlayer.filter {
                it.name.lowercase().contains(p.lowercase())
            } as ArrayList
            bookAdapter.setData(searchList)
        }
    }

    private fun openAddBottomDialog(player: Player?) {
        val bottomBinding = AddPlayerBottomSheetBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        bottomSheetDialog.setContentView(bottomBinding.root)
        bottomSheetDialog.setOnShowListener {
            val bsd = it as BottomSheetDialog
            val bs = bsd.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?: return@setOnShowListener
            bs.setBackgroundColor(Color.TRANSPARENT)
        }
        player?.let {
            bottomBinding.apply {
                edtName.setText(it.name)
                tvDateTime.text = it.birthday
                cbDefender.isChecked = it.isDefender
                cbMidfielder.isChecked = it.isMidfielder
                cbStriker.isChecked = it.isStriker
                if (it.gender == Gender.MALE) {
                    rbFemale.isChecked = false
                    rbMale.isChecked = true
                } else {
                    rbFemale.isChecked = true
                    rbMale.isChecked = false
                }
            }
        }
        bottomBinding.apply {
            ivDone.setOnClickListener {
                val name = edtName.text.toString()
                val date = tvDateTime.text.toString()
                val isDefender = cbDefender.isChecked
                val isMidfielder = cbMidfielder.isChecked
                val isStriker = cbStriker.isChecked
                val id = player?.id ?: System.currentTimeMillis()
                val gender = if (rbFemale.isChecked) Gender.FEMALE else Gender.MALE
                val color = if (id % 2 == 0L) R.color.ColorBlur else R.color.ColorLightYellow
                if (name.isEmpty() || date.isEmpty()) {
                    showToast(R.string.cannot_blank)
                } else if (!isDefender && !isMidfielder && !isStriker) {
                    showToast(R.string.must_choose_position)
                } else {
                    val newPlayer =
                        Player(name, date, gender, isDefender, isMidfielder, isStriker, color, id)
                    if (player == null) {
                        saveBook(newPlayer)
                    } else {
                        updateBook(newPlayer)
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
                        val chooseTime = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                        val chooseMillisecond =
                            chooseTime.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
                        if (chooseMillisecond > System.currentTimeMillis()) {
                            showToast(R.string.birthday_fail)
                        } else {
                            tvDateTime.text =
                                dayOfMonth.toString().plus("/").plus((monthOfYear + 1).toString())
                                    .plus("/").plus(year.toString())
                        }
                    }, mYear, mMonth, mDay
                )
                datePickerDialog.show()
            }

        }
        bottomSheetDialog.show()
    }

    private fun saveBook(newPlayer: Player) {
        listPlayer.add(newPlayer)
        bookAdapter.setData(listPlayer)
    }

    private fun updateBook(newPlayer: Player) {
        val index = listPlayer.indexOf(newPlayer)
        if (index < 0) {
            showToast(R.string.update_fail)
        } else {
            listPlayer[index] = newPlayer
            bookAdapter.setData(listPlayer)
        }
    }

    private fun initFirstBook() {
        listPlayer.add(
            Player(
                name = "David",
                gender = Gender.MALE,
                birthday = "12/07/2002",
                color = R.color.ColorLightYellow,
                isMidfielder = true,
                id = 1
            )
        )
        listPlayer.add(
            Player(
                name = "JK",
                birthday = "12/07/1996",
                gender = Gender.FEMALE,
                color = R.color.ColorBlur,
                isDefender = true,
                id = 2
            )
        )
    }

    private fun initBookAdapter() {
        val cacheList = ArrayList<Player>()
        cacheList.addAll(listPlayer)
        bookAdapter = BookAdapter(cacheList, object : OnClickBook {
            override fun update(item: Player) {
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
                listPlayer.removeAt(position)
                bookAdapter.setData(listPlayer)
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