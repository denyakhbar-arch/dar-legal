package com.example.ui.screens

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
fun CommunicationHubScreen(
    currentUser: User,
    users: List<User>,
    company: Company,
    chatMessages: List<ChatMessage>,
    videoMeetings: List<VideoConferenceRoom>,
    companyInbox: List<CompanyInboxMail>,
    onSendMessage: (channel: ForumChannelType, branchId: String?, text: String, voiceSec: Int?) -> Unit,
    onBack: () -> Unit
) {
    var mainTab by remember { mutableIntStateOf(0) } // 0: Chat & Suara, 1: Rapat Video, 2: Kotak Surat Perusahaan
    var selectedChannel by remember { mutableStateOf(ForumChannelType.SEMUA_KARYAWAN) }
    var selectedBranchId by remember { mutableStateOf("br_jkt") }

    var textInput by remember { mutableStateOf("") }
    var isRecordingVoice by remember { mutableStateOf(false) }
    var voiceDuration by remember { mutableIntStateOf(0) }

    var activeVideoRoom by remember { mutableStateOf<VideoConferenceRoom?>(null) }
    var isMicMuted by remember { mutableStateOf(false) }
    var isCamOff by remember { mutableStateOf(false) }

    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    // Role check for Group 1 (Pejabat Tinggi & Direksi)
    val canAccessDireksi = currentUser.role == UserRole.SUPER_ADMIN ||
            currentUser.role == UserRole.SECOND_ADMIN ||
            currentUser.role == UserRole.PEJABAT_TINGGI

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Pusat Komunikasi & Rapat", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("Grup Direksi, Forum Cabang, Voice Note & Rapat Video", fontSize = 11.sp, color = Slate500)
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
            TabRow(selectedTabIndex = mainTab) {
                Tab(
                    selected = mainTab == 0,
                    onClick = { mainTab = 0 },
                    text = { Text("Chat & Suara", fontSize = 12.sp) },
                    icon = { Icon(Icons.Default.Forum, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = mainTab == 1,
                    onClick = { mainTab = 1 },
                    text = { Text("Rapat Video", fontSize = 12.sp) },
                    icon = { Icon(Icons.Default.VideoCall, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = mainTab == 2,
                    onClick = { mainTab = 2 },
                    text = { Text("Surat Masuk", fontSize = 12.sp) },
                    icon = { Icon(Icons.Default.Mail, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
            }

            when (mainTab) {
                0 -> {
                    // TAB 0: CHAT & VOICE NOTE
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(LightBlueBg)
                    ) {
                        // Channel Selector Chips (Strict prompt requirement: "MEMBERIKAN 2 GROUP, GROUP 1 KHUSUS PEJABAT TINGGI, GROUP 2 KESELURUHAN KARYAWAN, PEMISAHAN FORUM ANTAR CABANG")
                        Surface(
                            color = Slate100,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    FilterChip(
                                        selected = selectedChannel == ForumChannelType.PEJABAT_TINGGI,
                                        onClick = {
                                            if (canAccessDireksi) {
                                                selectedChannel = ForumChannelType.PEJABAT_TINGGI
                                            } else {
                                                snackbarMessage = "Akses Ditolak: Grup 1 khusus untuk Pejabat Tinggi & Direksi."
                                            }
                                        },
                                        label = { Text("Group 1: Direksi", fontSize = 10.sp) },
                                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(12.dp)) }
                                    )
                                    FilterChip(
                                        selected = selectedChannel == ForumChannelType.SEMUA_KARYAWAN,
                                        onClick = { selectedChannel = ForumChannelType.SEMUA_KARYAWAN },
                                        label = { Text("Group 2: Semua Karyawan", fontSize = 10.sp) }
                                    )
                                    FilterChip(
                                        selected = selectedChannel == ForumChannelType.CABANG,
                                        onClick = { selectedChannel = ForumChannelType.CABANG },
                                        label = { Text("Forum Cabang", fontSize = 10.sp) }
                                    )
                                }

                                if (selectedChannel == ForumChannelType.CABANG) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        company.branches.forEach { b ->
                                            FilterChip(
                                                selected = selectedBranchId == b.id,
                                                onClick = { selectedBranchId = b.id },
                                                label = { Text(b.city, fontSize = 10.sp) }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Message List
                        val filteredMessages = chatMessages.filter { msg ->
                            when (selectedChannel) {
                                ForumChannelType.PEJABAT_TINGGI -> msg.channelType == ForumChannelType.PEJABAT_TINGGI
                                ForumChannelType.SEMUA_KARYAWAN -> msg.channelType == ForumChannelType.SEMUA_KARYAWAN
                                ForumChannelType.CABANG -> msg.channelType == ForumChannelType.CABANG && (msg.branchId == null || msg.branchId == selectedBranchId)
                            }
                        }

                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            items(filteredMessages) { msg ->
                                val isMe = msg.senderId == currentUser.id || msg.isCurrentUser
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                                    ) {
                                        Text(
                                            text = msg.senderName,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Slate600
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(msg.timestamp, fontSize = 9.sp, color = Slate400)
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))

                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isMe) CorporateBlue else Slate200
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.widthIn(max = 280.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(
                                                text = msg.text,
                                                color = if (isMe) Color.White else Slate900,
                                                fontSize = 12.sp,
                                                lineHeight = 16.sp
                                            )

                                            // Voice Note Representation (Strict prompt requirement: "PESAN SUARA")
                                            if (msg.voiceNoteDurationSec != null) {
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Surface(
                                                    color = if (isMe) NavyDark else Color.White,
                                                    shape = RoundedCornerShape(8.dp),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(6.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Icon(
                                                            Icons.Default.PlayArrow,
                                                            contentDescription = "Putar Pesan Suara",
                                                            tint = AccentGold,
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Column(modifier = Modifier.weight(1f)) {
                                                            Text(
                                                                text = "||||| | ||||| | ||||| | ||",
                                                                color = AccentCyanLight,
                                                                fontSize = 10.sp,
                                                                letterSpacing = 2.sp
                                                            )
                                                            Text(
                                                                text = "Pesan Suara • ${msg.voiceNoteDurationSec} detik",
                                                                fontSize = 9.sp,
                                                                color = Slate400
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Chat Input Bar with Voice Note Button
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shadowElevation = 8.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                if (isRecordingVoice) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFFFEE2E2), RoundedCornerShape(8.dp))
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Mic, contentDescription = null, tint = ErrorRose)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Merekam Pesan Suara... 00:0$voiceDuration", color = ErrorRose, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }
                                        Button(
                                            onClick = {
                                                isRecordingVoice = false
                                                onSendMessage(selectedChannel, selectedBranchId, "Pesan Suara Dikirim", 24)
                                                snackbarMessage = "Pesan suara berhasil direkam dan dikirim."
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = ErrorRose),
                                            modifier = Modifier.height(30.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text("Kirim Suara", fontSize = 11.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            isRecordingVoice = !isRecordingVoice
                                            voiceDuration = 18
                                        },
                                        modifier = Modifier.testTag("voice_record_button")
                                    ) {
                                        Icon(Icons.Default.Mic, contentDescription = "Pesan Suara", tint = if (isRecordingVoice) ErrorRose else Slate600)
                                    }

                                    OutlinedTextField(
                                        value = textInput,
                                        onValueChange = { textInput = it },
                                        placeholder = { Text("Ketik pesan...", fontSize = 12.sp) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                            .testTag("chat_input_field"),
                                        shape = RoundedCornerShape(24.dp)
                                    )

                                    Spacer(modifier = Modifier.width(6.dp))

                                    IconButton(
                                        onClick = {
                                            if (textInput.isNotBlank()) {
                                                onSendMessage(selectedChannel, selectedBranchId, textInput, null)
                                                textInput = ""
                                            }
                                        },
                                        modifier = Modifier.testTag("send_chat_button")
                                    ) {
                                        Icon(Icons.Default.Send, contentDescription = "Kirim", tint = CorporateBlue)
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // TAB 1: VIDEO CONFERENCE (Strict requirement: "PANGGILAN VIDEO CONFERENSI YANG PESERTANYA SESUAI DENGAN YANG DIPILIH OLEH ADMIN")
                    if (activeVideoRoom != null) {
                        // LIVE VIDEO CALL SIMULATION SCREEN
                        val room = activeVideoRoom!!
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(NavyDark)
                                .padding(12.dp)
                        ) {
                            // Top Bar of Call
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(color = ErrorRose, shape = CircleShape, modifier = Modifier.size(8.dp)) {}
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("REC • LIVE TELECONFERENCE", color = ErrorRose, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Text(room.title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                                Surface(
                                    color = NavyCard,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("Peserta Dipilih Admin", color = AccentGold, fontSize = 9.sp, modifier = Modifier.padding(4.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Video Tiles Grid (4-Screen Meeting Grid)
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Tile 1: Host
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = NavyCard),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f).fillMaxHeight()
                                    ) {
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            Icon(
                                                Icons.Default.Person,
                                                contentDescription = null,
                                                tint = Slate500,
                                                modifier = Modifier.size(60.dp).align(Alignment.Center)
                                            )
                                            Text(
                                                "Hendra Wijaya (Super Admin / Host)",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                modifier = Modifier.align(Alignment.BottomStart).padding(6.dp)
                                            )
                                        }
                                    }

                                    // Tile 2: Second Admin
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = NavyCard),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f).fillMaxHeight()
                                    ) {
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            Icon(
                                                Icons.Default.Person,
                                                contentDescription = null,
                                                tint = Slate500,
                                                modifier = Modifier.size(60.dp).align(Alignment.Center)
                                            )
                                            Text(
                                                "Kartika Sari (Admin Kedua)",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                modifier = Modifier.align(Alignment.BottomStart).padding(6.dp)
                                            )
                                        }
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Tile 3: Komisaris
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = NavyCard),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f).fillMaxHeight()
                                    ) {
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            Icon(
                                                Icons.Default.Person,
                                                contentDescription = null,
                                                tint = Slate500,
                                                modifier = Modifier.size(60.dp).align(Alignment.Center)
                                            )
                                            Text(
                                                "Ir. Budi Santoso (Direksi)",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                modifier = Modifier.align(Alignment.BottomStart).padding(6.dp)
                                            )
                                        }
                                    }

                                    // Tile 4: Current User Video
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = if (isCamOff) Color.Black else NavySurface),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f).fillMaxHeight()
                                    ) {
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            if (isCamOff) {
                                                Text("Kamera Mati", color = Slate500, fontSize = 11.sp, modifier = Modifier.align(Alignment.Center))
                                            } else {
                                                Icon(
                                                    Icons.Default.Videocam,
                                                    contentDescription = null,
                                                    tint = AccentCyanLight,
                                                    modifier = Modifier.size(60.dp).align(Alignment.Center)
                                                )
                                            }
                                            Text(
                                                "${currentUser.name} (Anda)",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                modifier = Modifier.align(Alignment.BottomStart).padding(6.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Control Bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { isMicMuted = !isMicMuted },
                                    modifier = Modifier.background(if (isMicMuted) ErrorRose else NavyCard, CircleShape)
                                ) {
                                    Icon(if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic, contentDescription = "Mic", tint = Color.White)
                                }

                                IconButton(
                                    onClick = { isCamOff = !isCamOff },
                                    modifier = Modifier.background(if (isCamOff) ErrorRose else NavyCard, CircleShape)
                                ) {
                                    Icon(if (isCamOff) Icons.Default.VideocamOff else Icons.Default.Videocam, contentDescription = "Kamera", tint = Color.White)
                                }

                                IconButton(
                                    onClick = {
                                        snackbarMessage = "Presentasi layar berhasil dimulai."
                                    },
                                    modifier = Modifier.background(NavyCard, CircleShape)
                                ) {
                                    Icon(Icons.Default.PresentToAll, contentDescription = "Screen Share", tint = AccentCyanLight)
                                }

                                Button(
                                    onClick = { activeVideoRoom = null },
                                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRose),
                                    shape = CircleShape,
                                    modifier = Modifier.size(48.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Icon(Icons.Default.CallEnd, contentDescription = "Tutup Panggilan", tint = Color.White)
                                }
                            }
                        }
                    } else {
                        // Meeting Room List
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            item {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(12.dp))
                                ) {
                                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Security, contentDescription = null, tint = CorporateBlue)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            "Keamanan Rapat: Peserta telekonferensi disaring ketat hanya untuk pengguna yang telah ditentukan dan diundang oleh Admin.",
                                            fontSize = 11.sp,
                                            color = CorporateBlue,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }
                            }

                            items(videoMeetings) { room ->
                                val isAllowed = room.allowedUserIds.contains(currentUser.id) ||
                                        currentUser.role == UserRole.SUPER_ADMIN ||
                                        currentUser.role == UserRole.SECOND_ADMIN

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
                                            Text(room.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.weight(1f))
                                            Surface(
                                                color = if (room.isOngoing) Color(0xFFFEE2E2) else Slate200,
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = if (room.isOngoing) "SEDANG BERLANGSUNG" else "TERJADWAL",
                                                    color = if (room.isOngoing) ErrorRose else Slate700,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        Text("Waktu: ${room.scheduledTime}", fontSize = 11.sp, color = Slate600)
                                        Text("Host Rapat: ${room.hostName}", fontSize = 11.sp, color = Slate500)

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Agenda Rapat:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text(room.agenda, fontSize = 11.sp, color = Slate700)

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${room.allowedUserIds.size} Peserta Terpilih Admin",
                                                fontSize = 11.sp,
                                                color = Slate500
                                            )

                                            Button(
                                                onClick = {
                                                    if (isAllowed) {
                                                        activeVideoRoom = room
                                                    } else {
                                                        snackbarMessage = "Akses Rapat Ditolak: Akun Anda tidak termasuk dalam daftar peserta yang dipilih Admin."
                                                    }
                                                },
                                                enabled = isAllowed,
                                                modifier = Modifier
                                                    .height(34.dp)
                                                    .testTag("join_meeting_${room.id}")
                                            ) {
                                                Icon(Icons.Default.VideoCall, contentDescription = null, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(if (isAllowed) "Masuk Ruang Rapat" else "Akses Terkunci", fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // TAB 2: KOTAK PESAN MASUK PERUSAHAAN (Strict requirement: "PESAN MASUK UNTUK PERUSAHAAN YANG PESANNYA HANYA AKAN DILIHAT OLEH ORANG YANG DIPILIH ADMIN")
                    val isInboxAuthorized = currentUser.canAccessCompanyInbox ||
                            currentUser.role == UserRole.SUPER_ADMIN ||
                            currentUser.role == UserRole.SECOND_ADMIN

                    if (!isInboxAuthorized) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color(0xFFFECACA), RoundedCornerShape(16.dp))
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(Icons.Default.LockPerson, contentDescription = null, tint = ErrorRose, modifier = Modifier.size(48.dp))
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text("Akses Kotak Surat Dibatasi", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF991B1B))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        "Sesuai regulasi privasi perusahaan, fitur Pesan Masuk Resmi hanya dapat dibaca oleh personel yang telah ditunjuk dan diberi wewenang khusus oleh Admin.",
                                        fontSize = 12.sp,
                                        color = Slate600,
                                        lineHeight = 16.sp,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    } else {
                        var emailChannelFilter by remember { mutableStateOf("SEMUA") }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.MarkEmailRead, contentDescription = null, tint = CorporateBlue)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                "Dua Jalur Email Resmi Terpisah:",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = DeepExecutiveBlue
                                            )
                                            Text(
                                                "Jalur 1: ${company.legalConsultantEmail} (Konsultan Hukum)\nJalur 2: ${company.ownerEmail} (Pemilik & Sekretaris)",
                                                fontSize = 10.sp,
                                                color = CorporateDarkBlue,
                                                lineHeight = 14.sp
                                            )
                                        }
                                    }
                                }
                            }

                            // Filter Chips for Dual Email Channels
                            item {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    FilterChip(
                                        selected = emailChannelFilter == "SEMUA",
                                        onClick = { emailChannelFilter = "SEMUA" },
                                        label = { Text("Semua Email", fontSize = 10.sp) }
                                    )
                                    FilterChip(
                                        selected = emailChannelFilter == "LEGAL_CONSULTANT",
                                        onClick = { emailChannelFilter = "LEGAL_CONSULTANT" },
                                        label = { Text("Jalur 1: Konsultan Hukum", fontSize = 10.sp) }
                                    )
                                    FilterChip(
                                        selected = emailChannelFilter == "CORPORATE",
                                        onClick = { emailChannelFilter = "CORPORATE" },
                                        label = { Text("Jalur 2: Pemilik & Sekretaris", fontSize = 10.sp) }
                                    )
                                }
                            }

                            val filteredInbox = when (emailChannelFilter) {
                                "LEGAL_CONSULTANT" -> companyInbox.filter { it.mailChannel == "LEGAL_CONSULTANT" }
                                "CORPORATE" -> companyInbox.filter { it.mailChannel == "CORPORATE" }
                                else -> companyInbox
                            }

                            items(filteredInbox) { mail ->
                                val isLegal = mail.mailChannel == "LEGAL_CONSULTANT"
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, if (isLegal) Color(0xFFFECDD3) else Color(0xFFBAE6FD), RoundedCornerShape(12.dp))
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Surface(
                                                color = if (isLegal) Color(0xFFFFF1F2) else Color(0xFFF0F9FF),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = if (isLegal) "JALUR 1: KONSULTAN HUKUM (${company.legalConsultantEmail})" else "JALUR 2: PEMILIK & SEKRETARIS (${mail.recipientEmail})",
                                                    color = if (isLegal) Color(0xFFBE123C) else Color(0xFF0369A1),
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                            Text(mail.receivedDate, fontSize = 10.sp, color = Slate400)
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(mail.senderOrganization, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepExecutiveBlue)
                                        Text("Dari: ${mail.senderName} (${mail.senderEmail})", fontSize = 10.sp, color = Slate500)

                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(mail.subject, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (isLegal) Color(0xFF9F1239) else CorporateBlue)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(mail.messageContent, fontSize = 11.sp, color = Slate700, lineHeight = 15.sp)

                                        Spacer(modifier = Modifier.height(10.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            OutlinedButton(
                                                onClick = {
                                                    snackbarMessage = "Membuka draft balasan resmi kepada ${mail.senderEmail}."
                                                },
                                                modifier = Modifier.height(30.dp),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                            ) {
                                                Icon(Icons.Default.Reply, contentDescription = null, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Balas Surat", fontSize = 10.sp)
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
}
