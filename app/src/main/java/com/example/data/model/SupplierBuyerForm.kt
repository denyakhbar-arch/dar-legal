package com.example.data.model

enum class PartnerFormType(val label: String, val shortDesc: String) {
    PENAWARAN_HARGA("Formulir Penawaran Harga", "Pengajuan harga barang/komoditas per kg"),
    KERJASAMA_JANGKA_PANJANG("Penawaran Kerja Sama Jangka Panjang", "Kemitraan pasokan & kontrak multi-tahun"),
    KESEPAKATAN_MOU("Formulir Kesepakatan (MoU)", "Perjanjian resmi kedua belah pihak bermaterai digital")
}

data class PartnerSubmission(
    val id: String,
    val formType: PartnerFormType,
    val partnerRole: UserRole, // SUPPLIER or BUYER
    val fullName: String,
    val companyName: String,
    val nik: String,
    val npwp: String,
    val email: String,
    val phone: String,
    val address: String,
    val commodityName: String,
    val proposedPricePerKg: Long,
    val supplyVolumeTon: Double,
    val termsAndConditions: String,
    val ktpUploaded: Boolean = true,
    val faceVerified: Boolean = true,
    val hasDigitalSignature: Boolean = true,
    val signaturePathPoints: String = "SIGNED_PATH",
    val submissionDate: String,
    val status: String = "Menunggu Review Direksi"
)
