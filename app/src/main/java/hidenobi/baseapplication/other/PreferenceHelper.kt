package hidenobi.baseapplication.other

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PreferenceHelper {

    private lateinit var preferences: SharedPreferences
    fun customPreference(context: Context, tag: String = APP_NAME) {
        preferences = context.getSharedPreferences(tag, Context.MODE_PRIVATE)
    }

    var codeLanguage
        get() = preferences[LANGUAGE_CODE, VIETNAMESE]
        set(value) {
            preferences[LANGUAGE_CODE] = value
        }
}

private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}

/**
 * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
 */
operator fun SharedPreferences.set(key: String, value: Any?) {
    val gson = Gson()
    when (value) {
        is String? -> edit { it.putString(key, value) }
        is Int -> edit { it.putInt(key, value) }
        is Boolean -> edit { it.putBoolean(key, value) }
        is Float -> edit { it.putFloat(key, value) }
        is Long -> edit { it.putLong(key, value) }
        else -> edit {
            val json = gson.toJson(value)
            it.putString(key, json)
        }
    }
}

/**
 * finds value on given key.
 * [T] is the type of value
 * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
 */
inline operator fun <reified T : Any> SharedPreferences.get(
    key: String,
    defaultValue: T? = null
): T? {
    val gson = Gson()
    return when (T::class) {
        String::class -> getString(key, defaultValue as? String) as T?
        Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
        Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
        Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
        Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
        else -> {
            val json = getString(key, "")
            gson.fromJson(json, T::class.java)
        }
    }
}

fun SharedPreferences.saveList(key: String, value: List<*>) {
    edit {
        val gson = Gson()
        val json = gson.toJson(value)
        it.putString(key, json)
    }
}

inline fun <reified T : Any> SharedPreferences.getList(key: String): ArrayList<T> {
    val json = getString(key, "")
    return jsonToList(json)
}

inline fun <reified T : Any> jsonToList(json: String?): ArrayList<T> {
    val gson = Gson()
    val type = TypeToken.getParameterized(ArrayList::class.java, T::class.java).type
    if (json.isNullOrEmpty()) {
        return ArrayList<T>()
    }
    return gson.fromJson(json, type)
}