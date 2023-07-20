package hidenobi.baseapplication.ui.activity

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import hidenobi.baseapplication.R
import hidenobi.baseapplication.base.BaseActivity
import hidenobi.baseapplication.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, HomeViewModel>() {
    private lateinit var navController: NavController
    private lateinit var hostFragment: NavHostFragment
    override fun initView() {
        setUpNavController()
    }

    override fun getClassViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private fun setUpNavController() {
        hostFragment =
            supportFragmentManager.findFragmentById(R.id.frameContainer) as NavHostFragment
        navController = hostFragment.navController
    }

}