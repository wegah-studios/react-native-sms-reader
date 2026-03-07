package com.smsreader

import com.facebook.react.bridge.*

class SmsModule(private val reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "SmsReader"
    }

    @ReactMethod
    fun fetchMessages(
        startDate: Double?,
        endDate: Double?,
        sender: String?,
        fetchCondition: String?,
        lastId: Double?,
        limit: Int?,
        promise: Promise
    ) {

        try {

            val reader = SmsReader(reactContext)

            val results = reader.fetchMessages(
                startDate?.toLong(),
                endDate?.toLong(),
                sender,
                fetchCondition,
                lastId?.toLong(),
                limit
            )

            promise.resolve(results)

        } catch (e: Exception) {
            promise.reject("SMS_FETCH_ERROR", e)
        }
    }
}