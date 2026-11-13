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
import com.example.data.model.AttendanceRecord
import com.example.data.model.LiveLocationTracking
import com.example.data.model.User
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceGpsScreen(
    currentUser: User,
    attendanceList: List<AttendanceRecord>,
    liveGps: LiveLocationTracking,
    onCheckIn: (String) -> Pair<Boolean, String>,
    onCheckOut: () -> Pair<Boolean, String>,
    onBack: () -> Unit
) {
    val currentTime = remember {
        SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()) + " WIB"
    }
    val currentDate = remember {
        SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID")).format(Date())
    }

    var selfieTaken by remember { mutableStateOf(true) }
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
                        Text("Presensi Kehadiran & GPS Jam Kerja", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("Waktu Masuk & Pulang Berbasis Geofence", fontSize = 11.sp, color = Slate500)
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LightBlueBg)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Real-time Clock & Date Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyDark),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentDate,
                            color = Slate300,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentTime,
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = NavyCard,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = "Shift Operasional Standar: 08:00 - 17:00 WIB",
                                color = AccentCyanLight,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Strict Prompt Requirement: "MENDETEKSI LOKASI KARYAWAN HANYA SELAMA JAM KERJA"
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF86EFAC), RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.GpsFixed, contentDescription = null, tint = SuccessEmerald, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Kebijakan Privasi GPS: Pelacakan Hanya Selama Jam Kerja",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFF14532D)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Aplikasi mendeteksi posisi koordinat GPS karyawan strictly selama jam kerja berlangsung untuk keperluan keselamatan operasional dan validasi kehadiran. Di luar jam kerja (sebelum 08:00 atau setelah 17:00), modul pelacakan otomatis non-aktif demi privasi penuh karyawan.",
                            fontSize = 11.sp,
                            color = Color(0xFF166534),
                            lineHeight = 15.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = Color(0xFFBBF7D0))
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Titik GPS Saat Ini:", fontSize = 10.sp, color = Slate600)
                                Text(
                                    "${liveGps.latitude}, ${liveGps.longitude}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF0F172A)
                                )
                                Text(liveGps.address, fontSize = 10.sp, color = Slate500)
                            }
                            Surface(
                                color = Color(0xFFDCFCE7),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    "Geofence Valid",
                                    color = Color(0xFF15803D),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Facial Verification & Selfie Snapshot Simulator
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Verifikasi Kamera & Wajah Asli", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Surface(
                                color = Color(0xFFFEF3C7),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text("Anti-Fake GPS & Liveness", color = Color(0xFF92400E), fontSize = 9.sp, modifier = Modifier.padding(4.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .background(NavySurface, CircleShape)
                                .border(2.dp, if (selfieTaken) SuccessEmerald else AccentCyan, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Face,
                                contentDescription = "Face Capture",
                                tint = if (selfieTaken) Color(0xFFA7F3D0) else Color.White,
                                modifier = Modifier.size(60.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Karyawan: ${currentUser.name} (${currentUser.role.label})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Foto selfie terverifikasi biometrik dengan stempel waktu dan lokasi",
                            fontSize = 10.sp,
                            color = Slate500
                        )
                    }
                }
            }

            // Check In & Check Out Action Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val res = onCheckIn("Foto Selfie & Biometrik Terverifikasi")
                            snackbarMessage = res.second
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("check_in_button")
                    ) {
                        Icon(Icons.Default.Login, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Absen Masuk", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            val res = onCheckOut()
                            snackbarMessage = res.second
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CorporateBlue),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("check_out_button")
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Absen Pulang", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Attendance Logs
            item {
                Text(
                    text = "Riwayat Catatan Presensi Masuk & Pulang",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            items(attendanceList) { att ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Slate200, RoundedCornerShape(10.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(att.userName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Surface(
                                color = Color(0xFFDCFCE7),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(att.status, color = Color(0xFF166534), fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text(att.date, fontSize = 11.sp, color = Slate500)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Masuk:", fontSize = 10.sp, color = Slate500)
                                Text(att.checkInTime ?: "-", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SuccessEmerald)
                                Text(att.checkInAddress ?: "", fontSize = 9.sp, color = Slate400, maxLines = 1)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("Pulang:", fontSize = 10.sp, color = Slate500)
                                Text(att.checkOutTime ?: "Belum Pulang", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (att.checkOutTime != null) CorporateBlue else WarningAmber)
                                Text(att.checkOutAddress ?: "", fontSize = 9.sp, color = Slate400, maxLines = 1)
                            }
                        }
                    }
                }
            }
        }
    }
}
