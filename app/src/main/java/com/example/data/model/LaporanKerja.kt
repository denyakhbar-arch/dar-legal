package com.example.data.model

data class DailyReport(
    val id: String,
    val userId: String,
    val userName: String,
    val companyId: String,
    val branchId: String,
    val date: String,
    val title: String,
    val activitiesDone: String,
    val obstacles: String,
    val nextPlan: String,
    val documentAttachment: String? = null,
    val fileSizeKb: Int = 245,
    val status: String = "Disetujui Admin",
    val isDownloadedLocally: Boolean = true
)

data class BusinessDocument(
    val id: String,
    val companyId: String,
    val title: String,
    val category: String, // Legal, Operasional, Pajak, Laporan Keuangan, Ekspor
    val uploadDate: String,
    val fileFormat: String,
    val sizeKb: Int,
    val isLocallyCached: Boolean = true
)
