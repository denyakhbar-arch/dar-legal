package com.example.data.model

data class Branch(
    val id: String,
    val name: String,
    val city: String,
    val address: String,
    val geofenceLat: Double,
    val geofenceLng: Double,
    val radiusMeters: Double = 300.0
)

enum class LogoPosition(val label: String) {
    LEFT("Rata Kiri"),
    CENTER("Rata Tengah"),
    RIGHT("Rata Kanan"),
    BANNER_TOP("Banner Penuh Atas")
}

data class DashboardCustomizerConfig(
    val logoUri: String? = null,
    val logoPosition: LogoPosition = LogoPosition.LEFT,
    val isBroadcastingActive: Boolean = true,
    val broadcastingMessage: String = "Pengumuman Resmi Direksi: Seluruh tim operasional dan mitra rantai pasok harap memastikan berita acara harian dan presensi terunggah sebelum pukul 17:30 WIB.",
    val broadcastingSender: String = "Direksi & Admin Utama",
    val broadcastingUrgency: String = "PENTING",
    val showBroadcastingWidget: Boolean = true,
    val showStatsWidget: Boolean = true,
    val showGpsWidget: Boolean = true,
    val showCalendarWidget: Boolean = true,
    val showGoogleWorkspaceHub: Boolean = true,
    val showCustomMenusWidget: Boolean = true
)

enum class FormAudience(val label: String) {
    SUPPLIER("Calon Supplier"),
    BUYER("Calon Buyer"),
    GENERAL("Umum / Rekanan")
}

data class GoogleFormItem(
    val id: String,
    val title: String,
    val audience: FormAudience,
    val formUrl: String,
    val description: String,
    val responseCount: Int = 12,
    val lastUpdated: String = "12 Nov 2026"
)

data class GoogleCalendarEvent(
    val id: String,
    val title: String,
    val date: String,
    val timeRange: String,
    val location: String,
    val isMeetLinked: Boolean = true,
    val meetUrl: String = "https://meet.google.com/dar-corp-sync"
)

data class GoogleDriveDocItem(
    val id: String,
    val title: String,
    val docType: String, // Google Docs, Google Sheets, Google Drive Folder
    val sizeOrPages: String,
    val driveUrl: String,
    val lastSync: String = "Baru saja"
)

data class AppThemePreset(
    val id: String,
    val name: String,
    val description: String,
    val bgHex: Long,
    val surfaceHex: Long,
    val primaryHex: Long,
    val borderHex: Long,
    val isDownloaded: Boolean = true,
    val isApplied: Boolean = false
)

data class CompanyFeaturesConfig(
    val enableAttendanceGps: Boolean = true,
    val enableDailyReports: Boolean = true,
    val enablePayroll: Boolean = true,
    val enableCommunications: Boolean = true,
    val enableVideoMeetings: Boolean = true,
    val enableSupplierBuyerPortal: Boolean = true,
    val enableProductCatalog: Boolean = true,
    val enableCompanyNewsAds: Boolean = true,
    val enableLegalConsultant: Boolean = true,
    val enableWorkHoursOnlyGps: Boolean = true,
    val enableGoogleWorkspaceSuite: Boolean = true
)

data class LeadershipOfficer(
    val id: String,
    val title: String,
    val officerName: String,
    val department: String,
    val contactEmail: String,
    val isActive: Boolean = true
)

data class AppPlugin(
    val id: String,
    val name: String,
    val developer: String,
    val version: String,
    val description: String,
    val category: String,
    val isInstalled: Boolean,
    val isEnabled: Boolean,
    val configEndpoint: String = "",
    val apiKey: String = "",
    val iconKey: String = "extension"
)

data class CustomContentSection(
    val id: String,
    val sectionTitle: String,
    val textContent: String,
    val photoUrls: List<String> = emptyList(),
    val photoCaptions: List<String> = emptyList(),
    val videoUrl: String? = null,
    val videoTitle: String? = null
)

data class CustomCompanyMenu(
    val id: String,
    val companyId: String,
    val menuTitle: String,
    val subMenuTitle: String,
    val categoryTag: String,
    val iconName: String = "Article",
    val sections: List<CustomContentSection>,
    val createdAt: String
)

data class Company(
    val id: String,
    val name: String,
    val code: String,
    val sector: String,
    val npwp: String,
    val nib: String,
    val websiteUrl: String,
    val email: String,
    val phone: String,
    val branches: List<Branch>,
    val verifiedLegalStatus: Boolean = true,
    val primaryAccentColorHex: Long = 0xFF0284C7,
    val featuresConfig: CompanyFeaturesConfig = CompanyFeaturesConfig(),
    val leadershipStructure: List<LeadershipOfficer> = emptyList(),
    val customMenus: List<CustomCompanyMenu> = emptyList(),
    val installedPlugins: List<AppPlugin> = emptyList(),
    val dashboardConfig: DashboardCustomizerConfig = DashboardCustomizerConfig(),
    val googleForms: List<GoogleFormItem> = emptyList(),
    // Dua Jalur Email Perusahaan & Pengaturan Khusus
    val legalConsultantEmail: String = "denyakhbar@gmail.com", // Jalur 1: Email Konsultan Hukum Perusahaan
    val ownerEmail: String = "pemilik@darmulticorp.co.id",      // Jalur 2: Email Pemilik Perusahaan
    val secretaryEmail: String = "sekretaris@darmulticorp.co.id", // Jalur 2: Email Sekretaris Perusahaan
    val workHoursStart: String = "08:00",
    val workHoursEnd: String = "17:00",
    val maxAppointedAdmins: Int = 2
)
