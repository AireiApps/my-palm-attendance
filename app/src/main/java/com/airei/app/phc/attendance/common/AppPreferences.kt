package com.airei.app.phc.attendance.common

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.charset.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import androidx.core.content.edit
import com.airei.app.phc.attendance.api.ApiDetails

object SecureCipher {
    private const val KEY_ALIAS = "secure_prefs_key"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val AES_MODE = "AES/GCM/NoPadding"

    fun init() {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE
            )
            val spec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()
            keyGenerator.init(spec)
            keyGenerator.generateKey()
        }
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    /** 🔓 make public */
    fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(iv + encrypted, Base64.DEFAULT)
    }

    /** 🔓 make public */
    fun decrypt(encryptedBase64: String): String {
        val data = Base64.decode(encryptedBase64, Base64.DEFAULT)
        val iv = data.copyOfRange(0, 12) // GCM IV length = 12
        val encrypted = data.copyOfRange(12, data.size)
        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, iv))
        return String(cipher.doFinal(encrypted), Charsets.UTF_8)
    }
}

const val MILL_API = ApiDetails.MILL_API
const val PLANTATION_API = ApiDetails.PLANTATION_API

object AppPreferences {

    private const val PREF_NAME = "phc_attendance"
    private lateinit var preferences: SharedPreferences

    private val LOGIN_ID = Pair("login_id", "")
    private val API_TYPE = Pair("api_type", "")
    private val IS_DATA_DOWNLOADED = Pair("is_data_downloaded", false)


    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        SecureCipher.init() // Initialize encryption helpers
    }

    var loginId: String
        get() {
            val encrypted = preferences.getString(LOGIN_ID.first, null)
            return encrypted?.let { SecureCipher.decrypt(it) } ?: LOGIN_ID.second
        }
        set(value) {
            val encrypted = SecureCipher.encrypt(value)
            preferences.edit { putString(LOGIN_ID.first, encrypted) }
        }

    var apiType: String
        get() {
            val encrypted = preferences.getString(API_TYPE.first, null)
            return encrypted?.let { SecureCipher.decrypt(it) } ?: API_TYPE.second
        }
        set(value) {
            val encrypted = SecureCipher.encrypt(value)
            preferences.edit { putString(API_TYPE.first, encrypted) }
        }

    var isDataDownloaded: Boolean
        get() {
            return preferences.getBoolean(IS_DATA_DOWNLOADED.first, IS_DATA_DOWNLOADED.second)
        }
        set(value) {
            preferences.edit { putBoolean(IS_DATA_DOWNLOADED.first, value) }
        }
}


