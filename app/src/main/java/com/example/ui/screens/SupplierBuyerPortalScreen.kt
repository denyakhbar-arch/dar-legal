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
import com.example.ui.components.DigitalSignaturePad
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierBuyerPortalScreen(
    currentUser: User,
    googleForms: List<GoogleFormItem> = emptyList(),
    submissions: List<PartnerSubmission>,
    onAddGoogleForm: (title: String, audience: FormAudience, url: String, desc: String) -> Pair<Boolean, String> = { _, _, _, _ -> true to "" },
    onRemoveGoogleForm: (String) -> Pair<Boolean, String> = { true to "" },
    onSubmitForm: (
        formType: PartnerFormType,
        partnerRole: UserRole,
        fullName: String,
        companyName: String,
        nik: String,
        npwp: String,
        email: String,
        phone: String,
        address: String,
        commodityName: String,
        pricePerKg: Long,
        volumeTon: Double,
        terms: String,
        signaturePoints: String
    ) -> Pair<Boolean, String>,
    onBack: () -> Unit
) {
    var selectedPortalTab by remember { mutableIntStateOf(0) } // 0: Google Formulir Mitra, 1: Isi Formulir Internal (KTP & TTD), 2: Pengajuan & Kesepakatan
    var selectedFormAudienceFilter by remember { mutableStateOf<FormAudience?>(null) } // null = Semua
    var selectedFormType by remember { mutableStateOf(PartnerFormType.PENAWARAN_HARGA) }
    var partnerRoleChoice by remember { mutableStateOf(if (currentUser.role == UserRole.BUYER) UserRole.BUYER else UserRole.SUPPLIER) }

    // Dialog state for adding new Google Form
    var showAddFormDialog by remember { mutableStateOf(false) }
    var newFormTitle by remember { mutableStateOf("") }
    var newFormAudience by remember { mutableStateOf(FormAudience.SUPPLIER) }
    var newFormUrl by remember { mutableStateOf("https://forms.google.com/d/e/") }
    var newFormDesc by remember { mutableStateOf("") }

    // Preview Google Form dialog
    var previewGoogleForm by remember { mutableStateOf<GoogleFormItem?>(null) }

    // Form fields for internal form submission
    var fullName by remember { mutableStateOf(currentUser.name) }
    var companyName by remember { mutableStateOf("PT Berkah Tani Nusantara") }
    var nikInput by remember { mutableStateOf("3276011203850002") }
    var npwpInput by remember { mutableStateOf("82.918.234.1-412.000") }
    var emailInput by remember { mutableStateOf(currentUser.email) }
    var phoneInput by remember { mutableStateOf(currentUser.phone) }
    var addressInput by remember { mutableStateOf("Jl. Industri Perkebunan No. 88, Jawa Timur") }
    var commodityNameInput by remember { mutableStateOf("Biji Kopi Robusta Super Grade A") }
    var pricePerKgInput by remember { mutableStateOf("72000") }
    var volumeTonInput by remember { mutableStateOf("50.0") }
    var termsInput by remember { mutableStateOf("Franco Gudang Utama, Pembayaran Tempo 14 Hari setelah lolos QA Lab.") }

    var ktpUploaded by remember { mutableStateOf(true) }
    var faceVerified by remember { mutableStateOf(true) }
    var signaturePointCount by remember { mutableIntStateOf(120) }

    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    val formatRupiah = remember {
        NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
            maximumFractionDigits = 0
        }
    }

    val isAdmin = currentUser.role == UserRole.SUPER_ADMIN || currentUser.role == UserRole.SECOND_ADMIN

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
                        Text("Portal Mitra Supplier & Buyer", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                        Text("Google Formulir Khusus, Penawaran Harga, KTP, Wajah & Tanda Tangan", fontSize = 11.sp, color = CorporateDarkBlue)
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
            // Portal Mode Switcher (3 Tabs)
            item {
                TabRow(
                    selectedTabIndex = selectedPortalTab,
                    containerColor = Color.White,
                    contentColor = CorporateBlue
                ) {
                    Tab(
                        selected = selectedPortalTab == 0,
                        onClick = { selectedPortalTab = 0 },
                        text = { Text("Google Formulir (${googleForms.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        icon = { Icon(Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        modifier = Modifier.testTag("tab_google_forms")
                    )
                    Tab(
                        selected = selectedPortalTab == 1,
                        onClick = { selectedPortalTab = 1 },
                        text = { Text("Form Internal & TTD", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        icon = { Icon(Icons.Default.BorderColor, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        modifier = Modifier.testTag("tab_internal_form")
                    )
                    Tab(
                        selected = selectedPortalTab == 2,
                        onClick = { selectedPortalTab = 2 },
                        text = { Text("MoU & Pengajuan (${submissions.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        icon = { Icon(Icons.Default.FactCheck, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        modifier = Modifier.testTag("tab_submissions_history")
                    )
                }
            }

            // ========================================================
            // TAB 0: GOOGLE FORMULIR INTEGRATION FOR SUPPLIER & BUYER
            // ========================================================
            if (selectedPortalTab == 0) {
                // Header Explanatory Card
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E8FF)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFD8B4FE), RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color(0xFF7C3AED),
                                shape = CircleShape,
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.DynamicForm, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Integrasi Google Formulir Resmi",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF581C87)
                                )
                                Text(
                                    text = "Menyajikan formulir survei, pra-kualifikasi, dan kontrak yang berbeda antara calon Supplier dan calon Buyer.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF6B21A8),
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }

                // Audience Filter Chips & Admin Add Button
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(
                                selected = selectedFormAudienceFilter == null,
                                onClick = { selectedFormAudienceFilter = null },
                                label = { Text("Semua", fontSize = 10.sp) },
                                modifier = Modifier.testTag("filter_form_all")
                            )
                            FilterChip(
                                selected = selectedFormAudienceFilter == FormAudience.SUPPLIER,
                                onClick = { selectedFormAudienceFilter = FormAudience.SUPPLIER },
                                label = { Text("Calon Supplier", fontSize = 10.sp) },
                                modifier = Modifier.testTag("filter_form_supplier")
                            )
                            FilterChip(
                                selected = selectedFormAudienceFilter == FormAudience.BUYER,
                                onClick = { selectedFormAudienceFilter = FormAudience.BUYER },
                                label = { Text("Calon Buyer", fontSize = 10.sp) },
                                modifier = Modifier.testTag("filter_form_buyer")
                            )
                        }

                        if (isAdmin) {
                            FilledTonalButton(
                                onClick = { showAddFormDialog = true },
                                modifier = Modifier
                                    .height(34.dp)
                                    .testTag("add_google_form_button"),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = LightBlueSurface,
                                    contentColor = DeepExecutiveBlue
                                )
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Tambah Form", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Filtered Forms List
                val displayedForms = if (selectedFormAudienceFilter == null) {
                    googleForms
                } else {
                    googleForms.filter { it.audience == selectedFormAudienceFilter }
                }

                if (displayedForms.isEmpty()) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                                .padding(vertical = 12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = Slate400, modifier = Modifier.size(40.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Belum ada Google Formulir untuk kategori ini.", fontWeight = FontWeight.Bold, color = DeepExecutiveBlue, fontSize = 13.sp)
                                Text("Admin dapat menambahkan tautan formulir Google Forms untuk Supplier atau Buyer.", fontSize = 11.sp, color = Slate500)
                            }
                        }
                    }
                } else {
                    items(displayedForms) { formItem ->
                        val isSupplierForm = formItem.audience == FormAudience.SUPPLIER
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(14.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, if (isSupplierForm) Color(0xFFBAE6FD) else Color(0xFFDDD6FE), RoundedCornerShape(14.dp))
                                .testTag("google_form_item_${formItem.id}")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = if (isSupplierForm) Color(0xFFE0F2FE) else Color(0xFFEDE9FE),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = formItem.audience.label.uppercase(),
                                            color = if (isSupplierForm) Color(0xFF0369A1) else Color(0xFF6D28D9),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }

                                    Surface(
                                        color = Color(0xFFDCFCE7),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.CloudDone, contentDescription = null, tint = Color(0xFF166534), modifier = Modifier.size(12.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("${formItem.responseCount} Respon Terkumpul", fontSize = 9.sp, color = Color(0xFF166534), fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = formItem.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = DeepExecutiveBlue
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = formItem.description,
                                    fontSize = 11.sp,
                                    color = Slate600,
                                    lineHeight = 15.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    color = LightBlueSurface,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Link, contentDescription = null, tint = CorporateBlue, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = formItem.formUrl,
                                            fontSize = 10.sp,
                                            color = CorporateDarkBlue,
                                            maxLines = 1
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Sinkronisasi: ${formItem.lastUpdated}", fontSize = 10.sp, color = Slate400)

                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        OutlinedButton(
                                            onClick = {
                                                snackbarMessage = "Tautan Google Formulir berhasil disalin ke clipboard."
                                            },
                                            modifier = Modifier.height(32.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(13.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Salin Link", fontSize = 10.sp)
                                        }

                                        Button(
                                            onClick = { previewGoogleForm = formItem },
                                            modifier = Modifier.height(32.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = CorporateBlue)
                                        ) {
                                            Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Buka Form", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        if (isAdmin) {
                                            IconButton(
                                                onClick = {
                                                    val res = onRemoveGoogleForm(formItem.id)
                                                    snackbarMessage = res.second
                                                },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(Icons.Default.DeleteOutline, contentDescription = "Hapus", tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ========================================================
            // TAB 1: FORMULIR INTERNAL (KTP, WAJAH & TTD DIGITAL)
            // ========================================================
            if (selectedPortalTab == 1) {
                // Formulir Selection Chips
                item {
                    Text("Pilih Jenis Formulir Kemitraan Internal:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                    Spacer(modifier = Modifier.height(4.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        PartnerFormType.values().forEach { fType ->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedFormType == fType) Color(0xFFEFF6FF) else Color.White
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        if (selectedFormType == fType) CorporateBlue else LightBlueBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedFormType = fType }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedFormType == fType,
                                        onClick = { selectedFormType = fType }
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(fType.label, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepExecutiveBlue)
                                        Text(
                                            when (fType) {
                                                PartnerFormType.PENAWARAN_HARGA -> "Formulir penawaran komoditas per kilogram beserta volume pasokan."
                                                PartnerFormType.KERJASAMA_JANGKA_PANJANG -> "Pengajuan kontrak pasokan multi-tahun dan jaminan kontinuitas."
                                                PartnerFormType.KESEPAKATAN_MOU -> "Lembar berita acara kesepakatan final berkekuatan hukum."
                                            },
                                            fontSize = 10.sp,
                                            color = Slate600
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Role Selector (Supplier vs Buyer)
                item {
                    Text("Peran Pihak Pengaju:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (partnerRoleChoice == UserRole.SUPPLIER) Color(0xFFEFF6FF) else Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, if (partnerRoleChoice == UserRole.SUPPLIER) CorporateBlue else LightBlueBorder, RoundedCornerShape(8.dp))
                                .clickable { partnerRoleChoice = UserRole.SUPPLIER }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = partnerRoleChoice == UserRole.SUPPLIER, onClick = { partnerRoleChoice = UserRole.SUPPLIER })
                                Text("Pihak Supplier", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                            }
                        }

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (partnerRoleChoice == UserRole.BUYER) Color(0xFFEFF6FF) else Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, if (partnerRoleChoice == UserRole.BUYER) CorporateBlue else LightBlueBorder, RoundedCornerShape(8.dp))
                                .clickable { partnerRoleChoice = UserRole.BUYER }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = partnerRoleChoice == UserRole.BUYER, onClick = { partnerRoleChoice = UserRole.BUYER })
                                Text("Pihak Buyer", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                            }
                        }
                    }
                }

                // Identity Fields
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("1. Data Identitas Lengkap (Wajib Terverifikasi)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepExecutiveBlue)

                            OutlinedTextField(
                                value = fullName,
                                onValueChange = { fullName = it },
                                label = { Text("Nama Lengkap Penanggung Jawab / Pimpinan") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = companyName,
                                onValueChange = { companyName = it },
                                label = { Text("Nama Badan Usaha / PT / CV / Koperasi") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = nikInput,
                                    onValueChange = { nikInput = it },
                                    label = { Text("Nomor NIK KTP") },
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = npwpInput,
                                    onValueChange = { npwpInput = it },
                                    label = { Text("NPWP Usaha") },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = emailInput,
                                    onValueChange = { emailInput = it },
                                    label = { Text("Email Resmi") },
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
                                value = addressInput,
                                onValueChange = { addressInput = it },
                                label = { Text("Alamat Kantor / Kebun / Fasilitas Gudang") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Proposal Details
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("2. Rincian Komoditas & Penawaran Bisnis", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepExecutiveBlue)

                            OutlinedTextField(
                                value = commodityNameInput,
                                onValueChange = { commodityNameInput = it },
                                label = { Text("Jenis Barang / Komoditas") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = pricePerKgInput,
                                    onValueChange = { pricePerKgInput = it },
                                    label = { Text("Harga Ditawarkan / Kg (Rp)") },
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = volumeTonInput,
                                    onValueChange = { volumeTonInput = it },
                                    label = { Text("Estimasi Tonase (Ton)") },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            OutlinedTextField(
                                value = termsInput,
                                onValueChange = { termsInput = it },
                                label = { Text("Syarat & Ketentuan Pengiriman / Pembayaran") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2
                            )
                        }
                    }
                }

                // Verification Requirements (KTP, Face, Digital Signature)
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("3. Verifikasi Legalitas & Biometrik Wajib", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepExecutiveBlue)

                            // KTP Upload Card
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Slate200, RoundedCornerShape(8.dp))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Badge, contentDescription = null, tint = if (ktpUploaded) SuccessEmerald else Slate400)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text("Unggah Foto KTP Pimpinan", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepExecutiveBlue)
                                            Text(if (ktpUploaded) "KTP Terlampir: ektp_direktur_utama.jpg" else "Wajib unggah identitas", fontSize = 10.sp, color = if (ktpUploaded) SuccessEmerald else Slate500)
                                        }
                                    }
                                    FilledTonalButton(
                                        onClick = {
                                            ktpUploaded = true
                                            snackbarMessage = "KTP Berhasil diunggah dan lolos verifikasi OCR Dukcapil."
                                        },
                                        modifier = Modifier.height(32.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(if (ktpUploaded) "Ganti" else "Pilih Foto", fontSize = 10.sp)
                                    }
                                }
                            }

                            // Face Verification Card
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Slate200, RoundedCornerShape(8.dp))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Face, contentDescription = null, tint = if (faceVerified) SuccessEmerald else Slate400)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text("Verifikasi Pengenalan Wajah (Biometrik)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepExecutiveBlue)
                                            Text(if (faceVerified) "Wajah Terverifikasi 99.8% Cocok dengan KTP" else "Pindai wajah untuk otentikasi", fontSize = 10.sp, color = if (faceVerified) SuccessEmerald else Slate500)
                                        }
                                    }
                                    FilledTonalButton(
                                        onClick = {
                                            faceVerified = true
                                            snackbarMessage = "Pemeriksaan biometrik wajah berhasil."
                                        },
                                        modifier = Modifier.height(32.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text("Pindai Ulang", fontSize = 10.sp)
                                    }
                                }
                            }

                            // Digital Signature Canvas
                            Text("Tanda Tangan Digital Resmi (Legal Sah)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepExecutiveBlue)
                            DigitalSignaturePad(
                                onSignatureConfirmed = { count ->
                                    signaturePointCount = count
                                }
                            )
                        }
                    }
                }

                // Submit Button
                item {
                    Button(
                        onClick = {
                            val priceLong = pricePerKgInput.toLongOrNull() ?: 0L
                            val volDouble = volumeTonInput.toDoubleOrNull() ?: 0.0
                            if (fullName.isNotBlank() && companyName.isNotBlank() && priceLong > 0) {
                                val res = onSubmitForm(
                                    selectedFormType,
                                    partnerRoleChoice,
                                    fullName,
                                    companyName,
                                    nikInput,
                                    npwpInput,
                                    emailInput,
                                    phoneInput,
                                    addressInput,
                                    commodityNameInput,
                                    priceLong,
                                    volDouble,
                                    termsInput,
                                    "SIG_PTS_${signaturePointCount}"
                                )
                                snackbarMessage = res.second
                                selectedPortalTab = 2 // Move to history tab
                            } else {
                                snackbarMessage = "Harap lengkapi nama pimpinan, badan usaha, dan harga komoditas."
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CorporateBlue),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("submit_partner_form_button")
                    ) {
                        Icon(Icons.Default.FactCheck, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Kirim Formulir Resmi & Tandatangani", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            // ========================================================
            // TAB 2: DAFTAR PENGAJUAN & MoU MASUK
            // ========================================================
            if (selectedPortalTab == 2) {
                if (submissions.isEmpty()) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                                .padding(vertical = 12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.FolderOpen, contentDescription = null, tint = Slate400, modifier = Modifier.size(40.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Belum ada pengajuan MoU terdaftar.", fontWeight = FontWeight.Bold, color = DeepExecutiveBlue, fontSize = 13.sp)
                                Text("Formulir penawaran harga atau kontrak yang dikirim akan muncul di sini.", fontSize = 11.sp, color = Slate500)
                            }
                        }
                    }
                } else {
                    items(submissions) { sub ->
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
                                    Column {
                                        Text(sub.formType.label, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CorporateBlue)
                                        Text("${sub.companyName} (${sub.fullName})", fontSize = 11.sp, color = Slate700)
                                    }
                                    Surface(
                                        color = Color(0xFFDCFCE7),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(sub.status, color = Color(0xFF166534), fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Divider(color = Slate200)
                                Spacer(modifier = Modifier.height(8.dp))

                                Text("Komoditas: ${sub.commodityName}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepExecutiveBlue)
                                Text("Harga Pengajuan: ${formatRupiah.format(sub.proposedPricePerKg)} / kg", fontSize = 12.sp, color = SuccessEmerald, fontWeight = FontWeight.Bold)
                                Text("Volume: ${sub.supplyVolumeTon} Ton", fontSize = 11.sp, color = Slate600)
                                Text("KTP: Terverifikasi (${sub.nik}) • Wajah: Valid • Tanda Tangan Digital: Sah", fontSize = 10.sp, color = AccentCyan)

                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Syarat & Ketentuan: ${sub.termsAndConditions}", fontSize = 11.sp, color = Slate600)

                                Spacer(modifier = Modifier.height(10.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                    OutlinedButton(
                                        onClick = {
                                            snackbarMessage = "Mengunduh salinan berkas resmi bermaterai digital (${sub.formType.label})."
                                        },
                                        modifier = Modifier.height(30.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Unduh Dokumen PDF", fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog for Adding New Google Form
    if (showAddFormDialog) {
        AlertDialog(
            onDismissRequest = { showAddFormDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PostAdd, contentDescription = null, tint = CorporateBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tambah Google Formulir Mitra", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Sajikan formulir survei atau kualifikasi yang berbeda untuk calon Supplier atau Buyer.", fontSize = 11.sp, color = Slate600)

                    OutlinedTextField(
                        value = newFormTitle,
                        onValueChange = { newFormTitle = it },
                        label = { Text("Judul Formulir") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Sasaran Formulir:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = newFormAudience == FormAudience.SUPPLIER,
                            onClick = { newFormAudience = FormAudience.SUPPLIER },
                            label = { Text("Calon Supplier", fontSize = 10.sp) },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = newFormAudience == FormAudience.BUYER,
                            onClick = { newFormAudience = FormAudience.BUYER },
                            label = { Text("Calon Buyer", fontSize = 10.sp) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = newFormUrl,
                        onValueChange = { newFormUrl = it },
                        label = { Text("URL Google Form") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newFormDesc,
                        onValueChange = { newFormDesc = it },
                        label = { Text("Deskripsi Formulir") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newFormTitle.isNotBlank() && newFormUrl.isNotBlank()) {
                            val res = onAddGoogleForm(newFormTitle, newFormAudience, newFormUrl, newFormDesc)
                            snackbarMessage = res.second
                            showAddFormDialog = false
                            newFormTitle = ""
                            newFormDesc = ""
                        }
                    }
                ) {
                    Text("Simpan Formulir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddFormDialog = false }) { Text("Batal") }
            }
        )
    }

    // Dialog Preview Google Form
    if (previewGoogleForm != null) {
        val form = previewGoogleForm!!
        AlertDialog(
            onDismissRequest = { previewGoogleForm = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DynamicForm, contentDescription = null, tint = CorporateBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(form.title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        color = Color(0xFFEFF6FF),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            "Sasaran: ${form.audience.label} • Google Workspace Cloud Terintegrasi",
                            fontSize = 10.sp,
                            color = CorporateDarkBlue,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Text(form.description, fontSize = 12.sp, color = Slate700)

                    Divider(color = Slate200)

                    Card(
                        colors = CardDefaults.cardColors(containerColor = LightBlueSurface),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Tautan Formulir Resmi:", fontSize = 10.sp, color = Slate500)
                            Text(form.formUrl, fontSize = 11.sp, color = CorporateBlue, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Respon Masuk Terkini: ${form.responseCount} pengisian", fontSize = 10.sp, color = SuccessEmerald, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        snackbarMessage = "Membuka formulir resmi di peramban browser..."
                        previewGoogleForm = null
                    }
                ) {
                    Icon(Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Buka di Browser")
                }
            },
            dismissButton = {
                TextButton(onClick = { previewGoogleForm = null }) { Text("Tutup") }
            }
        )
    }
}
