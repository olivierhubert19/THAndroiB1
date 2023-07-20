package hidenobi.baseapplication.other.extension

import android.content.Context
import hidenobi.baseapplication.other.PreferenceHelper.codeLanguage
import java.util.Locale

fun Context.setUpLanguage(): Context {
    val config = resources.configuration
    codeLanguage?.let {
        val locale = Locale(it)
        config.setLocale(locale)
    }
    return createConfigurationContext(config)
}