package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.util.AppDownloadHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalConsultantScreen(
    advocateTeam: List<AdvocateMember>,
    testimonials: List<LegalTestimonial>,
    consultationBookings: List<ConsultationBooking>,
    legalAidRequests: List<LegalAidRequest>,
    legalPartnerships: List<LegalPartnershipApplication>,
    onBookConsultation: (name: String, org: String, email: String, phone: String, topic: String, date: String, brief: String) -> Pair<Boolean, String>,
    onSubmitLegalAid: (name: String, contact: String, cat: String, desc: String) -> Pair<Boolean, String>,
    onSubmitPartnership: (name: String, office: String, loc: String, area: String, note: String) -> Pair<Boolean, String>,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var legalTab by remember { mutableIntStateOf(0) } // 0: Profil Advokat, 1: Form Konsultasi, 2: Bantuan Hukum, 3: Menjadi Mitra, 4: Testimoni Klien
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

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
                        Text("Konsultan Hukum & Advokat", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("Payung Hukum Resmi Korporasi & Rekanan Terpercaya", fontSize = 11.sp, color = Slate500)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Scrollable Tab Row for all 5 requested sections
            ScrollableTabRow(
                selectedTabIndex = legalTab,
                edgePadding = 12.dp
            ) {
                Tab(
                    selected = legalTab == 0,
                    onClick = { legalTab = 0 },
                    text = { Text("Profil Kantor Hukum", fontSize = 11.sp) }
                )
                Tab(
                    selected = legalTab == 1,
                    onClick = { legalTab = 1 },
                    text = { Text("Form Konsultasi", fontSize = 11.sp) }
                )
                Tab(
                    selected = legalTab == 2,
                    onClick = { legalTab = 2 },
                    text = { Text("Bantuan Hukum", fontSize = 11.sp) }
                )
                Tab(
                    selected = legalTab == 3,
                    onClick = { legalTab = 3 },
                    text = { Text("Menjadi Mitra", fontSize = 11.sp) }
                )
                Tab(
                    selected = legalTab == 4,
                    onClick = { legalTab = 4 },
                    text = { Text("Testimoni Sukses", fontSize = 11.sp) }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightBlueBg)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                when (legalTab) {
                    0 -> {
                        // 1. COMPANY PROFILE PENGACARA & FIRMA HUKUM
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
                                        .padding(16.dp)
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Surface(
                                                color = Color.White.copy(alpha = 0.2f),
                                                shape = CircleShape,
                                                modifier = Modifier.size(42.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Icon(Icons.Default.Gavel, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                                                }
                                            }
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Text(
                                                    "NUSANTARA LEGAL & CORPORATE PARTNERS",
                                                    color = Color(0xFFFECDD3),
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    letterSpacing = 1.sp
                                                )
                                                Text(
                                                    "Advokat, Auditor Hukum & Konsultan Korporasi",
                                                    color = Color.White,
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            "Memberikan jaminan kepatuhan hukum (legal compliance), perlindungan aset perseroan, mitigasi risiko sengketa, dan penyusunan kontrak bisnis berstandar nasional dan internasional.",
                                            color = Color(0xFFFEE2E2),
                                            fontSize = 11.sp,
                                            lineHeight = 16.sp
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))
                                        Divider(color = Color(0xFFBE123C))
                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("Terdaftar Kemenkumham & PERADI", color = Color(0xFFFECDD3), fontSize = 10.sp)
                                            Text("Akreditasi Grade A", color = Color(0xFFFECDD3), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(12.dp))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CloudDownload, contentDescription = null, tint = AccentGold, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Unduh Installer Aplikasi Resmi", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDark)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        "Simpan berkas aplikasi langsung ke folder Download smartphone Anda untuk dipasang mandiri.",
                                        fontSize = 11.sp,
                                        color = Slate600
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                val res = AppDownloadHelper.downloadAndroidApk(context)
                                                snackbarMessage = res.message
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f).height(38.dp)
                                        ) {
                                            Icon(Icons.Default.Android, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Unduh APK", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = {
                                                val res = AppDownloadHelper.downloadIosProfile(context)
                                                snackbarMessage = res.message
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = NavyDark),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f).height(38.dp)
                                        ) {
                                            Icon(Icons.Default.PhoneIphone, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Unduh iOS", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Text("Dewan Advokat & Konsultan Senior", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        items(advocateTeam) { adv ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = Color(0xFF991B1B),
                                        shape = CircleShape,
                                        modifier = Modifier.size(46.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(adv.name.take(2), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(adv.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(adv.title, fontSize = 11.sp, color = Color(0xFF991B1B), fontWeight = FontWeight.Medium)
                                        Text("Izin Beracara: ${adv.barLicenseNo}", fontSize = 10.sp, color = Slate500)
                                        Text("Spesialisasi: ${adv.specialization}", fontSize = 10.sp, color = Slate600)
                                        Text("Pengalaman: ${adv.experienceYears} Tahun Menangani Sengketa", fontSize = 9.sp, color = SuccessEmerald, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        // 2. MENU FORMULIR KONSULTASI HUKUM
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = CorporateBlue)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Jadwalkan sesi konsultasi hukum korporasi tatap muka atau virtual bersama Managing Partner.",
                                        fontSize = 11.sp,
                                        color = CorporateBlue
                                    )
                                }
                            }
                        }

                        item {
                            var clientName by remember { mutableStateOf("") }
                            var orgName by remember { mutableStateOf("") }
                            var emailInput by remember { mutableStateOf("") }
                            var phoneInput by remember { mutableStateOf("") }
                            var topicInput by remember { mutableStateOf("Kontrak Kerja Sama & MoU") }
                            var dateInput by remember { mutableStateOf("18 November 2026, 14:00 WIB") }
                            var briefInput by remember { mutableStateOf("") }

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = clientName,
                                    onValueChange = { clientName = it },
                                    label = { Text("Nama Lengkap Pemohon") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = orgName,
                                    onValueChange = { orgName = it },
                                    label = { Text("Perusahaan / Lembaga") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = emailInput,
                                        onValueChange = { emailInput = it },
                                        label = { Text("Email") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    OutlinedTextField(
                                        value = phoneInput,
                                        onValueChange = { phoneInput = it },
                                        label = { Text("No. WhatsApp") },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                OutlinedTextField(
                                    value = topicInput,
                                    onValueChange = { topicInput = it },
                                    label = { Text("Topik Konsultasi (Perizinan, Pajak, Ketenagakerjaan, Sengketa)") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = dateInput,
                                    onValueChange = { dateInput = it },
                                    label = { Text("Pilihan Waktu / Tanggal") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = briefInput,
                                    onValueChange = { briefInput = it },
                                    label = { Text("Uraian Singkat Permasalahan Hukum") },
                                    minLines = 3,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Button(
                                    onClick = {
                                        if (clientName.isNotBlank() && briefInput.isNotBlank()) {
                                            val res = onBookConsultation(clientName, orgName, emailInput, phoneInput, topicInput, dateInput, briefInput)
                                            snackbarMessage = res.second
                                            clientName = ""
                                            briefInput = ""
                                        } else {
                                            snackbarMessage = "Harap isi nama dan uraian singkat masalah hukum."
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF991B1B)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(46.dp)
                                        .testTag("submit_consultation_button")
                                ) {
                                    Text("Jadwalkan Konsultasi Sekarang", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Jadwal Konsultasi Terdaftar:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        items(consultationBookings) { book ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Slate200, RoundedCornerShape(10.dp))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(book.clientName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Surface(color = Color(0xFFDCFCE7), shape = RoundedCornerShape(4.dp)) {
                                            Text(book.status, color = Color(0xFF166534), fontSize = 9.sp, modifier = Modifier.padding(4.dp))
                                        }
                                    }
                                    Text("Topik: ${book.topic} • ${book.preferredDate}", fontSize = 10.sp, color = Slate600)
                                    Text(book.caseBrief, fontSize = 10.sp, color = Slate500)
                                }
                            }
                        }
                    }

                    2 -> {
                        // 3. MENU FORM PENGUNJUNG YANG MEMBUTUHKAN BANTUAN HUKUM (LEGAL AID)
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.VolunteerActivism, contentDescription = null, tint = Color(0xFF991B1B))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Layanan Bantuan Hukum (Pro Bono & Pendampingan Hak Pekerja/Petani Mitra).",
                                        fontSize = 11.sp,
                                        color = Color(0xFF991B1B)
                                    )
                                }
                            }
                        }

                        item {
                            var appName by remember { mutableStateOf("") }
                            var appContact by remember { mutableStateOf("") }
                            var appCat by remember { mutableStateOf("Perlindungan Hak Pekerja / Petani") }
                            var appDesc by remember { mutableStateOf("") }

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = appName,
                                    onValueChange = { appName = it },
                                    label = { Text("Nama Pemohon / Perwakilan Kelompok") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = appContact,
                                    onValueChange = { appContact = it },
                                    label = { Text("Kontak / WhatsApp Aktif") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = appCat,
                                    onValueChange = { appCat = it },
                                    label = { Text("Kategori Bantuan Hukum") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = appDesc,
                                    onValueChange = { appDesc = it },
                                    label = { Text("Deskripsi Kejadian & Kebutuhan Bantuan") },
                                    minLines = 4,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Button(
                                    onClick = {
                                        if (appName.isNotBlank() && appDesc.isNotBlank()) {
                                            val res = onSubmitLegalAid(appName, appContact, appCat, appDesc)
                                            snackbarMessage = res.second
                                            appName = ""
                                            appDesc = ""
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF991B1B)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(46.dp)
                                        .testTag("submit_legal_aid_button")
                                ) {
                                    Text("Kirim Permohonan Bantuan Hukum", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        items(legalAidRequests) { aid ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Slate200, RoundedCornerShape(10.dp))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(aid.applicantName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("Kategori: ${aid.category} • Urgensi: ${aid.urgencyLevel}", fontSize = 10.sp, color = Slate600)
                                    Text(aid.issueDescription, fontSize = 10.sp, color = Slate500)
                                    Text("Status: ${aid.status}", fontSize = 10.sp, color = SuccessEmerald, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    3 -> {
                        // 4. MENU FORM PENGUNJUNG YANG INGIN MENJADI MITRA (LEGAL PARTNERSHIP)
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Handshake, contentDescription = null, tint = SuccessEmerald)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Formulir Pendaftaran Kantor Advokat Rekanan / Konsultan Hukum Wilayah.",
                                        fontSize = 11.sp,
                                        color = Color(0xFF166534)
                                    )
                                }
                            }
                        }

                        item {
                            var partName by remember { mutableStateOf("") }
                            var partOffice by remember { mutableStateOf("") }
                            var partLoc by remember { mutableStateOf("") }
                            var partArea by remember { mutableStateOf("Hukum Pertanahan & Agraria") }
                            var partNote by remember { mutableStateOf("") }

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = partName,
                                    onValueChange = { partName = it },
                                    label = { Text("Nama Advokat / Notaris") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = partOffice,
                                    onValueChange = { partOffice = it },
                                    label = { Text("Nama Kantor Hukum / Law Office") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = partLoc,
                                    onValueChange = { partLoc = it },
                                    label = { Text("Kota / Wilayah Kerja Praktik") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = partArea,
                                    onValueChange = { partArea = it },
                                    label = { Text("Bidang Keahlian Utama") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = partNote,
                                    onValueChange = { partNote = it },
                                    label = { Text("Proposal Kerja Sama / Catatan Rekam Jejak") },
                                    minLines = 3,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Button(
                                    onClick = {
                                        if (partName.isNotBlank() && partOffice.isNotBlank()) {
                                            val res = onSubmitPartnership(partName, partOffice, partLoc, partArea, partNote)
                                            snackbarMessage = res.second
                                            partName = ""
                                            partOffice = ""
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = CorporateBlue),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(46.dp)
                                        .testTag("submit_legal_partner_button")
                                ) {
                                    Text("Ajukan Kemitraan Advokat Rekanan", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        items(legalPartnerships) { lp ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Slate200, RoundedCornerShape(10.dp))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(lp.applicantName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("${lp.firmOrAdvocateOffice} - ${lp.location}", fontSize = 10.sp, color = Slate600)
                                    Text("Bidang: ${lp.expertiseArea}", fontSize = 10.sp, color = Slate500)
                                    Text(lp.proposalNote, fontSize = 10.sp, color = Slate500)
                                    Text("Status: ${lp.status}", fontSize = 10.sp, color = AccentCyan, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    4 -> {
                        // 5. SERTA KOLOM TESTIMONI KLIEN YANG TELAH BERHASIL DIBANTU OLEH FIRMA TERSEBUT
                        item {
                            Text("Testimoni Keberhasilan Penanganan Perkara & Mediasi", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        items(testimonials) { test ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(test.companyName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF991B1B))
                                        Row {
                                            repeat(test.rating) {
                                                Icon(Icons.Default.Star, contentDescription = null, tint = AccentGold, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Perkara: ${test.caseType}", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Slate800)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(test.outcomeSummary, fontSize = 11.sp, color = Slate600, lineHeight = 16.sp)

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Pemberi Testimoni: ${test.clientName}", fontSize = 10.sp, color = Slate500)
                                        Text("Tahun: ${test.year}", fontSize = 10.sp, color = Slate400)
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
