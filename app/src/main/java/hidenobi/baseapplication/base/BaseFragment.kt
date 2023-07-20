package hidenobi.baseapplication.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {
    private var _binding: VB? = null
    val binding: VB get() = _binding!!
    private var _viewModel: VM? = null
    val activityViewModel: VM get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = initViewBinding(inflater, container)
        _viewModel = ViewModelProvider(requireActivity())[getClassViewModel()]
        return binding.root
    }

    abstract fun initView()

    abstract fun getClassViewModel(): Class<VM>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroy() {
        _binding = null
        _viewModel = null
        super.onDestroy()
    }

    abstract fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
}