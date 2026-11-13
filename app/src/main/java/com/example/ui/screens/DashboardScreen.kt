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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    currentCompany: Company,
    companies: List<Company>,
    currentUser: User,
    users: List<User>,
    calendarEvents: List<GoogleCalendarEvent> = emptyList(),
    driveDocs: List<GoogleDriveDocItem> = emptyList(),
    onSelectCompany: (String) -> Unit,
    onSelectUser: (String) -> Unit,
    isSuperAdminVerified: Boolean = false,
    onAuthenticateSuperAdmin: (String, String) -> Pair<Boolean, String> = { _, _ -> true to "" },
    onUpdateSuperAdminPassword: (String, String) -> Pair<Boolean, String> = { _, _ -> true to "" },
    onNavigate: (String) -> Unit,
    onOpenLegalPortal: () -> Unit,
    onOpenCustomMenu: (CustomCompanyMenu) -> Unit,
    onUpdateDashboardConfig: (DashboardCustomizerConfig) -> Pair<Boolean, String> = { true to "" },
    onUpdateCompanyLogo: (String?, LogoPosition) -> Pair<Boolean, String> = { _, _ -> true to "" },
    onUpdateBroadcastingAnnouncement: (String, String, Boolean) -> Pair<Boolean, String> = { _, _, _ -> true to "" },
    onAddCalendarEvent: (title: String, date: String, time: String, loc: String, meetUrl: String) -> Pair<Boolean, String> = { _, _, _, _, _ -> true to "" }
) {
    var showCompanyDialog by remember { mutableStateOf(false) }
    var showUserSwitcherDialog by remember { mutableStateOf(false) }
    var showSuperAdminAuthDialog by remember { mutableStateOf(false) }
    var showDashboardCustomizerDialog by remember { mutableStateOf(false) }
    var showWorkspaceDetailDialog by remember { mutableStateOf<String?>(null) } // "calendar", "drive", "gmail", "meet", "docs"
    var showAddEventDialog by remember { mutableStateOf(false) }

    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    val cfg = currentCompany.featuresConfig
    val dConfig = currentCompany.dashboardConfig
    val isAdmin = currentUser.role == UserRole.SUPER_ADMIN || currentUser.role == UserRole.SECOND_ADMIN

    // Photo picker launcher for company logo from mobile device
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val res = onUpdateCompanyLogo(uri.toString(), dConfig.logoPosition)
            snackbarMessage = "Logo perusahaan berhasil diperbarui dari perangkat: $uri"
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = LightBlueBg
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LightBlueBg)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ========================================================
            // 1. DYNAMIC COMPANY LOGO & HEADER CARD (ADAPTS TO LOGO POSITION)
            // ========================================================
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, LightBlueBorder, RoundedCornerShape(16.dp))
                        .testTag("dashboard_header_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Quick switch PT & Customizer button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "DAR MULTICORP • RUANG KERJA TERISOLASI",
                                color = CorporateDarkBlue,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (isAdmin) {
                                    FilledTonalButton(
                                        onClick = { showDashboardCustomizerDialog = true },
                                        modifier = Modifier
                                            .testTag("btn_customize_dashboard")
                                            .height(30.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = Color(0xFFFEF3C7),
                                            contentColor = Color(0xFF92400E)
                                        )
                                    ) {
                                        Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(13.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Kustom Laman", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                FilledTonalButton(
                                    onClick = { showCompanyDialog = true },
                                    modifier = Modifier
                                        .testTag("switch_company_button")
                                        .height(30.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = LightBlueSurface,
                                        contentColor = DeepExecutiveBlue
                                    )
                                ) {
                                    Text("Ganti PT", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Logo Rendering based on Position (LEFT, CENTER, RIGHT, BANNER_TOP)
                        when (dConfig.logoPosition) {
                            LogoPosition.BANNER_TOP -> {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(currentCompany.primaryAccentColorHex)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(70.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Apartment, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = currentCompany.name.uppercase(),
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                letterSpacing = 1.sp
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = currentCompany.sector,
                                    fontSize = 12.sp,
                                    color = Slate600
                                )
                            }
                            LogoPosition.CENTER -> {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Surface(
                                        color = Color(currentCompany.primaryAccentColorHex),
                                        shape = CircleShape,
                                        modifier = Modifier.size(54.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                Icons.Default.Apartment,
                                                contentDescription = "Logo",
                                                tint = Color.White,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = currentCompany.name,
                                        color = DeepExecutiveBlue,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = currentCompany.sector,
                                        fontSize = 11.sp,
                                        color = Slate600,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            LogoPosition.RIGHT -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = currentCompany.name,
                                            color = DeepExecutiveBlue,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = currentCompany.sector,
                                            fontSize = 11.sp,
                                            color = Slate600
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Surface(
                                        color = Color(currentCompany.primaryAccentColorHex),
                                        shape = CircleShape,
                                        modifier = Modifier.size(46.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                Icons.Default.Apartment,
                                                contentDescription = "Logo",
                                                tint = Color.White,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            LogoPosition.LEFT -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = Color(currentCompany.primaryAccentColorHex),
                                        shape = CircleShape,
                                        modifier = Modifier.size(46.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                Icons.Default.Apartment,
                                                contentDescription = "Logo",
                                                tint = Color.White,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = currentCompany.name,
                                            color = DeepExecutiveBlue,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = currentCompany.sector,
                                            fontSize = 11.sp,
                                            color = Slate600
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Kode: ${currentCompany.code} • ${currentCompany.branches.size} Cabang • ${users.size} Personel",
                                color = Slate600,
                                fontSize = 11.sp
                            )
                            Surface(
                                color = Color(0xFFDCFCE7),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Ruang Kerja Khusus",
                                    color = Color(0xFF166534),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // ========================================================
            // 2. LIVE BROADCASTING MESSAGE CARD (FOR ALL TEAM MEMBERS)
            // ========================================================
            if (dConfig.isBroadcastingActive && dConfig.showBroadcastingWidget) {
                val isUrgent = dConfig.broadcastingUrgency == "DARURAT"
                val isImportant = dConfig.broadcastingUrgency == "PENTING"
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                isUrgent -> Color(0xFFFEE2E2)
                                isImportant -> Color(0xFFFEF3C7)
                                else -> Color(0xFFE0F2FE)
                            }
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                when {
                                    isUrgent -> Color(0xFFF87171)
                                    isImportant -> Color(0xFFFCD34D)
                                    else -> Color(0xFF7DD3FC)
                                },
                                RoundedCornerShape(14.dp)
                            )
                            .testTag("broadcasting_banner_card")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Campaign,
                                        contentDescription = "Siaran",
                                        tint = when {
                                            isUrgent -> Color(0xFFDC2626)
                                            isImportant -> Color(0xFFD97706)
                                            else -> CorporateBlue
                                        },
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "SIARAN RESMI (BROADCASTING TIM)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when {
                                            isUrgent -> Color(0xFF991B1B)
                                            isImportant -> Color(0xFF92400E)
                                            else -> DeepExecutiveBlue
                                        }
                                    )
                                }

                                Surface(
                                    color = when {
                                        isUrgent -> Color(0xFFDC2626)
                                        isImportant -> Color(0xFFD97706)
                                        else -> CorporateBlue
                                    },
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = dConfig.broadcastingUrgency,
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = dConfig.broadcastingMessage,
                                fontSize = 12.sp,
                                color = DeepExecutiveBlue,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Pengirim: ${dConfig.broadcastingSender}",
                                    fontSize = 10.sp,
                                    color = Slate600
                                )
                                Text(
                                    text = "Dilihat oleh seluruh anggota",
                                    fontSize = 10.sp,
                                    color = Slate500
                                )
                            }
                        }
                    }
                }
            }

            // ========================================================
            // 3. GOOGLE SIGN-IN & CURRENT USER PROFILE BANNER
            // ========================================================
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, LightBlueBorder, RoundedCornerShape(14.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                color = Color(currentUser.role.badgeColorHex),
                                shape = CircleShape,
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = currentUser.avatarInitials,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = currentUser.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = DeepExecutiveBlue
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.CheckCircle, contentDescription = "Verified Google Account", tint = CorporateBlue, modifier = Modifier.size(14.dp))
                                }
                                Text(
                                    text = "${currentUser.role.label} • ${currentUser.position}",
                                    fontSize = 11.sp,
                                    color = Slate600
                                )
                                Text(
                                    text = currentUser.email,
                                    fontSize = 10.sp,
                                    color = Slate400
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            OutlinedButton(
                                onClick = { showUserSwitcherDialog = true },
                                modifier = Modifier
                                    .testTag("switch_user_role_button")
                                    .height(32.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Ganti Akun", fontSize = 10.sp)
                            }
                            FilledTonalButton(
                                onClick = { showSuperAdminAuthDialog = true },
                                modifier = Modifier
                                    .testTag("btn_verify_super_admin")
                                    .height(30.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = if (isSuperAdminVerified) Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
                                    contentColor = if (isSuperAdminVerified) Color(0xFF166534) else Color(0xFF92400E)
                                )
                            ) {
                                Icon(
                                    if (isSuperAdminVerified) Icons.Default.VerifiedUser else Icons.Default.Lock,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    if (isSuperAdminVerified) "Admin Utama Sah" else "Sandi Admin Utama",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // ========================================================
            // 4. GOOGLE WORKSPACE ENTERPRISE HUB (AUTHENTICATED VIA OAUTH)
            // ========================================================
            if (cfg.enableGoogleWorkspaceSuite && dConfig.showGoogleWorkspaceHub) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFBAE6FD), RoundedCornerShape(14.dp))
                            .testTag("google_workspace_hub_card")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = Color(0xFF0284C7),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.Cloud, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            "GOOGLE WORKSPACE CLOUD HUB",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = DeepExecutiveBlue
                                        )
                                        Text("Terotorisasi Google Cloud OAuth2", fontSize = 10.sp, color = CorporateDarkBlue)
                                    }
                                }
                                Surface(
                                    color = Color(0xFFDCFCE7),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text("Aktif & Siap", color = Color(0xFF166534), fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Workspace Grid Buttons
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                WorkspaceToolButton(
                                    icon = Icons.Default.DynamicForm,
                                    label = "Google Forms",
                                    desc = "Mitra Bisnis",
                                    color = Color(0xFF7C3AED),
                                    modifier = Modifier.weight(1f),
                                    onClick = { onNavigate("supplier_buyer") }
                                )
                                WorkspaceToolButton(
                                    icon = Icons.Default.Mail,
                                    label = "Gmail Resmi",
                                    desc = "Surat Menyurat",
                                    color = Color(0xFFEA4335),
                                    modifier = Modifier.weight(1f),
                                    onClick = { showWorkspaceDetailDialog = "gmail" }
                                )
                                WorkspaceToolButton(
                                    icon = Icons.Default.Article,
                                    label = "Google Docs",
                                    desc = "MoU & Dokumen",
                                    color = Color(0xFF4285F4),
                                    modifier = Modifier.weight(1f),
                                    onClick = { showWorkspaceDetailDialog = "docs" }
                                )
                                WorkspaceToolButton(
                                    icon = Icons.Default.Event,
                                    label = "Kalender",
                                    desc = "Acuan Waktu",
                                    color = Color(0xFF0F9D58),
                                    modifier = Modifier.weight(1f),
                                    onClick = { showWorkspaceDetailDialog = "calendar" }
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                WorkspaceToolButton(
                                    icon = Icons.Default.AddLocationAlt,
                                    label = "Google Maps",
                                    desc = "Lokasi Geofence",
                                    color = Color(0xFF34A853),
                                    modifier = Modifier.weight(1f),
                                    onClick = { onNavigate("attendance_gps") }
                                )
                                WorkspaceToolButton(
                                    icon = Icons.Default.Forum,
                                    label = "Google Chat",
                                    desc = "Obrolan Cepat",
                                    color = Color(0xFF00AC47),
                                    modifier = Modifier.weight(1f),
                                    onClick = { onNavigate("communication") }
                                )
                                WorkspaceToolButton(
                                    icon = Icons.Default.VideoCall,
                                    label = "Google Meet",
                                    desc = "Rapat Online",
                                    color = Color(0xFF00897B),
                                    modifier = Modifier.weight(1f),
                                    onClick = { showWorkspaceDetailDialog = "meet" }
                                )
                                WorkspaceToolButton(
                                    icon = Icons.Default.CloudSync,
                                    label = "Google Drive",
                                    desc = "Penyimpanan",
                                    color = Color(0xFFFFBA00),
                                    modifier = Modifier.weight(1f),
                                    onClick = { showWorkspaceDetailDialog = "drive" }
                                )
                            }
                        }
                    }
                }
            }

            // ========================================================
            // 5. PROMINENT PUSAT PENGATURAN CARD
            // ========================================================
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2FE)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF7DD3FC), RoundedCornerShape(14.dp))
                        .clickable { onNavigate("settings") }
                        .testTag("settings_dashboard_entry_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Surface(
                                color = CorporateBlue,
                                shape = CircleShape,
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    "MENU PENGATURAN APLIKASI & PERUSAHAAN",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = DeepExecutiveBlue
                                )
                                Text(
                                    "Fitur Modul • Struktur Pejabat • Toko Tema & Plugin • Menu Kustom",
                                    fontSize = 10.sp,
                                    color = CorporateDarkBlue
                                )
                            }
                        }

                        Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = CorporateBlue, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // ========================================================
            // 6. LEGAL CONSULTANT CARD
            // ========================================================
            if (cfg.enableLegalConsultant) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenLegalPortal() }
                            .testTag("front_legal_consultant_card")
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF881337), Color(0xFF9F1239), Color(0xFFBE123C))
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            color = Color.White.copy(alpha = 0.2f),
                                            shape = CircleShape,
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    Icons.Default.Gavel,
                                                    contentDescription = "Hukum",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = "PAYUNG HUKUM RESMI PERUSAHAAN",
                                                color = Color(0xFFFECDD3),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 1.sp
                                            )
                                            Text(
                                                text = "KONSULTAN HUKUM PERUSAHAAN",
                                                color = Color.White,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    FilledTonalButton(
                                        onClick = onOpenLegalPortal,
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = Color.White,
                                            contentColor = Color(0xFF9F1239)
                                        ),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                        modifier = Modifier.height(30.dp)
                                    ) {
                                        Text("Buka Laman", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Profil Advokat Resmi • Formulir Konsultasi • Permohonan Bantuan Hukum • Kemitraan • Testimoni Klien Berhasil",
                                    color = Color(0xFFFEE2E2),
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }

            // ========================================================
            // 7. GPS JAM KERJA STATUS CARD
            // ========================================================
            if (cfg.enableAttendanceGps && cfg.enableWorkHoursOnlyGps && dConfig.showGpsWidget) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = "GPS Active",
                                tint = SuccessEmerald,
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Deteksi Lokasi GPS Aktif (Hanya Selama Jam Kerja)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color(0xFF14532D)
                                )
                                Text(
                                    text = "08:00 - 17:00 WIB • Privasi di luar jam kerja terproteksi 100% tanpa pelacakan.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF166534)
                                )
                            }
                        }
                    }
                }
            }

            // ========================================================
            // 8. CUSTOM MENUS (CREATED BY ADMIN FOR THIS COMPANY)
            // ========================================================
            if (currentCompany.customMenus.isNotEmpty() && dConfig.showCustomMenusWidget) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Menu & Dokumentasi Kustom (${currentCompany.customMenus.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DeepExecutiveBlue
                        )
                        TextButton(onClick = { onNavigate("settings") }) {
                            Text("Kelola di Pengaturan", fontSize = 11.sp)
                        }
                    }
                }

                items(currentCompany.customMenus.size) { idx ->
                    val cMenu = currentCompany.customMenus[idx]
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                            .clickable { onOpenCustomMenu(cMenu) }
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
                                    color = LightBlueSurface,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = CorporateBlue, modifier = Modifier.size(20.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Surface(
                                        color = Color(0xFFEFF6FF),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(cMenu.categoryTag, color = CorporateDarkBlue, fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                    }
                                    Text(cMenu.menuTitle, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepExecutiveBlue)
                                    Text(cMenu.subMenuTitle, fontSize = 11.sp, color = Slate600)
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = CorporateBlue)
                        }
                    }
                }
            }

            // ========================================================
            // 9. CORE ENTERPRISE MODULE NAVIGATION CARDS
            // ========================================================
            item {
                Text(
                    text = "Modul Operasional & Layanan Perusahaan",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepExecutiveBlue
                )
            }

            // Row 1: Admin & Presensi
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ModuleCard(
                        title = "Admin & Personalia",
                        subtitle = "2-Tier Admin & Kuasa Kelola",
                        badge = "Super & Admin 2",
                        icon = Icons.Default.AdminPanelSettings,
                        accentColor = AccentGold,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("admin_mgmt") }
                    )
                    if (cfg.enableAttendanceGps) {
                        ModuleCard(
                            title = "Presensi & GPS",
                            subtitle = "Waktu Masuk & Pulang",
                            badge = "Selfie & Geofence",
                            icon = Icons.Default.Fingerprint,
                            accentColor = SuccessEmerald,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigate("attendance_gps") }
                        )
                    }
                }
            }

            // Row 2: Laporan Harian & Payroll
            if (cfg.enableDailyReports || cfg.enablePayroll) {
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (cfg.enableDailyReports) {
                            ModuleCard(
                                title = "Laporan Harian",
                                subtitle = "Aktivitas & Berkas Dokumen",
                                badge = "Unduh & Bersihkan",
                                icon = Icons.Default.Assignment,
                                accentColor = AccentCyan,
                                modifier = Modifier.weight(1f),
                                onClick = { onNavigate("daily_reports") }
                            )
                        }
                        if (cfg.enablePayroll) {
                            ModuleCard(
                                title = "Payroll Otomatis",
                                subtitle = "Tetap vs Harian/Borongan",
                                badge = "Midtrans & Xendit",
                                icon = Icons.Default.Payments,
                                accentColor = Color(0xFF7C3AED),
                                modifier = Modifier.weight(1f),
                                onClick = { onNavigate("payroll") }
                            )
                        }
                    }
                }
            }

            // Row 3: Komunikasi & Supplier Portal
            if (cfg.enableCommunications || cfg.enableSupplierBuyerPortal) {
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (cfg.enableCommunications) {
                            ModuleCard(
                                title = "Komunikasi & Rapat",
                                subtitle = "Direksi, Cabang, Voice & Video",
                                badge = "Grup Terpisah",
                                icon = Icons.Default.VideoCall,
                                accentColor = Color(0xFF0284C7),
                                modifier = Modifier.weight(1f),
                                onClick = { onNavigate("communication") }
                            )
                        }
                        if (cfg.enableSupplierBuyerPortal) {
                            ModuleCard(
                                title = "Portal Supplier & Buyer",
                                subtitle = "Google Forms, MoU & KTP",
                                badge = "Verifikasi Wajah",
                                icon = Icons.Default.Handshake,
                                accentColor = Color(0xFFEA580C),
                                modifier = Modifier.weight(1f),
                                onClick = { onNavigate("supplier_buyer") }
                            )
                        }
                    }
                }
            }

            // Row 4: Marketing & Web Browser
            if (cfg.enableProductCatalog || cfg.enableCompanyNewsAds) {
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        ModuleCard(
                            title = "Katalog & Pemasaran",
                            subtitle = "Spesifikasi & Harga per Kg",
                            badge = "Iklan & Feed",
                            icon = Icons.Default.Storefront,
                            accentColor = Color(0xFF0D9488),
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigate("marketing_catalog") }
                        )
                        ModuleCard(
                            title = "Portal Web & Vercel",
                            subtitle = "Layanan Hukum & Unduh APK",
                            badge = "Online Vercel",
                            icon = Icons.Default.Public,
                            accentColor = CorporateBlue,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigate("web_browser_preview") }
                        )
                    }
                }
            }
        }
    }

    // ========================================================
    // DIALOGS & CUSTOMIZATION EDITORS
    // ========================================================

    // 1. DASHBOARD CUSTOMIZER DIALOG (Admin Only)
    if (showDashboardCustomizerDialog) {
        var editPosition by remember { mutableStateOf(dConfig.logoPosition) }
        var editBroadcastingActive by remember { mutableStateOf(dConfig.isBroadcastingActive) }
        var editBroadcastingMsg by remember { mutableStateOf(dConfig.broadcastingMessage) }
        var editBroadcastingUrgency by remember { mutableStateOf(dConfig.broadcastingUrgency) }
        var editShowBroadcasting by remember { mutableStateOf(dConfig.showBroadcastingWidget) }
        var editShowGps by remember { mutableStateOf(dConfig.showGpsWidget) }
        var editShowHub by remember { mutableStateOf(dConfig.showGoogleWorkspaceHub) }
        var editShowCustomMenu by remember { mutableStateOf(dConfig.showCustomMenusWidget) }

        AlertDialog(
            onDismissRequest = { showDashboardCustomizerDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Tune, contentDescription = null, tint = CorporateBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Kustomisasi Tampilan Beranda", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.heightIn(max = 420.dp)
                ) {
                    // Upload Logo section
                    item {
                        Text("1. Logo Perusahaan dari Ponsel:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(if (dConfig.logoUri != null) "Logo Terpasang" else "Pakai Ikon Bawaan", fontSize = 11.sp, color = Slate600)
                                Text("Pilih gambar dari galeri HP", fontSize = 10.sp, color = Slate400)
                            }
                            Button(
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Pilih Foto", fontSize = 11.sp)
                            }
                        }
                    }

                    // Logo Position
                    item {
                        Text("2. Posisi Tampilan Logo:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            LogoPosition.values().forEach { pos ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { editPosition = pos },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(selected = editPosition == pos, onClick = { editPosition = pos })
                                    Text(pos.label, fontSize = 11.sp, color = DeepExecutiveBlue)
                                }
                            }
                        }
                    }

                    // Broadcasting Announcement Settings
                    item {
                        Text("3. Pesan Siaran (Broadcasting) Seluruh Tim:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Aktifkan Siaran", fontSize = 11.sp)
                            Switch(checked = editBroadcastingActive, onCheckedChange = { editBroadcastingActive = it })
                        }
                        if (editBroadcastingActive) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("DARURAT", "PENTING", "INFO").forEach { urg ->
                                    FilterChip(
                                        selected = editBroadcastingUrgency == urg,
                                        onClick = { editBroadcastingUrgency = urg },
                                        label = { Text(urg, fontSize = 10.sp) }
                                    )
                                }
                            }
                            OutlinedTextField(
                                value = editBroadcastingMsg,
                                onValueChange = { editBroadcastingMsg = it },
                                label = { Text("Teks Pesan Siaran") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2
                            )
                        }
                    }

                    // Widget Toggles
                    item {
                        Text("4. Visibilitas Widget Beranda:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Widget Siaran Broadcasting", fontSize = 11.sp)
                            Switch(checked = editShowBroadcasting, onCheckedChange = { editShowBroadcasting = it })
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Widget Google Workspace Hub", fontSize = 11.sp)
                            Switch(checked = editShowHub, onCheckedChange = { editShowHub = it })
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Widget Status GPS Jam Kerja", fontSize = 11.sp)
                            Switch(checked = editShowGps, onCheckedChange = { editShowGps = it })
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Widget Menu Kustom Perusahaan", fontSize = 11.sp)
                            Switch(checked = editShowCustomMenu, onCheckedChange = { editShowCustomMenu = it })
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newCfg = dConfig.copy(
                            logoPosition = editPosition,
                            isBroadcastingActive = editBroadcastingActive,
                            broadcastingMessage = editBroadcastingMsg,
                            broadcastingUrgency = editBroadcastingUrgency,
                            showBroadcastingWidget = editShowBroadcasting,
                            showGoogleWorkspaceHub = editShowHub,
                            showGpsWidget = editShowGps,
                            showCustomMenusWidget = editShowCustomMenu
                        )
                        val res = onUpdateDashboardConfig(newCfg)
                        snackbarMessage = res.second
                        showDashboardCustomizerDialog = false
                    }
                ) {
                    Text("Terapkan Tata Letak")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDashboardCustomizerDialog = false }) { Text("Batal") }
            }
        )
    }

    // 2. GOOGLE WORKSPACE DETAILS DIALOG (Gmail, Calendar, Docs, Drive, Meet)
    if (showWorkspaceDetailDialog != null) {
        val tool = showWorkspaceDetailDialog!!
        AlertDialog(
            onDismissRequest = { showWorkspaceDetailDialog = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        when (tool) {
                            "gmail" -> Icons.Default.Mail
                            "calendar" -> Icons.Default.Event
                            "drive" -> Icons.Default.CloudSync
                            "meet" -> Icons.Default.VideoCall
                            else -> Icons.Default.Article
                        },
                        contentDescription = null,
                        tint = CorporateBlue
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        when (tool) {
                            "gmail" -> "Google Mail (Gmail Resmi Perusahaan)"
                            "calendar" -> "Google Kalender & Jadwal Rapat"
                            "drive" -> "Google Drive Cloud Storage"
                            "meet" -> "Google Meet Konferensi Jarak Jauh"
                            else -> "Google Dokumen MoU & Kontrak"
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    when (tool) {
                        "gmail" -> {
                            Text("Akun Terautentikasi: ${currentUser.email}", fontSize = 11.sp, color = CorporateDarkBlue, fontWeight = FontWeight.Bold)
                            Text("Dukungan surat menyurat elektronik resmi perusahaan terhubung dengan server Google Mail. Setiap berita acara dan permohonan kerjasama dapat langsung dikirimkan dengan identitas korporat.", fontSize = 11.sp, color = Slate600)
                            Surface(color = LightBlueSurface, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Kotak Masuk Terkini: 3 Surat Dinas", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                                    Text("• [Kementerian Pertanian] Undangan Sosialisasi Regulasi Ekspor 2026", fontSize = 10.sp, color = Slate700)
                                    Text("• [PT Samudera Logistik] Konfirmasi Sandar Kontainer Kopi", fontSize = 10.sp, color = Slate700)
                                }
                            }
                        }
                        "calendar" -> {
                            Text("Sinkronisasi Waktu Nyata Google Calendar", fontSize = 11.sp, color = CorporateDarkBlue, fontWeight = FontWeight.Bold)
                            Text("Acuan waktu jadwal rapat direksi, apel siaga cabang, dan audit mutu lab terhubung secara real-time.", fontSize = 11.sp, color = Slate600)
                            if (calendarEvents.isNotEmpty()) {
                                calendarEvents.take(3).forEach { ev ->
                                    Surface(color = LightBlueSurface, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Text(ev.title, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DeepExecutiveBlue)
                                            Text("${ev.date} • ${ev.timeRange} (${ev.location})", fontSize = 10.sp, color = CorporateBlue)
                                        }
                                    }
                                }
                            }
                        }
                        "drive" -> {
                            Text("Ruang Cloud Google Drive Terproteksi", fontSize = 11.sp, color = CorporateDarkBlue, fontWeight = FontWeight.Bold)
                            Text("Menyimpan cadangan dokumen legalitas, rekap absensi karyawan, dan berkas transaksi dengan opsi pembersihan ruang lokal.", fontSize = 11.sp, color = Slate600)
                            if (driveDocs.isNotEmpty()) {
                                driveDocs.take(3).forEach { doc ->
                                    Surface(color = LightBlueSurface, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.InsertDriveFile, contentDescription = null, tint = CorporateBlue, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Column {
                                                Text(doc.title, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DeepExecutiveBlue)
                                                Text("${doc.docType} • ${doc.sizeOrPages}", fontSize = 10.sp, color = Slate500)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        "meet" -> {
                            Text("Tautan Google Meet Terjadwal:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                            Text("https://meet.google.com/dar-corp-exec-sync", fontSize = 11.sp, color = CorporateBlue)
                            Text("Ruang rapat virtual terenkripsi dengan peserta yang ditentukan oleh Admin untuk rapat direksi dan briefing cabang jarak jauh.", fontSize = 11.sp, color = Slate600)
                        }
                        else -> {
                            Text("Draf Google Dokumen Resmi:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                            Text("• Template MoU Kemitraan Komoditas Multi-Tahun (Docs)\n• Form Berita Acara Kesepakatan Final Pasokan (Docs)\n• Lampiran Spesifikasi Teknis Uji Mutu Laboratorium (Sheets)", fontSize = 11.sp, color = Slate600, lineHeight = 16.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        snackbarMessage = "Membuka integrasi ${tool.uppercase()} melalui akun Google yang terverifikasi."
                        showWorkspaceDetailDialog = null
                    }
                ) {
                    Text("Buka Layanan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showWorkspaceDetailDialog = null }) { Text("Tutup") }
            }
        )
    }

    // 3. COMPANY SWITCHER DIALOG
    if (showCompanyDialog) {
        AlertDialog(
            onDismissRequest = { showCompanyDialog = false },
            title = { Text("Pilih Ruang Kerja Entitas Perusahaan") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Setiap perusahaan memiliki struktur tenaga kerja, izin, dan ruang data terpisah yang tidak saling bercampur.",
                        fontSize = 11.sp,
                        color = Slate600
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    companies.forEach { comp ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (comp.id == currentCompany.id) LightBlueSurface else Color(0xFFF1F5F9)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, if (comp.id == currentCompany.id) CorporateBlue else Slate200, RoundedCornerShape(8.dp))
                                .clickable {
                                    onSelectCompany(comp.id)
                                    showCompanyDialog = false
                                }
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(comp.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepExecutiveBlue)
                                Text(comp.sector, fontSize = 11.sp, color = Slate600)
                                Text("Kode: ${comp.code} • NPWP: ${comp.npwp}", fontSize = 10.sp, color = Slate500)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCompanyDialog = false }) { Text("Tutup") }
            }
        )
    }

    // 4. USER ROLE SWITCHER DIALOG
    if (showUserSwitcherDialog) {
        AlertDialog(
            onDismissRequest = { showUserSwitcherDialog = false },
            title = { Text("Ganti Akun Personel (${currentCompany.name})") },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 380.dp)
                ) {
                    items(users.size) { idx ->
                        val u = users[idx]
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (u.id == currentUser.id) Color(0xFFFEF3C7) else Color(0xFFF1F5F9)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectUser(u.id)
                                    showUserSwitcherDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = Color(u.role.badgeColorHex),
                                    shape = CircleShape,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(u.avatarInitials, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(u.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepExecutiveBlue)
                                    Text(u.role.label, fontSize = 10.sp, color = Color(u.role.badgeColorHex), fontWeight = FontWeight.Medium)
                                    Text(u.position, fontSize = 10.sp, color = Slate600)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showUserSwitcherDialog = false }) { Text("Tutup") }
            }
        )
    }

    // 5. SUPER ADMIN GOOGLE ACCOUNT + PASSWORD VERIFICATION DIALOG
    if (showSuperAdminAuthDialog) {
        var authEmail by remember { mutableStateOf("denyakhbar@gmail.com") }
        var authPassword by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }
        var isChangePasswordMode by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showSuperAdminAuthDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = AccentGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (isChangePasswordMode) "Ubah Kata Sandi Admin Utama" else "Verifikasi Admin Utama (Google)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        if (isChangePasswordMode)
                            "Perbarui kata sandi otorisasi Admin Utama untuk akun Google denyakhbar@gmail.com."
                        else
                            "Masukkan kata sandi akses untuk mengesahkan akun Google denyakhbar@gmail.com sebagai Admin Utama dengan kuasa penuh lintas entitas perusahaan.",
                        fontSize = 11.sp,
                        color = Slate600
                    )

                    OutlinedTextField(
                        value = authEmail,
                        onValueChange = { authEmail = it },
                        label = { Text("Akun Google Admin Utama") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = authPassword,
                        onValueChange = { authPassword = it },
                        label = { Text(if (isChangePasswordMode) "Kata Sandi Saat Ini" else "Kata Sandi Admin Utama") },
                        singleLine = true,
                        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (isChangePasswordMode) {
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("Kata Sandi Baru (Min. 6 Karakter)") },
                            singleLine = true,
                            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    TextButton(
                        onClick = { isChangePasswordMode = !isChangePasswordMode },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            if (isChangePasswordMode) "Kembali ke Verifikasi Masuk Admin" else "Ingin Ubah Kata Sandi Admin Utama?",
                            fontSize = 11.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val res = if (isChangePasswordMode) {
                            onUpdateSuperAdminPassword(authPassword, newPassword)
                        } else {
                            onAuthenticateSuperAdmin(authEmail, authPassword)
                        }
                        snackbarMessage = res.second
                        if (res.first) {
                            showSuperAdminAuthDialog = false
                        }
                    }
                ) {
                    Text(if (isChangePasswordMode) "Simpan Sandi Baru" else "Verifikasi & Aktifkan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSuperAdminAuthDialog = false }) { Text("Batal") }
            }
        )
    }
}

@Composable
private fun WorkspaceToolButton(
    icon: ImageVector,
    label: String,
    desc: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .border(1.dp, LightBlueBorder, RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = color.copy(alpha = 0.12f),
                shape = CircleShape,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue, textAlign = TextAlign.Center, maxLines = 1)
            Text(desc, fontSize = 8.sp, color = Slate500, textAlign = TextAlign.Center, maxLines = 1)
        }
    }
}

@Composable
private fun ModuleCard(
    title: String,
    subtitle: String,
    badge: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .border(1.dp, LightBlueBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    color = accentColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
                    }
                }

                Surface(
                    color = LightBlueSurface,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = badge,
                        color = DeepExecutiveBlue,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = DeepExecutiveBlue,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Slate500,
                lineHeight = 14.sp,
                maxLines = 2
            )
        }
    }
}
