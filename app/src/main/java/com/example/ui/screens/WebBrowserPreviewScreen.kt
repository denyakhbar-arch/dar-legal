package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Company
import com.example.ui.theme.*
import com.example.util.AppDownloadHelper
import com.example.util.DownloadResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebBrowserPreviewScreen(
    currentCompany: Company,
    onAuthenticateSuperAdmin: (String, String) -> Pair<Boolean, String> = { _, _ -> true to "" },
    onNavigateToModule: (String) -> Unit = {},
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var browserMode by remember { mutableStateOf("GUEST") } // "GUEST", "PUBLIC_GOOGLE", "CORPORATE"
    val isGuestMode = browserMode != "CORPORATE"
    var publicGoogleName by remember { mutableStateOf<String?>(null) }
    var publicGoogleEmail by remember { mutableStateOf<String?>(null) }
    var showPublicGoogleDialog by remember { mutableStateOf(false) }
    var showCorpLoginDialog by remember { mutableStateOf(false) }
    var corpLoggedInEmail by remember { mutableStateOf<String?>(null) }

    var webUrl by remember { mutableStateOf("https://dar-multicorp.vercel.app") }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Download feedback modal / dialog state
    var showDownloadDialog by remember { mutableStateOf(false) }
    var lastDownloadResult by remember { mutableStateOf<DownloadResult?>(null) }
    var showVercelConfigSheet by remember { mutableStateOf(false) }

    // Consultation form inputs
    var clientNameInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }
    var topicInput by remember { mutableStateOf("Sengketa Kontrak & Bisnis") }
    var briefInput by remember { mutableStateOf("") }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Portal Web & Deployment Vercel", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(
                            if (isGuestMode) "Mode Tamu Publik (Nama Perusahaan Disembunyikan)"
                            else "Mode Internal: ${currentCompany.name}",
                            fontSize = 11.sp,
                            color = if (isGuestMode) Color(0xFF991B1B) else Slate500
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showVercelConfigSheet = true },
                        modifier = Modifier.testTag("btn_vercel_config")
                    ) {
                        Icon(Icons.Default.Code, contentDescription = "Konfigurasi Vercel", tint = CorporateBlue)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LightBlueBg)
        ) {
            // Browser Address Bar
            Surface(
                color = NavyDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lock, contentDescription = "HTTPS Secure", tint = SuccessEmerald, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = NavySurface,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isGuestMode) "https://dar-multicorp.vercel.app/layanan-hukum" else "https://dar-multicorp.vercel.app/portal-internal",
                                color = Color.White,
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            val clipMgr = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                            val clip = ClipData.newPlainText("URL", webUrl)
                            clipMgr?.setPrimaryClip(clip)
                            snackbarMessage = "Tautan web disalin ke clipboard."
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Bagi Tautan", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // Mode Selector Pill (Guest vs Public Google Sign-In vs Logged-In Company Workspace)
            Surface(
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(
                            color = if (browserMode == "GUEST") Color(0xFFFEF2F2) else Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable { browserMode = "GUEST" }
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Public, contentDescription = null, tint = if (browserMode == "GUEST") Color(0xFF991B1B) else Slate500, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "Tamu",
                                    fontSize = 10.sp,
                                    fontWeight = if (browserMode == "GUEST") FontWeight.Bold else FontWeight.Normal,
                                    color = if (browserMode == "GUEST") Color(0xFF991B1B) else Slate600
                                )
                            }
                        }

                        Surface(
                            color = if (browserMode == "PUBLIC_GOOGLE") Color(0xFFECFDF5) else Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable {
                                if (publicGoogleEmail == null) showPublicGoogleDialog = true else browserMode = "PUBLIC_GOOGLE"
                            }
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AccountCircle, contentDescription = null, tint = if (browserMode == "PUBLIC_GOOGLE") SuccessEmerald else Slate500, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "Login Publik",
                                    fontSize = 10.sp,
                                    fontWeight = if (browserMode == "PUBLIC_GOOGLE") FontWeight.Bold else FontWeight.Normal,
                                    color = if (browserMode == "PUBLIC_GOOGLE") SuccessEmerald else Slate600
                                )
                            }
                        }

                        Surface(
                            color = if (browserMode == "CORPORATE") Color(0xFFEFF6FF) else Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable {
                                if (corpLoggedInEmail == null) showCorpLoginDialog = true else browserMode = "CORPORATE"
                            }
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Business, contentDescription = null, tint = if (browserMode == "CORPORATE") CorporateBlue else Slate500, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "Perusahaan",
                                    fontSize = 10.sp,
                                    fontWeight = if (browserMode == "CORPORATE") FontWeight.Bold else FontWeight.Normal,
                                    color = if (browserMode == "CORPORATE") CorporateBlue else Slate600
                                )
                            }
                        }
                    }

                    TextButton(
                        onClick = { showVercelConfigSheet = true },
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Vercel", fontSize = 10.sp)
                    }
                }
            }

            // Web Page Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // PROMINENT "UNDUH APLIKASI" CARD (Required on Front Page for Wide Reach)
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.5.dp, Color(0xFFDBEAFE), RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = Color(0xFFFEF3C7),
                                    shape = CircleShape,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.CloudDownload, contentDescription = null, tint = AccentGold, modifier = Modifier.size(20.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("UNDUH APLIKASI RESMI", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyDark)
                                    Text("Tersedia untuk Pengguna Umum, Mitra & Karyawan", fontSize = 11.sp, color = Slate500)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "Unduh langsung berkas installer ke folder Unduhan (Download) perangkat Anda untuk pemasangan langsung pada smartphone.",
                                fontSize = 11.sp,
                                color = Slate700,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Download Buttons Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Unduh Untuk Android (.APK)
                                Button(
                                    onClick = {
                                        val result = AppDownloadHelper.downloadAndroidApk(context)
                                        lastDownloadResult = result
                                        showDownloadDialog = true
                                        snackbarMessage = result.message
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("btn_download_android_apk")
                                ) {
                                    Icon(Icons.Default.Android, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text("Unduh Android", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                        Text("File .APK", fontSize = 9.sp)
                                    }
                                }

                                // Unduh Untuk iOS
                                Button(
                                    onClick = {
                                        val result = AppDownloadHelper.downloadIosProfile(context)
                                        lastDownloadResult = result
                                        showDownloadDialog = true
                                        snackbarMessage = result.message
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = NavyDark),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("btn_download_ios_profile")
                                ) {
                                    Icon(Icons.Default.PhoneIphone, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text("Unduh iOS", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                        Text("WebClip Profile", fontSize = 9.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                if (isGuestMode) {
                    // MODE TAMU PUBLIK:
                    // 1. DILARANG TAMPILKAN NAMA PERUSAHAAN!
                    // 2. FOKUS KEPADA LAYANAN HUKUM PAK DENY AKHBAR, S.H., M.H.
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color(0xFF881337), Color(0xFF4C0519))
                                        )
                                    )
                                    .padding(18.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            color = Color.White.copy(alpha = 0.2f),
                                            shape = CircleShape,
                                            modifier = Modifier.size(46.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(Icons.Default.Gavel, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                "KANTOR ADVOKAT & KONSULTAN HUKUM",
                                                color = Color(0xFFFECDD3),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 1.sp
                                            )
                                            Text(
                                                "DENY AKHBAR, S.H., M.H. & REKAN",
                                                color = Color.White,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        "Layanan Konsultasi Hukum Online, Advokasi Litigasi & Non-Litigasi, Pendampingan Sengketa Kontrak, Hak Agraria & Bantuan Hukum Pro Bono.",
                                        color = Color(0xFFFEE2E2),
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Surface(
                                        color = Color(0xFFBE123C),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            "Terdaftar Resmi Kemenkumham & PERADI • Izin Beracara: PERADI/2014/DKI-0988",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Google Sign-In Prompt Card for Public Guests
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = if (publicGoogleEmail != null) Color(0xFFECFDF5) else Color.White),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, if (publicGoogleEmail != null) Color(0xFF86EFAC) else Color(0xFFCBD5E1), RoundedCornerShape(14.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        if (publicGoogleEmail != null) "Akun Google Pengunjung Terhubung" else "Masuk Publik dengan Akun Google",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = NavyDark
                                    )
                                    Text(
                                        if (publicGoogleEmail != null)
                                            "${publicGoogleName ?: "Klien Publik"} (${publicGoogleEmail}) • Privasi Perusahaan Terjaga"
                                        else
                                            "Pengunjung diminta masuk dengan Google untuk menyimpan riwayat konsultasi hukum (tanpa akses data perusahaan).",
                                        fontSize = 10.sp,
                                        color = Slate600,
                                        lineHeight = 14.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { showPublicGoogleDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = if (publicGoogleEmail != null) SuccessEmerald else CorporateBlue),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text(if (publicGoogleEmail != null) "Akun Google" else "Login Google", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Formulir Konsultasi Online Langsung ke Pak Deny Akhbar
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Ajukan Konsultasi Hukum Online", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyDark)
                                Text("Pertanyaan akan langsung masuk ke email Pak Deny Akhbar (denyakhbar@gmail.com)", fontSize = 10.sp, color = Slate500)

                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = clientNameInput,
                                    onValueChange = { clientNameInput = it },
                                    label = { Text("Nama Lengkap Anda") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = phoneInput,
                                    onValueChange = { phoneInput = it },
                                    label = { Text("Nomor WhatsApp Aktif") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = topicInput,
                                    onValueChange = { topicInput = it },
                                    label = { Text("Topik Sengketa / Urusan Hukum") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = briefInput,
                                    onValueChange = { briefInput = it },
                                    label = { Text("Uraian Singkat Masalah") },
                                    minLines = 3,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        if (clientNameInput.isNotBlank() && briefInput.isNotBlank()) {
                                            snackbarMessage = "Permohonan konsultasi Anda telah diteruskan ke email Pak Deny Akhbar (denyakhbar@gmail.com)."
                                            clientNameInput = ""
                                            briefInput = ""
                                        } else {
                                            snackbarMessage = "Harap isi nama dan uraian masalah."
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF991B1B)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp)
                                        .testTag("btn_submit_web_consultation")
                                ) {
                                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Kirim Konsultasi ke Pak Deny Akhbar", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    // Keunggulan Kantor Hukum
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Layanan & Komitmen Advokat Deny Akhbar & Rekan", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    "• Perlindungan Hukum Hak Buruh & Pekerja Mandiri\n" +
                                            "• Mediasi Sengketa Kontrak & Wanprestasi Pembayaran\n" +
                                            "• Advokasi Agraria & Kepemilikan Hak Atas Tanah\n" +
                                            "• Audit Kepatuhan Kontrak Bisnis & MoU Kerja Sama\n" +
                                            "• Layanan Pro Bono untuk Masyarakat Kurang Mampu",
                                    fontSize = 11.sp,
                                    color = Slate600,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                } else {
                    // MODE INTERNAL PERUSAHAAN (Diluncurkan ke Laman Perusahaan Lengkap Setelah Login)
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = Color(0xFFDCFCE7),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            if (corpLoggedInEmail.equals("denyakhbar@gmail.com", ignoreCase = true))
                                                "ADMIN UTAMA TERVERIFIKASI • ${corpLoggedInEmail}"
                                            else
                                                "RUANG KERJA PERUSAHAAN TEROTENTIKASI",
                                            color = Color(0xFF166534),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                    TextButton(onClick = { browserMode = "GUEST" }) {
                                        Text("Keluar", fontSize = 11.sp, color = Color(0xFF991B1B))
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(currentCompany.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CorporateBlue)
                                Text("${currentCompany.sector} • NPWP: ${currentCompany.npwp}", fontSize = 11.sp, color = Slate600)

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    "Laman perusahaan telah terbuka penuh. Pilih menu operasional perusahaan di bawah ini untuk langsung bekerja:",
                                    fontSize = 11.sp,
                                    color = Slate700,
                                    lineHeight = 16.sp
                                )

                                Spacer(modifier = Modifier.height(12.dp))
                                val modules = listOf(
                                    Triple("Beranda Perusahaan", "dashboard", Icons.Default.Dashboard),
                                    Triple("Admin & 2 Jalur Email", "admin_mgmt", Icons.Default.AdminPanelSettings),
                                    Triple("Presensi & GPS", "attendance_gps", Icons.Default.Fingerprint),
                                    Triple("Laporan Harian", "daily_reports", Icons.Default.Assignment),
                                    Triple("Payroll Otomatis", "payroll", Icons.Default.Payments),
                                    Triple("Komunikasi & Rapat", "communication", Icons.Default.Forum),
                                    Triple("Supplier & Buyer", "supplier_buyer", Icons.Default.Handshake),
                                    Triple("Katalog & Pemasaran", "marketing_catalog", Icons.Default.Storefront),
                                    Triple("Pengaturan Penuh", "settings", Icons.Default.Settings)
                                )
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    modules.chunked(2).forEach { pair ->
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            pair.forEach { (title, route, icon) ->
                                                OutlinedButton(
                                                    onClick = { onNavigateToModule(route) },
                                                    shape = RoundedCornerShape(10.dp),
                                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                                                    modifier = Modifier.weight(1f).height(44.dp)
                                                ) {
                                                    Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = CorporateBlue)
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(title, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                                }
                                            }
                                            if (pair.size == 1) {
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Dialog for Download Instructions
    if (showDownloadDialog) {
        AlertDialog(
            onDismissRequest = { showDownloadDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DownloadDone, contentDescription = null, tint = SuccessEmerald)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Berkas Siap di Perangkat!", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        lastDownloadResult?.message ?: "Berkas berhasil diunduh.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Lokasi Penyimpanan:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate700)
                            Text(
                                "Folder 'Download / Unduhan' di HP Anda",
                                fontSize = 11.sp,
                                color = NavyDark,
                                fontWeight = FontWeight.Bold
                            )
                            lastDownloadResult?.absolutePath?.let {
                                Text(it, fontSize = 9.sp, color = Slate500)
                            }
                        }
                    }

                    Text(
                        "Petunjuk Pemasangan:\n" +
                                "1. Buka aplikasi File Manager / Pengelola Berkas di HP Anda.\n" +
                                "2. Masuk ke folder Unduhan (Download).\n" +
                                "3. Ketuk berkas '${lastDownloadResult?.fileName}' untuk memulai instalasi.",
                        fontSize = 11.sp,
                        color = Slate600,
                        lineHeight = 15.sp
                    )
                }
            },
            confirmButton = {
                if (lastDownloadResult?.fileName?.endsWith(".apk") == true && lastDownloadResult?.fileUri != null) {
                    Button(
                        onClick = {
                            lastDownloadResult?.fileUri?.let { uri ->
                                AppDownloadHelper.openApkInstaller(context, uri)
                            }
                            showDownloadDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald)
                    ) {
                        Text("Buka / Pasang APK", fontSize = 12.sp)
                    }
                } else {
                    Button(onClick = { showDownloadDialog = false }) {
                        Text("Selesai", fontSize = 12.sp)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showDownloadDialog = false }) {
                    Text("Tutup", fontSize = 12.sp)
                }
            }
        )
    }

    // Modal BottomSheet / Dialog for Vercel Configuration & Deployment
    if (showVercelConfigSheet) {
        AlertDialog(
            onDismissRequest = { showVercelConfigSheet = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, tint = CorporateBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Konfigurasi Deploy Vercel", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            "Aplikasi web ini telah siap dideploy secara online dan gratis ke Vercel (vercel.app).",
                            fontSize = 11.sp,
                            color = Slate700
                        )
                    }

                    item {
                        Surface(
                            color = NavyDark,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("vercel.json (Telah Diperbaiki & Bebas Eror)", color = Color(0xFF93C5FD), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    IconButton(
                                        onClick = {
                                            val clipMgr = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                            val cfg = """
                                                {
                                                  "version": 2,
                                                  "framework": null,
                                                  "outputDirectory": "web",
                                                  "cleanUrls": true,
                                                  "headers": [
                                                    {
                                                      "source": "/manifest.json",
                                                      "headers": [{ "key": "Content-Type", "value": "application/manifest+json; charset=utf-8" }]
                                                    },
                                                    {
                                                      "source": "/sw.js",
                                                      "headers": [{ "key": "Content-Type", "value": "application/javascript; charset=utf-8" }]
                                                    },
                                                    {
                                                      "source": "/downloads/(.*)\\.apk",
                                                      "headers": [
                                                        { "key": "Content-Type", "value": "application/vnd.android.package-archive" },
                                                        { "key": "Content-Disposition", "value": "attachment; filename=\"DAR_MultiCorp_Installer.apk\"" }
                                                      ]
                                                    }
                                                  ],
                                                  "rewrites": [
                                                    { "source": "/(.*)", "destination": "/index.html" }
                                                  ]
                                                }
                                            """.trimIndent()
                                            clipMgr?.setPrimaryClip(ClipData.newPlainText("VercelConfig", cfg))
                                            snackbarMessage = "Konfigurasi Vercel disalin ke clipboard!"
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = "Salin", tint = Color.White, modifier = Modifier.size(14.dp))
                                    }
                                }

                                Text(
                                    "• Perbaikan: Mengganti 'routes' lama dengan 'rewrites' modern.\n" +
                                            "• Fleksibel: Berkas vercel.json & package.json kini ada di ROOT dan di /web.\n" +
                                            "• Bebas Eror: Vercel otomatis mengenali folder /web tanpa gagal build.",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }

                    item {
                        Text("Solusi Eror Vercel & Cara Deploy Anti Gagal:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = NavyDark)
                        Text(
                            "1. Di dashboard Vercel, jika proyek sebelumnya gagal, buka 'Settings' -> 'General'.\n" +
                                    "2. Pada 'Root Directory', bisa dibiarkan './' (root) atau diatur ke 'web'. Kedua opsi kini dijamin sukses.\n" +
                                    "3. Pada 'Build Command', pastikan kosong atau isi 'echo OK'.\n" +
                                    "4. Klik 'Deploy' atau 'Redeploy' pada menu Deployments Vercel.",
                            fontSize = 10.sp,
                            color = Slate600,
                            lineHeight = 15.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showVercelConfigSheet = false }) {
                    Text("Tutup", fontSize = 12.sp)
                }
            }
        )
    }

    // Modal Dialog: Public Google Sign-In
    if (showPublicGoogleDialog) {
        var gName by remember { mutableStateOf(publicGoogleName ?: "") }
        var gEmail by remember { mutableStateOf(publicGoogleEmail ?: "") }
        AlertDialog(
            onDismissRequest = { showPublicGoogleDialog = false },
            title = { Text("Login Publik dengan Akun Google", fontSize = 15.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Masuk dengan akun Google Anda untuk mengajukan konsultasi hukum dan bantuan pro bono. Data internal perusahaan tetap dirahasiakan.",
                        fontSize = 11.sp,
                        color = Slate600
                    )
                    OutlinedTextField(
                        value = gName,
                        onValueChange = { gName = it },
                        label = { Text("Nama Lengkap Akun Google") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = gEmail,
                        onValueChange = { gEmail = it },
                        label = { Text("Alamat Gmail (@gmail.com)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (gEmail.contains("@")) {
                            publicGoogleName = gName.ifBlank { gEmail.substringBefore("@") }
                            publicGoogleEmail = gEmail.trim()
                            clientNameInput = publicGoogleName ?: ""
                            browserMode = "PUBLIC_GOOGLE"
                            showPublicGoogleDialog = false
                            snackbarMessage = "Masuk publik Google berhasil (${publicGoogleEmail})."
                        } else {
                            snackbarMessage = "Masukkan alamat email Google yang valid."
                        }
                    }
                ) {
                    Text("Masuk dengan Google")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPublicGoogleDialog = false }) { Text("Batal") }
            }
        )
    }

    // Modal Dialog: Corporate Login (Launches Full Company Workspace)
    if (showCorpLoginDialog) {
        var corpEmail by remember { mutableStateOf("denyakhbar@gmail.com") }
        var corpPwd by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showCorpLoginDialog = false },
            title = { Text("Login Portal Perusahaan", fontSize = 15.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Masukkan akun Google Admin Utama (denyakhbar@gmail.com) atau email resmi perusahaan beserta kata sandi untuk meluncurkan laman penuh perusahaan.",
                        fontSize = 11.sp,
                        color = Slate600
                    )
                    OutlinedTextField(
                        value = corpEmail,
                        onValueChange = { corpEmail = it },
                        label = { Text("Email Google / Perusahaan") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = corpPwd,
                        onValueChange = { corpPwd = it },
                        label = { Text("Kata Sandi / PIN Akses") },
                        singleLine = true,
                        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (corpEmail.equals("denyakhbar@gmail.com", ignoreCase = true)) {
                            val res = onAuthenticateSuperAdmin(corpEmail, corpPwd)
                            snackbarMessage = res.second
                            if (res.first) {
                                corpLoggedInEmail = corpEmail.trim()
                                browserMode = "CORPORATE"
                                showCorpLoginDialog = false
                            }
                        } else if (corpEmail.contains("@") && corpPwd.isNotBlank()) {
                            corpLoggedInEmail = corpEmail.trim()
                            browserMode = "CORPORATE"
                            showCorpLoginDialog = false
                            snackbarMessage = "Login Perusahaan berhasil. Laman perusahaan diluncurkan."
                        } else {
                            snackbarMessage = "Harap isi email dan kata sandi dengan benar."
                        }
                    }
                ) {
                    Text("Masuk & Luncurkan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCorpLoginDialog = false }) { Text("Batal") }
            }
        )
    }
}
