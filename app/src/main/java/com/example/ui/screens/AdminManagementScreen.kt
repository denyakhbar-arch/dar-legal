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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Company
import com.example.data.model.User
import com.example.data.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminManagementScreen(
    currentUser: User,
    users: List<User>,
    company: Company,
    onAppointSecondAdmin: (String) -> Pair<Boolean, String>,
    onRevokeSecondAdmin: (String) -> Pair<Boolean, String>,
    onInviteEmployee: (name: String, email: String, role: UserRole, pos: String, branch: String, phone: String, wage: Long) -> Pair<Boolean, String>,
    onRemoveEmployee: (String) -> Pair<Boolean, String>,
    onToggleInboxAccess: (String, Boolean) -> Pair<Boolean, String>,
    onUpdateEmailRouting: (legalEmail: String, ownerEmail: String, secretaryEmail: String) -> Pair<Boolean, String> = { _, _, _ -> true to "" },
    onBack: () -> Unit
) {
    val superAdmin = users.find { it.role == UserRole.SUPER_ADMIN }
    // Support up to maxAppointedAdmins (Maksimal 2 orang Admin pendamping)
    val appointedAdmins = users.filter { it.role == UserRole.SECOND_ADMIN || it.isAppointedSecondAdmin }

    var showInviteDialog by remember { mutableStateOf(false) }
    var showAppointDialog by remember { mutableStateOf(false) }
    var showEmailConfigDialog by remember { mutableStateOf(false) }

    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    var filterCategory by remember { mutableStateOf("Semua") }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    val isCurrentSuperAdmin = currentUser.role == UserRole.SUPER_ADMIN
    val isCurrentAdminAuthorized = currentUser.role == UserRole.SUPER_ADMIN || currentUser.role == UserRole.SECOND_ADMIN

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Manajemen Admin & Personalia", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                        Text("Struktur Maksimal 2 Admin & Kuasa Penuh Akun", fontSize = 11.sp, color = CorporateDarkBlue)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = DeepExecutiveBlue)
                    }
                },
                actions = {
                    if (isCurrentAdminAuthorized) {
                        IconButton(
                            onClick = { showInviteDialog = true },
                            modifier = Modifier.testTag("invite_employee_header_button")
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = "Undang Karyawan", tint = CorporateBlue)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightBlueBg
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LightBlueBg)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Authority Rule Banner (Maksimal 2 Admin Pendamping)
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = AccentGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Tata Kelola Admin Bertingkat (Maksimal 2 Admin Pendamping)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFF92400E)
                            )
                            Text(
                                text = "Admin Utama berhak menunjuk maksimal 2 orang Admin. Kedua Admin pendamping memiliki kuasa sah untuk mengundang/mengeluarkan karyawan dan mengelola operasional perusahaan.",
                                fontSize = 11.sp,
                                color = Color(0xFFB45309),
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            // 1. Admin Tunggal (Super Admin)
            item {
                Text(
                    text = "1. Pimpinan Pengelola (Admin Utama)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = DeepExecutiveBlue
                )
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = AccentGold,
                                shape = CircleShape,
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Surface(
                                    color = Color(0xFF78350F),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "ADMIN UTAMA / PENGELOLA TUNGGAL",
                                        color = Color(0xFFFDE68A),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    text = superAdmin?.name ?: "Deny Akhbar (Admin Utama)",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = superAdmin?.email ?: company.email,
                                    color = Slate400,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Surface(
                            color = Color(0xFF1E293B),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Kuasa Mutlak",
                                color = AccentGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // 2. Admin Pendamping (Maksimal 2 Orang)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "2. Admin Pendamping (${appointedAdmins.size}/${company.maxAppointedAdmins} Slot Terisi)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = DeepExecutiveBlue
                    )

                    if (isCurrentSuperAdmin && appointedAdmins.size < company.maxAppointedAdmins) {
                        FilledTonalButton(
                            onClick = { showAppointDialog = true },
                            modifier = Modifier
                                .testTag("appoint_second_admin_button")
                                .height(32.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = LightBlueSurface,
                                contentColor = DeepExecutiveBlue
                            )
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tunjuk Admin (${appointedAdmins.size + 1}/2)", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            if (appointedAdmins.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.GroupAdd, contentDescription = null, tint = Slate400, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Belum Ada Admin Pendamping Ditunjuk", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepExecutiveBlue)
                            Text("Admin Utama dapat menunjuk hingga maksimal 2 orang Admin untuk membantu mengelola karyawan.", fontSize = 11.sp, color = Slate500)
                        }
                    }
                }
            } else {
                items(appointedAdmins.size) { idx ->
                    val admin = appointedAdmins[idx]
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFBAE6FD), RoundedCornerShape(12.dp))
                            .testTag("appointed_admin_card_${admin.id}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = CorporateBlue,
                                        shape = CircleShape,
                                        modifier = Modifier.size(38.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Surface(
                                            color = Color(0xFFE0F2FE),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "ADMIN PENDAMPING #${idx + 1} (DITUNJUK OLEH ADMIN UTAMA)",
                                                color = Color(0xFF0369A1),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Text(
                                            text = admin.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = DeepExecutiveBlue
                                        )
                                        Text(
                                            text = "${admin.position} • ${admin.email}",
                                            color = Slate600,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                if (isCurrentSuperAdmin) {
                                    OutlinedButton(
                                        onClick = {
                                            val result = onRevokeSecondAdmin(admin.id)
                                            snackbarMessage = result.second
                                        },
                                        modifier = Modifier.height(30.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text("Cabut", fontSize = 10.sp, color = ErrorRose, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(color = LightBlueSurface, shape = RoundedCornerShape(6.dp), modifier = Modifier.fillMaxWidth()) {
                                Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = SuccessEmerald, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Kuasa Sah: Mengundang / Menghapus Karyawan, Menyetujui Presensi & Membaca Pesan Masuk", fontSize = 9.sp, color = CorporateDarkBlue)
                                }
                            }
                        }
                    }
                }
            }

            // ========================================================
            // 3. DUA JALUR EMAIL RESMI PERUSAHAAN
            // ========================================================
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "3. Dua Jalur Email Resmi Perusahaan",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = DeepExecutiveBlue
                    )

                    if (isCurrentAdminAuthorized) {
                        TextButton(
                            onClick = { showEmailConfigDialog = true },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ubah Jalur Email", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Jalur 1: Email Konsultan Hukum Perusahaan
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFFECDD3), RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(color = Color(0xFFBE123C), shape = CircleShape, modifier = Modifier.size(34.dp)) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Gavel, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Surface(color = Color(0xFFFFF1F2), shape = RoundedCornerShape(4.dp)) {
                                    Text("JALUR 1: EMAIL KONSULTAN HUKUM PERUSAHAAN", color = Color(0xFF9F1239), fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                                Text(company.legalConsultantEmail, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepExecutiveBlue)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Menerima: Notifikasi konsultasi hukum perdata/bisnis, permohonan bantuan hukum, draf klausul arbitrase MoU kemitraan, dan perkara advokasi.",
                            fontSize = 11.sp,
                            color = Slate600,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // Jalur 2: Email Pemilik & Sekretaris Perusahaan
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFBAE6FD), RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(color = CorporateBlue, shape = CircleShape, modifier = Modifier.size(34.dp)) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Apartment, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Surface(color = LightBlueSurface, shape = RoundedCornerShape(4.dp)) {
                                    Text("JALUR 2: EMAIL OPERASIONAL PEMILIK & SEKRETARIS", color = CorporateDarkBlue, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                                Text("Pemilik: ${company.ownerEmail}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepExecutiveBlue)
                                Text("Sekretaris: ${company.secretaryEmail}", fontSize = 11.sp, color = Slate700)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Menerima: Surat dinas kementerian/pemerintah, penawaran harga dan pesanan supplier/buyer, rekap absensi, serta bukti potong pajak e-Billing payroll.",
                            fontSize = 11.sp,
                            color = Slate600,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // 4. Employee Directory Header & Filter
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "4. Daftar Seluruh Personel (${users.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = DeepExecutiveBlue
                    )
                    Text("PT: ${company.code}", fontSize = 11.sp, color = CorporateBlue, fontWeight = FontWeight.Bold)
                }
            }

            // Filter Chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Semua", "Tetap", "Harian/Borongan", "Direksi").forEach { cat ->
                        FilterChip(
                            selected = filterCategory == cat,
                            onClick = { filterCategory = cat },
                            label = { Text(cat, fontSize = 10.sp) },
                            modifier = Modifier.testTag("filter_staff_$cat")
                        )
                    }
                }
            }

            val filteredUsers = when (filterCategory) {
                "Tetap" -> users.filter { it.role == UserRole.KARYAWAN_TETAP }
                "Harian/Borongan" -> users.filter { it.role == UserRole.PEKERJA_HARIAN_BORONGAN }
                "Direksi" -> users.filter { it.role == UserRole.SUPER_ADMIN || it.role == UserRole.SECOND_ADMIN || it.role == UserRole.PEJABAT_TINGGI }
                else -> users
            }

            items(filteredUsers) { emp ->
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Surface(
                                color = Color(emp.role.badgeColorHex),
                                shape = CircleShape,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(emp.avatarInitials, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(emp.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepExecutiveBlue)
                                Text("${emp.role.label} • ${emp.position}", fontSize = 11.sp, color = Color(emp.role.badgeColorHex), fontWeight = FontWeight.Medium)
                                Text("${emp.email} • ${emp.phone}", fontSize = 10.sp, color = Slate500)
                            }
                        }

                        if (isCurrentAdminAuthorized && emp.role != UserRole.SUPER_ADMIN) {
                            IconButton(
                                onClick = {
                                    val res = onRemoveEmployee(emp.id)
                                    snackbarMessage = res.second
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.PersonRemove, contentDescription = "Keluarkan", tint = ErrorRose, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // DIALOG: CONFIG EMAIL ROUTING
    if (showEmailConfigDialog) {
        var legalEmailInput by remember { mutableStateOf(company.legalConsultantEmail) }
        var ownerEmailInput by remember { mutableStateOf(company.ownerEmail) }
        var secretaryEmailInput by remember { mutableStateOf(company.secretaryEmail) }

        AlertDialog(
            onDismissRequest = { showEmailConfigDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MailOutline, contentDescription = null, tint = CorporateBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pengaturan Dua Jalur Email Perusahaan", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Pisahkan email masuk antara Konsultan Hukum dengan Manajemen Resmi Pemilik & Sekretaris Perusahaan.",
                        fontSize = 11.sp,
                        color = Slate600
                    )

                    OutlinedTextField(
                        value = legalEmailInput,
                        onValueChange = { legalEmailInput = it },
                        label = { Text("Jalur 1: Email Konsultan Hukum") },
                        supportingText = { Text("Menerima sengketa, legalitas & MoU (Deny Akhbar)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = ownerEmailInput,
                        onValueChange = { ownerEmailInput = it },
                        label = { Text("Jalur 2A: Email Pemilik Perusahaan") },
                        supportingText = { Text("Menerima surat dinas eksekutif & kuota bisnis") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = secretaryEmailInput,
                        onValueChange = { secretaryEmailInput = it },
                        label = { Text("Jalur 2B: Email Sekretaris Perusahaan") },
                        supportingText = { Text("Menerima rekap operasional & perpajakan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val res = onUpdateEmailRouting(legalEmailInput, ownerEmailInput, secretaryEmailInput)
                        snackbarMessage = res.second
                        showEmailConfigDialog = false
                    }
                ) {
                    Text("Simpan Konfigurasi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEmailConfigDialog = false }) { Text("Batal") }
            }
        )
    }

    // Appoint Second Admin Dialog (Up to 2 Appointed Admins)
    if (showAppointDialog) {
        val eligibleCandidates = users.filter {
            it.role != UserRole.SUPER_ADMIN &&
            it.role != UserRole.SECOND_ADMIN &&
            !it.isAppointedSecondAdmin
        }

        AlertDialog(
            onDismissRequest = { showAppointDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.HowToReg, contentDescription = null, tint = CorporateBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tunjuk Admin Pendamping (Slot ${appointedAdmins.size + 1}/2)", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Pilih salah satu pejabat atau staf untuk diangkat menjadi Admin Pendamping (${company.name}). Maksimal 2 Admin pendamping.",
                        fontSize = 11.sp,
                        color = Slate600
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (eligibleCandidates.isEmpty()) {
                        Text("Tidak ada calon yang memenuhi syarat untuk ditunjuk.", fontSize = 11.sp, color = Slate500)
                    } else {
                        eligibleCandidates.forEach { candidate ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, LightBlueBorder, RoundedCornerShape(8.dp))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(candidate.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepExecutiveBlue)
                                        Text("${candidate.role.label} • ${candidate.position}", fontSize = 10.sp, color = Slate600)
                                    }
                                    Button(
                                        onClick = {
                                            val res = onAppointSecondAdmin(candidate.id)
                                            snackbarMessage = res.second
                                            showAppointDialog = false
                                        },
                                        modifier = Modifier.height(30.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text("Tunjuk", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAppointDialog = false }) { Text("Tutup") }
            }
        )
    }

    // Invite Employee Dialog
    if (showInviteDialog) {
        var newName by remember { mutableStateOf("") }
        var newEmail by remember { mutableStateOf("") }
        var newPos by remember { mutableStateOf("") }
        var newPhone by remember { mutableStateOf("") }
        var selectedRole by remember { mutableStateOf(UserRole.KARYAWAN_TETAP) }
        var selectedBranchId by remember { mutableStateOf(company.branches.firstOrNull()?.id ?: "br_jkt") }
        var wageInput by remember { mutableStateOf("7500000") }

        AlertDialog(
            onDismissRequest = { showInviteDialog = false },
            title = { Text("Undang Personel Baru ke ${company.name}") },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.heightIn(max = 400.dp)) {
                    item {
                        OutlinedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("Nama Lengkap Karyawan") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = newEmail,
                            onValueChange = { newEmail = it },
                            label = { Text("Email Akun Google") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = newPos,
                            onValueChange = { newPos = it },
                            label = { Text("Jabatan / Posisi") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = newPhone,
                            onValueChange = { newPhone = it },
                            label = { Text("Nomor WhatsApp/Telepon") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        Text("Klasifikasi Tenaga Kerja:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(
                                selected = selectedRole == UserRole.KARYAWAN_TETAP,
                                onClick = {
                                    selectedRole = UserRole.KARYAWAN_TETAP
                                    wageInput = "7500000"
                                },
                                label = { Text("Karyawan Tetap", fontSize = 10.sp) }
                            )
                            FilterChip(
                                selected = selectedRole == UserRole.PEKERJA_HARIAN_BORONGAN,
                                onClick = {
                                    selectedRole = UserRole.PEKERJA_HARIAN_BORONGAN
                                    wageInput = "175000"
                                },
                                label = { Text("Harian/Borongan", fontSize = 10.sp) }
                            )
                        }
                    }
                    item {
                        OutlinedTextField(
                            value = wageInput,
                            onValueChange = { wageInput = it },
                            label = { Text(if (selectedRole == UserRole.KARYAWAN_TETAP) "Gaji Bulanan (Rp)" else "Upah Harian / Borongan (Rp)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newName.isNotBlank() && newEmail.isNotBlank()) {
                            val res = onInviteEmployee(
                                newName,
                                newEmail,
                                selectedRole,
                                newPos.ifBlank { "Staf Operasional" },
                                selectedBranchId,
                                newPhone.ifBlank { "+62 812-0000-0000" },
                                wageInput.toLongOrNull() ?: 5000000L
                            )
                            snackbarMessage = res.second
                            showInviteDialog = false
                        }
                    },
                    modifier = Modifier.testTag("submit_invite_user_button")
                ) {
                    Text("Kirim Undangan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showInviteDialog = false }) { Text("Batal") }
            }
        )
    }
}
