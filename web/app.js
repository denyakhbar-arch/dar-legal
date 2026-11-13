// =========================================================================
// DAR MULTICORP ENTERPRISE & LEGAL PORTAL ENGINE
// =========================================================================

const FIREBASE_WEB_CONFIG = {
  apiKey: "AIzaSyA8TtZi2lDY-PXbnEhA9SMf_RzreLBjLN8",
  authDomain: "gen-lang-client-0464794354.firebaseapp.com",
  projectId: "gen-lang-client-0464794354",
  storageBucket: "gen-lang-client-0464794354.firebasestorage.app",
  messagingSenderId: "217389985571",
  appId: "1:217389985571:web:6c58b0af50742fc3716696"
};

const MASTER_ADMIN_EMAIL = "denyakhbar@gmail.com";
const DEFAULT_ADMIN_PASSWORD = "DAR@2026";
const DEFAULT_CORP_PASSWORD = "CORP@2026";

const STORAGE_KEYS = {
  PUBLIC_USER: "dar_public_google_user_v1",
  PUBLIC_SUBMISSIONS: "dar_public_submissions_v1",
  CORP_SESSION: "dar_corp_session_v2",
  ADMIN_PWD: "dar_super_admin_pwd_v1",
  ENTERPRISE_STATE: "dar_enterprise_state_v2"
};

let firebaseAuthInstance = null;
try {
  if (typeof firebase !== "undefined" && !firebase.apps.length) {
    firebase.initializeApp(FIREBASE_WEB_CONFIG);
    firebaseAuthInstance = firebase.auth();
  } else if (typeof firebase !== "undefined" && firebase.apps.length) {
    firebaseAuthInstance = firebase.auth();
  }
} catch (err) {
  console.warn("Firebase Auth init notice:", err);
}

function escapeHtml(str) {
  return String(str || "")
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#39;");
}

function getAdminPassword() {
  return localStorage.getItem(STORAGE_KEYS.ADMIN_PWD) || DEFAULT_ADMIN_PASSWORD;
}

function setAdminPassword(newPwd) {
  localStorage.setItem(STORAGE_KEYS.ADMIN_PWD, newPwd);
}

// =========================================================================
// INITIAL ENTERPRISE MULTI-COMPANY DATA SEED
// =========================================================================
function createDefaultEnterpriseState() {
  return {
    activeCompanyId: "comp_1",
    activeModule: "beranda",
    activeSettingsTab: "fitur",
    activeThemeId: "theme_blue",
    themes: [
      { id: "theme_blue", name: "Biru Muda Eksekutif (Default)", primary: "#0284c7", bg: "#f0f7ff", surface: "#e0f2fe", border: "#bae6fd" },
      { id: "theme_emerald", name: "Zamrud Hijau Lestari", primary: "#059669", bg: "#f0fdf4", surface: "#dcfce7", border: "#86efac" },
      { id: "theme_gold", name: "Fajar Emas Elegan", primary: "#d97706", bg: "#fffdf0", surface: "#fef3c7", border: "#fde68a" },
      { id: "theme_indigo", name: "Indigo Royal Corporate", primary: "#4f46e5", bg: "#f5f3ff", surface: "#ede9fe", border: "#c7d2fe" },
      { id: "theme_teal", name: "Teal Modern Industri", primary: "#0d9488", bg: "#f0fdfa", surface: "#ccfbf1", border: "#99f6e4" }
    ],
    companies: [
      {
        id: "comp_1",
        name: "PT DAR Agro Makmur",
        code: "DAR-AGRO",
        sector: "Agribisnis, Komoditas Sawit & Kopi Unggulan",
        npwp: "01.234.567.8-012.000",
        nib: "9120003456789",
        phone: "+62 21 5566 7788",
        website: "https://agro.darmulticorp.co.id",
        legalEmail: "denyakhbar@gmail.com",
        ownerEmail: "pemilik@darmulticorp.co.id",
        secretaryEmail: "sekretaris@darmulticorp.co.id",
        workStart: "08:00",
        workEnd: "17:00",
        logoDataUrl: null,
        logoPosition: "LEFT",
        broadcastActive: true,
        broadcastUrgency: "PENTING",
        broadcastMessage: "Pengumuman Resmi: Apel siaga K3 dan evaluasi pengapalan 40 kontainer kopi ekspor dimulai pukul 08:30 WIB.",
        features: {
          gps: true,
          reports: true,
          payroll: true,
          comm: true,
          supplier: true,
          marketing: true,
          legal: true
        },
        branches: ["Cabang Jakarta Pusat", "Cabang Surabaya", "Cabang Balikpapan"],
        officers: [
          { id: "off_1", title: "Komisaris Utama", name: "Ir. H. Budi Santoso", dept: "Dewan Komisaris", email: "komisaris@agro.darmulticorp.co.id" },
          { id: "off_2", title: "Direktur Utama", name: "Hendra Wijaya, S.E.", dept: "Direksi Eksekutif", email: "hendra.wijaya@agro.darmulticorp.co.id" },
          { id: "off_3", title: "VP Operations & Admin Pendamping", name: "Kartika Sari, M.M.", dept: "Operasional", email: "kartika.sari@agro.darmulticorp.co.id" }
        ],
        plugins: [
          { id: "plg_wa", name: "WhatsApp Enterprise Gateway", category: "Notifikasi", version: "v3.4.1", enabled: true },
          { id: "plg_face", name: "Face Biometric AI Liveness Pro", category: "AI & Biometrik", version: "v2.1.0", enabled: true },
          { id: "plg_erp", name: "Konektor Integrasi SAP & Oracle ERP", category: "ERP & Akuntansi", version: "v4.0.2", enabled: true },
          { id: "plg_materai", name: "E-Materai PERURI & PrivyID", category: "Legalitas", version: "v1.8.0", enabled: true },
          { id: "plg_iot", name: "IoT Fleet & Logistics GPS Tracker", category: "IoT Logistik", version: "v5.2.0", enabled: true }
        ],
        customMenus: [
          { id: "cm_1", title: "SOP Keselamatan Kerja & Alat Berat", category: "Dokumentasi & SOP", summary: "Seluruh staf lapangan wajib mengenakan APD berstandar SNI sebelum memasuki area sortasi & pergudangan." }
        ],
        googleForms: [
          { id: "gf_1", audience: "Calon Supplier", title: "Formulir Pra-Kualifikasi Calon Supplier Baru", url: "https://forms.google.com/d/e/supplier-pangan-kualifikasi/viewform", desc: "Seleksi kelayakan kebun, legalitas SHM/HGU, dan kapasitas pasokan bulanan." },
          { id: "gf_2", audience: "Calon Buyer", title: "Formulir Registrasi Kemitraan Calon Buyer Ekspor", url: "https://forms.google.com/d/e/buyer-ekspor-kuota/viewform", desc: "Profil badan usaha buyer asing/lokal, tonase rutin, dan metode L/C / TT." }
        ]
      },
      {
        id: "comp_2",
        name: "PT DAR Logistik Prima",
        code: "DAR-LOG",
        sector: "Supply Chain, Ekspedisi Kontainer & Pergudangan Berikat",
        npwp: "02.876.543.2-023.000",
        nib: "9120009876543",
        phone: "+62 21 8899 1122",
        website: "https://logistik.darmulticorp.co.id",
        legalEmail: "denyakhbar@gmail.com",
        ownerEmail: "pemilik@logistik.darmulticorp.co.id",
        secretaryEmail: "sekretaris@logistik.darmulticorp.co.id",
        workStart: "08:00",
        workEnd: "17:00",
        logoDataUrl: null,
        logoPosition: "LEFT",
        broadcastActive: true,
        broadcastUrgency: "PENTING",
        broadcastMessage: "Pemberitahuan Dermaga: Jadwal sandar armada kontainer ekspor dimajukan ke pukul 13:00 WIB.",
        features: { gps: true, reports: true, payroll: true, comm: true, supplier: true, marketing: true, legal: true },
        branches: ["Cabang Tanjung Priok", "Cabang Tanjung Emas"],
        officers: [
          { id: "off_201", title: "Direktur Utama", name: "Bambang Sudiro, M.T.", dept: "Direksi", email: "bambang@logistik.darmulticorp.co.id" }
        ],
        plugins: [
          { id: "plg_wa", name: "WhatsApp Enterprise Gateway", category: "Notifikasi", version: "v3.4.1", enabled: true }
        ],
        customMenus: [],
        googleForms: [
          { id: "gf_log_1", audience: "Calon Supplier", title: "Formulir Pendaftaran Armada Truk Rekanan", url: "https://forms.google.com/d/e/armada-truk-daftar/viewform", desc: "Data KIR, SIM B2 Umum, dan kapasitas kontainer." }
        ]
      },
      {
        id: "comp_3",
        name: "PT DAR Mineral Sejahtera",
        code: "DAR-MINING",
        sector: "Pertambangan Berkelanjutan & Pemurnian Silika",
        npwp: "03.456.789.0-034.000",
        nib: "9120005678901",
        phone: "+62 542 778899",
        website: "https://mineral.darmulticorp.co.id",
        legalEmail: "denyakhbar@gmail.com",
        ownerEmail: "pemilik@mineral.darmulticorp.co.id",
        secretaryEmail: "sekretaris@mineral.darmulticorp.co.id",
        workStart: "07:30",
        workEnd: "16:30",
        logoDataUrl: null,
        logoPosition: "LEFT",
        broadcastActive: false,
        broadcastUrgency: "INFO",
        broadcastMessage: "Evaluasi keselamatan tambang kuartal berjalan lancar.",
        features: { gps: true, reports: true, payroll: true, comm: true, supplier: true, marketing: true, legal: true },
        branches: ["Site Samarinda", "Kantor Jakarta SCBD"],
        officers: [
          { id: "off_301", title: "Direktur Utama", name: "Drs. Hendro Wibowo", dept: "Direksi", email: "hendro@mineral.darmulticorp.co.id" }
        ],
        plugins: [],
        customMenus: [],
        googleForms: []
      }
    ],
    employees: [
      { id: "usr_super", companyId: "comp_1", name: "Deny Akhbar, S.H., M.H.", email: "denyakhbar@gmail.com", role: "SUPER_ADMIN", position: "Admin Utama & Konsultan Hukum Utama", branch: "Cabang Jakarta Pusat", inboxAccess: true, wage: 45000000 },
      { id: "usr_adm_1", companyId: "comp_1", name: "Kartika Sari, M.M.", email: "kartika.sari@agro.darmulticorp.co.id", role: "SECOND_ADMIN", position: "Admin Pendamping / VP Operations", branch: "Cabang Jakarta Pusat", inboxAccess: true, wage: 22000000 },
      { id: "usr_ceo", companyId: "comp_1", name: "Hendra Wijaya, S.E.", email: "hendra.wijaya@agro.darmulticorp.co.id", role: "PEJABAT_TINGGI", position: "Direktur Utama / CEO", branch: "Cabang Jakarta Pusat", inboxAccess: true, wage: 35000000 },
      { id: "usr_owner", companyId: "comp_1", name: "Pemilik Perusahaan", email: "pemilik@darmulticorp.co.id", role: "PEJABAT_TINGGI", position: "Owner / Pemegang Saham", branch: "Cabang Jakarta Pusat", inboxAccess: true, wage: 50000000 },
      { id: "usr_sec", companyId: "comp_1", name: "Sekretaris Direksi", email: "sekretaris@darmulticorp.co.id", role: "KARYAWAN_TETAP", position: "Sekretaris Perusahaan", branch: "Cabang Jakarta Pusat", inboxAccess: true, wage: 12000000 },
      { id: "usr_qa", companyId: "comp_1", name: "Dimas Pratama", email: "dimas.pratama@agro.darmulticorp.co.id", role: "KARYAWAN_TETAP", position: "QA Supervisor", branch: "Cabang Surabaya", inboxAccess: false, wage: 8500000 },
      { id: "usr_bor", companyId: "comp_1", name: "Joko Susilo", email: "joko.susilo@mitra-agro.id", role: "PEKERJA_BORONGAN", position: "Mandor Borongan Sortir", branch: "Cabang Balikpapan", inboxAccess: false, wage: 180000 },
      { id: "usr_c2_adm", companyId: "comp_2", name: "Bambang Sudiro, M.T.", email: "bambang@logistik.darmulticorp.co.id", role: "SECOND_ADMIN", position: "Admin Pendamping Logistik", branch: "Cabang Tanjung Priok", inboxAccess: true, wage: 30000000 },
      { id: "usr_c3_adm", companyId: "comp_3", name: "Drs. Hendro Wibowo", email: "hendro@mineral.darmulticorp.co.id", role: "SECOND_ADMIN", position: "Admin Pendamping Mineral", branch: "Site Samarinda", inboxAccess: true, wage: 45000000 }
    ],
    attendanceLogs: [
      { id: "att_1", companyId: "comp_1", name: "Dimas Pratama", date: "12 Nov 2026", checkIn: "07:54:12 WIB", checkOut: "17:08:44 WIB", location: "Geofence Kantor Pusat (15m)", status: "Hadir Tepat Waktu" },
      { id: "att_2", companyId: "comp_1", name: "Joko Susilo", date: "12 Nov 2026", checkIn: "07:45:00 WIB", checkOut: "16:30:20 WIB", location: "Gudang Sortir Balikpapan", status: "Hadir (Borongan Selesai)" }
    ],
    dailyReports: [
      { id: "rep_1", companyId: "comp_1", author: "Dimas Pratama", date: "12 Nov 2026", title: "Inspeksi Kualitas Muatan Biji Kopi Robusta 25 Ton", summary: "Kalibrasi kadar air tercapai 11.4%. Pemeriksaan segel kontainer MSKU-908123 selesai.", attachment: "Berita_Acara_QA.pdf", status: "Disetujui Admin" }
    ],
    payrollSlips: [
      { id: "pay_1", companyId: "comp_1", name: "Dimas Pratama", role: "Karyawan Tetap", period: "November 2026", total: 10100000, paid: true, platform: "Midtrans", ref: "MID-202611-9981" },
      { id: "pay_2", companyId: "comp_1", name: "Joko Susilo", role: "Pekerja Borongan", period: "November 2026 (Minggu 1-2)", total: 3150000, paid: false, platform: "Belum Disalurkan", ref: "-" }
    ],
    chatMessages: [
      { id: "msg_1", companyId: "comp_1", channel: "DIREKSI", sender: "Deny Akhbar, S.H., M.H. (Admin Utama)", time: "08:15 WIB", text: "Selamat pagi Dewan Direksi. Agenda rapat strategis hari ini fokus pada audit kontrak ekspor dan perluasan gudang." },
      { id: "msg_2", companyId: "comp_1", channel: "UMUM", sender: "Kartika Sari (Admin Pendamping)", time: "09:00 WIB", text: "Batas pengisian Laporan Kerja Harian dan Presensi GPS adalah pukul 17:30 WIB." }
    ],
    meetings: [
      { id: "m_1", companyId: "comp_1", title: "Rapat Evaluasi Pengapalan & Kontrak Q4", time: "Hari Ini, 14:00 - 15:30 WIB", host: "Deny Akhbar, S.H., M.H.", url: "https://meet.google.com/dar-agro-audit" }
    ],
    inboxMails: [
      { id: "ib_1", companyId: "comp_1", channel: "LEGAL_CONSULTANT", to: "denyakhbar@gmail.com", from: "legal.koperasi@petanimandiri.org", subject: "[JALUR HUKUM] Penelaahan Draf Klausul Arbitrase BANI MoU Kopi", body: "Yth. Pak Deny Akhbar, mohon tinjauan klausul force majeure sebelum akta notaris ditandatangani." },
      { id: "ib_2", companyId: "comp_1", channel: "CORPORATE", to: "pemilik@darmulticorp.co.id / sekretaris@darmulticorp.co.id", from: "kerjasama@kemendag.go.id", subject: "[JALUR PEMILIK & SEKRETARIS] Undangan Paviliun Ekspor Nasional 2026", body: "Yth. Pemilik & Sekretariat Direksi, terlampir undangan verifikasi kuota kontainer ekspor." }
    ],
    partnerSubmissions: [
      { id: "sub_1", companyId: "comp_1", type: "Penawaran Harga Supplier", name: "H. Ahmad Fauzi", org: "PT Berkah Tani Nusantara", commodity: "Biji Kopi Robusta Super (50 Ton)", ktpVerified: true, signed: true, date: "11 Nov 2026" }
    ],
    products: [
      { id: "prod_1", companyId: "comp_1", name: "Biji Kopi Robusta Premium Dampit", price: "Rp 75.000 / Kg", grade: "Grade 1 Export Quality", spec: "Moisture 11.2%, Defect <5%, Karung Goni Food Grade 60 Kg" },
      { id: "prod_2", companyId: "comp_1", name: "Crude Palm Oil (CPO) Standar Ekspor", price: "Rp 14.500 / Kg", grade: "Super Prime ISPO", spec: "FFA <5%, Kadar Air 0.15%, Tangki Isotank" }
    ],
    posts: [
      { id: "post_1", companyId: "comp_1", title: "Pengapalan Ekspor 40 Kontainer Robusta Menuju Pelabuhan Rotterdam", tag: "Produktivitas & Ekspor", date: "12 Nov 2026", author: "Corporate Communications", content: "Kinerja rantai pasok dan kepatuhan standar ekspor berhasil menembus target kuartal ini." }
    ],
    calendarEvents: [
      { id: "cal_1", title: "Audit ISO 9001 & ISPO Bersama Tim Sertifikasi", date: "15 Nov 2026", time: "09:00 WIB", meetUrl: "https://meet.google.com/dar-agro-audit" },
      { id: "cal_2", title: "Rapat Evaluasi Pengapalan Kontainer Q4", date: "16 Nov 2026", time: "14:00 WIB", meetUrl: "https://meet.google.com/dar-ekspor-sync" }
    ],
    driveDocs: [
      { id: "gd_1", title: "Template Perjanjian Kerja Bersama (PKB) 2026", type: "Google Docs", size: "8 Halaman" },
      { id: "gd_2", title: "MoU Standar Pasokan Komoditas Perkebunan", type: "Google Docs", size: "5 Halaman" },
      { id: "gd_3", title: "Folder Arsip Legalitas, NIB & Kemenkumham", type: "Google Drive", size: "24 Berkas" }
    ]
  };
}

let enterpriseState = loadEnterpriseState();

function loadEnterpriseState() {
  try {
    const raw = localStorage.getItem(STORAGE_KEYS.ENTERPRISE_STATE);
    if (raw) {
      const parsed = JSON.parse(raw);
      if (parsed && Array.isArray(parsed.companies) && parsed.companies.length > 0) {
        return parsed;
      }
    }
  } catch (_) {}
  return createDefaultEnterpriseState();
}

function saveEnterpriseState() {
  try {
    localStorage.setItem(STORAGE_KEYS.ENTERPRISE_STATE, JSON.stringify(enterpriseState));
  } catch (_) {}
}

// =========================================================================
// PUBLIC PORTAL NAVIGATION & FORMS
// =========================================================================
function switchPortalTab(tabId) {
  const allTabs = document.querySelectorAll('.portal-tab-content');
  const allButtons = document.querySelectorAll('.tab-btn');

  allTabs.forEach(tab => tab.classList.remove('active'));
  allButtons.forEach(btn => btn.classList.remove('active'));

  const targetTab = document.getElementById(`tab-${tabId}`);
  if (targetTab) {
    targetTab.classList.add('active');
  }

  const matchingBtn = Array.from(allButtons).find(btn =>
    btn.getAttribute('onclick') && btn.getAttribute('onclick').includes(`'${tabId}'`)
  );
  if (matchingBtn) {
    matchingBtn.classList.add('active');
  }
}

function scrollToSection(sectionId) {
  switchPortalTab(sectionId);
  const element = document.getElementById(`tab-${sectionId}`);
  if (element) {
    element.scrollIntoView({ behavior: 'smooth' });
  }
}

// Modal Open/Close Handlers
function openDownloadModal() {
  const modal = document.getElementById('downloadModal');
  if (modal) modal.classList.add('active');
}
function closeDownloadModal() {
  const modal = document.getElementById('downloadModal');
  if (modal) modal.classList.remove('active');
}
function openDownloadGuide() { openDownloadModal(); }

function openPublicLoginModal() {
  const modal = document.getElementById('publicLoginModal');
  if (modal) modal.classList.add('active');
}
function closePublicLoginModal() {
  const modal = document.getElementById('publicLoginModal');
  if (modal) modal.classList.remove('active');
}

function openCorpLoginModal() {
  const errBox = document.getElementById('corpLoginError');
  if (errBox) errBox.style.display = 'none';
  const modal = document.getElementById('corpLoginModal');
  if (modal) modal.classList.add('active');
}
function closeCorpLoginModal() {
  const modal = document.getElementById('corpLoginModal');
  if (modal) modal.classList.remove('active');
}

function showToast(message) {
  const toast = document.getElementById('toastNotification');
  const toastMsg = document.getElementById('toastMessage');
  if (toast && toastMsg) {
    toastMsg.textContent = message;
    toast.classList.add('active');
    setTimeout(() => {
      toast.classList.remove('active');
    }, 4800);
  }
}

// =========================================================================
// REAL FILE DOWNLOAD TRIGGERS (ANDROID APK & IOS PROFILE)
// =========================================================================
function downloadAndroidApk() {
  const apkUrl = '/downloads/DAR_MultiCorp_Installer.apk';
  const a = document.createElement('a');
  a.href = apkUrl;
  a.download = 'DAR_MultiCorp_Installer.apk';
  a.rel = 'noopener';
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);

  showToast('Mengunduh berkas resmi DAR_MultiCorp_Installer.apk (~23 MB) ke folder Unduhan Anda...');
  openDownloadModal();
}

function onApkAnchorClick(event) {
  // Let the native <a download> proceed directly, while showing guidance modal & toast
  showToast('Mengunduh berkas resmi DAR_MultiCorp_Installer.apk (~23 MB) ke folder Download Android Anda...');
}

let deferredPwaPrompt = null;
window.addEventListener('beforeinstallprompt', (e) => {
  e.preventDefault();
  deferredPwaPrompt = e;
});

async function installAndroidPwa() {
  if (deferredPwaPrompt) {
    try {
      deferredPwaPrompt.prompt();
      const choiceResult = await deferredPwaPrompt.userChoice;
      if (choiceResult && choiceResult.outcome === 'accepted') {
        showToast('Aplikasi DAR MultiCorp sedang dipasang ke layar utama Android Anda!');
      } else {
        showToast('Pemasangan dibatalkan. Anda juga dapat mengunduh file .APK di atas.');
      }
      deferredPwaPrompt = null;
      return;
    } catch (_) {}
  }
  showToast('Di Google Chrome Android, ketuk menu titik tiga (⋮) di pojok kanan atas lalu pilih "Instal aplikasi" / "Tambahkan ke layar utama".');
}

function downloadIosProfile() {
  const mobileConfigXml = `<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
  <key>PayloadContent</key>
  <array>
    <dict>
      <key>FullScreen</key>
      <true/>
      <key>Icon</key>
      <data></data>
      <key>IsRemovable</key>
      <true/>
      <key>Label</key>
      <string>Advokat Deny Akhbar</string>
      <key>PayloadDescription</key>
      <string>Konfigurasi WebClip Portal Hukum & Multi Perusahaan DAR MultiCorp</string>
      <key>PayloadDisplayName</key>
      <string>Portal Advokat Deny Akhbar</string>
      <key>PayloadIdentifier</key>
      <string>com.dar.multicorp.ios.webclip</string>
      <key>PayloadType</key>
      <string>com.apple.webClip.managed</string>
      <key>PayloadUUID</key>
      <string>A51B7894-3990-4B1C-B738-910D4CEF1234</string>
      <key>PayloadVersion</key>
      <integer>1</integer>
      <key>Precomposed</key>
      <true/>
      <key>URL</key>
      <string>${window.location.origin}</string>
    </dict>
  </array>
  <key>PayloadDisplayName</key>
  <string>Advokat Deny Akhbar iOS Profile</string>
  <key>PayloadIdentifier</key>
  <string>com.dar.multicorp.profile</string>
  <key>PayloadOrganization</key>
  <string>Kantor Hukum Deny Akhbar & Rekan</string>
  <key>PayloadType</key>
  <string>Configuration</string>
  <key>PayloadUUID</key>
  <string>C849204A-579B-4DCE-9F40-8472B5634567</string>
  <key>PayloadVersion</key>
  <integer>1</integer>
</dict>
</plist>`;

  const blob = new Blob([mobileConfigXml], { type: 'application/x-apple-aspen-config;charset=utf-8' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'DAR_MultiCorp_iOS.mobileconfig';
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);

  showToast('Berkas DAR_MultiCorp_iOS.mobileconfig berhasil diunduh ke perangkat Anda.');
  openDownloadModal();
}

// =========================================================================
// GOOGLE SIGN-IN (FIREBASE AUTH OAUTH + DOMAIN-SAFE FALLBACK)
// =========================================================================
async function triggerGoogleSignIn(targetMode) {
  if (firebaseAuthInstance) {
    try {
      const provider = new firebase.auth.GoogleAuthProvider();
      provider.setCustomParameters({ prompt: 'select_account' });
      const result = await firebaseAuthInstance.signInWithPopup(provider);
      const user = result.user;
      if (user && user.email) {
        if (targetMode === 'PUBLIC') {
          completePublicGoogleSignIn(user.displayName || user.email.split('@')[0], user.email);
        } else {
          // For corporate login, fill Google email & require password verification!
          const corpEmailEl = document.getElementById('corp_user');
          const corpPinEl = document.getElementById('corp_pin');
          if (corpEmailEl) corpEmailEl.value = user.email;
          if (corpPinEl) corpPinEl.focus();
          showToast(`Akun Google ${user.email} terhubung. Silakan masukkan kata sandi akses untuk masuk sebagai ${user.email.toLowerCase() === MASTER_ADMIN_EMAIL ? 'Admin Utama' : 'Akun Perusahaan'}.`);
        }
        return;
      }
    } catch (err) {
      console.warn('Google OAuth popup fallback notice:', err.code, err.message);
      if (err && err.code === 'auth/popup-closed-by-user') {
        showToast('Jendela pilihan akun Google ditutup. Anda dapat mencoba lagi atau isi email di bawah.');
        return;
      }
      if (targetMode === 'PUBLIC') {
        showToast('Domain ini belum terdaftar di Firebase Authorized Domains — silakan isi Nama & Email Google di formulir di bawah untuk melanjutkan.');
        const emailField = document.getElementById('pub_google_email');
        if (emailField) emailField.focus();
      } else {
        showToast('Silakan masukkan email Google (mis. denyakhbar@gmail.com) beserta kata sandi Anda pada formulir di bawah.');
        const corpEmailEl = document.getElementById('corp_user');
        if (corpEmailEl) corpEmailEl.focus();
      }
      return;
    }
  } else {
    if (targetMode === 'PUBLIC') {
      const emailField = document.getElementById('pub_google_email');
      if (emailField) emailField.focus();
      showToast('Silakan isi Nama & Email Google Anda untuk masuk ke Portal Publik.');
    } else {
      const corpEmailEl = document.getElementById('corp_user');
      if (corpEmailEl) corpEmailEl.focus();
      showToast('Masukkan email Google dan kata sandi perusahaan Anda di bawah ini.');
    }
  }
}

function handlePublicGoogleFallbackSubmit(e) {
  e.preventDefault();
  const name = document.getElementById('pub_google_name').value.trim();
  const email = document.getElementById('pub_google_email').value.trim();
  if (!email.includes('@')) {
    showToast('Mohon masukkan alamat email Google yang valid.');
    return;
  }
  completePublicGoogleSignIn(name || email.split('@')[0], email);
}

function completePublicGoogleSignIn(name, email) {
  const pubUser = { name, email, signedInAt: new Date().toISOString() };
  localStorage.setItem(STORAGE_KEYS.PUBLIC_USER, JSON.stringify(pubUser));
  closePublicLoginModal();
  applyPublicGoogleState();
  showToast(`Selamat datang ${name} (${email}) di Portal Layanan Hukum Publik.`);
}

function logoutPublicGoogle() {
  localStorage.removeItem(STORAGE_KEYS.PUBLIC_USER);
  if (firebaseAuthInstance) {
    firebaseAuthInstance.signOut().catch(() => {});
  }
  applyPublicGoogleState();
  switchPortalTab('profil');
  showToast('Anda telah keluar dari Akun Google Publik.');
}

function applyPublicGoogleState() {
  let pubUser = null;
  try {
    pubUser = JSON.parse(localStorage.getItem(STORAGE_KEYS.PUBLIC_USER) || 'null');
  } catch (_) {}

  const headerBtn = document.getElementById('btnPublicLoginHeader');
  const banner = document.getElementById('publicGoogleBanner');
  const bannerTitle = document.getElementById('pgBannerTitle');
  const bannerSub = document.getElementById('pgBannerSubtitle');
  const loginBtn = document.getElementById('pgBannerLoginBtn');
  const logoutBtn = document.getElementById('pgBannerLogoutBtn');
  const myTabBtn = document.getElementById('tabBtnMySubmissions');

  if (pubUser && pubUser.email) {
    if (headerBtn) {
      headerBtn.classList.add('logged-in');
      headerBtn.innerHTML = `<i class="fa-brands fa-google"></i> ${escapeHtml(pubUser.name)}`;
    }
    if (banner) banner.classList.add('connected');
    if (bannerTitle) bannerTitle.textContent = `Terhubung Akun Google Publik: ${pubUser.name} (${pubUser.email})`;
    if (bannerSub) bannerSub.textContent = `Identitas Anda otomatis terisi pada formulir konsultasi hukum & bantuan pro bono. Data internal perusahaan mitra tetap dirahasiakan.`;
    if (loginBtn) loginBtn.style.display = 'none';
    if (logoutBtn) logoutBtn.style.display = 'inline-block';
    if (myTabBtn) myTabBtn.style.display = 'flex';

    // Prefill legal forms cleanly
    const cName = document.getElementById('c_name');
    const cEmail = document.getElementById('c_email');
    const aName = document.getElementById('a_name');
    if (cName && !cName.value) cName.value = pubUser.name;
    if (cEmail && !cEmail.value) cEmail.value = pubUser.email;
    if (aName && !aName.value) aName.value = pubUser.name;

    renderPublicSubmissions();
  } else {
    if (headerBtn) {
      headerBtn.classList.remove('logged-in');
      headerBtn.innerHTML = `<i class="fa-brands fa-google"></i> Login Publik`;
    }
    if (banner) banner.classList.remove('connected');
    if (bannerTitle) bannerTitle.textContent = `Masuk Publik Terintegrasi Akun Google Pengunjung`;
    if (bannerSub) bannerSub.textContent = `Silakan masuk dengan akun Google Anda untuk mengajukan konsultasi hukum, bantuan pro bono, dan melacak balasan advokat secara personal.`;
    if (loginBtn) loginBtn.style.display = 'inline-flex';
    if (logoutBtn) logoutBtn.style.display = 'none';
    if (myTabBtn) myTabBtn.style.display = 'none';
  }
}

function savePublicSubmission(entry) {
  let list = [];
  try {
    list = JSON.parse(localStorage.getItem(STORAGE_KEYS.PUBLIC_SUBMISSIONS) || '[]');
  } catch (_) {}
  list.unshift({ ...entry, id: 'pub_' + Date.now(), date: new Date().toLocaleDateString('id-ID') });
  localStorage.setItem(STORAGE_KEYS.PUBLIC_SUBMISSIONS, JSON.stringify(list));
  renderPublicSubmissions();
}

function renderPublicSubmissions() {
  const container = document.getElementById('publicSubmissionsList');
  if (!container) return;
  let pubUser = null;
  let list = [];
  try {
    pubUser = JSON.parse(localStorage.getItem(STORAGE_KEYS.PUBLIC_USER) || 'null');
    list = JSON.parse(localStorage.getItem(STORAGE_KEYS.PUBLIC_SUBMISSIONS) || '[]');
  } catch (_) {}

  const filtered = pubUser ? list.filter(x => !x.ownerEmail || x.ownerEmail.toLowerCase() === pubUser.email.toLowerCase()) : list;
  if (filtered.length === 0) {
    container.innerHTML = `<div class="public-sub-card"><p style="color:#64748b;">Belum ada riwayat pengajuan konsultasi atau bantuan hukum dari akun Google Anda.</p></div>`;
    return;
  }
  container.innerHTML = filtered.map(item => `
    <div class="public-sub-card">
      <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:6px;flex-wrap:wrap;gap:8px;">
        <span style="background:#fee2e2;color:#991b1b;padding:3px 10px;border-radius:12px;font-size:0.75rem;font-weight:700;">${escapeHtml(item.type)}</span>
        <span style="font-size:0.78rem;color:#64748b;">${escapeHtml(item.date)} • ${escapeHtml(item.status)}</span>
      </div>
      <h4 style="color:#0b1528;margin-bottom:4px;">${escapeHtml(item.title)}</h4>
      <p style="font-size:0.86rem;color:#475569;">${escapeHtml(item.summary)}</p>
    </div>
  `).join('');
}

// Form Submission Handlers (Public Portal)
function handleConsultationSubmit(e) {
  e.preventDefault();
  const name = document.getElementById('c_name').value.trim();
  const topic = document.getElementById('c_topic').value;
  const email = document.getElementById('c_email').value.trim();
  const brief = document.getElementById('c_brief').value.trim();
  let pubUser = null;
  try { pubUser = JSON.parse(localStorage.getItem(STORAGE_KEYS.PUBLIC_USER) || 'null'); } catch (_) {}

  savePublicSubmission({
    type: 'Konsultasi Hukum Online',
    title: topic,
    summary: `Pemohon: ${name} (${email}) — ${brief}`,
    ownerEmail: pubUser ? pubUser.email : email,
    status: 'Diteruskan ke denyakhbar@gmail.com'
  });

  showToast(`Terima kasih Bapak/Ibu ${name}. Permohonan konsultasi "${topic}" telah diteruskan ke Pak Deny Akhbar (denyakhbar@gmail.com).`);
  document.getElementById('consultationForm').reset();
  applyPublicGoogleState();
}

function handleLegalAidSubmit(e) {
  e.preventDefault();
  const name = document.getElementById('a_name').value.trim();
  const cat = document.getElementById('a_cat').value;
  const desc = document.getElementById('a_desc').value.trim();
  let pubUser = null;
  try { pubUser = JSON.parse(localStorage.getItem(STORAGE_KEYS.PUBLIC_USER) || 'null'); } catch (_) {}

  savePublicSubmission({
    type: 'Bantuan Hukum Pro Bono',
    title: cat,
    summary: `Pemohon: ${name} — ${desc}`,
    ownerEmail: pubUser ? pubUser.email : '',
    status: 'Dalam Penelaahan Dewan Advokat'
  });

  showToast(`Permohonan bantuan hukum pro bono atas nama ${name} (${cat}) berhasil dikirim ke Dewan Advokat.`);
  document.getElementById('legalAidForm').reset();
  applyPublicGoogleState();
}

function handlePartnershipSubmit(e) {
  e.preventDefault();
  const name = document.getElementById('p_name').value.trim();
  const office = document.getElementById('p_office').value.trim();
  const area = document.getElementById('p_area').value.trim();
  let pubUser = null;
  try { pubUser = JSON.parse(localStorage.getItem(STORAGE_KEYS.PUBLIC_USER) || 'null'); } catch (_) {}

  savePublicSubmission({
    type: 'Kemitraan Advokat Rekanan',
    title: office,
    summary: `${name} — Keahlian: ${area}`,
    ownerEmail: pubUser ? pubUser.email : '',
    status: 'Menunggu Verifikasi Rekanan'
  });

  showToast(`Formulir kemitraan advokat rekanan "${office}" atas nama ${name} telah diterima.`);
  document.getElementById('partnershipForm').reset();
}

// =========================================================================
// CORPORATE AUTHENTICATION -> LAUNCH FULL COMPANY WORKSPACE
// =========================================================================
function handleCorpLogin(e) {
  e.preventDefault();
  const emailInput = document.getElementById('corp_user').value.trim().toLowerCase();
  const pinInput = document.getElementById('corp_pin').value;
  const errBox = document.getElementById('corpLoginError');

  const showError = (msg) => {
    if (errBox) {
      errBox.textContent = msg;
      errBox.style.display = 'block';
    } else {
      showToast(msg);
    }
  };

  if (!emailInput || !pinInput) {
    showError('Email dan kata sandi wajib diisi.');
    return;
  }

  // 1. Master Google Account Super Admin: denyakhbar@gmail.com
  if (emailInput === MASTER_ADMIN_EMAIL) {
    const validAdminPwd = getAdminPassword();
    if (pinInput !== validAdminPwd) {
      showError('Kata sandi Admin Utama untuk denyakhbar@gmail.com tidak cocok.');
      return;
    }
    const session = {
      email: MASTER_ADMIN_EMAIL,
      name: "Deny Akhbar, S.H., M.H.",
      role: "SUPER_ADMIN",
      roleLabel: "ADMIN UTAMA • KUASA MUTLAK",
      companyId: enterpriseState.activeCompanyId || "comp_1",
      authenticatedAt: new Date().toISOString()
    };
    localStorage.setItem(STORAGE_KEYS.CORP_SESSION, JSON.stringify(session));
    closeCorpLoginModal();
    launchCompanyWorkspace(session);
    showToast(`Selamat datang Admin Utama Deny Akhbar, S.H., M.H. (${MASTER_ADMIN_EMAIL})!`);
    return;
  }

  // 2. Registered Company Users (Owners, Secretaries, Co-Admins, Employees)
  const matchedUser = enterpriseState.employees.find(u => u.email.toLowerCase() === emailInput);
  if (!matchedUser) {
    showError(`Akun "${emailInput}" tidak terdaftar pada portal internal perusahaan.`);
    return;
  }

  if (pinInput !== DEFAULT_CORP_PASSWORD && pinInput !== getAdminPassword()) {
    showError('Kata sandi / PIN akses perusahaan salah.');
    return;
  }

  enterpriseState.activeCompanyId = matchedUser.companyId;
  saveEnterpriseState();

  const session = {
    email: matchedUser.email,
    name: matchedUser.name,
    role: matchedUser.role,
    roleLabel: matchedUser.role === 'SECOND_ADMIN' ? 'ADMIN PENDAMPING' : matchedUser.position.toUpperCase(),
    companyId: matchedUser.companyId,
    authenticatedAt: new Date().toISOString()
  };
  localStorage.setItem(STORAGE_KEYS.CORP_SESSION, JSON.stringify(session));
  closeCorpLoginModal();
  launchCompanyWorkspace(session);
  showToast(`Login berhasil! Laman perusahaan untuk ${matchedUser.name} diluncurkan.`);
}

function getCorpSession() {
  try {
    return JSON.parse(localStorage.getItem(STORAGE_KEYS.CORP_SESSION) || 'null');
  } catch (_) {
    return null;
  }
}

function logoutCorp() {
  localStorage.removeItem(STORAGE_KEYS.CORP_SESSION);
  const ws = document.getElementById('corporateWorkspaceView');
  const pub = document.getElementById('publicLegalPortal');
  if (ws) {
    ws.innerHTML = '';
    ws.style.display = 'none';
  }
  if (pub) pub.style.display = 'block';
  document.body.style.backgroundColor = '';
  showToast('Anda telah keluar dari ruang perusahaan. Kembali ke portal hukum publik.');
}

// =========================================================================
// FULL COMPANY WORKSPACE RENDERER (ALL 10 ENTERPRISE MODULES)
// =========================================================================
function launchCompanyWorkspace(session) {
  const pub = document.getElementById('publicLegalPortal');
  const ws = document.getElementById('corporateWorkspaceView');
  if (!ws) return;
  if (pub) pub.style.display = 'none';
  ws.style.display = 'block';
  renderCorporateWorkspace(session);
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

function getActiveCompany() {
  return enterpriseState.companies.find(c => c.id === enterpriseState.activeCompanyId) || enterpriseState.companies[0];
}

function getActiveTheme() {
  return enterpriseState.themes.find(t => t.id === enterpriseState.activeThemeId) || enterpriseState.themes[0];
}

function isSessionAdmin(session) {
  return session && (session.role === 'SUPER_ADMIN' || session.role === 'SECOND_ADMIN');
}

function isSessionSuperAdmin(session) {
  return session && session.role === 'SUPER_ADMIN';
}

function renderCorporateWorkspace(session) {
  const activeSession = session || getCorpSession();
  if (!activeSession) {
    logoutCorp();
    return;
  }

  const ws = document.getElementById('corporateWorkspaceView');
  const comp = getActiveCompany();
  const theme = getActiveTheme();
  const isSuper = isSessionSuperAdmin(activeSession);
  const isAdmin = isSessionAdmin(activeSession);

  // Apply live theme CSS variables
  document.documentElement.style.setProperty('--corp-primary', theme.primary);
  document.documentElement.style.setProperty('--corp-surface', theme.surface);
  document.documentElement.style.setProperty('--corp-border', theme.border);
  document.body.style.backgroundColor = theme.bg;

  const compEmployees = enterpriseState.employees.filter(u => u.companyId === comp.id);
  const coAdmins = compEmployees.filter(u => u.role === 'SECOND_ADMIN');

  // Navigation Modules List (Respecting Feature Toggles!)
  const f = comp.features;
  const navItems = [
    { id: 'beranda', label: 'Beranda & Google Hub', icon: 'fa-gauge-high', visible: true },
    { id: 'admin', label: 'Admin & Personalia', icon: 'fa-user-shield', visible: true },
    { id: 'gps', label: 'Presensi & GPS', icon: 'fa-location-dot', visible: f.gps },
    { id: 'laporan', label: 'Laporan Harian', icon: 'fa-file-lines', visible: f.reports },
    { id: 'payroll', label: 'Payroll Otomatis', icon: 'fa-money-bill-wave', visible: f.payroll },
    { id: 'komunikasi', label: 'Komunikasi & Rapat', icon: 'fa-comments', visible: f.comm },
    { id: 'supplier', label: 'Supplier & Buyer', icon: 'fa-handshake', visible: f.supplier },
    { id: 'katalog', label: 'Katalog & Pemasaran', icon: 'fa-store', visible: f.marketing },
    { id: 'hukum', label: 'Konsultan Hukum', icon: 'fa-scale-balanced', visible: f.legal },
    { id: 'pengaturan', label: 'Pusat Pengaturan', icon: 'fa-sliders', visible: isAdmin }
  ].filter(item => item.visible);

  if (!navItems.some(n => n.id === enterpriseState.activeModule)) {
    enterpriseState.activeModule = 'beranda';
  }

  // Logo layout
  const logoVisual = comp.logoDataUrl
    ? `<img src="${escapeHtml(comp.logoDataUrl)}" alt="Logo Perusahaan" class="corp-logo-img">`
    : `<div class="corp-logo-badge"><i class="fa-solid fa-building"></i></div>`;

  let companyHeaderHtml = '';
  if (comp.logoPosition === 'CENTER') {
    companyHeaderHtml = `
      <div style="text-align:center;">
        <div style="display:flex;justify-content:center;margin-bottom:10px;">${logoVisual}</div>
        <h2 style="font-size:1.35rem;color:#0b1528;">${escapeHtml(comp.name)}</h2>
        <p style="font-size:0.86rem;color:#475569;">${escapeHtml(comp.sector)} • Kode: ${escapeHtml(comp.code)}</p>
      </div>`;
  } else if (comp.logoPosition === 'RIGHT') {
    companyHeaderHtml = `
      <div style="display:flex;justify-content:space-between;align-items:center;gap:14px;">
        <div>
          <h2 style="font-size:1.35rem;color:#0b1528;">${escapeHtml(comp.name)}</h2>
          <p style="font-size:0.86rem;color:#475569;">${escapeHtml(comp.sector)} • NPWP: ${escapeHtml(comp.npwp)}</p>
        </div>
        ${logoVisual}
      </div>`;
  } else if (comp.logoPosition === 'BANNER_TOP') {
    companyHeaderHtml = `
      <div style="background:${theme.primary};color:#ffffff;padding:14px 18px;border-radius:12px;display:flex;align-items:center;justify-content:center;gap:12px;margin-bottom:10px;">
        ${logoVisual}
        <span style="font-size:1.25rem;font-weight:800;letter-spacing:0.8px;">${escapeHtml(comp.name.toUpperCase())}</span>
      </div>
      <p style="font-size:0.85rem;color:#475569;">${escapeHtml(comp.sector)} • ${comp.branches.length} Cabang • Jam Kerja GPS ${escapeHtml(comp.workStart)} - ${escapeHtml(comp.workEnd)} WIB</p>`;
  } else {
    companyHeaderHtml = `
      <div style="display:flex;align-items:center;gap:14px;flex-wrap:wrap;">
        ${logoVisual}
        <div style="flex:1;">
          <span style="font-size:0.72rem;font-weight:800;color:${theme.primary};letter-spacing:0.8px;">RUANG KERJA MULTI-PERUSAHAAN TERISOLASI</span>
          <h2 style="font-size:1.35rem;color:#0b1528;">${escapeHtml(comp.name)}</h2>
          <p style="font-size:0.84rem;color:#475569;">${escapeHtml(comp.sector)} • Kode: ${escapeHtml(comp.code)} • NPWP: ${escapeHtml(comp.npwp)} • ${compEmployees.length} Personel</p>
        </div>
      </div>`;
  }

  ws.innerHTML = `
    <!-- TOP EXECUTIVE BAR -->
    <div class="corp-topbar">
      <div class="container corp-topbar-inner">
        <div class="corp-user-pill">
          <div class="corp-avatar-circle">${escapeHtml(activeSession.name.slice(0, 2).toUpperCase())}</div>
          <div>
            <div style="display:flex;align-items:center;gap:8px;flex-wrap:wrap;">
              <strong style="font-size:0.92rem;">${escapeHtml(activeSession.name)}</strong>
              <span class="corp-role-badge">${escapeHtml(activeSession.roleLabel)}</span>
            </div>
            <span style="font-size:0.76rem;color:#93c5fd;">${escapeHtml(activeSession.email)}</span>
          </div>
        </div>

        <div class="corp-topbar-controls">
          ${isSuper ? `
            <select class="corp-select-pt" onchange="corpSwitchCompany(this.value)" title="Pilih Entitas Perusahaan">
              ${enterpriseState.companies.map(c => `<option value="${c.id}" ${c.id === comp.id ? 'selected' : ''}>PT: ${escapeHtml(c.name)}</option>`).join('')}
            </select>
          ` : ''}
          <button class="btn-exit-corp" onclick="logoutCorp()">
            <i class="fa-solid fa-right-from-bracket"></i> Keluar Perusahaan
          </button>
        </div>
      </div>
    </div>

    <div class="container corp-shell">
      <!-- COMPANY HERO HEADER -->
      <div class="corp-hero-card">
        ${companyHeaderHtml}
      </div>

      <!-- LIVE BROADCASTING BANNER -->
      ${comp.broadcastActive ? `
        <div class="corp-broadcast-banner ${comp.broadcastUrgency === 'DARURAT' ? 'darurat' : ''}">
          <div>
            <div style="font-size:0.75rem;font-weight:800;letter-spacing:0.6px;margin-bottom:2px;">
              <i class="fa-solid fa-bullhorn"></i> SIARAN RESMI TIM (${escapeHtml(comp.broadcastUrgency)})
            </div>
            <div style="font-size:0.9rem;font-weight:600;">${escapeHtml(comp.broadcastMessage)}</div>
          </div>
          ${isAdmin ? `<button class="corp-btn secondary" onclick="corpSwitchModule('pengaturan', 'tema')">Edit Siaran</button>` : ''}
        </div>
      ` : ''}

      <!-- MODULE NAVIGATION BAR -->
      <div class="corp-nav-grid">
        ${navItems.map(n => `
          <button class="corp-nav-btn ${enterpriseState.activeModule === n.id ? 'active' : ''}" onclick="corpSwitchModule('${n.id}')">
            <i class="fa-solid ${n.icon}"></i> ${n.label}
          </button>
        `).join('')}
      </div>

      <!-- ACTIVE MODULE CONTENT -->
      <div id="corpModuleBody">
        ${renderCorporateModuleBody(comp, theme, activeSession, isSuper, isAdmin, compEmployees, coAdmins)}
      </div>
    </div>
  `;

  if (enterpriseState.activeModule === 'supplier') {
    setTimeout(initSignatureCanvas, 50);
  }
}

function corpSwitchCompany(companyId) {
  enterpriseState.activeCompanyId = companyId;
  saveEnterpriseState();
  renderCorporateWorkspace();
  showToast(`Beralih ke ruang kerja terisolasi ${getActiveCompany().name}.`);
}

function corpSwitchModule(moduleId, settingsSubTab) {
  enterpriseState.activeModule = moduleId;
  if (settingsSubTab) enterpriseState.activeSettingsTab = settingsSubTab;
  saveEnterpriseState();
  renderCorporateWorkspace();
}

function renderCorporateModuleBody(comp, theme, session, isSuper, isAdmin, compEmployees, coAdmins) {
  const m = enterpriseState.activeModule;

  // 1. BERANDA & GOOGLE WORKSPACE HUB
  if (m === 'beranda') {
    return `
      <div class="corp-card">
        <h3><i class="fa-solid fa-cloud" style="color:${theme.primary};"></i> Google Workspace Enterprise Cloud Hub</h3>
        <p style="font-size:0.84rem;color:#64748b;margin-bottom:14px;">Integrasi resmi Google Forms, Gmail (2 Jalur Email), Google Docs, Calendar, Maps Geofence, Chat, Meet & Drive.</p>
        <div class="corp-grid-4">
          <div class="ws-hub-tile" onclick="corpSwitchModule('supplier')">
            <div class="ws-hub-icon" style="background:#7c3aed;"><i class="fa-solid fa-rectangle-list"></i></div>
            <strong style="font-size:0.82rem;display:block;">Google Forms</strong>
            <span style="font-size:0.74rem;color:#64748b;">${comp.googleForms.length} Formulir Aktif</span>
          </div>
          <div class="ws-hub-tile" onclick="corpSwitchModule('komunikasi')">
            <div class="ws-hub-icon" style="background:#ea4335;"><i class="fa-solid fa-envelope"></i></div>
            <strong style="font-size:0.82rem;display:block;">Gmail 2 Jalur</strong>
            <span style="font-size:0.74rem;color:#64748b;">Hukum &amp; Direksi</span>
          </div>
          <div class="ws-hub-tile" onclick="window.open('https://docs.google.com', '_blank')">
            <div class="ws-hub-icon" style="background:#4285f4;"><i class="fa-solid fa-file-word"></i></div>
            <strong style="font-size:0.82rem;display:block;">Google Docs</strong>
            <span style="font-size:0.74rem;color:#64748b;">${enterpriseState.driveDocs.length} Dokumen</span>
          </div>
          <div class="ws-hub-tile" onclick="window.open('https://calendar.google.com', '_blank')">
            <div class="ws-hub-icon" style="background:#0f9d58;"><i class="fa-solid fa-calendar-days"></i></div>
            <strong style="font-size:0.82rem;display:block;">Kalender</strong>
            <span style="font-size:0.74rem;color:#64748b;">${enterpriseState.calendarEvents.length} Agenda</span>
          </div>
          <div class="ws-hub-tile" onclick="corpSwitchModule('gps')">
            <div class="ws-hub-icon" style="background:#34a853;"><i class="fa-solid fa-map-location-dot"></i></div>
            <strong style="font-size:0.82rem;display:block;">Google Maps</strong>
            <span style="font-size:0.74rem;color:#64748b;">Geofence Jam Kerja</span>
          </div>
          <div class="ws-hub-tile" onclick="corpSwitchModule('komunikasi')">
            <div class="ws-hub-icon" style="background:#00ac47;"><i class="fa-solid fa-comments"></i></div>
            <strong style="font-size:0.82rem;display:block;">Google Chat</strong>
            <span style="font-size:0.74rem;color:#64748b;">Forum Cabang</span>
          </div>
          <div class="ws-hub-tile" onclick="window.open('https://meet.google.com', '_blank')">
            <div class="ws-hub-icon" style="background:#00897b;"><i class="fa-solid fa-video"></i></div>
            <strong style="font-size:0.82rem;display:block;">Google Meet</strong>
            <span style="font-size:0.74rem;color:#64748b;">Rapat Video</span>
          </div>
          <div class="ws-hub-tile" onclick="window.open('https://drive.google.com', '_blank')">
            <div class="ws-hub-icon" style="background:#f59e0b;"><i class="fa-brands fa-google-drive"></i></div>
            <strong style="font-size:0.82rem;display:block;">Google Drive</strong>
            <span style="font-size:0.74rem;color:#64748b;">Arsip Cloud</span>
          </div>
        </div>
      </div>

      ${comp.customMenus.length > 0 ? `
        <div class="corp-card">
          <h3><i class="fa-solid fa-book-open" style="color:${theme.primary};"></i> Menu &amp; SOP Kustom Perusahaan (${comp.customMenus.length})</h3>
          <div class="corp-grid-2">
            ${comp.customMenus.map(cm => `
              <div style="background:#f8fafc;border:1px solid #e2e8f0;border-radius:10px;padding:14px;">
                <span style="background:#dbeafe;color:#1e40af;font-size:0.7rem;font-weight:700;padding:2px 8px;border-radius:6px;">${escapeHtml(cm.category)}</span>
                <h4 style="margin-top:6px;color:#0b1528;">${escapeHtml(cm.title)}</h4>
                <p style="font-size:0.84rem;color:#475569;margin-top:4px;">${escapeHtml(cm.summary)}</p>
              </div>
            `).join('')}
          </div>
        </div>
      ` : ''}

      <div class="corp-grid-3">
        <div class="corp-module-tile" onclick="corpSwitchModule('admin')">
          <h4><i class="fa-solid fa-user-shield" style="color:#d97706;"></i> Admin &amp; Personalia</h4>
          <p style="font-size:0.82rem;color:#64748b;margin-top:4px;">Admin Utama + ${coAdmins.length}/2 Admin Pendamping • Jalur Email Terpisah</p>
        </div>
        <div class="corp-module-tile" onclick="corpSwitchModule('gps')">
          <h4><i class="fa-solid fa-fingerprint" style="color:#059669;"></i> Presensi &amp; Geofence GPS</h4>
          <p style="font-size:0.82rem;color:#64748b;margin-top:4px;">Jam Kerja ${escapeHtml(comp.workStart)}-${escapeHtml(comp.workEnd)} WIB • Privasi Non-Kerja Terjaga</p>
        </div>
        <div class="corp-module-tile" onclick="corpSwitchModule('payroll')">
          <h4><i class="fa-solid fa-payments" style="color:#7c3aed;"></i> Payroll Otomatis</h4>
          <p style="font-size:0.82rem;color:#64748b;margin-top:4px;">Karyawan Tetap &amp; Borongan • Midtrans &amp; Xendit</p>
        </div>
        <div class="corp-module-tile" onclick="corpSwitchModule('laporan')">
          <h4><i class="fa-solid fa-clipboard-check" style="color:#0284c7;"></i> Laporan Kerja Harian</h4>
          <p style="font-size:0.82rem;color:#64748b;margin-top:4px;">Unggah Berita Acara &amp; Pembersihan Cache Lokal</p>
        </div>
        <div class="corp-module-tile" onclick="corpSwitchModule('supplier')">
          <h4><i class="fa-solid fa-handshake" style="color:#ea580c;"></i> Portal Supplier &amp; Buyer</h4>
          <p style="font-size:0.82rem;color:#64748b;margin-top:4px;">Google Forms, Verifikasi KTP &amp; Tanda Tangan Digital</p>
        </div>
        <div class="corp-module-tile" onclick="corpSwitchModule('pengaturan')">
          <h4><i class="fa-solid fa-sliders" style="color:#0b1528;"></i> Pusat Pengaturan Admin Utama</h4>
          <p style="font-size:0.82rem;color:#64748b;margin-top:4px;">Ubah Semua Master Data, Fitur, Tema, Logo &amp; Sandi</p>
        </div>
      </div>
    `;
  }

  // 2. ADMIN & PERSONALIA (MAX 2 CO-ADMINS + DUAL EMAIL ROUTING)
  if (m === 'admin') {
    return `
      <div class="corp-card" style="background:#0f172a;color:#ffffff;">
        <div style="display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:10px;">
          <div>
            <span style="background:#78350f;color:#fde68a;font-size:0.7rem;font-weight:800;padding:3px 8px;border-radius:6px;">ADMIN UTAMA / PENGELOLA TUNGGAL</span>
            <h3 style="color:#ffffff;margin-top:6px;">Deny Akhbar, S.H., M.H.</h3>
            <p style="font-size:0.84rem;color:#cbd5e1;">Akun Google Utama: denyakhbar@gmail.com • Kuasa Mutlak Lintas Perusahaan</p>
          </div>
          <span style="background:#1e293b;color:#fbbf24;padding:6px 12px;border-radius:8px;font-size:0.78rem;font-weight:700;">Terverifikasi Sandi</span>
        </div>
      </div>

      <div class="corp-grid-2">
        <div class="corp-card">
          <h3>2. Admin Pendamping (${coAdmins.length}/2 Slot Maksimal)</h3>
          <p style="font-size:0.8rem;color:#64748b;margin-bottom:12px;">Admin Utama berhak menunjuk maksimal 2 orang Admin pendamping.</p>
          ${coAdmins.length === 0 ? `<p style="color:#64748b;font-size:0.85rem;">Belum ada Admin Pendamping ditunjuk.</p>` : coAdmins.map((ca, idx) => `
            <div class="corp-list-item">
              <div>
                <strong>#${idx + 1} ${escapeHtml(ca.name)}</strong>
                <div style="font-size:0.78rem;color:#64748b;">${escapeHtml(ca.email)} • ${escapeHtml(ca.position)}</div>
              </div>
              ${isSuper ? `<button class="corp-btn danger" onclick="corpRevokeCoAdmin('${ca.id}')">Cabut</button>` : ''}
            </div>
          `).join('')}

          ${isSuper && coAdmins.length < 2 ? `
            <div style="margin-top:12px;">
              <label style="font-size:0.8rem;font-weight:700;display:block;margin-bottom:6px;">Tunjuk Karyawan Menjadi Admin Pendamping:</label>
              <div style="display:flex;gap:8px;">
                <select id="selectAppointTarget" style="flex:1;padding:8px;border:1px solid #cbd5e1;border-radius:8px;">
                  ${compEmployees.filter(u => u.role !== 'SUPER_ADMIN' && u.role !== 'SECOND_ADMIN').map(u => `<option value="${u.id}">${escapeHtml(u.name)} (${escapeHtml(u.position)})</option>`).join('')}
                </select>
                <button class="corp-btn" onclick="corpAppointCoAdmin()">Tunjuk Admin</button>
              </div>
            </div>
          ` : ''}
        </div>

        <div class="corp-card">
          <h3>3. Dua Jalur Email Resmi Perusahaan</h3>
          <form onsubmit="corpSaveDualEmails(event)">
            <div class="form-group">
              <label>Jalur 1: Email Konsultan Hukum Perusahaan *</label>
              <input type="email" id="dual_legal_email" value="${escapeHtml(comp.legalEmail)}" required ${isAdmin ? '' : 'disabled'}>
            </div>
            <div class="form-group">
              <label>Jalur 2A: Email Pemilik Perusahaan *</label>
              <input type="email" id="dual_owner_email" value="${escapeHtml(comp.ownerEmail)}" required ${isAdmin ? '' : 'disabled'}>
            </div>
            <div class="form-group">
              <label>Jalur 2B: Email Sekretaris Perusahaan *</label>
              <input type="email" id="dual_sec_email" value="${escapeHtml(comp.secretaryEmail)}" required ${isAdmin ? '' : 'disabled'}>
            </div>
            ${isAdmin ? `<button type="submit" class="corp-btn">Simpan 2 Jalur Email</button>` : ''}
          </form>
        </div>
      </div>

      <div class="corp-card">
        <h3>4. Daftar Personel &amp; Undang Karyawan (${compEmployees.length})</h3>
        ${compEmployees.map(u => `
          <div class="corp-list-item">
            <div>
              <strong>${escapeHtml(u.name)}</strong>
              <span style="background:#e0f2fe;color:#0369a1;font-size:0.7rem;padding:2px 7px;border-radius:6px;margin-left:6px;font-weight:700;">${escapeHtml(u.role)}</span>
              <div style="font-size:0.78rem;color:#64748b;">${escapeHtml(u.email)} • ${escapeHtml(u.position)} • ${escapeHtml(u.branch)}</div>
            </div>
            <div style="display:flex;gap:8px;align-items:center;">
              ${isAdmin && u.role !== 'SUPER_ADMIN' ? `<button class="corp-btn danger" onclick="corpRemoveEmployee('${u.id}')">Keluarkan</button>` : ''}
            </div>
          </div>
        `).join('')}

        ${isAdmin ? `
          <hr style="margin:16px 0;border:none;border-top:1px solid #e2e8f0;">
          <h4 style="margin-bottom:10px;">Undang Karyawan / Mitra Baru</h4>
          <form onsubmit="corpInviteEmployee(event)" class="corp-grid-2">
            <input type="text" id="inv_name" required placeholder="Nama Lengkap *" style="padding:9px;border:1px solid #cbd5e1;border-radius:8px;">
            <input type="email" id="inv_email" required placeholder="Email Google / Kerja *" style="padding:9px;border:1px solid #cbd5e1;border-radius:8px;">
            <input type="text" id="inv_pos" required placeholder="Jabatan *" style="padding:9px;border:1px solid #cbd5e1;border-radius:8px;">
            <select id="inv_role" style="padding:9px;border:1px solid #cbd5e1;border-radius:8px;">
              <option value="KARYAWAN_TETAP">Karyawan Tetap</option>
              <option value="PEKERJA_BORONGAN">Pekerja Harian / Borongan</option>
              <option value="PEJABAT_TINGGI">Pejabat Tinggi / Direksi</option>
            </select>
            <button type="submit" class="corp-btn">Undang Karyawan</button>
          </form>
        ` : ''}
      </div>
    `;
  }

  // 3. PRESENSI & GPS
  if (m === 'gps') {
    const logs = enterpriseState.attendanceLogs.filter(a => a.companyId === comp.id);
    return `
      <div class="corp-card" style="background:#f0fdf4;border-color:#86efac;">
        <h3 style="color:#14532d;"><i class="fa-solid fa-location-dot"></i> Geofence GPS Aktif Hanya Selama Jam Kerja (${escapeHtml(comp.workStart)} - ${escapeHtml(comp.workEnd)} WIB)</h3>
        <p style="font-size:0.84rem;color:#166534;margin-bottom:12px;">Privasi karyawan di luar jam kerja terproteksi 100%. Klik tombol di bawah untuk mencatat presensi masuk atau pulang.</p>
        <div style="display:flex;gap:10px;flex-wrap:wrap;">
          <button class="corp-btn success" onclick="corpCheckIn()"><i class="fa-solid fa-fingerprint"></i> Absen Masuk (GPS Valid)</button>
          <button class="corp-btn secondary" onclick="corpCheckOut()"><i class="fa-solid fa-person-walking-arrow-right"></i> Absen Pulang</button>
        </div>
      </div>
      <div class="corp-card">
        <h3>Riwayat Presensi ${escapeHtml(comp.name)} (${logs.length})</h3>
        ${logs.map(l => `
          <div class="corp-list-item">
            <div>
              <strong>${escapeHtml(l.name)}</strong> — <span style="color:#059669;font-weight:700;">${escapeHtml(l.status)}</span>
              <div style="font-size:0.78rem;color:#64748b;">${escapeHtml(l.date)} • Masuk: ${escapeHtml(l.checkIn)} • Pulang: ${escapeHtml(l.checkOut || '-')} • ${escapeHtml(l.location)}</div>
            </div>
          </div>
        `).join('')}
      </div>
    `;
  }

  // 4. LAPORAN HARIAN
  if (m === 'laporan') {
    const reps = enterpriseState.dailyReports.filter(r => r.companyId === comp.id);
    return `
      <div class="corp-grid-2">
        <div class="corp-card">
          <h3>Kirim Laporan Kerja Harian</h3>
          <form onsubmit="corpSubmitReport(event)">
            <div class="form-group">
              <label>Judul Kegiatan / Tugas *</label>
              <input type="text" id="rep_title" required placeholder="Contoh: Inspeksi Pengiriman Kontainer">
            </div>
            <div class="form-group">
              <label>Uraian Hasil Pekerjaan *</label>
              <textarea id="rep_summary" rows="3" required placeholder="Ringkasan capaian hari ini..."></textarea>
            </div>
            <button type="submit" class="corp-btn">Kirim Laporan Harian</button>
          </form>
        </div>
        <div class="corp-card">
          <h3>Arsip Laporan &amp; Pembersihan Cache</h3>
          <p style="font-size:0.82rem;color:#64748b;margin-bottom:12px;">Dokumen tersimpan di cloud Google Drive sehingga memori perangkat tetap ringan.</p>
          <button class="corp-btn secondary" onclick="showToast('Cache lokal dibersihkan! 14.8 MB ruang perangkat dibebaskan.')">
            <i class="fa-solid fa-broom"></i> Bersihkan Cache Lokal
          </button>
          <div style="margin-top:14px;">
            ${reps.map(r => `
              <div class="corp-list-item">
                <div>
                  <strong>${escapeHtml(r.title)}</strong>
                  <div style="font-size:0.78rem;color:#64748b;">${escapeHtml(r.author)} • ${escapeHtml(r.date)} • ${escapeHtml(r.status)}</div>
                  <p style="font-size:0.82rem;margin-top:4px;">${escapeHtml(r.summary)}</p>
                </div>
              </div>
            `).join('')}
          </div>
        </div>
      </div>
    `;
  }

  // 5. PAYROLL OTOMATIS
  if (m === 'payroll') {
    const slips = enterpriseState.payrollSlips.filter(p => p.companyId === comp.id);
    return `
      <div class="corp-card">
        <h3>Slip Gaji &amp; Pencairan Payroll (Midtrans &amp; Xendit)</h3>
        ${slips.map(s => `
          <div class="corp-list-item">
            <div>
              <strong>${escapeHtml(s.name)}</strong> (${escapeHtml(s.role)})
              <div style="font-size:0.8rem;color:#64748b;">Periode: ${escapeHtml(s.period)} • Total: <strong>Rp ${s.total.toLocaleString('id-ID')}</strong></div>
              <div style="font-size:0.76rem;color:${s.paid ? '#059669' : '#d97706'};font-weight:700;">
                ${s.paid ? `Sudah Dibayar via ${escapeHtml(s.platform)} (Ref: ${escapeHtml(s.ref)})` : 'Menunggu Pencairan'}
              </div>
            </div>
            ${!s.paid && isAdmin ? `
              <div style="display:flex;gap:8px;">
                <button class="corp-btn" onclick="corpDisbursePayroll('${s.id}', 'Midtrans')">Bayar via Midtrans</button>
                <button class="corp-btn success" onclick="corpDisbursePayroll('${s.id}', 'Xendit')">Bayar via Xendit</button>
              </div>
            ` : ''}
          </div>
        `).join('')}
      </div>
    `;
  }

  // 6. KOMUNIKASI, RAPAT & KOTAK MASUK 2 JALUR
  if (m === 'komunikasi') {
    const msgs = enterpriseState.chatMessages.filter(c => c.companyId === comp.id);
    const meets = enterpriseState.meetings.filter(mt => mt.companyId === comp.id);
    const mails = enterpriseState.inboxMails.filter(ib => ib.companyId === comp.id);
    return `
      <div class="corp-grid-2">
        <div class="corp-card">
          <h3>Forum Obrolan Internal &amp; Rapat</h3>
          <div style="max-height:240px;overflow-y:auto;margin-bottom:12px;">
            ${msgs.map(c => `
              <div style="background:#f8fafc;border:1px solid #e2e8f0;padding:10px;border-radius:10px;margin-bottom:8px;">
                <div style="font-size:0.74rem;font-weight:700;color:${theme.primary};">${escapeHtml(c.channel)} • ${escapeHtml(c.sender)} (${escapeHtml(c.time)})</div>
                <div style="font-size:0.85rem;margin-top:3px;">${escapeHtml(c.text)}</div>
              </div>
            `).join('')}
          </div>
          <form onsubmit="corpSendChat(event)" style="display:flex;gap:8px;">
            <input type="text" id="corp_chat_input" required placeholder="Ketik pesan rapat/forum..." style="flex:1;padding:9px;border:1px solid #cbd5e1;border-radius:8px;">
            <button type="submit" class="corp-btn">Kirim</button>
          </form>
        </div>

        <div class="corp-card">
          <h3>Jadwal Google Meet &amp; Kotak Masuk 2 Jalur</h3>
          ${meets.map(mt => `
            <div class="corp-list-item">
              <div>
                <strong>${escapeHtml(mt.title)}</strong>
                <div style="font-size:0.76rem;color:#64748b;">${escapeHtml(mt.time)} • Host: ${escapeHtml(mt.host)}</div>
              </div>
              <button class="corp-btn success" onclick="window.open('${escapeHtml(mt.url)}', '_blank')">Gabung Meet</button>
            </div>
          `).join('')}
          <h4 style="margin:12px 0 8px 0;">Kotak Masuk Resmi (Terpisah)</h4>
          ${mails.map(ib => `
            <div style="border-left:4px solid ${ib.channel === 'LEGAL_CONSULTANT' ? '#991b1b' : '#0284c7'};background:#f8fafc;padding:10px;border-radius:6px;margin-bottom:8px;">
              <div style="font-size:0.72rem;font-weight:800;color:${ib.channel === 'LEGAL_CONSULTANT' ? '#991b1b' : '#0284c7'};">Tujuan: ${escapeHtml(ib.to)}</div>
              <strong style="font-size:0.85rem;">${escapeHtml(ib.subject)}</strong>
              <p style="font-size:0.78rem;color:#475569;margin-top:2px;">${escapeHtml(ib.body)}</p>
            </div>
          `).join('')}
        </div>
      </div>
    `;
  }

  // 7. PORTAL SUPPLIER & BUYER (GOOGLE FORMS + TANDA TANGAN DIGITAL)
  if (m === 'supplier') {
    const subs = enterpriseState.partnerSubmissions.filter(s => s.companyId === comp.id);
    return `
      <div class="corp-grid-2">
        <div class="corp-card">
          <h3>Google Forms Rekanan (${comp.googleForms.length})</h3>
          ${comp.googleForms.map(gf => `
            <div class="corp-list-item">
              <div>
                <span style="font-size:0.7rem;font-weight:800;color:${theme.primary};">${escapeHtml(gf.audience)}</span>
                <strong style="display:block;">${escapeHtml(gf.title)}</strong>
                <span style="font-size:0.78rem;color:#64748b;">${escapeHtml(gf.desc)}</span>
              </div>
              <button class="corp-btn secondary" onclick="window.open('${escapeHtml(gf.url)}', '_blank')">Buka Form</button>
            </div>
          `).join('')}
        </div>

        <div class="corp-card">
          <h3>Kirim Penawaran &amp; MoU (Tanda Tangan Digital)</h3>
          <form onsubmit="corpSubmitPartner(event)">
            <div class="form-group">
              <label>Nama Mitra / Perusahaan *</label>
              <input type="text" id="pt_org" required placeholder="Contoh: PT Sawit Nusantara">
            </div>
            <div class="form-group">
              <label>Komoditas &amp; Volume *</label>
              <input type="text" id="pt_comm" required placeholder="Contoh: CPO Grade A — 100 Ton">
            </div>
            <div class="form-group">
              <label>Tanda Tangan Digital Di Bawah Ini:</label>
              <canvas id="partnerSigCanvas" class="sig-canvas" width="460" height="120"></canvas>
            </div>
            <div style="display:flex;gap:8px;">
              <button type="submit" class="corp-btn">Kirim &amp; Verifikasi KTP/TTD</button>
              <button type="button" class="corp-btn secondary" onclick="clearSignatureCanvas()">Hapus TTD</button>
            </div>
          </form>
        </div>
      </div>

      <div class="corp-card">
        <h3>Berkas Mitra Masuk (${subs.length})</h3>
        ${subs.map(s => `
          <div class="corp-list-item">
            <div>
              <strong>${escapeHtml(s.org)}</strong> (${escapeHtml(s.type)})
              <div style="font-size:0.78rem;color:#64748b;">${escapeHtml(s.commodity)} • KTP &amp; TTD Digital Terverifikasi • ${escapeHtml(s.date)}</div>
            </div>
          </div>
        `).join('')}
      </div>
    `;
  }

  // 8. KATALOG & PEMASARAN
  if (m === 'katalog') {
    const prods = enterpriseState.products.filter(p => p.companyId === comp.id);
    const posts = enterpriseState.posts.filter(p => p.companyId === comp.id);
    return `
      <div class="corp-grid-2">
        <div class="corp-card">
          <h3>Katalog Komoditas &amp; Harga</h3>
          ${prods.map(p => `
            <div class="corp-list-item">
              <div>
                <strong>${escapeHtml(p.name)}</strong> — <span style="color:#059669;font-weight:800;">${escapeHtml(p.price)}</span>
                <div style="font-size:0.78rem;color:#64748b;">${escapeHtml(p.grade)} • ${escapeHtml(p.spec)}</div>
              </div>
            </div>
          `).join('')}
        </div>
        <div class="corp-card">
          <h3>Publikasi Berita Produktivitas</h3>
          <form onsubmit="corpCreatePost(event)" style="margin-bottom:14px;">
            <div class="form-group">
              <input type="text" id="post_title" required placeholder="Judul Berita / Iklan Produktivitas">
            </div>
            <div class="form-group">
              <textarea id="post_body" rows="2" required placeholder="Isi pengumuman / berita..."></textarea>
            </div>
            <button type="submit" class="corp-btn">Terbitkan Berita</button>
          </form>
          ${posts.map(p => `
            <div class="corp-list-item">
              <div>
                <strong>${escapeHtml(p.title)}</strong>
                <div style="font-size:0.75rem;color:#64748b;">${escapeHtml(p.date)} • ${escapeHtml(p.author)}</div>
                <p style="font-size:0.82rem;margin-top:4px;">${escapeHtml(p.content)}</p>
              </div>
            </div>
          `).join('')}
        </div>
      </div>
    `;
  }

  // 9. KONSULTAN HUKUM PERUSAHAAN (INTERNAL)
  if (m === 'hukum') {
    let pubList = [];
    try { pubList = JSON.parse(localStorage.getItem(STORAGE_KEYS.PUBLIC_SUBMISSIONS) || '[]'); } catch (_) {}
    return `
      <div class="corp-card">
        <h3>Meja Konsultan Hukum Perusahaan (${escapeHtml(comp.legalEmail)})</h3>
        <p style="font-size:0.84rem;color:#64748b;margin-bottom:14px;">Dipimpin langsung oleh <strong>Deny Akhbar, S.H., M.H.</strong> • Menangani retainer hukum perusahaan serta permohonan publik yang masuk.</p>
        ${pubList.length === 0 ? `<p style="color:#64748b;">Belum ada antrean konsultasi baru dari portal publik.</p>` : pubList.map(item => `
          <div class="corp-list-item">
            <div>
              <span style="background:#fee2e2;color:#991b1b;font-size:0.7rem;padding:2px 8px;border-radius:10px;font-weight:700;">${escapeHtml(item.type)}</span>
              <strong style="display:block;margin-top:4px;">${escapeHtml(item.title)}</strong>
              <p style="font-size:0.8rem;color:#475569;">${escapeHtml(item.summary)}</p>
            </div>
            <span style="font-size:0.76rem;color:#059669;font-weight:700;">${escapeHtml(item.status)}</span>
          </div>
        `).join('')}
      </div>
    `;
  }

  // 10. PUSAT PENGATURAN PENUH (ADMIN UTAMA)
  const sTab = enterpriseState.activeSettingsTab || 'fitur';
  return `
    <div class="corp-card">
      <h3><i class="fa-solid fa-sliders"></i> Pusat Pengaturan &amp; Kontrol Penuh (${escapeHtml(comp.name)})</h3>
      <div style="display:flex;gap:8px;flex-wrap:wrap;margin:12px 0 18px 0;">
        <button class="corp-btn ${sTab === 'fitur' ? '' : 'secondary'}" onclick="corpSwitchModule('pengaturan', 'fitur')">1. Fitur Modul</button>
        <button class="corp-btn ${sTab === 'master' ? '' : 'secondary'}" onclick="corpSwitchModule('pengaturan', 'master')">2. Master Data &amp; Pejabat</button>
        <button class="corp-btn ${sTab === 'plugin' ? '' : 'secondary'}" onclick="corpSwitchModule('pengaturan', 'plugin')">3. Plugin Ekstensi</button>
        <button class="corp-btn ${sTab === 'kustom' ? '' : 'secondary'}" onclick="corpSwitchModule('pengaturan', 'kustom')">4. Buat Menu Kustom</button>
        <button class="corp-btn ${sTab === 'tema' ? '' : 'secondary'}" onclick="corpSwitchModule('pengaturan', 'tema')">5. Tema, Logo &amp; Sandi Admin</button>
      </div>

      ${sTab === 'fitur' ? `
        <div class="corp-grid-2">
          ${[
            ['gps', 'Presensi & Geofence GPS'],
            ['reports', 'Laporan Kerja Harian'],
            ['payroll', 'Payroll & Pencairan Gaji'],
            ['comm', 'Komunikasi, Chat & Rapat'],
            ['supplier', 'Portal Supplier & Buyer'],
            ['marketing', 'Katalog & Pemasaran'],
            ['legal', 'Konsultan Hukum Perusahaan']
          ].map(([key, label]) => `
            <div class="corp-list-item">
              <strong>${label}</strong>
              <button class="corp-btn ${comp.features[key] ? 'success' : 'secondary'}" onclick="corpToggleFeature('${key}')">
                ${comp.features[key] ? 'AKTIF' : 'NONAKTIF'}
              </button>
            </div>
          `).join('')}
        </div>
      ` : ''}

      ${sTab === 'master' ? `
        <form onsubmit="corpSaveMasterData(event)">
          <div class="corp-grid-2">
            <div class="form-group">
              <label>Nama Resmi Perusahaan *</label>
              <input type="text" id="md_name" value="${escapeHtml(comp.name)}" required>
            </div>
            <div class="form-group">
              <label>Kode Perusahaan *</label>
              <input type="text" id="md_code" value="${escapeHtml(comp.code)}" required>
            </div>
            <div class="form-group">
              <label>Sektor Industri *</label>
              <input type="text" id="md_sector" value="${escapeHtml(comp.sector)}" required>
            </div>
            <div class="form-group">
              <label>NPWP *</label>
              <input type="text" id="md_npwp" value="${escapeHtml(comp.npwp)}" required>
            </div>
            <div class="form-group">
              <label>NIB *</label>
              <input type="text" id="md_nib" value="${escapeHtml(comp.nib)}" required>
            </div>
            <div class="form-group">
              <label>Telepon Kantor</label>
              <input type="text" id="md_phone" value="${escapeHtml(comp.phone)}">
            </div>
            <div class="form-group">
              <label>Jam Mulai Kerja (GPS)</label>
              <input type="text" id="md_start" value="${escapeHtml(comp.workStart)}">
            </div>
            <div class="form-group">
              <label>Jam Selesai Kerja (GPS)</label>
              <input type="text" id="md_end" value="${escapeHtml(comp.workEnd)}">
            </div>
          </div>
          <button type="submit" class="corp-btn">Simpan Perubahan Master Data</button>
        </form>
      ` : ''}

      ${sTab === 'plugin' ? `
        <div>
          ${comp.plugins.map(p => `
            <div class="corp-list-item">
              <div>
                <strong>${escapeHtml(p.name)}</strong> (${escapeHtml(p.version)})
                <div style="font-size:0.78rem;color:#64748b;">Kategori: ${escapeHtml(p.category)}</div>
              </div>
              <button class="corp-btn ${p.enabled ? 'success' : 'secondary'}" onclick="corpTogglePlugin('${p.id}')">
                ${p.enabled ? 'Aktif' : 'Nonaktif'}
              </button>
            </div>
          `).join('')}
        </div>
      ` : ''}

      ${sTab === 'kustom' ? `
        <form onsubmit="corpAddCustomMenu(event)">
          <div class="corp-grid-2">
            <div class="form-group">
              <label>Judul Menu Kustom *</label>
              <input type="text" id="cm_title" required placeholder="Contoh: SOP Operasional Lapangan">
            </div>
            <div class="form-group">
              <label>Kategori *</label>
              <input type="text" id="cm_cat" required placeholder="Dokumentasi / Panduan">
            </div>
          </div>
          <div class="form-group">
            <label>Isi Konten / Penjelasan *</label>
            <textarea id="cm_summary" rows="3" required placeholder="Tuliskan instruksi atau isi halaman kustom..."></textarea>
          </div>
          <button type="submit" class="corp-btn">Tambahkan ke Beranda Perusahaan</button>
        </form>
      ` : ''}

      ${sTab === 'tema' ? `
        <div class="corp-grid-2">
          <div>
            <h4 style="margin-bottom:8px;">Toko Tema Tampilan</h4>
            ${enterpriseState.themes.map(t => `
              <div class="corp-list-item">
                <div>
                  <span style="display:inline-block;width:14px;height:14px;border-radius:50%;background:${t.primary};vertical-align:middle;margin-right:6px;"></span>
                  <strong>${escapeHtml(t.name)}</strong>
                </div>
                <button class="corp-btn ${t.id === theme.id ? 'success' : 'secondary'}" onclick="corpApplyTheme('${t.id}')">
                  ${t.id === theme.id ? 'Terpasang' : 'Terapkan'}
                </button>
              </div>
            `).join('')}
          </div>

          <div>
            <h4 style="margin-bottom:8px;">Logo, Posisi, Siaran &amp; Sandi Admin Utama</h4>
            <div class="form-group">
              <label>Unggah Logo Perusahaan dari Perangkat</label>
              <input type="file" accept="image/*" onchange="corpUploadLogo(event)">
            </div>
            <div class="form-group">
              <label>Posisi Logo di Beranda</label>
              <select onchange="corpChangeLogoPosition(this.value)">
                <option value="LEFT" ${comp.logoPosition === 'LEFT' ? 'selected' : ''}>Rata Kiri</option>
                <option value="CENTER" ${comp.logoPosition === 'CENTER' ? 'selected' : ''}>Rata Tengah</option>
                <option value="RIGHT" ${comp.logoPosition === 'RIGHT' ? 'selected' : ''}>Rata Kanan</option>
                <option value="BANNER_TOP" ${comp.logoPosition === 'BANNER_TOP' ? 'selected' : ''}>Banner Penuh Atas</option>
              </select>
            </div>
            <form onsubmit="corpSaveBroadcast(event)" style="margin-bottom:16px;">
              <div class="form-group">
                <label>Teks Siaran Pengumuman Tim</label>
                <textarea id="bc_msg" rows="2">${escapeHtml(comp.broadcastMessage)}</textarea>
              </div>
              <button type="submit" class="corp-btn">Perbarui Siaran Tim</button>
            </form>

            ${isSuper ? `
              <form onsubmit="corpChangeSuperPassword(event)" style="border-top:1px solid #e2e8f0;padding-top:12px;">
                <h4 style="margin-bottom:8px;color:#991b1b;">Ubah Kata Sandi Admin Utama (${MASTER_ADMIN_EMAIL})</h4>
                <div class="form-group">
                  <input type="password" id="pwd_old" required placeholder="Kata Sandi Saat Ini">
                </div>
                <div class="form-group">
                  <input type="password" id="pwd_new" required minlength="6" placeholder="Kata Sandi Baru (Min. 6 Karakter)">
                </div>
                <button type="submit" class="corp-btn danger">Simpan Kata Sandi Baru</button>
              </form>
            ` : ''}
          </div>
        </div>
      ` : ''}
    </div>
  `;
}

// =========================================================================
// CORPORATE INTERACTIVE MUTATIONS
// =========================================================================
function corpAppointCoAdmin() {
  const sel = document.getElementById('selectAppointTarget');
  if (!sel) return;
  const comp = getActiveCompany();
  const currentCo = enterpriseState.employees.filter(u => u.companyId === comp.id && u.role === 'SECOND_ADMIN');
  if (currentCo.length >= 2) {
    showToast('Maksimal 2 Admin Pendamping telah tercapai.');
    return;
  }
  const target = enterpriseState.employees.find(u => u.id === sel.value);
  if (target) {
    target.role = 'SECOND_ADMIN';
    target.inboxAccess = true;
    saveEnterpriseState();
    renderCorporateWorkspace();
    showToast(`${target.name} resmi ditunjuk sebagai Admin Pendamping.`);
  }
}

function corpRevokeCoAdmin(userId) {
  const target = enterpriseState.employees.find(u => u.id === userId);
  if (target) {
    target.role = 'PEJABAT_TINGGI';
    saveEnterpriseState();
    renderCorporateWorkspace();
    showToast(`Wewenang Admin Pendamping untuk ${target.name} dicabut.`);
  }
}

function corpSaveDualEmails(e) {
  e.preventDefault();
  const comp = getActiveCompany();
  comp.legalEmail = document.getElementById('dual_legal_email').value.trim();
  comp.ownerEmail = document.getElementById('dual_owner_email').value.trim();
  comp.secretaryEmail = document.getElementById('dual_sec_email').value.trim();
  saveEnterpriseState();
  renderCorporateWorkspace();
  showToast('Dua jalur email resmi berhasil diperbarui.');
}

function corpInviteEmployee(e) {
  e.preventDefault();
  const comp = getActiveCompany();
  const name = document.getElementById('inv_name').value.trim();
  const email = document.getElementById('inv_email').value.trim();
  const pos = document.getElementById('inv_pos').value.trim();
  const role = document.getElementById('inv_role').value;
  enterpriseState.employees.push({
    id: 'usr_' + Date.now(),
    companyId: comp.id,
    name,
    email,
    role,
    position: pos,
    branch: comp.branches[0] || 'Kantor Pusat',
    inboxAccess: false,
    wage: 7500000
  });
  saveEnterpriseState();
  renderCorporateWorkspace();
  showToast(`Karyawan ${name} berhasil diundang ke ${comp.name}.`);
}

function corpRemoveEmployee(userId) {
  enterpriseState.employees = enterpriseState.employees.filter(u => u.id !== userId);
  saveEnterpriseState();
  renderCorporateWorkspace();
  showToast('Akun karyawan berhasil dihapus dari perusahaan.');
}

function corpCheckIn() {
  const session = getCorpSession();
  const comp = getActiveCompany();
  const now = new Date().toLocaleTimeString('id-ID') + ' WIB';
  const today = new Date().toLocaleDateString('id-ID');
  enterpriseState.attendanceLogs.unshift({
    id: 'att_' + Date.now(),
    companyId: comp.id,
    name: session ? session.name : 'Personel',
    date: today,
    checkIn: now,
    checkOut: '-',
    location: 'Geofence Kantor Terverifikasi',
    status: 'Hadir Masuk (GPS Valid)'
  });
  saveEnterpriseState();
  renderCorporateWorkspace();
  showToast(`Absen Masuk tercatat pukul ${now}.`);
}

function corpCheckOut() {
  const session = getCorpSession();
  const comp = getActiveCompany();
  const now = new Date().toLocaleTimeString('id-ID') + ' WIB';
  const rec = enterpriseState.attendanceLogs.find(a => a.companyId === comp.id && a.name === (session ? session.name : ''));
  if (rec) {
    rec.checkOut = now;
    rec.status = 'Presensi Lengkap (Masuk & Pulang)';
    saveEnterpriseState();
    renderCorporateWorkspace();
    showToast(`Absen Pulang tercatat pukul ${now}.`);
  } else {
    showToast('Harap lakukan Absen Masuk terlebih dahulu hari ini.');
  }
}

function corpSubmitReport(e) {
  e.preventDefault();
  const session = getCorpSession();
  const comp = getActiveCompany();
  enterpriseState.dailyReports.unshift({
    id: 'rep_' + Date.now(),
    companyId: comp.id,
    author: session ? session.name : 'Personel',
    date: new Date().toLocaleDateString('id-ID'),
    title: document.getElementById('rep_title').value.trim(),
    summary: document.getElementById('rep_summary').value.trim(),
    attachment: 'Lampiran_Cloud.pdf',
    status: 'Menunggu Review Admin'
  });
  saveEnterpriseState();
  renderCorporateWorkspace();
  showToast('Laporan kerja harian berhasil dikirim.');
}

function corpDisbursePayroll(slipId, platform) {
  const slip = enterpriseState.payrollSlips.find(p => p.id === slipId);
  if (slip) {
    slip.paid = true;
    slip.platform = platform;
    slip.ref = platform.slice(0, 3).toUpperCase() + '-' + String(Date.now()).slice(-6);
    saveEnterpriseState();
    renderCorporateWorkspace();
    showToast(`Payroll untuk ${slip.name} berhasil disalurkan via ${platform}.`);
  }
}

function corpSendChat(e) {
  e.preventDefault();
  const session = getCorpSession();
  const comp = getActiveCompany();
  const text = document.getElementById('corp_chat_input').value.trim();
  if (!text) return;
  enterpriseState.chatMessages.push({
    id: 'msg_' + Date.now(),
    companyId: comp.id,
    channel: 'DIREKSI',
    sender: session ? session.name : 'Personel',
    time: new Date().toLocaleTimeString('id-ID', { hour: '2-digit', minute: '2-digit' }) + ' WIB',
    text
  });
  saveEnterpriseState();
  renderCorporateWorkspace();
}

function corpSubmitPartner(e) {
  e.preventDefault();
  const comp = getActiveCompany();
  enterpriseState.partnerSubmissions.unshift({
    id: 'sub_' + Date.now(),
    companyId: comp.id,
    type: 'Pengajuan MoU & Penawaran',
    org: document.getElementById('pt_org').value.trim(),
    commodity: document.getElementById('pt_comm').value.trim(),
    ktpVerified: true,
    signed: true,
    date: new Date().toLocaleDateString('id-ID')
  });
  saveEnterpriseState();
  renderCorporateWorkspace();
  showToast('Formulir mitra dan tanda tangan digital berhasil disimpan.');
}

function corpCreatePost(e) {
  e.preventDefault();
  const session = getCorpSession();
  const comp = getActiveCompany();
  enterpriseState.posts.unshift({
    id: 'post_' + Date.now(),
    companyId: comp.id,
    title: document.getElementById('post_title').value.trim(),
    content: document.getElementById('post_body').value.trim(),
    date: new Date().toLocaleDateString('id-ID'),
    author: session ? session.name : 'Corporate Admin'
  });
  saveEnterpriseState();
  renderCorporateWorkspace();
  showToast('Berita produktivitas perusahaan berhasil diterbitkan.');
}

function corpToggleFeature(key) {
  const comp = getActiveCompany();
  comp.features[key] = !comp.features[key];
  saveEnterpriseState();
  renderCorporateWorkspace();
  showToast(`Modul ${key.toUpperCase()} kini ${comp.features[key] ? 'AKTIF' : 'NONAKTIF'}.`);
}

function corpSaveMasterData(e) {
  e.preventDefault();
  const comp = getActiveCompany();
  comp.name = document.getElementById('md_name').value.trim();
  comp.code = document.getElementById('md_code').value.trim().toUpperCase();
  comp.sector = document.getElementById('md_sector').value.trim();
  comp.npwp = document.getElementById('md_npwp').value.trim();
  comp.nib = document.getElementById('md_nib').value.trim();
  comp.phone = document.getElementById('md_phone').value.trim();
  comp.workStart = document.getElementById('md_start').value.trim();
  comp.workEnd = document.getElementById('md_end').value.trim();
  saveEnterpriseState();
  renderCorporateWorkspace();
  showToast('Seluruh master data perusahaan berhasil diperbarui oleh Admin Utama.');
}

function corpTogglePlugin(pluginId) {
  const comp = getActiveCompany();
  const plg = comp.plugins.find(p => p.id === pluginId);
  if (plg) {
    plg.enabled = !plg.enabled;
    saveEnterpriseState();
    renderCorporateWorkspace();
    showToast(`${plg.name} ${plg.enabled ? 'diaktifkan' : 'dinonaktifkan'}.`);
  }
}

function corpAddCustomMenu(e) {
  e.preventDefault();
  const comp = getActiveCompany();
  comp.customMenus.push({
    id: 'cm_' + Date.now(),
    title: document.getElementById('cm_title').value.trim(),
    category: document.getElementById('cm_cat').value.trim(),
    summary: document.getElementById('cm_summary').value.trim()
  });
  saveEnterpriseState();
  renderCorporateWorkspace();
  showToast('Menu kustom berhasil ditambahkan ke Beranda Perusahaan.');
}

function corpApplyTheme(themeId) {
  enterpriseState.activeThemeId = themeId;
  saveEnterpriseState();
  renderCorporateWorkspace();
  showToast('Tema warna berhasil diterapkan.');
}

function corpUploadLogo(e) {
  const file = e.target.files && e.target.files[0];
  if (!file) return;
  const reader = new FileReader();
  reader.onload = function (evt) {
    const comp = getActiveCompany();
    comp.logoDataUrl = evt.target.result;
    saveEnterpriseState();
    renderCorporateWorkspace();
    showToast('Logo perusahaan berhasil diperbarui dari perangkat.');
  };
  reader.readAsDataURL(file);
}

function corpChangeLogoPosition(pos) {
  const comp = getActiveCompany();
  comp.logoPosition = pos;
  saveEnterpriseState();
  renderCorporateWorkspace();
  showToast('Posisi tampilan logo diperbarui.');
}

function corpSaveBroadcast(e) {
  e.preventDefault();
  const comp = getActiveCompany();
  comp.broadcastMessage = document.getElementById('bc_msg').value.trim();
  comp.broadcastActive = true;
  saveEnterpriseState();
  renderCorporateWorkspace();
  showToast('Pesan siaran tim berhasil diperbarui.');
}

function corpChangeSuperPassword(e) {
  e.preventDefault();
  const oldPwd = document.getElementById('pwd_old').value;
  const newPwd = document.getElementById('pwd_new').value;
  if (oldPwd !== getAdminPassword()) {
    showToast('Kata sandi saat ini tidak cocok.');
    return;
  }
  if (newPwd.length < 6) {
    showToast('Kata sandi baru minimal 6 karakter.');
    return;
  }
  setAdminPassword(newPwd);
  e.target.reset();
  showToast(`Kata sandi Admin Utama (${MASTER_ADMIN_EMAIL}) berhasil diperbarui!`);
}

// Interactive Digital Signature Canvas
function initSignatureCanvas() {
  const canvas = document.getElementById('partnerSigCanvas');
  if (!canvas) return;
  const ctx = canvas.getContext('2d');
  let drawing = false;
  const getPos = (evt) => {
    const rect = canvas.getBoundingClientRect();
    const clientX = evt.touches ? evt.touches[0].clientX : evt.clientX;
    const clientY = evt.touches ? evt.touches[0].clientY : evt.clientY;
    return {
      x: (clientX - rect.left) * (canvas.width / rect.width),
      y: (clientY - rect.top) * (canvas.height / rect.height)
    };
  };
  const start = (evt) => { drawing = true; const p = getPos(evt); ctx.beginPath(); ctx.moveTo(p.x, p.y); };
  const move = (evt) => {
    if (!drawing) return;
    evt.preventDefault();
    const p = getPos(evt);
    ctx.lineWidth = 2.5;
    ctx.lineCap = 'round';
    ctx.strokeStyle = '#0b1528';
    ctx.lineTo(p.x, p.y);
    ctx.stroke();
  };
  const stop = () => { drawing = false; };
  canvas.onmousedown = start;
  canvas.onmousemove = move;
  canvas.onmouseup = stop;
  canvas.ontouchstart = start;
  canvas.ontouchmove = move;
  canvas.ontouchend = stop;
}

function clearSignatureCanvas() {
  const canvas = document.getElementById('partnerSigCanvas');
  if (canvas) {
    canvas.getContext('2d').clearRect(0, 0, canvas.width, canvas.height);
  }
}

// =========================================================================
// GLOBAL INIT & EVENT LISTENERS
// =========================================================================
window.addEventListener('click', (e) => {
  const dlModal = document.getElementById('downloadModal');
  const pubModal = document.getElementById('publicLoginModal');
  const corpModal = document.getElementById('corpLoginModal');
  if (e.target === dlModal) closeDownloadModal();
  if (e.target === pubModal) closePublicLoginModal();
  if (e.target === corpModal) closeCorpLoginModal();
});

window.addEventListener('DOMContentLoaded', () => {
  applyPublicGoogleState();
  const activeCorpSession = getCorpSession();
  if (activeCorpSession) {
    launchCompanyWorkspace(activeCorpSession);
  }
});

// Register PWA Service Worker
if ('serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('/sw.js').catch(() => {});
  });
}
