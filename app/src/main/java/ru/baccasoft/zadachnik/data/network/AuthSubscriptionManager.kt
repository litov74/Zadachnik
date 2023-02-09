package ru.baccasoft.zadachnik.data.network

object AuthSubscriptionManager {
    private var subscription = HashSet<AuthSubscriptionCallback>()

    fun addSubscription(subscriber: AuthSubscriptionCallback) {
        subscription.add(subscriber)
    }

    fun removeSubscription(subscriber: AuthSubscriptionCallback) {
        subscription.remove(subscriber)
    }

    fun notifySubscribersRefreshTokenExpired() {
        subscription.forEach {
            it.expiredRefreshToken()
        }
    }

}

interface AuthSubscriptionCallback {
    fun expiredRefreshToken()
}