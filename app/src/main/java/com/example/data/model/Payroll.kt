package com.example.data.model

enum class PaymentPlatform(val label: String, val providerType: String) {
    MIDTRANS("Midtrans Disburse", "Payment Gateway API"),
    XENDIT("Xendit XenPlatform", "Instant Payout"),
    BCA_TRANSFER("BCA Corporate Transfer", "Virtual Account / API"),
    MANDIRI_PAYROLL("Mandiri MCM Payroll", "Host to Host"),
    BRI_CASHLINK("BRI CashLink", "Direct Debit"),
    QRIS_SETTLEMENT("QRIS Bisnis Settlement", "National QR Switch")
}

data class PayrollSlip(
    val id: String,
    val userId: String,
    val userName: String,
    val companyId: String,
    val branchId: String,
    val period: String, // e.g. "November 2026"
    val employeeType: UserRole, // KARYAWAN_TETAP vs PEKERJA_HARIAN_BORONGAN
    val attendanceDays: Int,
    val totalWorkUnits: Int = 0, // For borongan workers (e.g. 150 sak / 80 kg)
    val baseRate: Long, // Monthly for tetap, daily or per-unit for borongan
    val overtimeBonus: Long = 0L,
    val transportMealAllowance: Long = 0L,
    val deductions: Long = 0L,
    val netSalary: Long,
    val paymentPlatform: PaymentPlatform,
    val transactionRef: String,
    val isPaid: Boolean = false,
    val paidAtTimestamp: String? = null
)
