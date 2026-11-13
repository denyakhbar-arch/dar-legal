package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentCompany: Company,
    currentUser: User,
    users: List<User>,
    themePresets: List<AppThemePreset> = emptyList(),
    activeTheme: AppThemePreset? = null,
    onDownloadTheme: (String) -> Pair<Boolean, String> = { true to "" },
    onApplyTheme: (String) -> Pair<Boolean, String> = { true to "" },
    onUpdateDashboardConfig: (DashboardCustomizerConfig) -> Pair<Boolean, String> = { true to "" },
    onUpdateCompanyLogo: (String?, LogoPosition) -> Pair<Boolean, String> = { _, _ -> true to "" },
    onUpdateBroadcastingAnnouncement: (String, String, Boolean) -> Pair<Boolean, String> = { _, _, _ -> true to "" },
    onUpdateEmailRouting: (legalEmail: String, ownerEmail: String, secretaryEmail: String) -> Pair<Boolean, String> = { _, _, _ -> true to "" },
    onUpdateMasterData: (name: String, code: String, sector: String, npwp: String, nib: String, website: String, phone: String, legalEmail: String, ownerEmail: String, secretaryEmail: String, workStart: String, workEnd: String, accentHex: Long) -> Pair<Boolean, String> = { _, _, _, _, _, _, _, _, _, _, _, _, _ -> true to "" },
    onUpdateFeatures: (CompanyFeaturesConfig) -> Pair<Boolean, String>,
    onUpdateCompanyDesign: (name: String, sector: String, website: String, phone: String, accentHex: Long) -> Pair<Boolean, String>,
    onAddOfficer: (title: String, name: String, dept: String, email: String) -> Pair<Boolean, String>,
    onRemoveOfficer: (String) -> Pair<Boolean, String>,
    onTogglePlugin: (pluginId: String, isEnabled: Boolean) -> Pair<Boolean, String>,
    onInstallPlugin: (name: String, dev: String, ver: String, desc: String, cat: String, endpoint: String) -> Pair<Boolean, String>,
    onSavePluginConfig: (pluginId: String, endpoint: String, apiKey: String) -> Pair<Boolean, String>,
    onCreateCustomMenu: (menuTitle: String, subMenuTitle: String, catTag: String, text: String, photoUrl: String?, caption: String?, videoUrl: String?, videoTitle: String?) -> Pair<Boolean, String>,
    onDeleteCustomMenu: (String) -> Pair<Boolean, String>,
    onOpenCustomMenu: (CustomCompanyMenu) -> Unit,
    onBack: () -> Unit
) {
    var selectedSubMenu by remember { mutableIntStateOf(0) }
    // 0: Pengaturan Fitur Aplikasi
    // 1: Pengaturan Perusahaan & Pejabat
    // 2: Plugin & Ekstensi Eksternal
    // 3: Desain Menu & Halaman Kustom
    // 4: Toko Tema Unduhan & Kustomisasi Beranda

    // Dialog state variables
    var showAddOfficerDialog by remember { mutableStateOf(false) }
    var selectedPluginForConfig by remember { mutableStateOf<AppPlugin?>(null) }
    var showCreateMenuDialog by remember { mutableStateOf(false) }

    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val res = onUpdateCompanyLogo(uri.toString(), currentCompany.dashboardConfig.logoPosition)
            snackbarMessage = "Logo perusahaan berhasil diperbarui dari perangkat: $uri"
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    val isAdmin = currentUser.role == UserRole.SUPER_ADMIN || currentUser.role == UserRole.SECOND_ADMIN

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Pusat Pengaturan DAR MultiCorp", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                        Text("Kelola Modul, Struktur Perusahaan, Plugin & Menu Kustom", fontSize = 11.sp, color = CorporateDarkBlue)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = DeepExecutiveBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightBlueBg
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LightBlueBg)
        ) {
            // Horizontal scrollable tabs for the 4 requested submenus
            ScrollableTabRow(
                selectedTabIndex = selectedSubMenu,
                edgePadding = 12.dp,
                containerColor = Color.White,
                contentColor = CorporateBlue
            ) {
                Tab(
                    selected = selectedSubMenu == 0,
                    onClick = { selectedSubMenu = 0 },
                    text = { Text("1. Fitur Aplikasi", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.ToggleOn, contentDescription = null, modifier = Modifier.size(16.dp)) }
                )
                Tab(
                    selected = selectedSubMenu == 1,
                    onClick = { selectedSubMenu = 1 },
                    text = { Text("2. Perusahaan & Pejabat", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.CorporateFare, contentDescription = null, modifier = Modifier.size(16.dp)) }
                )
                Tab(
                    selected = selectedSubMenu == 2,
                    onClick = { selectedSubMenu = 2 },
                    text = { Text("3. Plugin & Ekstensi", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Extension, contentDescription = null, modifier = Modifier.size(16.dp)) }
                )
                Tab(
                    selected = selectedSubMenu == 3,
                    onClick = { selectedSubMenu = 3 },
                    text = { Text("4. Buat Menu Kustom", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.PostAdd, contentDescription = null, modifier = Modifier.size(16.dp)) }
                )
                Tab(
                    selected = selectedSubMenu == 4,
                    onClick = { selectedSubMenu = 4 },
                    text = { Text("5. Tema & Beranda", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_theme_store")
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // SUBMENU 0: PENGATURAN APLIKASI (FITUR MANA YANG MAU DIGUNAKAN / TIDAK)
                if (selectedSubMenu == 0) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Tune, contentDescription = null, tint = CorporateBlue)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text("Modul Aktif: ${currentCompany.name}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepExecutiveBlue)
                                        Text("Pilih fitur yang ingin diaktifkan atau dinonaktifkan untuk entitas perusahaan ini.", fontSize = 11.sp, color = Slate600)
                                    }
                                }
                            }
                        }
                    }

                    val cfg = currentCompany.featuresConfig

                    item {
                        FeatureToggleItem(
                            title = "Presensi Kehadiran & Geofencing GPS",
                            desc = "Waktu masuk/pulang, foto biometrik selfie wajah, dan deteksi radius kerja.",
                            isEnabled = cfg.enableAttendanceGps,
                            onToggle = { onUpdateFeatures(cfg.copy(enableAttendanceGps = it)) }
                        )
                    }

                    item {
                        FeatureToggleItem(
                            title = "Pelacakan GPS Hanya Selama Jam Kerja",
                            desc = "Menonaktifkan pelacakan di luar jam kerja (sebelum 08:00 / setelah 17:00) untuk perlindungan privasi.",
                            isEnabled = cfg.enableWorkHoursOnlyGps,
                            onToggle = { onUpdateFeatures(cfg.copy(enableWorkHoursOnlyGps = it)) }
                        )
                    }

                    item {
                        FeatureToggleItem(
                            title = "Laporan Kerja Harian & Berkas Usaha",
                            desc = "Formulir tugas harian staf, persetujuan admin, dan fitur pembersih memori.",
                            isEnabled = cfg.enableDailyReports,
                            onToggle = { onUpdateFeatures(cfg.copy(enableDailyReports = it)) }
                        )
                    }

                    item {
                        FeatureToggleItem(
                            title = "Payroll Otomatis & Gateway Pembayaran",
                            desc = "Perhitungan upah karyawan tetap vs pekerja harian/borongan serta transfer Midtrans/Xendit/QRIS.",
                            isEnabled = cfg.enablePayroll,
                            onToggle = { onUpdateFeatures(cfg.copy(enablePayroll = it)) }
                        )
                    }

                    item {
                        FeatureToggleItem(
                            title = "Grup Komunikasi & Pesan Suara",
                            desc = "Pemisahan Grup Direksi (Pejabat Tinggi), Grup Umum, dan Forum Cabang.",
                            isEnabled = cfg.enableCommunications,
                            onToggle = { onUpdateFeatures(cfg.copy(enableCommunications = it)) }
                        )
                    }

                    item {
                        FeatureToggleItem(
                            title = "Rapat Video Jarak Jauh (Telekonferensi)",
                            desc = "Ruang pertemuan video dengan seleksi peserta khusus yang ditentukan Admin.",
                            isEnabled = cfg.enableVideoMeetings,
                            onToggle = { onUpdateFeatures(cfg.copy(enableVideoMeetings = it)) }
                        )
                    }

                    item {
                        FeatureToggleItem(
                            title = "Portal Supplier & Buyer (MoU & Tanda Tangan)",
                            desc = "Formulir penawaran harga, kontrak jangka panjang, KTP, dan pad tanda tangan digital.",
                            isEnabled = cfg.enableSupplierBuyerPortal,
                            onToggle = { onUpdateFeatures(cfg.copy(enableSupplierBuyerPortal = it)) }
                        )
                    }

                    item {
                        FeatureToggleItem(
                            title = "Katalog Komoditas & Spesifikasi Harga/Kg",
                            desc = "Katalog barang dengan detail mutu serta harga per kilogram transparan.",
                            isEnabled = cfg.enableProductCatalog,
                            onToggle = { onUpdateFeatures(cfg.copy(enableProductCatalog = it)) }
                        )
                    }

                    item {
                        FeatureToggleItem(
                            title = "Feed Publikasi & Iklan Produktivitas",
                            desc = "Laman berita keaktifan dan promosi kapasitas produksi kepada publik.",
                            isEnabled = cfg.enableCompanyNewsAds,
                            onToggle = { onUpdateFeatures(cfg.copy(enableCompanyNewsAds = it)) }
                        )
                    }

                    item {
                        FeatureToggleItem(
                            title = "Laman Konsultan Hukum Perusahaan",
                            desc = "Menu payung hukum, tim advokat terdaftar PERADI, formulir bantuan dan testimoni.",
                            isEnabled = cfg.enableLegalConsultant,
                            onToggle = { onUpdateFeatures(cfg.copy(enableLegalConsultant = it)) }
                        )
                    }
                }

                // SUBMENU 1: PENGATURAN PERUSAHAAN, TAMPILAN, DAN STRUKTUR PEJABAT
                else if (selectedSubMenu == 1) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Master Profil Perusahaan Penuh & Dua Jalur Email", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepExecutiveBlue)
                                        Text("Admin Utama memiliki kuasa penuh mengubah seluruh data bawaan aplikasi.", fontSize = 11.sp, color = Slate600)
                                    }
                                    Surface(
                                        color = Color(0xFFFEF3C7),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text("Kuasa Admin Utama", color = Color(0xFF92400E), fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                var editName by remember { mutableStateOf(currentCompany.name) }
                                var editCode by remember { mutableStateOf(currentCompany.code) }
                                var editSector by remember { mutableStateOf(currentCompany.sector) }
                                var editNpwp by remember { mutableStateOf(currentCompany.npwp) }
                                var editNib by remember { mutableStateOf(currentCompany.nib) }
                                var editWeb by remember { mutableStateOf(currentCompany.websiteUrl) }
                                var editPhone by remember { mutableStateOf(currentCompany.phone) }
                                var editLegalEmail by remember { mutableStateOf(currentCompany.legalConsultantEmail) }
                                var editOwnerEmail by remember { mutableStateOf(currentCompany.ownerEmail) }
                                var editSecretaryEmail by remember { mutableStateOf(currentCompany.secretaryEmail) }
                                var editWorkStart by remember { mutableStateOf(currentCompany.workHoursStart) }
                                var editWorkEnd by remember { mutableStateOf(currentCompany.workHoursEnd) }
                                var selectedAccentHex by remember { mutableLongStateOf(currentCompany.primaryAccentColorHex) }

                                Text("1. Identitas Badan Usaha:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                                Spacer(modifier = Modifier.height(4.dp))

                                OutlinedTextField(
                                    value = editName,
                                    onValueChange = { editName = it },
                                    label = { Text("Nama Resmi Perusahaan (PT/CV/Korporasi)") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = editCode,
                                        onValueChange = { editCode = it },
                                        label = { Text("Kode Singkatan PT") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    OutlinedTextField(
                                        value = editSector,
                                        onValueChange = { editSector = it },
                                        label = { Text("Bidang / Sektor Bisnis") },
                                        modifier = Modifier.weight(2f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = editNpwp,
                                        onValueChange = { editNpwp = it },
                                        label = { Text("NPWP Perusahaan") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    OutlinedTextField(
                                        value = editNib,
                                        onValueChange = { editNib = it },
                                        label = { Text("Nomor Induk Berusaha (NIB)") },
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = editWeb,
                                        onValueChange = { editWeb = it },
                                        label = { Text("Website Portal") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    OutlinedTextField(
                                        value = editPhone,
                                        onValueChange = { editPhone = it },
                                        label = { Text("Telepon / Call Center") },
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                                Divider(color = LightBlueBorder)
                                Spacer(modifier = Modifier.height(10.dp))

                                Text("2. Pemisahan Dua Jalur Email Perusahaan:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                                Text("Jalur 1 untuk urusan hukum, Jalur 2 untuk urusan pemilik & operasional dinas.", fontSize = 10.sp, color = Slate600)
                                Spacer(modifier = Modifier.height(6.dp))

                                OutlinedTextField(
                                    value = editLegalEmail,
                                    onValueChange = { editLegalEmail = it },
                                    label = { Text("Jalur 1: Email Konsultan Hukum Perusahaan") },
                                    supportingText = { Text("Menerima konsultasi hukum, draf MoU, perkara advokasi (Deny Akhbar)") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                OutlinedTextField(
                                    value = editOwnerEmail,
                                    onValueChange = { editOwnerEmail = it },
                                    label = { Text("Jalur 2A: Email Pemilik Perusahaan") },
                                    supportingText = { Text("Menerima surat dinas kementerian & kontrak bisnis eksekutif") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                OutlinedTextField(
                                    value = editSecretaryEmail,
                                    onValueChange = { editSecretaryEmail = it },
                                    label = { Text("Jalur 2B: Email Sekretaris Perusahaan") },
                                    supportingText = { Text("Menerima rekapitulasi operasional, perizinan & e-Faktur payroll") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(14.dp))
                                Divider(color = LightBlueBorder)
                                Spacer(modifier = Modifier.height(10.dp))

                                Text("3. Jam Kerja Operasional & Geofencing GPS:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                                Spacer(modifier = Modifier.height(6.dp))

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = editWorkStart,
                                        onValueChange = { editWorkStart = it },
                                        label = { Text("Jam Masuk Kerja (WIB)") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    OutlinedTextField(
                                        value = editWorkEnd,
                                        onValueChange = { editWorkEnd = it },
                                        label = { Text("Jam Pulang Kerja (WIB)") },
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                                Divider(color = LightBlueBorder)
                                Spacer(modifier = Modifier.height(10.dp))

                                Text("4. Pilihan Warna Aksen Tampilan Brand:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                                Spacer(modifier = Modifier.height(6.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    val colorsList = listOf(
                                        0xFF0284C7 to "Biru Samudera",
                                        0xFF0D9488 to "Hijau Toska",
                                        0xFFD97706 to "Emas Korporat",
                                        0xFF7C3AED to "Ungu Eksekutif",
                                        0xFF059669 to "Zamrud Lestari"
                                    )
                                    colorsList.forEach { (cHex, _) ->
                                        Surface(
                                            color = Color(cHex),
                                            shape = CircleShape,
                                            modifier = Modifier
                                                .size(32.dp)
                                                .border(2.dp, if (selectedAccentHex == cHex) DeepExecutiveBlue else Color.Transparent, CircleShape)
                                                .clickable { selectedAccentHex = cHex }
                                        ) {}
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        val res = onUpdateMasterData(
                                            editName,
                                            editCode,
                                            editSector,
                                            editNpwp,
                                            editNib,
                                            editWeb,
                                            editPhone,
                                            editLegalEmail,
                                            editOwnerEmail,
                                            editSecretaryEmail,
                                            editWorkStart,
                                            editWorkEnd,
                                            selectedAccentHex
                                        )
                                        snackbarMessage = res.second
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = CorporateBlue)
                                ) {
                                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Simpan Perubahan Master Data Penuh", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }

                    // Struktur Pejabat Perusahaan
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Struktur Pejabat Perusahaan", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepExecutiveBlue)
                                Text("Bagan struktur pimpinan dan pejabat eksekutif aktif.", fontSize = 11.sp, color = Slate600)
                            }
                            FilledTonalButton(
                                onClick = { showAddOfficerDialog = true },
                                modifier = Modifier.height(32.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Tambah Pejabat", fontSize = 10.sp)
                            }
                        }
                    }

                    items(currentCompany.leadershipStructure) { officer ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, LightBlueBorder, RoundedCornerShape(10.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = LightBlueSurface,
                                        shape = CircleShape,
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.Badge, contentDescription = null, tint = CorporateBlue, modifier = Modifier.size(20.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(officer.officerName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepExecutiveBlue)
                                        Text("${officer.title} • ${officer.department}", fontSize = 10.sp, color = CorporateDarkBlue, fontWeight = FontWeight.Medium)
                                        Text(officer.contactEmail, fontSize = 10.sp, color = Slate500)
                                    }
                                }

                                if (isAdmin) {
                                    IconButton(
                                        onClick = {
                                            val res = onRemoveOfficer(officer.id)
                                            snackbarMessage = res.second
                                        }
                                    ) {
                                        Icon(Icons.Default.DeleteOutline, contentDescription = "Hapus", tint = ErrorRose, modifier = Modifier.size(20.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                // SUBMENU 2: MENU PLUGIN & EKSTENSI APLIKASI
                else if (selectedSubMenu == 2) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Extension, contentDescription = null, tint = AccentGold, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text("Katalog Plugin & Integrasi Platform", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepExecutiveBlue)
                                        Text("Instal fitur tambahan atau hubungkan ke platform lain penyedia ekstensi.", fontSize = 11.sp, color = Slate600)
                                    }
                                }
                            }
                        }
                    }

                    items(currentCompany.installedPlugins) { plugin ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, if (plugin.isEnabled) LightBlueBorder else Slate200, RoundedCornerShape(10.dp))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Surface(
                                                color = if (plugin.isEnabled) Color(0xFFDCFCE7) else Slate100,
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    plugin.category,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (plugin.isEnabled) Color(0xFF166534) else Slate600,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(plugin.version, fontSize = 9.sp, color = Slate400)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(plugin.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepExecutiveBlue)
                                        Text("Pengembang: ${plugin.developer}", fontSize = 10.sp, color = Slate500)
                                    }

                                    Switch(
                                        checked = plugin.isEnabled,
                                        onCheckedChange = { isChecked ->
                                            val res = onTogglePlugin(plugin.id, isChecked)
                                            snackbarMessage = res.second
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(plugin.description, fontSize = 11.sp, color = Slate700, lineHeight = 15.sp)

                                Spacer(modifier = Modifier.height(8.dp))
                                Divider(color = Slate100)
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (plugin.configEndpoint.isNotBlank()) "Endpoint: ${plugin.configEndpoint}" else "Siap dikonfigurasi API",
                                        fontSize = 10.sp,
                                        color = CorporateDarkBlue,
                                        maxLines = 1,
                                        modifier = Modifier.weight(1f)
                                    )
                                    OutlinedButton(
                                        onClick = { selectedPluginForConfig = plugin },
                                        modifier = Modifier.height(28.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Konfigurasi", fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                // SUBMENU 3: PEMBUAT MENU KUSTOM & HALAMAN DOKUMENTASI DENGAN TEKS, FOTO & VIDEO
                else if (selectedSubMenu == 3) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Desain Menu & Halaman Kustom Perusahaan", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepExecutiveBlue)
                                        Text("Admin bebas menambah menu, sub-menu, tulisan bebas, foto galeri, dan video dokumentasi.", fontSize = 11.sp, color = Slate600)
                                    }
                                    Button(
                                        onClick = { showCreateMenuDialog = true },
                                        modifier = Modifier.testTag("create_custom_menu_button")
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Buat Menu", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            "Daftar Menu Kustom Tersedia (${currentCompany.customMenus.size}):",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = DeepExecutiveBlue
                        )
                    }

                    if (currentCompany.customMenus.isEmpty()) {
                        item {
                            Surface(
                                color = Color.White,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, LightBlueBorder, RoundedCornerShape(10.dp))
                                    .padding(24.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.LibraryAdd, contentDescription = null, tint = Slate400, modifier = Modifier.size(36.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Belum Ada Menu Kustom", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Slate600)
                                    Text("Klik tombol 'Buat Menu' di atas untuk mendesain halaman informasi dan dokumentasi baru.", fontSize = 10.sp, color = Slate500)
                                }
                            }
                        }
                    }

                    items(currentCompany.customMenus) { cMenu ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                                .clickable { onOpenCustomMenu(cMenu) }
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = LightBlueSurface,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(cMenu.categoryTag, color = CorporateDarkBlue, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                    if (isAdmin) {
                                        IconButton(
                                            onClick = {
                                                val res = onDeleteCustomMenu(cMenu.id)
                                                snackbarMessage = res.second
                                            },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(Icons.Default.DeleteOutline, contentDescription = "Hapus", tint = ErrorRose, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(cMenu.menuTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepExecutiveBlue)
                                Text("Sub Menu: ${cMenu.subMenuTitle}", fontSize = 11.sp, color = CorporateBlue, fontWeight = FontWeight.Medium)

                                val firstSec = cMenu.sections.firstOrNull()
                                if (firstSec != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(firstSec.textContent, fontSize = 11.sp, color = Slate600, maxLines = 2)

                                    if (firstSec.videoUrl != null || firstSec.photoUrls.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            if (firstSec.photoUrls.isNotEmpty()) {
                                                Surface(color = Color(0xFFEFF6FF), shape = RoundedCornerShape(4.dp)) {
                                                    Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = CorporateBlue, modifier = Modifier.size(12.dp))
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text("Disertai Foto Galeri", fontSize = 9.sp, color = CorporateBlue)
                                                    }
                                                }
                                            }
                                            if (firstSec.videoUrl != null) {
                                                Surface(color = Color(0xFFFEF2F2), shape = RoundedCornerShape(4.dp)) {
                                                    Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Default.PlayCircle, contentDescription = null, tint = ErrorRose, modifier = Modifier.size(12.dp))
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text("Disertai Video", fontSize = 9.sp, color = ErrorRose)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                 Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(
                                        onClick = { onOpenCustomMenu(cMenu) },
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text("Buka Laman & Dokumentasi", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
                // SUBMENU 4: TOKO TEMA UNDUHAN & KUSTOMISASI BERANDA
                else if (selectedSubMenu == 4) {
                    val dConfig = currentCompany.dashboardConfig

                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = Color(0xFF0284C7),
                                    shape = CircleShape,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Palette, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Toko Koleksi Tema Google Cloud", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepExecutiveBlue)
                                    Text("Unduh dan terapkan tema warna dinamis ke seluruh antarmuka aplikasi secara instan.", fontSize = 11.sp, color = Slate600)
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            "Koleksi Tema Tersedia untuk Diunduh (${themePresets.size}):",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = DeepExecutiveBlue
                        )
                    }

                    items(themePresets) { themeItem ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    if (themeItem.isApplied) CorporateBlue else LightBlueBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .testTag("theme_card_${themeItem.id}")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Color Swatch
                                        Row(
                                            modifier = Modifier
                                                .border(1.dp, Color(themeItem.borderHex), RoundedCornerShape(8.dp))
                                                .padding(3.dp),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(modifier = Modifier.size(16.dp).background(Color(themeItem.bgHex), CircleShape).border(1.dp, Slate300, CircleShape))
                                            Box(modifier = Modifier.size(16.dp).background(Color(themeItem.surfaceHex), CircleShape))
                                            Box(modifier = Modifier.size(16.dp).background(Color(themeItem.primaryHex), CircleShape))
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(themeItem.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepExecutiveBlue)
                                            Text(themeItem.description, fontSize = 10.sp, color = Slate600)
                                        }
                                    }

                                    if (themeItem.isApplied) {
                                        Surface(color = Color(0xFFDCFCE7), shape = RoundedCornerShape(6.dp)) {
                                            Text("Sedang Aktif", color = Color(0xFF166534), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (themeItem.isApplied) {
                                        Text("Tema aktif di semua layar", fontSize = 11.sp, color = SuccessEmerald, fontWeight = FontWeight.Medium)
                                    } else if (themeItem.isDownloaded) {
                                        Button(
                                            onClick = {
                                                val res = onApplyTheme(themeItem.id)
                                                snackbarMessage = res.second
                                            },
                                            modifier = Modifier.height(34.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = CorporateBlue)
                                        ) {
                                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Terapkan Tema", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    } else {
                                        FilledTonalButton(
                                            onClick = {
                                                val res = onDownloadTheme(themeItem.id)
                                                snackbarMessage = res.second
                                            },
                                            modifier = Modifier.height(34.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp)
                                        ) {
                                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Unduh Tema", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Dashboard Layout & Logo Settings Card
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Pengaturan Tata Letak Beranda & Logo Perusahaan:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = DeepExecutiveBlue
                        )
                    }

                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("Posisi Logo Saat Ini: ${dConfig.logoPosition.label}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    LogoPosition.values().forEach { pos ->
                                        FilterChip(
                                            selected = dConfig.logoPosition == pos,
                                            onClick = {
                                                val res = onUpdateCompanyLogo(dConfig.logoUri, pos)
                                                snackbarMessage = res.second
                                            },
                                            label = { Text(pos.label, fontSize = 9.sp) }
                                        )
                                    }
                                }

                                Divider(color = Slate200)

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Unggah Logo dari Perangkat HP", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepExecutiveBlue)
                                        Text(if (dConfig.logoUri != null) "Logo Kustom Terpasang" else "Belum ada logo terunggah", fontSize = 10.sp, color = Slate500)
                                    }
                                    Button(
                                        onClick = {
                                            photoPickerLauncher.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        },
                                        modifier = Modifier.height(34.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Pilih Foto", fontSize = 11.sp)
                                    }
                                }

                                Divider(color = Slate200)

                                Text("Pesan Broadcasting Aktif:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepExecutiveBlue)
                                Text("\"${dConfig.broadcastingMessage}\"", fontSize = 11.sp, color = Slate700)
                                Text("Urgensi: ${dConfig.broadcastingUrgency} • Status: ${if (dConfig.isBroadcastingActive) "Aktif Ditayangkan" else "Nonaktif"}", fontSize = 10.sp, color = CorporateDarkBlue)
                            }
                        }
                    }
                }
            }
        }
    }

    // DIALOG 1: TAMBAH PEJABAT STRUKTURAL (Rendered at composable scope)
    if (showAddOfficerDialog) {
        var offTitle by remember { mutableStateOf("") }
        var offName by remember { mutableStateOf("") }
        var offDept by remember { mutableStateOf("") }
        var offEmail by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddOfficerDialog = false },
            title = { Text("Tambah Pejabat Struktural Baru") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = offTitle,
                        onValueChange = { offTitle = it },
                        label = { Text("Nama Jabatan (misal: Direktur Keuangan)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = offName,
                        onValueChange = { offName = it },
                        label = { Text("Nama Lengkap Pejabat") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = offDept,
                        onValueChange = { offDept = it },
                        label = { Text("Departemen / Divisi") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = offEmail,
                        onValueChange = { offEmail = it },
                        label = { Text("Email Resmi") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (offTitle.isNotBlank() && offName.isNotBlank()) {
                            val res = onAddOfficer(offTitle, offName, offDept, offEmail)
                            snackbarMessage = res.second
                            showAddOfficerDialog = false
                        }
                    }
                ) { Text("Tambah") }
            },
            dismissButton = {
                TextButton(onClick = { showAddOfficerDialog = false }) { Text("Batal") }
            }
        )
    }

    // DIALOG 2: KONFIGURASI PLUGIN (Rendered at composable scope)
    selectedPluginForConfig?.let { plugin ->
        var ep by remember { mutableStateOf(plugin.configEndpoint) }
        var ak by remember { mutableStateOf(plugin.apiKey) }

        AlertDialog(
            onDismissRequest = { selectedPluginForConfig = null },
            title = { Text("Konfigurasi ${plugin.name}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Atur tautan integrasi ke platform eksternal atau server perusahaan:", fontSize = 11.sp, color = Slate600)
                    OutlinedTextField(
                        value = ep,
                        onValueChange = { ep = it },
                        label = { Text("Webhook / REST API Endpoint URL") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = ak,
                        onValueChange = { ak = it },
                        label = { Text("API Secret Key / Token") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val res = onSavePluginConfig(plugin.id, ep, ak)
                        snackbarMessage = res.second
                        selectedPluginForConfig = null
                    }
                ) { Text("Simpan") }
            },
            dismissButton = {
                TextButton(onClick = { selectedPluginForConfig = null }) { Text("Batal") }
            }
        )
    }

    // DIALOG 3: BUAT MENU KUSTOM (Rendered at composable scope)
    if (showCreateMenuDialog) {
        var mTitle by remember { mutableStateOf("") }
        var subTitle by remember { mutableStateOf("") }
        var catTag by remember { mutableStateOf("Dokumentasi & SOP") }
        var bodyText by remember { mutableStateOf("") }
        var photoUrlInput by remember { mutableStateOf("https://images.unsplash.com/photo-1504384308090-c894fdcc538d?w=600") }
        var photoCaptionInput by remember { mutableStateOf("Dokumentasi Fasilitas dan Tim Operasional") }
        var videoUrlInput by remember { mutableStateOf("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4") }
        var videoTitleInput by remember { mutableStateOf("Video Paparan & Dokumentasi Resmi") }

        AlertDialog(
            onDismissRequest = { showCreateMenuDialog = false },
            title = { Text("Desain Menu & Halaman Kustom Baru") },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 440.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = mTitle,
                            onValueChange = { mTitle = it },
                            label = { Text("Judul Menu Utama") },
                            placeholder = { Text("Contoh: SOP Perawatan Mesin Pabrik") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = subTitle,
                            onValueChange = { subTitle = it },
                            label = { Text("Judul Sub-Menu / Laman") },
                            placeholder = { Text("Contoh: Panduan Operasional & Checklist") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = catTag,
                            onValueChange = { catTag = it },
                            label = { Text("Kategori Menu") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = bodyText,
                            onValueChange = { bodyText = it },
                            label = { Text("Isi Ketikan Laman / Narasi Informasi") },
                            minLines = 4,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        Text("Sisipkan Dokumentasi Foto:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = photoUrlInput,
                            onValueChange = { photoUrlInput = it },
                            label = { Text("URL / Tautan Foto Dokumentasi") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = photoCaptionInput,
                            onValueChange = { photoCaptionInput = it },
                            label = { Text("Keterangan / Caption Foto") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        Text("Sisipkan Dokumentasi Video:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = videoUrlInput,
                            onValueChange = { videoUrlInput = it },
                            label = { Text("Tautan Video (MP4 / Web Video)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = videoTitleInput,
                            onValueChange = { videoTitleInput = it },
                            label = { Text("Judul Video") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (mTitle.isNotBlank() && bodyText.isNotBlank()) {
                            val res = onCreateCustomMenu(
                                mTitle,
                                subTitle.ifBlank { mTitle },
                                catTag,
                                bodyText,
                                photoUrlInput.ifBlank { null },
                                photoCaptionInput.ifBlank { null },
                                videoUrlInput.ifBlank { null },
                                videoTitleInput.ifBlank { null }
                            )
                            snackbarMessage = res.second
                            showCreateMenuDialog = false
                        }
                    }
                ) { Text("Terbitkan Menu") }
            },
            dismissButton = {
                TextButton(onClick = { showCreateMenuDialog = false }) { Text("Batal") }
            }
        )
    }
}

@Composable
private fun FeatureToggleItem(
    title: String,
    desc: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, if (isEnabled) LightBlueBorder else Slate200, RoundedCornerShape(10.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepExecutiveBlue)
                Text(desc, fontSize = 10.sp, color = Slate600, lineHeight = 14.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle
            )
        }
    }
}
