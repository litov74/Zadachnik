package ru.baccasoft.zadachnik.data

interface SessionDataStorage {

    fun isUserLogged(): Boolean

    fun getAccessToken(): String?
    fun setAccessToken(token: String)
    fun tokenExpired(expired: Boolean)
    fun isExpired(): Boolean?

    fun getRefreshToken(): String
    fun setRefreshToken(token: String)

    fun cleanSession()

    fun setLogin(login: String)
    fun getLogin(): String?

    fun setPhoneNumber(number: String)
    fun getPhoneNumber(): String?

    fun setFirebaseToken(token: String)
    fun getFirebaseToken(): String?

    fun getHints(): Boolean?
    fun setHints(hints: Boolean)

    fun setContactsRequestShown(shown: Boolean)
    fun getContactsRequestShown() : Boolean



}