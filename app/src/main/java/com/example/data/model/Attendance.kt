package com.example.data.model

data class AttendanceRecord(
    val id: String,
    val userId: String,
    val userName: String,
    val companyId: String,
    val branchId: String,
    val date: String,
    val checkInTime: String? = null,
    val checkOutTime: String? = null,
    val checkInLat: Double? = null,
    val checkInLng: Double? = null,
    val checkInAddress: String? = null,
    val checkOutLat: Double? = null,
    val checkOutLng: Double? = null,
    val checkOutAddress: String? = null,
    val isWithinWorkingHoursOnly: Boolean = true,
    val isGeofenceVerified: Boolean = true,
    val photoProofNote: String = "Foto Verifikasi Wajah & GPS Valid",
    val status: String = "Hadir Tepat Waktu"
)

data class LiveLocationTracking(
    val userId: String,
    val userName: String,
    val branchName: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val timestamp: String,
    val isWorkHoursActive: Boolean, // True only during 08:00 - 17:00
    val statusDescription: String
)
