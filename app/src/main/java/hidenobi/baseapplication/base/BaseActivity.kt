package hidenobi.baseapplication.base

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import hidenobi.baseapplication.other.extension.setUpLanguage

abstract class BaseActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity() {
    private var _binding: VB? = null
    val binding: VB get() = _binding!!
    private var _viewModel: VM? = null
    val viewModel: VM get() = _viewModel!!
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ContextWrapper(newBase?.setUpLanguage()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = initViewBinding()
        _viewModel = ViewModelProvider(this)[getClassViewModel()]
        initView()
        setContentView(binding.root)
    }

    override fun onDestroy() {
        _binding = null
        _viewModel = null
        super.onDestroy()
    }

    abstract fun initView()
    abstract fun getClassViewModel(): Class<VM>
    abstract fun initViewBinding(): VB
}