package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.BusinessDocument
import com.example.data.model.DailyReport
import com.example.data.model.User
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyReportScreen(
    currentUser: User,
    dailyReports: List<DailyReport>,
    documents: List<BusinessDocument>,
    onSubmitReport: (title: String, done: String, obstacles: String, plan: String, fileName: String?) -> Pair<Boolean, String>,
    onClearStorage: () -> String,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Laporan Harian, 1: Berkas Dokumen Usaha
    var showSubmitDialog by remember { mutableStateOf(false) }
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
                        Text("Laporan Harian & Dokumen Usaha", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("Manajemen Berkas & Pembersihan Memori", fontSize = 11.sp, color = Slate500)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showSubmitDialog = true },
                        modifier = Modifier.testTag("add_report_button")
                    ) {
                        Icon(Icons.Default.PostAdd, contentDescription = "Tulis Laporan", tint = AccentCyan)
                    }
                }
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
            // Storage Cleaning Card (Strict prompt requirement: "DOKUMEN DAPAT DI UNDUH AGAR RUANG PENYIMPANAN DAPAT DI BERSIHKAN KEMBALI")
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyDark),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CleaningServices, contentDescription = null, tint = AccentCyanLight, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "OPTIMASI RUANG PENYIMPANAN",
                                        color = AccentCyanLight,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = "Unduh Dokumen & Bersihkan Cache Lokal",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Dokumen operasional tersinkronisasi aman di cloud enterprise. Anda dapat mengunduh salinan lokal, lalu menekan tombol pembersihan agar memori penyimpanan smartphone kembali lega.",
                            color = Slate300,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                val msg = onClearStorage()
                                snackbarMessage = msg
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGold),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("clear_storage_button")
                        ) {
                            Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Unduh Semua & Bersihkan Penyimpanan Lokal", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Tab Selector
            item {
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Laporan Harian (${dailyReports.size})", fontSize = 12.sp) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Dokumen Usaha (${documents.size})", fontSize = 12.sp) }
                    )
                }
            }

            if (selectedTab == 0) {
                // List of Daily Reports
                items(dailyReports) { rep ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Slate200, RoundedCornerShape(10.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(rep.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                                Surface(
                                    color = Color(0xFFDCFCE7),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(rep.status, color = Color(0xFF166534), fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Text("Oleh: ${rep.userName} • ${rep.date}", fontSize = 11.sp, color = Slate500)

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Aktivitas yang Diselesaikan:", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text(rep.activitiesDone, fontSize = 11.sp, color = Slate700)

                            if (rep.obstacles.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Kendala Lapangan: ${rep.obstacles}", fontSize = 11.sp, color = Slate600)
                            }

                            if (rep.nextPlan.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Rencana Besok: ${rep.nextPlan}", fontSize = 11.sp, color = Slate600)
                            }

                            rep.documentAttachment?.let { doc ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    color = Slate100,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.AttachFile, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(doc, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                        }
                                        Text("${rep.fileSizeKb} KB", fontSize = 10.sp, color = Slate500)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // List of Business Documents
                items(documents) { doc ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Slate200, RoundedCornerShape(10.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    color = Color(0xFFDBEAFE),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Description, contentDescription = null, tint = CorporateBlue, modifier = Modifier.size(22.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(doc.title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("Kategori: ${doc.category} • Format: ${doc.fileFormat} (${doc.sizeKb} KB)", fontSize = 10.sp, color = Slate500)
                                    Text("Diunggah: ${doc.uploadDate}", fontSize = 10.sp, color = Slate400)
                                }
                            }

                            FilledTonalButton(
                                onClick = {
                                    snackbarMessage = "Mengunduh ${doc.title} (${doc.fileFormat}). Berkas tersimpan di folder Download."
                                },
                                modifier = Modifier.height(32.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Icon(Icons.Default.Download, contentDescription = "Unduh", modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Unduh", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    // Submit Daily Report Dialog
    if (showSubmitDialog) {
        var reportTitle by remember { mutableStateOf("") }
        var reportActivities by remember { mutableStateOf("") }
        var reportObstacles by remember { mutableStateOf("") }
        var reportPlan by remember { mutableStateOf("") }
        var attachFileName by remember { mutableStateOf("Laporan_Harian_Lampiran.pdf") }

        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            title = { Text("Tulis Laporan Kerja Harian") },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 420.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = reportTitle,
                            onValueChange = { reportTitle = it },
                            label = { Text("Judul Laporan Tugas") },
                            placeholder = { Text("Misal: Inspeksi Kualitas Komoditas") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = reportActivities,
                            onValueChange = { reportActivities = it },
                            label = { Text("Aktivitas yang Dikerjakan") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = reportObstacles,
                            onValueChange = { reportObstacles = it },
                            label = { Text("Kendala yang Dihadapi (Opsional)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = reportPlan,
                            onValueChange = { reportPlan = it },
                            label = { Text("Rencana Kerja Selanjutnya") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = attachFileName,
                            onValueChange = { attachFileName = it },
                            label = { Text("Nama Berkas Lampiran / Berita Acara") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (reportTitle.isNotBlank() && reportActivities.isNotBlank()) {
                            val res = onSubmitReport(
                                reportTitle,
                                reportActivities,
                                reportObstacles,
                                reportPlan,
                                attachFileName.ifBlank { null }
                            )
                            snackbarMessage = res.second
                            showSubmitDialog = false
                        }
                    },
                    modifier = Modifier.testTag("submit_daily_report_confirm_button")
                ) {
                    Text("Kirim Laporan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitDialog = false }) { Text("Batal") }
            }
        )
    }
}
