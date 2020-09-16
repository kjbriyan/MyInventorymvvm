package com.conceptdesignarchitect.laporanku.session
import android.content.Context
import android.content.SharedPreferences
import com.conceptdesignarchitect.laporanku.models.User

class SharedPrefManager private constructor(private val mCtx: Context) {

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt("id", -1) != -1
        }

    val isPengawas: Boolean
        get() {
            val sharePreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharePreferences.getString("posisi",  null) == "Pengawas"
        }

    val isPPK: Boolean
        get() {
            val sharePreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharePreferences.getString("posisi",  null) == "PPK"
        }

    val isKPA: Boolean
        get() {
            val sharePreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharePreferences.getString("posisi",  null) == "KPA"
        }

    val isPPSPM: Boolean
        get() {
            val sharePreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharePreferences.getString("posisi",  null) == "PPSPM"
        }

    val isKASUBDIT: Boolean
        get() {
            val sharePreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharePreferences.getString("posisi",  null) == "KASUBDIT"
        }

    val user: User
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return User(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("nama", null),
                sharedPreferences.getString("password", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("posisi", null),
                sharedPreferences.getString("nip", null)
            )
        }

    fun saveUser(user: User) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("id", user.id)
        editor.putString("nama", user.nama)
        editor.putString("password", user.password)
        editor.putString("email", user.email)
        editor.putString("posisi", user.posisi)
        editor.putString("nip", user.nip)
        editor.apply()
    }
    


    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        val SHARED_PREF_NAME = "my_shared_preff"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

}