package com.example.data.model

enum class UserRole(val label: String, val badgeColorHex: Long) {
    SUPER_ADMIN("Admin Tunggal (Super)", 0xFFD97706),
    SECOND_ADMIN("Admin Kedua (Ditunjuk)", 0xFF0284C7),
    PEJABAT_TINGGI("Pejabat Tinggi & Direksi", 0xFF7C3AED),
    KARYAWAN_TETAP("Karyawan Tetap", 0xFF059669),
    PEKERJA_HARIAN_BORONGAN("Pekerja Harian / Borongan", 0xFFEA580C),
    SUPPLIER("Supplier Rekanan", 0xFF2563EB),
    BUYER("Buyer / Mitra Pembeli", 0xFF0D9488),
    LEGAL_COUNSEL("Konsultan Hukum Resmi", 0xFFB91C1C)
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val companyId: String,
    val branchId: String,
    val position: String,
    val phone: String,
    val isActive: Boolean = true,
    val isAppointedSecondAdmin: Boolean = false,
    val canAccessCompanyInbox: Boolean = false,
    val dailyRateOrMonthlyWage: Long = 0L,
    val avatarInitials: String = name.take(2).uppercase()
)
