package com.example.data.model

data class AdvocateMember(
    val name: String,
    val title: String,
    val barLicenseNo: String,
    val specialization: String,
    val experienceYears: Int
)

data class LegalTestimonial(
    val id: String,
    val clientName: String,
    val companyName: String,
    val caseType: String,
    val outcomeSummary: String,
    val rating: Int = 5,
    val year: String = "2026"
)

data class ConsultationBooking(
    val id: String,
    val clientName: String,
    val organization: String,
    val email: String,
    val phone: String,
    val topic: String, // Kontrak Bisnis, Sengketa Perdata, Perizinan Korporasi, Audit Ketenagakerjaan
    val preferredDate: String,
    val caseBrief: String,
    val status: String = "Jadwal Dikonfirmasi"
)

data class LegalAidRequest(
    val id: String,
    val applicantName: String,
    val contact: String,
    val category: String, // Hak Ketenagakerjaan, Perlindungan Konsumen, UMKM Mitra
    val issueDescription: String,
    val urgencyLevel: String, // Segera, Prioritas, Normal
    val status: String = "Dalam Penelaahan Advokat"
)

data class LegalPartnershipApplication(
    val id: String,
    val applicantName: String,
    val firmOrAdvocateOffice: String,
    val location: String,
    val expertiseArea: String,
    val proposalNote: String,
    val status: String = "Tahap Verifikasi Berkas"
)
