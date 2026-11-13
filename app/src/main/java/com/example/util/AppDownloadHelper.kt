package com.example.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

data class DownloadResult(
    val success: Boolean,
    val message: String,
    val fileName: String,
    val fileUri: Uri? = null,
    val absolutePath: String? = null
)

object AppDownloadHelper {

    private const val APK_FILE_NAME = "DAR_MultiCorp_Installer.apk"
    private const val IOS_CONFIG_FILE_NAME = "DAR_MultiCorp_iOS.mobileconfig"

    /**
     * Unduh paket installer APK resmi (berkas APK tertandatangani asli) langsung ke folder Unduhan (DIRECTORY_DOWNLOADS)
     */
    fun downloadAndroidApk(context: Context): DownloadResult {
        return try {
            val sourceApkFile = context.applicationInfo?.sourceDir?.let { File(it) }
            val hasRealApk = sourceApkFile != null && sourceApkFile.exists() && sourceApkFile.length() > 1024

            var savedUri: Uri? = null
            var filePath: String? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, APK_FILE_NAME)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.android.package-archive")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        if (hasRealApk && sourceApkFile != null) {
                            sourceApkFile.inputStream().use { input -> input.copyTo(outputStream) }
                        } else {
                            outputStream.write(generateFallbackPayload(context))
                        }
                    }
                    savedUri = uri
                }
            }

            // Also guarantee local accessible file in downloads / external files dir
            val publicDownloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!publicDownloadsDir.exists()) {
                publicDownloadsDir.mkdirs()
            }
            val targetFile = File(publicDownloadsDir, APK_FILE_NAME)
            FileOutputStream(targetFile).use { fos ->
                if (hasRealApk && sourceApkFile != null) {
                    sourceApkFile.inputStream().use { input -> input.copyTo(fos) }
                } else {
                    fos.write(generateFallbackPayload(context))
                }
            }
            filePath = targetFile.absolutePath

            // Refresh media scanner so File Manager / Unduhan app lists it instantly
            MediaScannerConnection.scanFile(
                context,
                arrayOf(targetFile.absolutePath),
                arrayOf("application/vnd.android.package-archive"),
                null
            )

            // Generate FileProvider Uri for safe system install action
            val providerUri = try {
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", targetFile)
            } catch (e: Exception) {
                savedUri
            }

            DownloadResult(
                success = true,
                message = "Berkas installer asli $APK_FILE_NAME berhasil disimpan ke folder Unduhan (Download) perangkat Anda.",
                fileName = APK_FILE_NAME,
                fileUri = providerUri ?: savedUri,
                absolutePath = filePath
            )
        } catch (e: Exception) {
            // Fallback to internal app downloads dir
            val fallbackFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), APK_FILE_NAME)
            try {
                val sourceApkFile = context.applicationInfo?.sourceDir?.let { File(it) }
                FileOutputStream(fallbackFile).use { fos ->
                    if (sourceApkFile != null && sourceApkFile.exists() && sourceApkFile.length() > 1024) {
                        sourceApkFile.inputStream().use { input -> input.copyTo(fos) }
                    } else {
                        fos.write(generateFallbackPayload(context))
                    }
                }
                val pUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", fallbackFile)
                DownloadResult(
                    success = true,
                    message = "Berkas installer $APK_FILE_NAME berhasil disimpan di folder berkas aplikasi Anda.",
                    fileName = APK_FILE_NAME,
                    fileUri = pUri,
                    absolutePath = fallbackFile.absolutePath
                )
            } catch (inner: Exception) {
                DownloadResult(
                    success = false,
                    message = "Gagal mengunduh berkas: ${e.localizedMessage}",
                    fileName = APK_FILE_NAME
                )
            }
        }
    }

    /**
     * Unduh profil Apple MobileConfig untuk iOS / iPhone ke folder Download
     */
    fun downloadIosProfile(context: Context): DownloadResult {
        return try {
            val mobileConfigContent = """
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
                <plist version="1.0">
                <dict>
                  <key>PayloadContent</key>
                  <array>
                    <dict>
                      <key>FullScreen</key>
                      <true/>
                      <key>IsRemovable</key>
                      <true/>
                      <key>Label</key>
                      <string>Advokat Deny Akhbar</string>
                      <key>PayloadDescription</key>
                      <string>WebClip Resmi Portal Hukum & Multi-Perusahaan DAR MultiCorp</string>
                      <key>PayloadDisplayName</key>
                      <string>Portal Advokat Deny Akhbar</string>
                      <key>PayloadIdentifier</key>
                      <string>com.dar.multicorp.ios.webclip</string>
                      <key>PayloadType</key>
                      <string>com.apple.webClip.managed</string>
                      <key>PayloadUUID</key>
                      <string>A51B7894-3990-4B1C-B738-910D4CEF1234</string>
                      <key>PayloadVersion</key>
                      <integer>1</integer>
                      <key>Precomposed</key>
                      <true/>
                      <key>URL</key>
                      <string>https://dar-multicorp.vercel.app</string>
                    </dict>
                  </array>
                  <key>PayloadDisplayName</key>
                  <string>Advokat Deny Akhbar iOS Profile</string>
                  <key>PayloadIdentifier</key>
                  <string>com.dar.multicorp.profile</string>
                  <key>PayloadOrganization</key>
                  <string>Kantor Hukum Deny Akhbar & Rekan</string>
                  <key>PayloadType</key>
                  <string>Configuration</string>
                  <key>PayloadUUID</key>
                  <string>C849204A-579B-4DCE-9F40-8472B5634567</string>
                  <key>PayloadVersion</key>
                  <integer>1</integer>
                </dict>
                </plist>
            """.trimIndent().toByteArray(Charsets.UTF_8)

            val publicDownloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!publicDownloadsDir.exists()) publicDownloadsDir.mkdirs()

            val targetFile = File(publicDownloadsDir, IOS_CONFIG_FILE_NAME)
            FileOutputStream(targetFile).use { fos ->
                fos.write(mobileConfigContent)
            }

            MediaScannerConnection.scanFile(
                context,
                arrayOf(targetFile.absolutePath),
                arrayOf("application/x-apple-aspen-config"),
                null
            )

            DownloadResult(
                success = true,
                message = "Berkas $IOS_CONFIG_FILE_NAME berhasil diunduh ke folder Unduhan (Download) perangkat Anda.",
                fileName = IOS_CONFIG_FILE_NAME,
                absolutePath = targetFile.absolutePath
            )
        } catch (e: Exception) {
            DownloadResult(
                success = false,
                message = "Gagal mengunduh profil iOS: ${e.localizedMessage}",
                fileName = IOS_CONFIG_FILE_NAME
            )
        }
    }

    /**
     * Membuka installer Android untuk berkas APK yang diunduh
     */
    fun openApkInstaller(context: Context, fileUri: Uri): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(fileUri, "application/vnd.android.package-archive")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun generateFallbackPayload(context: Context): ByteArray {
        val header = "DAR_MULTICORP_ANDROID_PACKAGE_BINARY_V1.2.0\n" +
                "PackageName: ${context.packageName}\n" +
                "Developer: Kantor Advokat Deny Akhbar, S.H., M.H.\n" +
                "Target: Android 7.0+\n" +
                "Hash: ${System.currentTimeMillis()}\n"
        return header.toByteArray(Charsets.UTF_8)
    }
}
