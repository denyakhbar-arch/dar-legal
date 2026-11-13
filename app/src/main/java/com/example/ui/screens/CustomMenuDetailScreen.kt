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
import com.example.data.model.CustomCompanyMenu
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomMenuDetailScreen(
    customMenu: CustomCompanyMenu,
    onBack: () -> Unit
) {
    var isVideoPlaying by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(customMenu.menuTitle, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue, maxLines = 1)
                        Text(customMenu.subMenuTitle, fontSize = 11.sp, color = CorporateDarkBlue, maxLines = 1)
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LightBlueBg)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Info Badge
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
                            Surface(
                                color = LightBlueSurface,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    customMenu.categoryTag,
                                    color = CorporateDarkBlue,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Text("Diterbitkan: ${customMenu.createdAt}", fontSize = 10.sp, color = Slate500)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(customMenu.menuTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepExecutiveBlue)
                        Text("Sub-Menu: ${customMenu.subMenuTitle}", fontSize = 12.sp, color = CorporateBlue, fontWeight = FontWeight.Medium)
                    }
                }
            }

            // Render each custom content section
            items(customMenu.sections) { section ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Article, contentDescription = null, tint = CorporateBlue, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(section.sectionTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepExecutiveBlue)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = section.textContent,
                            fontSize = 12.sp,
                            color = Slate700,
                            lineHeight = 18.sp
                        )

                        // Embedded Photo Documentation
                        if (section.photoUrls.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Text("Dokumentasi Foto Lapangan:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepExecutiveBlue)
                            Spacer(modifier = Modifier.height(6.dp))

                            section.photoUrls.forEachIndexed { idx, _ ->
                                Surface(
                                    color = LightBlueSurface,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(160.dp)
                                        .border(1.dp, LightBlueBorder, RoundedCornerShape(10.dp))
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(Icons.Default.Image, contentDescription = null, tint = CorporateBlue, modifier = Modifier.size(44.dp))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = section.photoCaptions.getOrNull(idx) ?: "Foto Dokumentasi Perusahaan",
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 11.sp,
                                                color = DeepExecutiveBlue
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Lampiran Foto #${idx + 1}: ${section.photoCaptions.getOrNull(idx) ?: "Dokumentasi Resmi"}",
                                    fontSize = 10.sp,
                                    color = Slate500
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        // Embedded Video Documentation Player
                        if (section.videoUrl != null) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Text("Video Dokumentasi Resmi:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepExecutiveBlue)
                            Spacer(modifier = Modifier.height(6.dp))

                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F294A)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Column(
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        IconButton(
                                            onClick = { isVideoPlaying = !isVideoPlaying },
                                            modifier = Modifier
                                                .size(54.dp)
                                                .background(if (isVideoPlaying) CorporateBlue else Color.White.copy(alpha = 0.25f), CircleShape)
                                        ) {
                                            Icon(
                                                if (isVideoPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                contentDescription = "Putar Video",
                                                tint = Color.White,
                                                modifier = Modifier.size(32.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = if (isVideoPlaying) "Sedang Memutar Video..." else (section.videoTitle ?: "Putar Video Dokumentasi"),
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = if (isVideoPlaying) "02:15 / 05:40 • Full HD 1080p" else "Tautan: ${section.videoUrl}",
                                            color = Slate300,
                                            fontSize = 9.sp,
                                            maxLines = 1
                                        )
                                    }

                                    // Video Controls Bar at bottom
                                    Surface(
                                        color = Color.Black.copy(alpha = 0.6f),
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(if (isVideoPlaying) "02:15" else "00:00", color = Color.White, fontSize = 10.sp)
                                            LinearProgressIndicator(
                                                progress = { if (isVideoPlaying) 0.42f else 0.0f },
                                                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                                                color = CorporateBlue,
                                                trackColor = Slate700
                                            )
                                            Text("05:40", color = Slate300, fontSize = 10.sp)
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
