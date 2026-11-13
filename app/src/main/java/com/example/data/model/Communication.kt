package com.example.data.model

enum class ForumChannelType(val title: String, val description: String) {
    PEJABAT_TINGGI("Ruang Direksi & Pejabat Tinggi", "Akses Khusus C-Level & Direksi Utama"),
    SEMUA_KARYAWAN("Forum Umum Karyawan", "Seluruh Karyawan Tetap & Harian/Borongan"),
    CABANG("Forum Khusus Cabang", "Komunikasi Operasional per Cabang Perusahaan")
}

data class ChatMessage(
    val id: String,
    val channelType: ForumChannelType,
    val branchId: String? = null,
    val senderId: String,
    val senderName: String,
    val senderRole: UserRole,
    val text: String,
    val voiceNoteDurationSec: Int? = null, // If present, it's a voice message!
    val timestamp: String,
    val isCurrentUser: Boolean = false
)

data class VideoConferenceRoom(
    val id: String,
    val companyId: String,
    val title: String,
    val scheduledTime: String,
    val agenda: String,
    val hostName: String,
    val allowedUserIds: List<String>, // Selected strictly by admin
    val isOngoing: Boolean = true
)

data class CompanyInboxMail(
    val id: String,
    val companyId: String,
    val senderName: String,
    val senderOrganization: String,
    val senderEmail: String,
    val subject: String,
    val messageContent: String,
    val receivedDate: String,
    val allowedUserIds: List<String>, // Only admin appointed persons can view
    val isRead: Boolean = false,
    val isImportant: Boolean = true,
    val mailChannel: String = "CORPORATE", // "LEGAL_CONSULTANT" (Jalur 1) or "CORPORATE" (Jalur 2)
    val recipientEmail: String = "pemilik@darmulticorp.co.id"
)
