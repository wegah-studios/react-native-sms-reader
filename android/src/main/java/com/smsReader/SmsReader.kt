package com.smsReader

import android.content.Context
import android.net.Uri
import com.facebook.react.bridge.*
import java.util.regex.Pattern

class SmsReader(private val context: Context) {

    fun fetchMessages(
        startDate: Long?,
        endDate: Long?,
        sender: String?,
        fetchCondition: String?,
        lastId: Long?,
        limit: Int?
    ): WritableArray {

        val uri = Uri.parse("content://sms/inbox")
        val resolver = context.contentResolver

        val selectionParts = mutableListOf<String>()
        val args = mutableListOf<String>()

        if (lastId != null) {
            selectionParts.add("_id > ?")
            args.add(lastId.toString())
        }

        if (startDate != null) {
            selectionParts.add("date >= ?")
            args.add(startDate.toString())
        }

        if (endDate != null) {
            selectionParts.add("date <= ?")
            args.add(endDate.toString())
        }

        if (!sender.isNullOrEmpty()) {
            selectionParts.add("address LIKE ? COLLATE NOCASE")
            args.add("%$sender%")
        }

        val selection =
            if (selectionParts.isEmpty()) null
            else selectionParts.joinToString(" AND ")

        val sort = if (limit != null)
            "date DESC LIMIT $limit"
        else
            "date DESC"

        val cursor = resolver.query(
            uri,
            arrayOf("_id", "address", "body", "date"),
            selection,
            args.toTypedArray(),
            sort
        )

        val results = Arguments.createArray()

        val regex =
            if (!fetchCondition.isNullOrEmpty())
                Pattern.compile(fetchCondition, Pattern.CASE_INSENSITIVE)
            else null

        cursor?.use {

            val idIdx = it.getColumnIndex("_id")
            val bodyIdx = it.getColumnIndex("body")
            val addrIdx = it.getColumnIndex("address")
            val dateIdx = it.getColumnIndex("date")

            while (it.moveToNext()) {

                val body = it.getString(bodyIdx)

                if (regex != null && !regex.matcher(body).find()) {
                    continue
                }

                val map = Arguments.createMap()

                map.putInt("id", it.getInt(idIdx))
                map.putString("body", body)
                map.putString("sender", it.getString(addrIdx))
                map.putDouble("date", it.getLong(dateIdx).toDouble())

                results.pushMap(map)
            }
        }

        return results
    }
}