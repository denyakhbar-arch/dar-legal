package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class EnterpriseRepository {

    // Downloadable Theme Presets
    private val _themePresets = MutableStateFlow<List<AppThemePreset>>(
        listOf(
            AppThemePreset("theme_blue", "Biru Muda Eksekutif (Default)", "Warna biru muda cerah profesional khas DAR MultiCorp.", 0xFFF0F7FF, 0xFFE0F2FE, 0xFF0284C7, 0xFFBAE6FD, isDownloaded = true, isApplied = true),
            AppThemePreset("theme_emerald", "Zamrud Hijau Lestari", "Palet hijau segar untuk industri agribisnis ramah lingkungan.", 0xFFF0FDF4, 0xFFDCFCE7, 0xFF059669, 0xFF86EFAC, isDownloaded = true, isApplied = false),
            AppThemePreset("theme_gold", "Fajar Emas Elegan", "Aksen emas prestisius untuk dewan direksi dan eksekutif puncak.", 0xFFFFFDF0, 0xFFFEF3C7, 0xFFD97706, 0xFFFDE68A, isDownloaded = false, isApplied = false),
            AppThemePreset("theme_indigo", "Indigo Royal Corporate", "Nuansa indigo mendalam untuk keandalan dan teknologi tinggi.", 0xFFF5F3FF, 0xFFEDE9FE, 0xFF4F46E5, 0xFFC7D2FE, isDownloaded = false, isApplied = false),
            AppThemePreset("theme_teal", "Teal Modern Industri", "Gradasi toska modern untuk logistik dan manufaktur cerdas.", 0xFFF0FDFA, 0xFFCCFBF1, 0xFF0D9488, 0xFF99F6E4, isDownloaded = false, isApplied = false)
        )
    )
    val themePresets: StateFlow<List<AppThemePreset>> = _themePresets.asStateFlow()

    private val _activeTheme = MutableStateFlow<AppThemePreset>(_themePresets.value[0])
    val activeTheme: StateFlow<AppThemePreset> = _activeTheme.asStateFlow()

    // Pre-seeded Multi-Companies with Google Forms and Dashboard Customizer Config
    private val _companies = MutableStateFlow<List<Company>>(
        listOf(
            Company(
                id = "comp_1",
                name = "DAR Agro Makmur",
                code = "DAR-AGRO",
                sector = "Agribisnis, Komoditas Sawit & Kopi Unggulan",
                npwp = "01.234.567.8-012.000",
                nib = "9120003456789",
                websiteUrl = "https://agro.darmulticorp.co.id",
                email = "corporate@agro.darmulticorp.co.id",
                phone = "+62 21 5566 7788",
                primaryAccentColorHex = 0xFF0284C7,
                branches = listOf(
                    Branch("br_jkt", "Cabang Jakarta Pusat (Kantor Pusat)", "DKI Jakarta", "Gedung Cyber 2 Lantai 18, Jl. HR Rasuna Said", -6.2241, 106.8315),
                    Branch("br_sby", "Cabang Surabaya (Hub Timur)", "Surabaya", "Jl. Pemuda No. 45, Genteng", -7.2654, 112.7483),
                    Branch("br_bpn", "Cabang Balikpapan (Hub Kalimantan)", "Balikpapan", "Jl. Jenderal Sudirman No. 120", -1.2653, 116.8312)
                ),
                featuresConfig = CompanyFeaturesConfig(),
                dashboardConfig = DashboardCustomizerConfig(
                    logoUri = null,
                    logoPosition = LogoPosition.LEFT,
                    isBroadcastingActive = true,
                    broadcastingMessage = "Pengumuman Resmi: Apel siaga keselamatan kerja K3 dan evaluasi pengapalan 40 kontainer kopi ekspor dimulai pukul 08:30 WIB.",
                    broadcastingSender = "Direksi & Admin Utama",
                    broadcastingUrgency = "PENTING",
                    showBroadcastingWidget = true,
                    showStatsWidget = true,
                    showGpsWidget = true,
                    showCalendarWidget = true,
                    showGoogleWorkspaceHub = true,
                    showCustomMenusWidget = true
                ),
                googleForms = listOf(
                    GoogleFormItem("gf_sup_1", "Formulir Pra-Kualifikasi Calon Supplier Baru", FormAudience.SUPPLIER, "https://forms.google.com/d/e/supplier-pangan-kualifikasi/viewform", "Formulir resmi Google Form untuk seleksi kelayakan kebun, legalitas SHM/HGU, dan kapasitas pasokan bulanan."),
                    GoogleFormItem("gf_sup_2", "Formulir Uji Sampel & Uji Mutu Laboratorium Komoditas", FormAudience.SUPPLIER, "https://forms.google.com/d/e/uji-lab-komoditas/viewform", "Pengajuan pengiriman sampel biji kopi/sawit untuk uji kadar air dan uji pestisida di QA Lab."),
                    GoogleFormItem("gf_buy_1", "Formulir Registrasi Kemitraan Calon Buyer Ekspor", FormAudience.BUYER, "https://forms.google.com/d/e/buyer-ekspor-kuota/viewform", "Pengisian profil badan usaha buyer asing/lokal, tonase kebutuhan rutin, dan metode pembayaran (L/C, TT)."),
                    GoogleFormItem("gf_buy_2", "Formulir Negosiasi Kontrak Berjangka & Volume Pasokan", FormAudience.BUYER, "https://forms.google.com/d/e/kontrak-berjangka/viewform", "Formulir penawaran volume kontrak 12-24 bulan dengan penetapan formula harga indeks bursa.")
                ),
                leadershipStructure = listOf(
                    LeadershipOfficer("off_1", "Komisaris Utama", "Ir. H. Budi Santoso", "Dewan Komisaris", "komisaris@agro.darmulticorp.co.id"),
                    LeadershipOfficer("off_2", "Direktur Utama", "Hendra Wijaya, S.E.", "Direksi Eksekutif", "hendra@agro.darmulticorp.co.id"),
                    LeadershipOfficer("off_3", "VP Operations & Supply Chain", "Kartika Sari, M.M.", "Operasional", "kartika@agro.darmulticorp.co.id"),
                    LeadershipOfficer("off_4", "Manajer SDM & Personalia", "Siti Rahmawati, S.Psi.", "Human Resources", "hrd@agro.darmulticorp.co.id"),
                    LeadershipOfficer("off_5", "Supervisor QA & Laboratorium", "Dimas Pratama, S.T.", "Quality Assurance", "dimas@agro.darmulticorp.co.id")
                ),
                customMenus = listOf(
                    CustomCompanyMenu(
                        id = "cmenu_1",
                        companyId = "comp_1",
                        menuTitle = "SOP Keselamatan Kerja & Pengoperasian Alat Berat",
                        subMenuTitle = "Panduan K3 Kebun & Pabrik Pengolahan",
                        categoryTag = "Dokumentasi & SOP",
                        iconName = "Safety",
                        createdAt = "12 Nov 2026",
                        sections = listOf(
                            CustomContentSection(
                                id = "sec_1",
                                sectionTitle = "Prosedur Baku Keselamatan & APD Lapangan",
                                textContent = "Seluruh staf operasional, operator forklift, dan regu muat wajib mengenakan Alat Pelindung Diri (APD) standar: Helm proyek (safety helmet), kacamata pelindung, sarung tangan heavy-duty, dan sepatu safety bersertifikat SNI sebelum memasuki area sortasi dan pergudangan.",
                                photoUrls = listOf("https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=600"),
                                photoCaptions = listOf("Pemeriksaan Kelayakan APD & Pengarahan Apel Pagi"),
                                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                                videoTitle = "Video Tutorial Prosedur K3 & Standar Pengoperasian Mesin Press Sawit"
                            )
                        )
                    )
                ),
                installedPlugins = listOf(
                    AppPlugin("plg_wa", "WhatsApp Enterprise Gateway", "Meta Cloud Verified", "v3.4.1", "Otomatis kirim broadcast slip gaji, rekap presensi, dan persetujuan lembur ke nomor WhatsApp karyawan.", "Notifikasi", true, true, "https://api.whatsapp.com/v1/messages", "WA_ENTERPRISE_KEY_88912"),
                    AppPlugin("plg_face", "Face Biometric AI Liveness Pro", "Biometrics Global", "v2.1.0", "Deteksi wajah 3D anti-spoofing mendalam untuk verifikasi kehadiran tanpa sentuh dan absensi bebas kecurangan.", "AI & Biometrik", true, true, "https://ai.biometrics-cloud.com/v2/liveness", "BIO_FACE_TOKEN_9901"),
                    AppPlugin("plg_erp", "Konektor Integrasi SAP & Oracle ERP", "Enterprise Bridge", "v4.0.2", "Sinkronisasi buku besar akuntansi, data biaya upah payroll, dan persediaan komoditas secara real-time.", "ERP & Akuntansi", true, true, "https://sap.darmulticorp.co.id/rfc/payroll", "SAP_HOST_AUTH_1289"),
                    AppPlugin("plg_materai", "E-Materai PERURI & PrivyID Resmi", "PERURI Digital", "v1.8.0", "Pemberian materai digital Rp 10.000 otomatis pada dokumen MoU dan kontrak kerja sama supplier.", "Legalitas", true, true, "https://ematerai.peruri.co.id/api", "PERURI_DIGITAL_KEY_77"),
                    AppPlugin("plg_iot", "IoT Fleet & Logistics GPS Tracker", "Navindo Telematics", "v5.2.0", "Pelacakan koordinat GPS armada truk pengangkut komoditas secara berkala dengan peringatan batas kecepatan.", "IoT Logistik", true, true, "https://fleet.navindo.co.id/telemetry", "FLEET_TRACK_IOT_441"),
                    AppPlugin("plg_webhook", "Custom Webhook & REST API Hub", "DAR Open Platform", "v1.0.0", "Menghubungkan aplikasi ke server internal perusahaan atau platform penyedia ekstensi pihak ketiga.", "Webhook API", true, true, "https://webhook.darmulticorp.co.id/events", "DAR_WEBHOOK_SECRET_007")
                )
            ),
            Company(
                id = "comp_2",
                name = "DAR Logistik Prima",
                code = "DAR-LOG",
                sector = "Supply Chain, Ekspedisi Kontainer & Pergudangan Berikat",
                npwp = "02.876.543.2-023.000",
                nib = "9120009876543",
                websiteUrl = "https://logistik.darmulticorp.co.id",
                email = "ops@logistik.darmulticorp.co.id",
                phone = "+62 21 8899 1122",
                primaryAccentColorHex = 0xFF0D9488,
                branches = listOf(
                    Branch("br_tgp", "Cabang Tanjung Priok", "Jakarta Utara", "Kawasan Pelabuhan Maritim Blok C3", -6.1072, 106.8833),
                    Branch("br_smg", "Cabang Tanjung Emas", "Semarang", "Jl. Coaster No. 8, Semarang Utara", -6.9538, 110.4262)
                ),
                featuresConfig = CompanyFeaturesConfig(),
                dashboardConfig = DashboardCustomizerConfig(
                    broadcastingMessage = "Pemberitahuan Dermaga: Jadwal sandar armada kontainer ekspor dimajukan ke pukul 13:00 WIB.",
                    broadcastingUrgency = "PENTING"
                ),
                googleForms = listOf(
                    GoogleFormItem("gf_log_sup", "Formulir Pendaftaran Armada Truk Rekanan", FormAudience.SUPPLIER, "https://forms.google.com/d/e/armada-truk-daftar/viewform", "Formulir data KIR, SIM B2 Umum, dan kapasitas armada kontainer."),
                    GoogleFormItem("gf_log_buy", "Formulir Booking Ruang Depo & Freight Kontainer", FormAudience.BUYER, "https://forms.google.com/d/e/booking-depo-kontainer/viewform", "Permintaan sewa ruang pergudangan berikat dan handling peti kemas.")
                ),
                leadershipStructure = listOf(
                    LeadershipOfficer("off_201", "Direktur Utama", "Bambang Sudiro, M.T.", "Direksi Eksekutif", "bambang@logistik.darmulticorp.co.id"),
                    LeadershipOfficer("off_202", "Direktur Operasional Pelabuhan", "Agus Prasetyo, S.T.", "Operasional", "agus@logistik.darmulticorp.co.id"),
                    LeadershipOfficer("off_203", "Manajer SDM & Kepatuhan", "Dewi Lestari, S.H.", "Human Resources", "dewi@logistik.darmulticorp.co.id")
                ),
                customMenus = emptyList(),
                installedPlugins = listOf(
                    AppPlugin("plg_wa", "WhatsApp Enterprise Gateway", "Meta Cloud", "v3.4.1", "Broadcast notifikasi muatan armada truk.", "Notifikasi", true, true)
                )
            ),
            Company(
                id = "comp_3",
                name = "DAR Mineral Sejahtera",
                code = "DAR-MINING",
                sector = "Pertambangan Berkelanjutan & Pemurnian Pasir Silika",
                npwp = "03.456.789.0-034.000",
                nib = "9120005678901",
                websiteUrl = "https://mineral.darmulticorp.co.id",
                email = "info@mineral.darmulticorp.co.id",
                phone = "+62 542 778899",
                primaryAccentColorHex = 0xFFD97706,
                branches = listOf(
                    Branch("br_smd", "Site Samarinda", "Samarinda", "Kawasan Konsesi Blok Kutai No. 9", -0.5021, 117.1536),
                    Branch("br_jkt_sts", "Kantor Korporat Jakarta", "Jakarta Selatan", "SCBD Lot 11, Sudirman", -6.2268, 106.8080)
                ),
                featuresConfig = CompanyFeaturesConfig(),
                googleForms = emptyList(),
                leadershipStructure = listOf(
                    LeadershipOfficer("off_301", "Direktur Utama", "Drs. Hendro Wibowo", "Direksi Eksekutif", "hendro@mineral.darmulticorp.co.id"),
                    LeadershipOfficer("off_302", "Kepala Teknik Tambang (KTT)", "Ir. Surya Kencana", "Operasional Site", "surya@mineral.darmulticorp.co.id")
                ),
                customMenus = emptyList(),
                installedPlugins = emptyList()
            )
        )
    )
    val companies: StateFlow<List<Company>> = _companies.asStateFlow()

    private val _currentCompany = MutableStateFlow<Company>(_companies.value[0])
    val currentCompany: StateFlow<Company> = _currentCompany.asStateFlow()

    // GOOGLE WORKSPACE DATA
    private val _googleCalendarEvents = MutableStateFlow<List<GoogleCalendarEvent>>(
        listOf(
            GoogleCalendarEvent("cal_1", "Audit ISO 9001 & ISPO Bersama Tim Sertifikasi", "Kamis, 15 Nov 2026", "09:00 - 12:00 WIB", "Ruang Rapat Utama & Google Meet", true, "https://meet.google.com/dar-agro-audit"),
            GoogleCalendarEvent("cal_2", "Rapat Evaluasi Pengapalan Kontainer Q4", "Jumat, 16 Nov 2026", "14:00 - 15:30 WIB", "Kantor Pusat Jakarta", true, "https://meet.google.com/dar-ekspor-sync"),
            GoogleCalendarEvent("cal_3", "Batas Akhir Penutupan Payroll & Presensi", "Selasa, 25 Nov 2026", "10:00 WIB", "Divisi SDM & Keuangan", false, "")
        )
    )
    val googleCalendarEvents: StateFlow<List<GoogleCalendarEvent>> = _googleCalendarEvents.asStateFlow()

    private val _googleDriveDocs = MutableStateFlow<List<GoogleDriveDocItem>>(
        listOf(
            GoogleDriveDocItem("gdoc_1", "Template Perjanjian Kerja Bersama (PKB) 2026", "Google Docs", "8 Halaman", "https://docs.google.com/document/d/dar-pkb-2026/edit"),
            GoogleDriveDocItem("gdoc_2", "MoU Standar Pasokan Komoditas Perkebunan", "Google Docs", "5 Halaman", "https://docs.google.com/document/d/dar-mou-supplier/edit"),
            GoogleDriveDocItem("gdoc_3", "Folder Drive: Arsip Legalitas, NIB & Kemenkumham", "Google Drive", "24 Berkas", "https://drive.google.com/drive/folders/dar-legalitas-folder"),
            GoogleDriveDocItem("gdoc_4", "Lembar Kerja Rekapitulasi Ekspor & Mutu Q4", "Google Sheets", "12 Sheets", "https://docs.google.com/spreadsheets/d/dar-rekap-ekspor/edit")
        )
    )
    val googleDriveDocs: StateFlow<List<GoogleDriveDocItem>> = _googleDriveDocs.asStateFlow()

    // Isolated Users List
    private val _allUsers = MutableStateFlow<List<User>>(
        listOf(
            User("usr_super_admin", "Deny Akhbar, S.H., M.H.", "denyakhbar@gmail.com", UserRole.SUPER_ADMIN, "comp_1", "br_jkt", "Admin Utama & Konsultan Hukum Utama", "+62 812-9999-0001", true, false, true, 45000000L),
            User("usr_second_admin", "Kartika Sari, M.M.", "kartika.sari@agro.darmulticorp.co.id", UserRole.SECOND_ADMIN, "comp_1", "br_jkt", "Admin Kedua / VP Operations", "+62 812-2345-6789", true, true, true, 22000000L),
            User("usr_direksi_ceo", "Hendra Wijaya, S.E.", "hendra.wijaya@agro.darmulticorp.co.id", UserRole.PEJABAT_TINGGI, "comp_1", "br_jkt", "Direktur Utama / CEO", "+62 811-1234-5678", true, false, true, 35000000L),
            User("usr_direksi_1", "Ir. H. Budi Santoso", "budi.santoso@agro.darmulticorp.co.id", UserRole.PEJABAT_TINGGI, "comp_1", "br_jkt", "Komisaris Utama / C-Level", "+62 813-3456-7890", true, false, true, 40000000L),
            User("usr_tetap_1", "Dimas Pratama", "dimas.pratama@agro.darmulticorp.co.id", UserRole.KARYAWAN_TETAP, "comp_1", "br_jkt", "Quality Assurance Supervisor", "+62 815-4567-8901", true, false, false, 8500000L),
            User("usr_tetap_sby", "Rina Anggraini", "rina.a@agro.darmulticorp.co.id", UserRole.KARYAWAN_TETAP, "comp_1", "br_sby", "Kepala Gudang Surabaya", "+62 817-5678-9012", true, false, false, 7800000L),
            User("usr_harian_1", "Joko Susilo", "joko.susilo@mitra-agro.id", UserRole.PEKERJA_HARIAN_BORONGAN, "comp_1", "br_bpn", "Mandor Borongan Sortir & Muat", "+62 819-6789-0123", true, false, false, 180000L),
            User("usr_supplier_1", "H. Ahmad Fauzi", "fauzi@pt-berkahtani.com", UserRole.SUPPLIER, "comp_1", "br_jkt", "Direktur PT Berkah Tani (Supplier)", "+62 821-7890-1234"),
            User("usr_buyer_1", "Michael Tan", "m.tan@singapore-commodities.sg", UserRole.BUYER, "comp_1", "br_jkt", "Head of Procurement (Global Buyer)", "+65 9123 4567"),
            User("usr_legal_1", "Adv. Farhan Pratama, S.H., M.H.", "farhan@nusantaralawpartners.id", UserRole.LEGAL_COUNSEL, "comp_1", "br_jkt", "Partner Advokat Litigasi", "+62 811-9988-7766"),

            User("usr_log_super", "Bambang Sudiro, M.T.", "bambang@logistik.darmulticorp.co.id", UserRole.SUPER_ADMIN, "comp_2", "br_tgp", "Direktur Utama / Pengelola Utama", "+62 811-2233-4455", true, false, true, 30000000L),
            User("usr_log_sec", "Agus Prasetyo, S.T.", "agus@logistik.darmulticorp.co.id", UserRole.SECOND_ADMIN, "comp_2", "br_tgp", "Admin Kedua / Dir. Ops Pelabuhan", "+62 812-3344-5566", true, true, true, 20000000L),
            User("usr_log_hrd", "Dewi Lestari, S.H.", "dewi@logistik.darmulticorp.co.id", UserRole.PEJABAT_TINGGI, "comp_2", "br_tgp", "Manajer SDM & Legal", "+62 813-4455-6677", true, false, true, 16000000L),
            User("usr_log_staff", "Arif Wibowo", "arif@logistik.darmulticorp.co.id", UserRole.KARYAWAN_TETAP, "comp_2", "br_tgp", "Dispatcher Armada Trailer", "+62 815-5566-7788", true, false, false, 7200000L),
            User("usr_log_borong", "Supriadi", "supriadi@mitra-logistik.id", UserRole.PEKERJA_HARIAN_BORONGAN, "comp_2", "br_smg", "Petugas Muat Crane Borongan", "+62 817-6677-8899", true, false, false, 200000L),

            User("usr_min_super", "Drs. Hendro Wibowo", "hendro@mineral.darmulticorp.co.id", UserRole.SUPER_ADMIN, "comp_3", "br_jkt_sts", "Direktur Utama / Pengelola Utama", "+62 811-6677-8899", true, false, true, 45000000L),
            User("usr_min_sec", "Ir. Surya Kencana", "surya@mineral.darmulticorp.co.id", UserRole.SECOND_ADMIN, "comp_3", "br_smd", "Admin Kedua / KTT Tambang", "+62 812-7788-9900", true, true, true, 28000000L),
            User("usr_min_staff", "Faisal Akbar", "faisal@mineral.darmulticorp.co.id", UserRole.KARYAWAN_TETAP, "comp_3", "br_smd", "Surveyor Geologi Lapangan", "+62 813-8899-0011", true, false, false, 9500000L),
            User("usr_min_borong", "Teguh Santoso", "teguh@mitra-tambang.id", UserRole.PEKERJA_HARIAN_BORONGAN, "comp_3", "br_smd", "Operator Borongan Excavator", "+62 815-9900-1122", true, false, false, 250000L)
        )
    )

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _currentUser = MutableStateFlow<User>(_allUsers.value[0])
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _allAttendanceList = MutableStateFlow<List<AttendanceRecord>>(
        listOf(
            AttendanceRecord("att_1", "usr_tetap_1", "Dimas Pratama", "comp_1", "br_jkt", "12 Nov 2026", "07:54:12 WIB", "17:08:44 WIB", -6.2241, 106.8315, "Gedung Cyber 2 Lantai 18, Jl. HR Rasuna Said", -6.2241, 106.8315, "Gedung Cyber 2 Lantai 18, Jl. HR Rasuna Said", status = "Hadir Tepat Waktu"),
            AttendanceRecord("att_2", "usr_harian_1", "Joko Susilo", "comp_1", "br_bpn", "12 Nov 2026", "07:45:00 WIB", "16:30:20 WIB", -1.2653, 116.8312, "Gudang Sortir Komoditas Balikpapan", -1.2653, 116.8312, "Gudang Sortir Komoditas Balikpapan", status = "Hadir (Shift Borongan Selesai)"),
            AttendanceRecord("att_201", "usr_log_staff", "Arif Wibowo", "comp_2", "br_tgp", "12 Nov 2026", "07:50:00 WIB", "17:00:10 WIB", -6.1072, 106.8833, "Pos Dermaga 3 Tanjung Priok", -6.1072, 106.8833, "Pos Dermaga 3 Tanjung Priok", status = "Hadir Tepat Waktu")
        )
    )
    private val _attendanceList = MutableStateFlow<List<AttendanceRecord>>(emptyList())
    val attendanceList: StateFlow<List<AttendanceRecord>> = _attendanceList.asStateFlow()

    private val _liveGps = MutableStateFlow(
        LiveLocationTracking("usr_tetap_1", "Dimas Pratama", "Cabang Jakarta Pusat", -6.2242, 106.8316, "Gedung Cyber 2, Jl. HR Rasuna Said Jakarta", "10:15 WIB", true, "Didalam Radius Geofencing Kantor (15 meter dari titik pusat)")
    )
    val liveGps: StateFlow<LiveLocationTracking> = _liveGps.asStateFlow()

    private val _allDailyReports = MutableStateFlow<List<DailyReport>>(
        listOf(
            DailyReport("rep_1", "usr_tetap_1", "Dimas Pratama", "comp_1", "br_jkt", "12 Nov 2026", "Inspeksi Kualitas Muatan Biji Kopi Robusta 25 Ton", "1. Kalibrasi kadar air biji kopi (target <12% tercapai 11.4%).\n2. Pemeriksaan sertifikasi karantina ekspor.\n3. Pengecekan segel kontainer nomor MSKU-908123.", "Tidak ada kendala, cuaca cerah dan proses muat selesai tepat waktu.", "Lanjut pemeriksaan sampel 15 ton minyak kelapa sawit mentah (CPO) besok pagi.", "Berita_Acara_Inspeksi_QA_12Nov.pdf", 320, "Disetujui Admin"),
            DailyReport("rep_2", "usr_harian_1", "Joko Susilo", "comp_1", "br_bpn", "12 Nov 2026", "Laporan Borongan Penimbangan & Pemuatan Sawit Tandan", "Menyelesaikan muat 120 sak komoditas ke armada truk logistik bersama regu 4 orang.", "Antrian timbangan jembatan sempat padat 20 menit.", "Penyusunan palet gudang blok B.", "Formulir_Borongan_Timbang_12Nov.pdf", 180, "Disetujui Admin")
        )
    )
    private val _dailyReports = MutableStateFlow<List<DailyReport>>(emptyList())
    val dailyReports: StateFlow<List<DailyReport>> = _dailyReports.asStateFlow()

    private val _allDocuments = MutableStateFlow<List<BusinessDocument>>(
        listOf(
            BusinessDocument("doc_1", "comp_1", "Akta Pendirian & Kepmenkumham 2020", "Legalitas", "10 Jan 2020", "PDF", 1450),
            BusinessDocument("doc_2", "comp_1", "Laporan Keuangan Kuartal III Audited", "Keuangan", "15 Okt 2026", "XLSX/PDF", 2890),
            BusinessDocument("doc_3", "comp_1", "Sertifikasi Indonesian Sustainable Palm Oil (ISPO)", "Sertifikasi", "04 Feb 2026", "PDF", 980)
        )
    )
    private val _documents = MutableStateFlow<List<BusinessDocument>>(emptyList())
    val documents: StateFlow<List<BusinessDocument>> = _documents.asStateFlow()

    private val _allPayrollSlips = MutableStateFlow<List<PayrollSlip>>(
        listOf(
            PayrollSlip("pay_001", "usr_tetap_1", "Dimas Pratama", "comp_1", "br_jkt", "November 2026", UserRole.KARYAWAN_TETAP, 22, 0, 8500000L, 750000L, 1100000L, 250000L, 10100000L, PaymentPlatform.MIDTRANS, "MID-PAY-202611-99812", true, "10 Nov 2026 09:30 WIB"),
            PayrollSlip("pay_002", "usr_harian_1", "Joko Susilo", "comp_1", "br_bpn", "November 2026 (Minggu 1-2)", UserRole.PEKERJA_HARIAN_BORONGAN, 14, 180, 180000L, 350000L, 280000L, 0L, 3150000L, PaymentPlatform.XENDIT, "XEN-DISB-77491-BORONGAN", true, "11 Nov 2026 14:15 WIB")
        )
    )
    private val _payrollSlips = MutableStateFlow<List<PayrollSlip>>(emptyList())
    val payrollSlips: StateFlow<List<PayrollSlip>> = _payrollSlips.asStateFlow()

    private val _allChatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage("msg_1", ForumChannelType.PEJABAT_TINGGI, null, "usr_super_admin", "Deny Akhbar, S.H., M.H. (Admin Utama)", UserRole.SUPER_ADMIN, "Selamat pagi Dewan Direksi. Agenda rapat strategis hari ini fokus pada akuisisi lahan ekspansi di Kaltim.", null, "08:15 WIB"),
            ChatMessage("msg_2", ForumChannelType.SEMUA_KARYAWAN, null, "usr_second_admin", "Kartika Sari (Admin Kedua)", UserRole.SECOND_ADMIN, "Pemberitahuan: Batas pengisian Laporan Kerja Harian dan Presensi GPS adalah pukul 17:30 WIB.", null, "09:00 WIB")
        )
    )
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _allVideoMeetings = MutableStateFlow<List<VideoConferenceRoom>>(
        listOf(
            VideoConferenceRoom("meet_01", "comp_1", "Rapat Koordinasi Evaluasi Ekspor Q4", "Hari Ini, 14:00 - 15:30 WIB", "1. Progres pengapalan 500 Ton komoditas\n2. Persetujuan payroll pekerja borongan", "Deny Akhbar, S.H., M.H. (Admin Utama)", listOf("usr_super_admin", "usr_second_admin", "usr_direksi_1", "usr_tetap_1"), true)
        )
    )
    private val _videoMeetings = MutableStateFlow<List<VideoConferenceRoom>>(emptyList())
    val videoMeetings: StateFlow<List<VideoConferenceRoom>> = _videoMeetings.asStateFlow()

    private val _allCompanyInbox = MutableStateFlow<List<CompanyInboxMail>>(
        listOf(
            // Jalur 1: Email Masuk Khusus Konsultan Hukum Perusahaan
            CompanyInboxMail(
                id = "mail_legal_1",
                companyId = "comp_1",
                senderName = "Koperasi Petani Mitra Mandiri",
                senderOrganization = "Biro Kemitraan & Legalitas Agraria",
                senderEmail = "legal.koperasi@petanimandiri.org",
                subject = "[JALUR KONSULTAN HUKUM] Permohonan Penelaahan Draf Klausul Arbitrase MoU Biji Kopi",
                messageContent = "Yth. Bpk. Deny Akhbar (Konsultan Hukum Perusahaan). Mohon analisis legalitas terhadap draf klausul force majeure gagal panen dan yurisdiksi BANI sebelum para pihak menandatangani akta notaris resmi.",
                receivedDate = "12 Nov 2026",
                allowedUserIds = listOf("usr_super_admin", "usr_legal_counsel", "usr_second_admin"),
                isRead = false,
                isImportant = true,
                mailChannel = "LEGAL_CONSULTANT",
                recipientEmail = "denyakhbar@gmail.com"
            ),
            CompanyInboxMail(
                id = "mail_legal_2",
                companyId = "comp_1",
                senderName = "Pengadilan Hubungan Industrial & Tenaga Kerja",
                senderOrganization = "Kepaniteraan Hukum Perdata",
                senderEmail = "phi.jakarta@mahkamahagung.go.id",
                subject = "[JALUR KONSULTAN HUKUM] Pengesahan Peraturan Perusahaan (PP) & Standar PKWT 2026",
                messageContent = "Kepada Advokat / Konsultan Hukum Perusahaan: Konfirmasi kelengkapan berkas pendaftaran Peraturan Perusahaan DAR MultiCorp dinyatakan sah dan terdaftar resmi.",
                receivedDate = "11 Nov 2026",
                allowedUserIds = listOf("usr_super_admin", "usr_legal_counsel"),
                isRead = true,
                isImportant = true,
                mailChannel = "LEGAL_CONSULTANT",
                recipientEmail = "denyakhbar@gmail.com"
            ),
            // Jalur 2: Email Masuk Operasional untuk Pemilik & Sekretaris Perusahaan
            CompanyInboxMail(
                id = "mail_corp_1",
                companyId = "comp_1",
                senderName = "Kementerian Perdagangan RI",
                senderOrganization = "Direktorat Pengembangan Ekspor Nasional",
                senderEmail = "kerjasama@kemendag.go.id",
                subject = "[JALUR PEMILIK & SEKRETARIS] Undangan Paviliun Ekspor Komoditas & Kuota Kontainer",
                messageContent = "Yth. Pemilik Perusahaan & Sekretariat Direksi: Undangan partisipasi paviliun ekspor agribisnis Indonesia 2026 dan verifikasi tonase pengapalan internasional.",
                receivedDate = "11 Nov 2026",
                allowedUserIds = listOf("usr_super_admin", "usr_second_admin", "usr_direksi_1"),
                isRead = false,
                isImportant = true,
                mailChannel = "CORPORATE",
                recipientEmail = "pemilik@darmulticorp.co.id"
            ),
            CompanyInboxMail(
                id = "mail_corp_2",
                companyId = "comp_1",
                senderName = "Kantor Pelayanan Pajak (KPP) Madya",
                senderOrganization = "Direktorat Jenderal Pajak RI",
                senderEmail = "kpp.madya@pajak.go.id",
                subject = "[JALUR PEMILIK & SEKRETARIS] Surat Keterangan Fiskal (SKF) & e-Billing Payroll",
                messageContent = "Diterima Sekretariat Direksi: Konfirmasi penerbitan SKF tahunan dan rekapitulasi bukti potong PPh 21 karyawan tetap & harian borongan.",
                receivedDate = "10 Nov 2026",
                allowedUserIds = listOf("usr_super_admin", "usr_second_admin", "usr_direksi_1"),
                isRead = true,
                isImportant = false,
                mailChannel = "CORPORATE",
                recipientEmail = "sekretaris@darmulticorp.co.id"
            )
        )
    )
    private val _companyInbox = MutableStateFlow<List<CompanyInboxMail>>(emptyList())
    val companyInbox: StateFlow<List<CompanyInboxMail>> = _companyInbox.asStateFlow()

    private val _partnerSubmissions = MutableStateFlow<List<PartnerSubmission>>(
        listOf(
            PartnerSubmission("part_1", PartnerFormType.PENAWARAN_HARGA, UserRole.SUPPLIER, "H. Ahmad Fauzi", "PT Berkah Tani Nusantara", "3276011203850002", "82.918.234.1-412.000", "fauzi@pt-berkahtani.com", "+62 821-7890-1234", "Jl. Perkebunan Malang", "Biji Kopi Robusta Super", 72000L, 50.0, "Franco Gudang Surabaya, Tempo 14 hari.", true, true, true, "SIG_PTS_120", "11 Nov 2026", "Disetujui Direksi")
        )
    )
    val partnerSubmissions: StateFlow<List<PartnerSubmission>> = _partnerSubmissions.asStateFlow()

    private val _allProducts = MutableStateFlow<List<ProductCommodity>>(
        listOf(
            ProductCommodity("prod_1", "comp_1", "Biji Kopi Robusta Premium Dampit", "Agro Komoditas", 75000L, 100, 45000L, "Moisture <12%, Defect <5%, Screen size 16-18, Aroma karamel cokelat.", "11.2%", "Grade 1 Export Quality", "Karung Goni Food Grade 60 Kg", "Gunung Semeru, Jawa Timur"),
            ProductCommodity("prod_2", "comp_1", "Crude Palm Oil (CPO) Standar Ekspor", "Agro Minyak Nabati", 14500L, 1000, 850000L, "FFA <5%, Kadar Air <0.2%, DOBI Index >2.3, Sertifikasi ISPO.", "0.15%", "Super Prime CPO", "Flexibag / Tangki Isotank", "Perkebunan Mitra Kalimantan Timur")
        )
    )
    private val _products = MutableStateFlow<List<ProductCommodity>>(emptyList())
    val products: StateFlow<List<ProductCommodity>> = _products.asStateFlow()

    private val _allPosts = MutableStateFlow<List<CompanyPostAd>>(
        listOf(
            CompanyPostAd("post_1", "comp_1", "Pengapalan Ekspor 40 Kontainer Robusta Menuju Pelabuhan Rotterdam", "DAR MultiCorp Agro membuktikan produktivitas dan kepatuhan standar internasional dengan melepas ekspor 40 kontainer biji kopi pilihan.", "Produktivitas & Ekspor", "12 Nov 2026", "Corporate Communications", 128, 3400, true)
        )
    )
    private val _posts = MutableStateFlow<List<CompanyPostAd>>(emptyList())
    val posts: StateFlow<List<CompanyPostAd>> = _posts.asStateFlow()

    val advocateTeam = listOf(
        AdvocateMember("Deny Akhbar, S.H., M.H.", "Kepala Konsultan Hukum Perusahaan (In-House Counsel)", "PERADI/2010/8819", "Hukum Korporasi, Kontrak Bisnis, Arbitrase BANI & Advokasi (denyakhbar@gmail.com)", 16),
        AdvocateMember("Adv. Nindya Maharani, S.H., LL.M.", "Senior Partner - Commercial Dispute", "PERADI/2015/1442", "Arbitrase Dagang Internasional, Sengketa Kontrak, Tenaga Kerja", 11),
        AdvocateMember("Adv. Reza Firmansyah, S.H., M.Kn.", "Notaris Rekanan & Legal Auditor", "INI/2016/7712", "Legal Due Diligence, Agraria & Pertanahan (HGU/SHM), Pajak", 10)
    )

    val testimonials = listOf(
        LegalTestimonial("test_1", "Direksi PT Agro Mandiri Lestari", "PT Agro Mandiri Lestari", "Sengketa Perjanjian Pasokan Ekspor Rp 38 Miliar", "Penyelesaian damai melalui mediasi BANI tanpa ke pengadilan, menyelamatkan piutang klien 100%.", 5, "2026"),
        LegalTestimonial("test_2", "Manajemen PT Samudera Logistik", "PT Samudera Logistik Nusantara", "Restrukturisasi Tenaga Kerja & Hubungan Industrial", "Penyelarasan PKB untuk 450 karyawan sesuai regulasi omnibus law secara harmonis.", 5, "2025")
    )

    private val _consultationBookings = MutableStateFlow<List<ConsultationBooking>>(
        listOf(
            ConsultationBooking("cons_1", "Hendra Wijaya, S.E.", "DAR MultiCorp", "corporate@darmulticorp.co.id", "+62 811-1234-5678", "Kontrak Bisnis & MoU Kerja Sama Buyer Asing", "15 Nov 2026, 10:00 WIB", "Pemeriksaan klausul ICC Incoterms 2020 untuk ekspor komoditas.", "Terkonfirmasi Advokat")
        )
    )
    val consultationBookings: StateFlow<List<ConsultationBooking>> = _consultationBookings.asStateFlow()

    private val _legalAidRequests = MutableStateFlow<List<LegalAidRequest>>(
        listOf(
            LegalAidRequest("aid_1", "Sugeng Hariyadi (Perwakilan Kelompok Tani Mitra)", "+62 852-3344-5566", "Perlindungan Hak Kemitraan Tani", "Bantuan penelaahan hak kepemilikan bibit dan perlindungan terhadap spekulan.", "Prioritas", "Sedang Ditelaah Advokat")
        )
    )
    val legalAidRequests: StateFlow<List<LegalAidRequest>> = _legalAidRequests.asStateFlow()

    private val _legalPartnerships = MutableStateFlow<List<LegalPartnershipApplication>>(
        listOf(
            LegalPartnershipApplication("lp_1", "Adv. Hendra Kusuma, S.H.", "Kantor Advokat Kusuma & Rekan Balikpapan", "Balikpapan, Kaltim", "Hukum Pertambangan & Hubungan Industrial", "Mengajukan diri sebagai mitra advokat regional di Kalimantan Timur.", "Diterima Sebagai Mitra Regional")
        )
    )
    val legalPartnerships: StateFlow<List<LegalPartnershipApplication>> = _legalPartnerships.asStateFlow()

    // Master Google Account Super Admin & Password Verification
    companion object {
        const val MASTER_ADMIN_EMAIL = "denyakhbar@gmail.com"
        const val DEFAULT_ADMIN_PASSWORD = "DAR@2026"
    }

    private var _superAdminPassword: String = DEFAULT_ADMIN_PASSWORD
    private val _isSuperAdminPasswordVerified = MutableStateFlow(false)
    val isSuperAdminPasswordVerified: StateFlow<Boolean> = _isSuperAdminPasswordVerified.asStateFlow()

    init {
        refreshCompanyWorkspace("comp_1")
    }

    private fun refreshCompanyWorkspace(companyId: String) {
        val comp = _companies.value.find { it.id == companyId } ?: _companies.value[0]
        _currentCompany.value = comp

        val scopedUsers = _allUsers.value.filter { it.companyId == comp.id }
        _users.value = scopedUsers

        val currentEmail = _currentUser.value.email
        val activeCompanySuperAdmin = if (currentEmail.equals(MASTER_ADMIN_EMAIL, ignoreCase = true) && _isSuperAdminPasswordVerified.value) {
            User(
                id = "usr_super_admin",
                name = "Deny Akhbar, S.H., M.H.",
                email = MASTER_ADMIN_EMAIL,
                role = UserRole.SUPER_ADMIN,
                companyId = comp.id,
                branchId = comp.branches.firstOrNull()?.id ?: "br_jkt",
                position = "Admin Utama & Konsultan Hukum Utama",
                phone = "+62 812-9999-0001",
                isActive = true,
                isAppointedSecondAdmin = false,
                canAccessCompanyInbox = true,
                dailyRateOrMonthlyWage = 45000000L
            )
        } else {
            scopedUsers.find { it.role == UserRole.SUPER_ADMIN } ?: scopedUsers.firstOrNull() ?: _allUsers.value[0]
        }
        _currentUser.value = activeCompanySuperAdmin

        _attendanceList.value = _allAttendanceList.value.filter { it.companyId == comp.id }
        _dailyReports.value = _allDailyReports.value.filter { it.companyId == comp.id }
        _documents.value = _allDocuments.value.filter { it.companyId == comp.id }
        _payrollSlips.value = _allPayrollSlips.value.filter { it.companyId == comp.id }
        _chatMessages.value = _allChatMessages.value.filter { it.senderId in scopedUsers.map { u -> u.id } }
        _videoMeetings.value = _allVideoMeetings.value.filter { it.companyId == comp.id }
        _companyInbox.value = _allCompanyInbox.value.filter { it.companyId == comp.id }
        _products.value = _allProducts.value.filter { it.companyId == comp.id }
        _posts.value = _allPosts.value.filter { it.companyId == comp.id }
    }

    fun authenticateSuperAdmin(email: String, password: String): Pair<Boolean, String> {
        val cleanEmail = email.trim()
        if (!cleanEmail.equals(MASTER_ADMIN_EMAIL, ignoreCase = true)) {
            return false to "Email '$cleanEmail' bukan akun Google Admin Utama terdaftar ($MASTER_ADMIN_EMAIL)."
        }
        if (password != _superAdminPassword) {
            return false to "Kata sandi Admin Utama tidak sesuai. Silakan periksa kembali."
        }
        _isSuperAdminPasswordVerified.value = true
        val comp = _currentCompany.value
        _currentUser.value = User(
            id = "usr_super_admin",
            name = "Deny Akhbar, S.H., M.H.",
            email = MASTER_ADMIN_EMAIL,
            role = UserRole.SUPER_ADMIN,
            companyId = comp.id,
            branchId = comp.branches.firstOrNull()?.id ?: "br_jkt",
            position = "Admin Utama & Konsultan Hukum Utama",
            phone = "+62 812-9999-0001",
            isActive = true,
            isAppointedSecondAdmin = false,
            canAccessCompanyInbox = true,
            dailyRateOrMonthlyWage = 45000000L
        )
        return true to "Akun Google $MASTER_ADMIN_EMAIL resmi terverifikasi sebagai Admin Utama (${comp.name})."
    }

    fun updateSuperAdminPassword(currentPassword: String, newPassword: String): Pair<Boolean, String> {
        if (currentPassword != _superAdminPassword) {
            return false to "Kata sandi lama tidak cocok."
        }
        if (newPassword.length < 6) {
            return false to "Kata sandi baru minimal harus terdiri dari 6 karakter."
        }
        _superAdminPassword = newPassword
        return true to "Kata sandi Admin Utama untuk $MASTER_ADMIN_EMAIL berhasil diperbarui."
    }

    fun switchCompany(companyId: String) {
        refreshCompanyWorkspace(companyId)
    }

    fun switchCurrentUser(userId: String) {
        val found = _allUsers.value.find { it.id == userId } ?: return
        _currentUser.value = found
    }

    // DASHBOARD CUSTOMIZER METHODS
    fun updateDashboardConfig(config: DashboardCustomizerConfig): Pair<Boolean, String> {
        val compId = _currentCompany.value.id
        val updated = _companies.value.map {
            if (it.id == compId) it.copy(dashboardConfig = config) else it
        }
        _companies.value = updated
        _currentCompany.value = updated.find { it.id == compId } ?: _currentCompany.value
        return true to "Tata letak dashboard dan widget beranda berhasil disimpan."
    }

    fun updateCompanyLogo(uri: String?, position: LogoPosition): Pair<Boolean, String> {
        val compId = _currentCompany.value.id
        val updated = _companies.value.map {
            if (it.id == compId) {
                val newConfig = it.dashboardConfig.copy(logoUri = uri, logoPosition = position)
                it.copy(dashboardConfig = newConfig)
            } else it
        }
        _companies.value = updated
        _currentCompany.value = updated.find { it.id == compId } ?: _currentCompany.value
        return true to "Logo perusahaan dan posisi berhasil diperbarui."
    }

    fun updateBroadcastingAnnouncement(message: String, urgency: String, isActive: Boolean): Pair<Boolean, String> {
        val compId = _currentCompany.value.id
        val updated = _companies.value.map {
            if (it.id == compId) {
                val newConfig = it.dashboardConfig.copy(
                    broadcastingMessage = message,
                    broadcastingUrgency = urgency,
                    isBroadcastingActive = isActive
                )
                it.copy(dashboardConfig = newConfig)
            } else it
        }
        _companies.value = updated
        _currentCompany.value = updated.find { it.id == compId } ?: _currentCompany.value
        return true to "Pesan siaran (broadcasting) berhasil disiarkan ke seluruh tim."
    }

    // THEMES DOWNLOAD & APPLY
    fun downloadTheme(themeId: String): Pair<Boolean, String> {
        val updated = _themePresets.value.map {
            if (it.id == themeId) it.copy(isDownloaded = true) else it
        }
        _themePresets.value = updated
        val themeName = updated.find { it.id == themeId }?.name ?: "Tema"
        return true to "Tema '$themeName' berhasil diunduh dan siap diterapkan."
    }

    fun applyTheme(themeId: String): Pair<Boolean, String> {
        val updated = _themePresets.value.map {
            it.copy(isApplied = (it.id == themeId))
        }
        _themePresets.value = updated
        val target = updated.find { it.id == themeId } ?: return false to "Tema tidak ditemukan."
        _activeTheme.value = target
        return true to "Tema '${target.name}' berhasil diterapkan ke seluruh aplikasi."
    }

    // GOOGLE FORMS CRUD
    fun addGoogleForm(title: String, audience: FormAudience, url: String, desc: String): Pair<Boolean, String> {
        val compId = _currentCompany.value.id
        val newForm = GoogleFormItem(
            id = "gf_" + System.currentTimeMillis(),
            title = title,
            audience = audience,
            formUrl = url,
            description = desc
        )
        val updated = _companies.value.map {
            if (it.id == compId) it.copy(googleForms = it.googleForms + newForm) else it
        }
        _companies.value = updated
        _currentCompany.value = updated.find { it.id == compId } ?: _currentCompany.value
        return true to "Google Formulir untuk ${audience.label} berhasil ditambahkan."
    }

    fun removeGoogleForm(formId: String): Pair<Boolean, String> {
        val compId = _currentCompany.value.id
        val updated = _companies.value.map {
            if (it.id == compId) it.copy(googleForms = it.googleForms.filterNot { f -> f.id == formId }) else it
        }
        _companies.value = updated
        _currentCompany.value = updated.find { it.id == compId } ?: _currentCompany.value
        return true to "Google Formulir berhasil dihapus."
    }

    // GOOGLE WORKSPACE LAUNCHERS & LOGS
    fun addGoogleCalendarEvent(title: String, date: String, time: String, loc: String, meetUrl: String): Pair<Boolean, String> {
        val newEvt = GoogleCalendarEvent(
            id = "cal_" + System.currentTimeMillis(),
            title = title,
            date = date,
            timeRange = time,
            location = loc,
            isMeetLinked = meetUrl.isNotBlank(),
            meetUrl = meetUrl.ifBlank { "https://meet.google.com/dar-new-meeting" }
        )
        _googleCalendarEvents.value = listOf(newEvt) + _googleCalendarEvents.value
        return true to "Jadwal rapat berhasil disinkronisasi ke Google Kalender."
    }

    fun addGoogleDriveDoc(title: String, type: String, sizeOrPages: String, url: String): Pair<Boolean, String> {
        val newDoc = GoogleDriveDocItem(
            id = "gdoc_" + System.currentTimeMillis(),
            title = title,
            docType = type,
            sizeOrPages = sizeOrPages,
            driveUrl = url
        )
        _googleDriveDocs.value = listOf(newDoc) + _googleDriveDocs.value
        return true to "Dokumen $title berhasil disinkronisasi ke Google Drive."
    }

    // SETTINGS METHODS
    fun updateFeatureConfig(newConfig: CompanyFeaturesConfig): Pair<Boolean, String> {
        val compId = _currentCompany.value.id
        val updatedCompanies = _companies.value.map { comp ->
            if (comp.id == compId) comp.copy(featuresConfig = newConfig) else comp
        }
        _companies.value = updatedCompanies
        _currentCompany.value = updatedCompanies.find { it.id == compId } ?: _currentCompany.value
        return true to "Konfigurasi modul fitur aplikasi untuk ${_currentCompany.value.name} berhasil disimpan."
    }

    fun updateCompanyDesign(name: String, sector: String, website: String, phone: String, accentHex: Long): Pair<Boolean, String> {
        val compId = _currentCompany.value.id
        val updatedCompanies = _companies.value.map { comp ->
            if (comp.id == compId) {
                comp.copy(name = name, sector = sector, websiteUrl = website, phone = phone, primaryAccentColorHex = accentHex)
            } else comp
        }
        _companies.value = updatedCompanies
        _currentCompany.value = updatedCompanies.find { it.id == compId } ?: _currentCompany.value
        return true to "Informasi profil dan desain tampilan perusahaan berhasil diperbarui."
    }

    fun addLeadershipOfficer(title: String, name: String, dept: String, email: String): Pair<Boolean, String> {
        val compId = _currentCompany.value.id
        val newOfficer = LeadershipOfficer(id = "off_" + System.currentTimeMillis(), title = title, officerName = name, department = dept, contactEmail = email)
        val updatedCompanies = _companies.value.map { comp ->
            if (comp.id == compId) comp.copy(leadershipStructure = comp.leadershipStructure + newOfficer) else comp
        }
        _companies.value = updatedCompanies
        _currentCompany.value = updatedCompanies.find { it.id == compId } ?: _currentCompany.value
        return true to "Pejabat struktural $name ($title) berhasil ditambahkan."
    }

    fun removeLeadershipOfficer(officerId: String): Pair<Boolean, String> {
        val compId = _currentCompany.value.id
        val updatedCompanies = _companies.value.map { comp ->
            if (comp.id == compId) comp.copy(leadershipStructure = comp.leadershipStructure.filterNot { it.id == officerId }) else comp
        }
        _companies.value = updatedCompanies
        _currentCompany.value = updatedCompanies.find { it.id == compId } ?: _currentCompany.value
        return true to "Pejabat struktural berhasil dihapus dari bagan perusahaan."
    }

    fun togglePlugin(pluginId: String, isEnabled: Boolean): Pair<Boolean, String> {
        val compId = _currentCompany.value.id
        val updatedCompanies = _companies.value.map { comp ->
            if (comp.id == compId) {
                val updatedPlugins = comp.installedPlugins.map { plg ->
                    if (plg.id == pluginId) plg.copy(isEnabled = isEnabled) else plg
                }
                comp.copy(installedPlugins = updatedPlugins)
            } else comp
        }
        _companies.value = updatedCompanies
        _currentCompany.value = updatedCompanies.find { it.id == compId } ?: _currentCompany.value
        val plgName = _currentCompany.value.installedPlugins.find { it.id == pluginId }?.name ?: "Plugin"
        return true to "$plgName ${if (isEnabled) "berhasil diaktifkan" else "dinonaktifkan"}."
    }

    fun installNewPlugin(name: String, dev: String, ver: String, desc: String, category: String, endpoint: String): Pair<Boolean, String> {
        val compId = _currentCompany.value.id
        val newPlg = AppPlugin(id = "plg_" + System.currentTimeMillis(), name = name, developer = dev, version = ver, description = desc, category = category, isInstalled = true, isEnabled = true, configEndpoint = endpoint)
        val updatedCompanies = _companies.value.map { comp ->
            if (comp.id == compId) comp.copy(installedPlugins = comp.installedPlugins + newPlg) else comp
        }
        _companies.value = updatedCompanies
        _currentCompany.value = updatedCompanies.find { it.id == compId } ?: _currentCompany.value
        return true to "Plugin $name berhasil dipasang ke ekosistem DAR MultiCorp."
    }

    fun savePluginConfig(pluginId: String, endpoint: String, apiKey: String): Pair<Boolean, String> {
        val compId = _currentCompany.value.id
        val updatedCompanies = _companies.value.map { comp ->
            if (comp.id == compId) {
                val updatedPlugins = comp.installedPlugins.map { plg ->
                    if (plg.id == pluginId) plg.copy(configEndpoint = endpoint, apiKey = apiKey) else plg
                }
                comp.copy(installedPlugins = updatedPlugins)
            } else comp
        }
        _companies.value = updatedCompanies
        _currentCompany.value = updatedCompanies.find { it.id == compId } ?: _currentCompany.value
        return true to "Konfigurasi API & Endpoint Plugin berhasil disimpan."
    }

    fun addCustomCompanyMenu(
        menuTitle: String,
        subMenuTitle: String,
        categoryTag: String,
        bodyText: String,
        photoUrl: String?,
        photoCaption: String?,
        videoUrl: String?,
        videoTitle: String?
    ): Pair<Boolean, String> {
        val compId = _currentCompany.value.id
        val section = CustomContentSection(
            id = "sec_" + System.currentTimeMillis(),
            sectionTitle = subMenuTitle,
            textContent = bodyText,
            photoUrls = if (!photoUrl.isNullOrBlank()) listOf(photoUrl) else emptyList(),
            photoCaptions = if (!photoCaption.isNullOrBlank()) listOf(photoCaption) else emptyList(),
            videoUrl = if (!videoUrl.isNullOrBlank()) videoUrl else null,
            videoTitle = videoTitle
        )

        val newMenu = CustomCompanyMenu(
            id = "cmenu_" + System.currentTimeMillis(),
            companyId = compId,
            menuTitle = menuTitle,
            subMenuTitle = subMenuTitle,
            categoryTag = categoryTag,
            createdAt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
            sections = listOf(section)
        )

        val updatedCompanies = _companies.value.map { comp ->
            if (comp.id == compId) comp.copy(customMenus = comp.customMenus + newMenu) else comp
        }
        _companies.value = updatedCompanies
        _currentCompany.value = updatedCompanies.find { it.id == compId } ?: _currentCompany.value
        return true to "Menu kustom '$menuTitle' berhasil dibuat."
    }

    fun deleteCustomCompanyMenu(menuId: String): Pair<Boolean, String> {
        val compId = _currentCompany.value.id
        val updatedCompanies = _companies.value.map { comp ->
            if (comp.id == compId) comp.copy(customMenus = comp.customMenus.filterNot { it.id == menuId }) else comp
        }
        _companies.value = updatedCompanies
        _currentCompany.value = updatedCompanies.find { it.id == compId } ?: _currentCompany.value
        return true to "Menu kustom berhasil dihapus."
    }

    fun appointSecondAdmin(targetUserId: String): Pair<Boolean, String> {
        val activeUser = _currentUser.value
        if (activeUser.role != UserRole.SUPER_ADMIN) {
            return false to "Hanya Admin Utama yang berhak menunjuk Admin Pendamping."
        }

        val currentAppointed = _allUsers.value.filter {
            it.companyId == _currentCompany.value.id &&
            (it.role == UserRole.SECOND_ADMIN || it.isAppointedSecondAdmin) &&
            it.id != targetUserId
        }

        val maxAllowed = _currentCompany.value.maxAppointedAdmins // Default: 2
        if (currentAppointed.size >= maxAllowed) {
            return false to "Maksimal $maxAllowed orang Admin pendamping telah ditunjuk. Cabut salah satu terlebih dahulu jika ingin mengganti."
        }

        val updatedAll = _allUsers.value.map { user ->
            if (user.companyId == _currentCompany.value.id && user.id == targetUserId) {
                user.copy(role = UserRole.SECOND_ADMIN, isAppointedSecondAdmin = true, canAccessCompanyInbox = true)
            } else user
        }
        _allUsers.value = updatedAll
        refreshCompanyWorkspace(_currentCompany.value.id)
        val totalNow = updatedAll.count { it.companyId == _currentCompany.value.id && (it.role == UserRole.SECOND_ADMIN || it.isAppointedSecondAdmin) }
        return true to "Admin Pendamping (Slot $totalNow/$maxAllowed) resmi berhasil ditunjuk untuk ${_currentCompany.value.name} dengan kuasa penuh kelola akun & surat."
    }

    fun revokeSecondAdmin(targetUserId: String): Pair<Boolean, String> {
        val activeUser = _currentUser.value
        if (activeUser.role != UserRole.SUPER_ADMIN) {
            return false to "Hanya Admin Utama yang berhak mencabut wewenang Admin Pendamping."
        }
        val updatedAll = _allUsers.value.map { user ->
            if (user.id == targetUserId && user.companyId == _currentCompany.value.id) {
                user.copy(role = UserRole.PEJABAT_TINGGI, isAppointedSecondAdmin = false)
            } else user
        }
        _allUsers.value = updatedAll
        refreshCompanyWorkspace(_currentCompany.value.id)
        return true to "Wewenang Admin Pendamping berhasil dicabut."
    }

    fun updateCompanyEmailRouting(legalEmail: String, ownerEmail: String, secretaryEmail: String): Pair<Boolean, String> {
        val activeUser = _currentUser.value
        if (activeUser.role != UserRole.SUPER_ADMIN && activeUser.role != UserRole.SECOND_ADMIN) {
            return false to "Hanya Admin Utama yang berhak mengubah konfigurasi jalur email perusahaan."
        }
        val compId = _currentCompany.value.id
        val updated = _companies.value.map {
            if (it.id == compId) {
                it.copy(
                    legalConsultantEmail = legalEmail.trim(),
                    ownerEmail = ownerEmail.trim(),
                    secretaryEmail = secretaryEmail.trim()
                )
            } else it
        }
        _companies.value = updated
        _currentCompany.value = updated.find { it.id == compId } ?: _currentCompany.value
        return true to "Dua Jalur Email (Jalur 1: Konsultan Hukum & Jalur 2: Pemilik/Sekretaris) berhasil diperbarui."
    }

    fun updateCompanyMasterData(
        name: String,
        code: String,
        sector: String,
        npwp: String,
        nib: String,
        website: String,
        phone: String,
        legalEmail: String,
        ownerEmail: String,
        secretaryEmail: String,
        workStart: String,
        workEnd: String,
        accentHex: Long
    ): Pair<Boolean, String> {
        val activeUser = _currentUser.value
        if (activeUser.role != UserRole.SUPER_ADMIN) {
            return false to "Hanya Admin Utama yang berhak mengubah seluruh master data perusahaan."
        }
        val compId = _currentCompany.value.id
        val updated = _companies.value.map {
            if (it.id == compId) {
                it.copy(
                    name = name.trim(),
                    code = code.trim().uppercase(),
                    sector = sector.trim(),
                    npwp = npwp.trim(),
                    nib = nib.trim(),
                    websiteUrl = website.trim(),
                    phone = phone.trim(),
                    legalConsultantEmail = legalEmail.trim(),
                    ownerEmail = ownerEmail.trim(),
                    secretaryEmail = secretaryEmail.trim(),
                    workHoursStart = workStart.trim(),
                    workHoursEnd = workEnd.trim(),
                    primaryAccentColorHex = accentHex
                )
            } else it
        }
        _companies.value = updated
        _currentCompany.value = updated.find { it.id == compId } ?: _currentCompany.value
        return true to "Master data perusahaan '${name.trim()}' berhasil diperbarui secara penuh oleh Admin Utama."
    }

    fun inviteEmployee(name: String, email: String, role: UserRole, position: String, branchId: String, phone: String, wageRate: Long): Pair<Boolean, String> {
        val activeUser = _currentUser.value
        if (activeUser.role != UserRole.SUPER_ADMIN && activeUser.role != UserRole.SECOND_ADMIN) {
            return false to "Hanya Admin Tunggal atau Admin Kedua yang memiliki kuasa mengundang karyawan."
        }

        val newUser = User(
            id = "usr_" + UUID.randomUUID().toString().take(8),
            name = name,
            email = email,
            role = role,
            companyId = _currentCompany.value.id,
            branchId = branchId,
            position = position,
            phone = phone,
            isActive = true,
            dailyRateOrMonthlyWage = wageRate
        )
        _allUsers.value = _allUsers.value + newUser
        refreshCompanyWorkspace(_currentCompany.value.id)
        return true to "Akun karyawan $name berhasil diundang ke ${_currentCompany.value.name}."
    }

    fun removeEmployee(userId: String): Pair<Boolean, String> {
        val activeUser = _currentUser.value
        if (activeUser.role != UserRole.SUPER_ADMIN && activeUser.role != UserRole.SECOND_ADMIN) {
            return false to "Hanya Admin Tunggal atau Admin Kedua yang memiliki kuasa mengeluarkan karyawan."
        }
        val target = _allUsers.value.find { it.id == userId }
        if (target?.role == UserRole.SUPER_ADMIN) {
            return false to "Tidak dapat menghapus akun Admin Tunggal."
        }
        _allUsers.value = _allUsers.value.filterNot { it.id == userId }
        refreshCompanyWorkspace(_currentCompany.value.id)
        return true to "Akun karyawan berhasil dinonaktifkan."
    }

    fun toggleUserInboxAccess(userId: String, allow: Boolean): Pair<Boolean, String> {
        val activeUser = _currentUser.value
        if (activeUser.role != UserRole.SUPER_ADMIN && activeUser.role != UserRole.SECOND_ADMIN) {
            return false to "Hanya Admin yang dapat mengatur hak akses pesan masuk perusahaan."
        }
        _allUsers.value = _allUsers.value.map {
            if (it.id == userId && it.companyId == _currentCompany.value.id) it.copy(canAccessCompanyInbox = allow) else it
        }
        refreshCompanyWorkspace(_currentCompany.value.id)
        return true to "Hak akses pesan masuk berhasil diperbarui."
    }

    fun recordCheckIn(photoProof: String = "Selfie & Wajah Terverifikasi"): Pair<Boolean, String> {
        val user = _currentUser.value
        val now = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()) + " WIB"
        val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

        val record = AttendanceRecord(
            id = "att_" + System.currentTimeMillis(),
            userId = user.id,
            userName = user.name,
            companyId = user.companyId,
            branchId = user.branchId,
            date = today,
            checkInTime = now,
            checkInLat = -6.2241,
            checkInLng = 106.8315,
            checkInAddress = "Area Kerja Terdaftar (Geofence Valid)",
            photoProofNote = photoProof,
            status = "Hadir Masuk (GPS Valid)"
        )
        _allAttendanceList.value = listOf(record) + _allAttendanceList.value
        refreshCompanyWorkspace(_currentCompany.value.id)
        return true to "Absen Masuk Berhasil dicatat pada $now dengan verifikasi GPS & Wajah."
    }

    fun recordCheckOut(): Pair<Boolean, String> {
        val user = _currentUser.value
        val now = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()) + " WIB"
        val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

        val existing = _allAttendanceList.value.find { it.userId == user.id && it.date == today }
        if (existing == null) {
            return false to "Harap lakukan Absen Masuk terlebih dahulu sebelum Absen Pulang."
        }

        val updated = _allAttendanceList.value.map {
            if (it.id == existing.id) {
                it.copy(
                    checkOutTime = now,
                    checkOutLat = -6.2241,
                    checkOutLng = 106.8315,
                    checkOutAddress = "Area Kerja Terdaftar",
                    status = "Presensi Lengkap (Masuk & Pulang)"
                )
            } else it
        }
        _allAttendanceList.value = updated
        refreshCompanyWorkspace(_currentCompany.value.id)
        return true to "Absen Pulang Berhasil dicatat pada $now. Jam kerja selesai."
    }

    fun submitDailyReport(title: String, done: String, obstacles: String, plan: String, fileName: String?): Pair<Boolean, String> {
        val user = _currentUser.value
        val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

        val report = DailyReport(
            id = "rep_" + System.currentTimeMillis(),
            userId = user.id,
            userName = user.name,
            companyId = user.companyId,
            branchId = user.branchId,
            date = today,
            title = title,
            activitiesDone = done,
            obstacles = if (obstacles.isBlank()) "Tidak ada kendala" else obstacles,
            nextPlan = plan,
            documentAttachment = fileName,
            fileSizeKb = 310,
            status = "Menunggu Review Supervisor"
        )
        _allDailyReports.value = listOf(report) + _allDailyReports.value
        refreshCompanyWorkspace(_currentCompany.value.id)
        return true to "Laporan Kerja Harian berhasil disimpan."
    }

    fun clearStorageCache(): String {
        _allDocuments.value = _allDocuments.value.map { it.copy(isLocallyCached = false) }
        _allDailyReports.value = _allDailyReports.value.map { it.copy(isDownloadedLocally = false) }
        refreshCompanyWorkspace(_currentCompany.value.id)
        return "Penyimpanan lokal berhasil dibersihkan! 14.8 MB ruang penyimpanan perangkat telah dibebaskan. Dokumen tersimpan aman di cloud DAR MultiCorp & Google Drive."
    }

    fun disbursePayroll(slipId: String, platform: PaymentPlatform): Pair<Boolean, String> {
        val now = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date()) + " WIB"
        val refCode = platform.name.take(4) + "-" + System.currentTimeMillis().toString().takeLast(6)

        val updated = _allPayrollSlips.value.map {
            if (it.id == slipId) {
                it.copy(
                    isPaid = true,
                    paymentPlatform = platform,
                    transactionRef = refCode,
                    paidAtTimestamp = now
                )
            } else it
        }
        _allPayrollSlips.value = updated
        refreshCompanyWorkspace(_currentCompany.value.id)
        return true to "Gaji berhasil disalurkan via ${platform.label} (Ref: $refCode)."
    }

    fun sendChatMessage(channel: ForumChannelType, branchId: String?, text: String, voiceDuration: Int? = null) {
        val user = _currentUser.value
        val now = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()) + " WIB"

        val msg = ChatMessage(
            id = "msg_" + System.currentTimeMillis(),
            channelType = channel,
            branchId = branchId,
            senderId = user.id,
            senderName = user.name,
            senderRole = user.role,
            text = text,
            voiceNoteDurationSec = voiceDuration,
            timestamp = now,
            isCurrentUser = true
        )
        _allChatMessages.value = _allChatMessages.value + msg
        refreshCompanyWorkspace(_currentCompany.value.id)
    }

    fun submitPartnerForm(
        formType: PartnerFormType,
        partnerRole: UserRole,
        fullName: String,
        companyName: String,
        nik: String,
        npwp: String,
        email: String,
        phone: String,
        address: String,
        commodityName: String,
        pricePerKg: Long,
        volumeTon: Double,
        terms: String,
        signaturePoints: String
    ): Pair<Boolean, String> {
        val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        val submission = PartnerSubmission(
            id = "part_" + System.currentTimeMillis(),
            formType = formType,
            partnerRole = partnerRole,
            fullName = fullName,
            companyName = companyName,
            nik = nik,
            npwp = npwp,
            email = email,
            phone = phone,
            address = address,
            commodityName = commodityName,
            proposedPricePerKg = pricePerKg,
            supplyVolumeTon = volumeTon,
            termsAndConditions = terms,
            ktpUploaded = true,
            faceVerified = true,
            hasDigitalSignature = true,
            signaturePathPoints = signaturePoints,
            submissionDate = today,
            status = "Terkirim - Siap Ditinjau Direksi"
        )
        _partnerSubmissions.value = listOf(submission) + _partnerSubmissions.value
        return true to "Formulir ${formType.label} berhasil dikirim dengan verifikasi KTP, Wajah & Tanda Tangan Digital!"
    }

    fun createPost(title: String, content: String, tag: String): Pair<Boolean, String> {
        val user = _currentUser.value
        val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        val post = CompanyPostAd(
            id = "post_" + System.currentTimeMillis(),
            companyId = _currentCompany.value.id,
            title = title,
            content = content,
            tag = tag,
            publishedDate = today,
            author = user.name,
            likesCount = 1,
            viewsCount = 10,
            isPromoted = true
        )
        _allPosts.value = listOf(post) + _allPosts.value
        refreshCompanyWorkspace(_currentCompany.value.id)
        return true to "Postingan Laman & Iklan Produktivitas berhasil diterbitkan ke publik."
    }

    fun bookLegalConsultation(clientName: String, organization: String, email: String, phone: String, topic: String, date: String, brief: String): Pair<Boolean, String> {
        val booking = ConsultationBooking(
            id = "cons_" + System.currentTimeMillis(),
            clientName = clientName,
            organization = organization,
            email = email,
            phone = phone,
            topic = topic,
            preferredDate = date,
            caseBrief = brief,
            status = "Diterima - Jadwal Konsultasi Terjadwal"
        )
        _consultationBookings.value = listOf(booking) + _consultationBookings.value
        return true to "Konsultasi hukum berhasil dijadwalkan."
    }

    fun submitLegalAidRequest(applicantName: String, contact: String, category: String, issueDescription: String): Pair<Boolean, String> {
        val aid = LegalAidRequest(
            id = "aid_" + System.currentTimeMillis(),
            applicantName = applicantName,
            contact = contact,
            category = category,
            issueDescription = issueDescription,
            urgencyLevel = "Prioritas",
            status = "Diterima - Dalam Penelaahan Pro Bono"
        )
        _legalAidRequests.value = listOf(aid) + _legalAidRequests.value
        return true to "Permohonan bantuan hukum telah diterima."
    }

    fun submitLegalPartnership(name: String, office: String, location: String, area: String, note: String): Pair<Boolean, String> {
        val part = LegalPartnershipApplication(
            id = "lp_" + System.currentTimeMillis(),
            applicantName = name,
            firmOrAdvocateOffice = office,
            location = location,
            expertiseArea = area,
            proposalNote = note,
            status = "Berkas Diterima Untuk Verifikasi"
        )
        _legalPartnerships.value = listOf(part) + _legalPartnerships.value
        return true to "Pengajuan kemitraan advokat rekanan berhasil dikirimkan."
    }
}
