package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.data.model.PaymentPlatform
import com.example.data.model.PayrollSlip
import com.example.data.model.User
import com.example.data.model.UserRole
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayrollScreen(
    currentUser: User,
    payrollSlips: List<PayrollSlip>,
    onDisbursePayroll: (slipId: String, platform: PaymentPlatform) -> Pair<Boolean, String>,
    onBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Semua") }
    var selectedSlipForPayment by remember { mutableStateOf<PayrollSlip?>(null) }
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

    val isCurrentAdminAuthorized = currentUser.role == UserRole.SUPER_ADMIN || currentUser.role == UserRole.SECOND_ADMIN

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Payroll & Platform Pembayaran", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("Perhitungan Berdasarkan Kehadiran & Jenis Pekerja", fontSize = 11.sp, color = Slate500)
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
            // Executive Payroll Summary Banner
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyDark),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "TOTAL ANGGARAN PAYROLL BULAN BERJALAN",
                            color = Slate400,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        val totalNet = payrollSlips.sumOf { it.netSalary }
                        Text(
                            text = formatRupiah.format(totalNet),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = Slate700)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Platform Gateway Terhubung:", fontSize = 10.sp, color = Slate400)
                                Text("Midtrans • Xendit • BCA • Mandiri • QRIS", fontSize = 11.sp, color = AccentCyanLight, fontWeight = FontWeight.Bold)
                            }
                            Surface(
                                color = Color(0xFF065F46),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Otomatisasi Presensi 100%",
                                    color = Color(0xFFA7F3D0),
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Employee Category Filter Chips (Strict requirement: "MEMISAHKAN ANTARA KARYAWAN TETAP DENGAN PEKERJA HARIAN/BORONGAN")
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Semua", "Karyawan Tetap", "Pekerja Harian/Borongan").forEach { cat ->
                        FilterChip(
                            selected = selectedFilter == cat,
                            onClick = { selectedFilter = cat },
                            label = { Text(cat, fontSize = 11.sp) }
                        )
                    }
                }
            }

            val filteredSlips = payrollSlips.filter { slip ->
                when (selectedFilter) {
                    "Karyawan Tetap" -> slip.employeeType == UserRole.KARYAWAN_TETAP
                    "Pekerja Harian/Borongan" -> slip.employeeType == UserRole.PEKERJA_HARIAN_BORONGAN
                    else -> true
                }
            }

            items(filteredSlips) { slip ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(slip.userName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Surface(
                                    color = if (slip.employeeType == UserRole.KARYAWAN_TETAP) Color(0xFFDCFCE7) else Color(0xFFFFEDD5),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (slip.employeeType == UserRole.KARYAWAN_TETAP) "Karyawan Tetap (Gaji Bulanan)" else "Pekerja Harian / Borongan (Per Hadir/Unit)",
                                        color = if (slip.employeeType == UserRole.KARYAWAN_TETAP) Color(0xFF166534) else Color(0xFF9A3412),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Surface(
                                color = if (slip.isPaid) Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (slip.isPaid) "LUNAS / TERBAYAR" else "MENUNGGU TRANSFER",
                                    color = if (slip.isPaid) Color(0xFF166534) else Color(0xFF92400E),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = Slate200)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Calculation Breakdown
                        Text("Rincian Perhitungan Gaji Berdasarkan Presensi:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate700)
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Kehadiran Tercatat:", fontSize = 11.sp, color = Slate600)
                            Text("${slip.attendanceDays} Hari Kerja", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        if (slip.totalWorkUnits > 0) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Volume Borongan Diselesaikan:", fontSize = 11.sp, color = Slate600)
                                Text("${slip.totalWorkUnits} Unit / Sak", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(if (slip.employeeType == UserRole.KARYAWAN_TETAP) "Gaji Pokok Bulanan:" else "Tarif Dasar Upah:", fontSize = 11.sp, color = Slate600)
                            Text(formatRupiah.format(slip.baseRate), fontSize = 11.sp)
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Tunjangan Makan & Transport:", fontSize = 11.sp, color = Slate600)
                            Text("+ ${formatRupiah.format(slip.transportMealAllowance)}", fontSize = 11.sp, color = SuccessEmerald)
                        }

                        if (slip.overtimeBonus > 0) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Uang Lembur / Bonus:", fontSize = 11.sp, color = Slate600)
                                Text("+ ${formatRupiah.format(slip.overtimeBonus)}", fontSize = 11.sp, color = SuccessEmerald)
                            }
                        }

                        if (slip.deductions > 0) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Potongan (BPJS / Pajak PPh21):", fontSize = 11.sp, color = Slate600)
                                Text("- ${formatRupiah.format(slip.deductions)}", fontSize = 11.sp, color = ErrorRose)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = Slate200)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Net Salary
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("Total Upah Diterima (Take Home Pay):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate800)
                                Text(
                                    text = formatRupiah.format(slip.netSalary),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CorporateBlue
                                )
                            }

                            if (slip.isPaid) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Gateway: ${slip.paymentPlatform.label}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate700)
                                    Text("Ref: ${slip.transactionRef}", fontSize = 9.sp, color = Slate500)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    snackbarMessage = "Slip Gaji digital ${slip.userName} (${slip.period}) berhasil diunduh (PDF)."
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Unduh Slip PDF", fontSize = 11.sp)
                            }

                            if (!slip.isPaid && isCurrentAdminAuthorized) {
                                Button(
                                    onClick = { selectedSlipForPayment = slip },
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .testTag("disburse_payroll_button_${slip.id}"),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Icon(Icons.Default.SendToMobile, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Salurkan Gaji", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Payment Platform Selection Dialog
    if (selectedSlipForPayment != null) {
        val slip = selectedSlipForPayment!!
        AlertDialog(
            onDismissRequest = { selectedSlipForPayment = null },
            title = { Text("Pilih Gateway / Platform Pembayaran Gaji") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Karyawan: ${slip.userName}\nJumlah: ${formatRupiah.format(slip.netSalary)}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CorporateBlue
                    )
                    Text("Pilih saluran integrasi disbursement:", fontSize = 11.sp, color = Slate600)

                    PaymentPlatform.values().forEach { platform ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Slate100),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val res = onDisbursePayroll(slip.id, platform)
                                    snackbarMessage = res.second
                                    selectedSlipForPayment = null
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(platform.label, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text(platform.providerType, fontSize = 10.sp, color = Slate500)
                                }
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Slate400)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedSlipForPayment = null }) { Text("Batal") }
            }
        )
    }
}
