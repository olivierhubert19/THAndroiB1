package hidenobi.baseapplication.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import hidenobi.baseapplication.base.BaseFragment
import hidenobi.baseapplication.databinding.FragmentHomeBinding
import hidenobi.baseapplication.ui.activity.HomeViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override fun initView() {

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