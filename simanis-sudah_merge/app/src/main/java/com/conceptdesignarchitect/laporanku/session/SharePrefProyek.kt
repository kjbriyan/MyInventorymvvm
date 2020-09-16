package com.conceptdesignarchitect.laporanku.session

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.conceptdesignarchitect.laporanku.models.User

class SharePrefProyek private constructor(private val mCtx: Context) {

    val USER_ID = "USER_ID"
    val USER_PASSWORD = "PASSWORD"

    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.password
        get() = getString(USER_PASSWORD, "")
        set(value) {
            editMe {
                it.putString(USER_PASSWORD, value)
            }
        }

    val user: User
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return User(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("nama", null),
                sharedPreferences.getString("password", null),
                sharedPreferences.getString("proyek", null),
                sharedPreferences.getString("posisi", null),
                sharedPreferences.getString("pdf", null)
            )
        }

    fun saveUser(user: User) {

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("id", user.id)
        editor.putString("nama", user.nama)
        editor.putString("password", user.password)
        editor.putString("posisi", user.posisi)

        editor.apply()
    }

    companion object {
        val SHARED_PREF_NAME = "my_shared_preff_proyek"
        private var mInstance: SharePrefProyek? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharePrefProyek {
            if (mInstance == null) {
                mInstance = SharePrefProyek(mCtx)
            }
            return mInstance as SharePrefProyek
        }
    }

}