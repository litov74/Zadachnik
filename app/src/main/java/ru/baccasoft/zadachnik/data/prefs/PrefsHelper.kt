package ru.baccasoft.zadachnik.data.prefs

import android.content.Context
import android.content.SharedPreferences
import ru.baccasoft.zadachnik.data.SessionDataStorage

class PrefsHelper(ctx: Context) : SessionDataStorage {

    private val prefsName = "prefs"
    private val prefs: SharedPreferences

    private enum class PrefsKey {
        API_ACCESS_TOKEN,
        API_REFRESH_TOKEN,
        LOGIN,
        API_USER_ID,
        HINTS,
        EXPIRED,
        FIREBASE_TOKEN,
        PHONE,
        TASKS,
        ALERT
    }

    override fun isUserLogged(): Boolean = prefs.getBoolean(PrefsKey.API_USER_ID.name, false)

    override fun getAccessToken(): String? {
        val key = PrefsKey.API_ACCESS_TOKEN.name
        if (!prefs.contains(key)) return null
        return prefs.getString(key, "") ?: ""
    }

    override fun setAccessToken(token: String) {
        prefs.edit().putString(PrefsKey.API_ACCESS_TOKEN.name, token).apply()
        return
    }

    override fun tokenExpired(expired: Boolean) {
        prefs.edit().putBoolean(PrefsKey.EXPIRED.name, expired).apply()
    }

    override fun isExpired(): Boolean? {
        val key = PrefsKey.EXPIRED.name
        if (!prefs.contains(key)) return null
        return prefs.getBoolean(key, true) ?: true
    }

    override fun getRefreshToken(): String {
        val key = PrefsKey.API_REFRESH_TOKEN.name
        if (!prefs.contains(key)) return ""
        return prefs.getString(key, "") ?: ""
    }

    override fun setRefreshToken(token: String) {
        prefs.edit().putString(PrefsKey.API_REFRESH_TOKEN.name, token).apply()
    }

    override fun setFirebaseToken(token: String) {
        prefs.edit().putString(PrefsKey.FIREBASE_TOKEN.name, token).apply()
    }

    override fun getFirebaseToken(): String? {
        val key = PrefsKey.FIREBASE_TOKEN.name
        if (!prefs.contains(key)) return ""
        return prefs.getString(key, "") ?: ""
    }

    override fun cleanSession() {
        val editor = prefs.edit()
        PrefsKey.values().forEach {
            editor.remove(it.name)
        }
        editor.apply()
    }

    override fun setLogin(login: String) {
        prefs.edit().putString(PrefsKey.LOGIN.name, login).apply()
    }

    override fun getLogin(): String? = prefs.getString(PrefsKey.LOGIN.name, "")

    override fun setPhoneNumber(number: String) {
        prefs.edit().putString(PrefsKey.PHONE.name, number).apply()
    }

    override fun getPhoneNumber(): String? {
        return prefs.getString(PrefsKey.PHONE.name, "") ?: ""
    }

    override fun setHints(hints: Boolean) {
        prefs.edit().putBoolean(PrefsKey.HINTS.name, hints).apply()
    }

    override fun getHints(): Boolean? = prefs.getBoolean(PrefsKey.HINTS.name, true)

    override fun getContactsRequestShown(): Boolean = prefs.getBoolean(PrefsKey.ALERT.name, false)

    override fun setContactsRequestShown(shown: Boolean) {
        prefs.edit().putBoolean(PrefsKey.ALERT.name, shown).apply()
    }


    init {
        prefs = ctx.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }

}