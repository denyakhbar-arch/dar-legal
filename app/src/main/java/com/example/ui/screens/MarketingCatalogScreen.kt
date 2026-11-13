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
import com.example.data.model.CompanyPostAd
import com.example.data.model.ProductCommodity
import com.example.data.model.User
import com.example.data.model.UserRole
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketingCatalogScreen(
    currentCompany: Company,
    currentUser: User,
    products: List<ProductCommodity>,
    posts: List<CompanyPostAd>,
    onCreatePost: (title: String, content: String, tag: String) -> Pair<Boolean, String>,
    onBack: () -> Unit
) {
    var marketTab by remember { mutableIntStateOf(1) } // 0: Profil, 1: Katalog Barang per Kg, 2: Feed & Iklan
    var showCreatePostDialog by remember { mutableStateOf(false) }

    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    val formatRupiah = remember {
        NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
            maximumFractionDigits = 0
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    val canManagePages = currentUser.role == UserRole.SUPER_ADMIN || currentUser.role == UserRole.SECOND_ADMIN

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Marketing & Katalog Komoditas", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("Spesifikasi Rinci, Harga per Kg & Feed Produktivitas", fontSize = 11.sp, color = Slate500)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    if (canManagePages) {
                        IconButton(
                            onClick = { showCreatePostDialog = true },
                            modifier = Modifier.testTag("create_marketing_post_button")
                        ) {
                            Icon(Icons.Default.Campaign, contentDescription = "Posting Iklan", tint = AccentGold)
                        }
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
            TabRow(selectedTabIndex = marketTab) {
                Tab(
                    selected = marketTab == 0,
                    onClick = { marketTab = 0 },
                    text = { Text("Profil Usaha", fontSize = 11.sp) }
                )
                Tab(
                    selected = marketTab == 1,
                    onClick = { marketTab = 1 },
                    text = { Text("Katalog (Rp/Kg)", fontSize = 11.sp) }
                )
                Tab(
                    selected = marketTab == 2,
                    onClick = { marketTab = 2 },
                    text = { Text("Feed & Iklan", fontSize = 11.sp) }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightBlueBg)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                when (marketTab) {
                    0 -> {
                        // COMPANY PROFILE
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = NavyDark),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Surface(
                                        color = AccentGold,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            "PROFIL RESMI PERUSAHAAN",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(currentCompany.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    Text(currentCompany.sector, color = AccentCyanLight, fontSize = 12.sp)

                                    Spacer(modifier = Modifier.height(12.dp))
                                    Divider(color = Slate700)
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text("Legalitas & Kepatuhan Usaha:", color = Slate300, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text("• NIB (Nomor Induk Berusaha): ${currentCompany.nib}", color = Slate300, fontSize = 11.sp)
                                    Text("• NPWP Perseroan: ${currentCompany.npwp}", color = Slate300, fontSize = 11.sp)
                                    Text("• Portal Website: ${currentCompany.websiteUrl}", color = Slate300, fontSize = 11.sp)
                                    Text("• Kontak Bisnis: ${currentCompany.phone} | ${currentCompany.email}", color = Slate300, fontSize = 11.sp)
                                }
                            }
                        }

                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("Visi & Komitmen Mutu", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "Menjadi agregator komoditas dan logistik agribisnis berdaya saing global dengan transparansi harga per kilogram, kepastian mutu ekspor, dan perlindungan payung hukum menyeluruh bagi seluruh pemangku kepentingan.",
                                        fontSize = 11.sp,
                                        color = Slate600,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }

                    1 -> {
                        // KATALOG BARANG DENGAN HARGA PER KG (Strict prompt requirement: "JENIS BARANG YANG TERSEDIA, DENGAN SPESIFIKASI DETAIL DI SERTAI HARGA BARANG PER KILO NYA")
                        items(products) { prod ->
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
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Surface(
                                                color = Color(0xFFEFF6FF),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(prod.category, color = CorporateBlue, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(prod.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Text("Asal: ${prod.harvestOrigin}", fontSize = 10.sp, color = Slate500)
                                        }

                                        // Big Price per KG badge
                                        Surface(
                                            color = Color(0xFFDCFCE7),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                horizontalAlignment = Alignment.End
                                            ) {
                                                Text("HARGA PER KILO", color = Color(0xFF166534), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                                Text(
                                                    text = "${formatRupiah.format(prod.pricePerKg)} / kg",
                                                    color = Color(0xFF15803D),
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Divider(color = Slate200)
                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text("Spesifikasi Rinci Barang:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate800)
                                    Text(prod.specifications, fontSize = 11.sp, color = Slate600)

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("• Standar Mutu: ${prod.gradeQuality}", fontSize = 10.sp, color = Slate600)
                                        Text("• Kadar Air: ${prod.moistureContent}", fontSize = 10.sp, color = Slate600)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("• Kemasan: ${prod.packagingType}", fontSize = 10.sp, color = Slate600)
                                        Text("• Min Order: ${prod.minOrderKg} Kg", fontSize = 10.sp, color = Slate600)
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            "Stok Tersedia: ${prod.availableStockKg} Kg",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SuccessEmerald
                                        )

                                        Button(
                                            onClick = {
                                                snackbarMessage = "Membuka formulir pemesanan langsung untuk komoditas ${prod.name}."
                                            },
                                            modifier = Modifier.height(32.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                                        ) {
                                            Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Pesan / Beli", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        // FEED PRODUKTIVITAS & IKLAN PERUSAHAAN (Strict prompt requirement: "FITUR MEMPOSTING LAMAN ATAU IKLAN AGAR PENGUNJUNG DAPAT MELIHAT PRODUKTIVITAS DAN KEAKTIFAN PERUSAHAAN")
                        if (canManagePages) {
                            item {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.EditNote, contentDescription = null, tint = AccentGold)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Menu Kelola Laman & Iklan Aktif", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                                        }
                                        FilledTonalButton(
                                            onClick = { showCreatePostDialog = true },
                                            modifier = Modifier.height(28.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text("Buat Postingan", fontSize = 10.sp)
                                        }
                                    }
                                }
                            }
                        }

                        items(posts) { p ->
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
                                        Surface(
                                            color = Color(0xFFFEF3C7),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(p.tag, color = Color(0xFF92400E), fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                        Text(p.publishedDate, fontSize = 10.sp, color = Slate400)
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(p.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(p.content, fontSize = 11.sp, color = Slate600, lineHeight = 16.sp)

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Divider(color = Slate200)
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Penulis: ${p.author}", fontSize = 10.sp, color = Slate500)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.ThumbUp, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("${p.likesCount}", fontSize = 11.sp, color = Slate600)
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Icon(Icons.Default.Visibility, contentDescription = null, tint = Slate400, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("${p.viewsCount} Dilihat", fontSize = 11.sp, color = Slate500)
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

    // Create Post Dialog
    if (showCreatePostDialog) {
        var postTitle by remember { mutableStateOf("") }
        var postContent by remember { mutableStateOf("") }
        var postTag by remember { mutableStateOf("Produktivitas & Ekspor") }

        AlertDialog(
            onDismissRequest = { showCreatePostDialog = false },
            title = { Text("Posting Laman Baru / Iklan Produktivitas") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = postTitle,
                        onValueChange = { postTitle = it },
                        label = { Text("Judul Kegiatan / Iklan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = postContent,
                        onValueChange = { postContent = it },
                        label = { Text("Isi Berita Produktivitas Perusahaan") },
                        minLines = 4,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = postTag,
                        onValueChange = { postTag = it },
                        label = { Text("Kategori / Tag Iklan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (postTitle.isNotBlank() && postContent.isNotBlank()) {
                            val res = onCreatePost(postTitle, postContent, postTag)
                            snackbarMessage = res.second
                            showCreatePostDialog = false
                        }
                    }
                ) {
                    Text("Terbitkan Sekarang")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreatePostDialog = false }) { Text("Batal") }
            }
        )
    }
}
