package com.stiproject.kelassti.data.local.datastore

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

object EncryptedStringSerializer: Serializer<String> {
    override val defaultValue = ""
    override suspend fun readFrom(input: InputStream): String {
        val encryptedBytes = input.readBytes()
        if (encryptedBytes.isEmpty()) return ""
        return String(Crypto.decrypt(encryptedBytes))
    }

    override suspend fun writeTo(t: String, output: OutputStream) {
        val bytes = t.toByteArray()
        val encryptedBytes = Crypto.encrypt(bytes)
        output.write(encryptedBytes)
    }
}