# PANDUAN LENGKAP & SOLUSI EROR DEPLOY VERCEL (vercel.app)
## Kantor Advokat Deny Akhbar, S.H., M.H. & Portal DAR MultiCorp

Panduan ini disusun untuk memperbaiki kesalahan **404 NOT_FOUND** maupun tampilan kosong (*blank*) saat meng-onlinekan aplikasi ke Vercel (`vercel.app`).

---

### PENYEBAB EROR `404 NOT_FOUND` DI VERCEL YANG TELAH DIPERBAIKI
1. **`outputDirectory` belum ditetapkan pada `vercel.json` root**:
   Saat `package.json` memiliki skrip `build`, Vercel secara default mencari folder `public/`. Karena tidak ada folder `public/`, deployment menghasilkan `404 NOT_FOUND`. Kini `/vercel.json` di root secara eksplisit menetapkan `"outputDirectory": "web"` (dan `/web/vercel.json` menetapkan `"outputDirectory": "."`).
2. **Aturan Redirect SPA (`rewrites`) untuk semua rute**:
   Rute client-side SPA yang tidak merujuk ke file fisik langsung diarahkan ke `/index.html` dengan aturan resmi:
   ```json
   "rewrites": [
     { "source": "/(.*)", "destination": "/index.html" }
   ]
   ```
   *(Di Vercel, pengecekan file fisik seperti `styles.css`, `app.js`, dan `/downloads/*.apk` selalu dijalankan lebih dulu sebelum `rewrites`, sehingga aset tetap termuat sempurna tanpa 404.)*

---

### LANGKAH PERBAIKAN DI DASHBOARD VERCEL (PENTING)
Jika proyek di Vercel Anda sebelumnya menampilkan `404 NOT_FOUND`:
1. Buka **[https://vercel.com](https://vercel.com)** &rarr; pilih proyek Anda.
2. Masuk ke **Settings &rarr; General**:
   - **Framework Preset**: Pilih `Other`.
   - **Root Directory**: Biarkan kosong (`./`) atau isi `web` (keduanya kini didukung penuh).
   - **Build & Development Settings**: Pastikan tidak ada override manual yang mengarah ke folder `public` atau `dist` (matikan toggle *Override* pada **Output Directory** agar mengikuti `vercel.json`).
3. Simpan perubahan jika ada, lalu buka tab **Deployments** &rarr; klik titik tiga (**`...`**) pada deployment terbaru &rarr; pilih **Redeploy** (jangan centang *Use existing Build Cache*).
4. Tunggu 15–30 detik hingga status **Ready (Hijau)** muncul, lalu buka tautan `.vercel.app` Anda.

---

### AKTIVASI POPUP GOOGLE SIGN-IN DI DOMAIN `.vercel.app`
1. Buka **Firebase Console** proyek `gen-lang-client-0464794354` &rarr; **Authentication** &rarr; **Settings** &rarr; **Authorized domains**.
2. Klik **Add domain** dan tambahkan domain Vercel Anda (contoh: `dar-multicorp.vercel.app`).
3. Setelah domain ditambahkan, tombol **Login Publik (Google)** dan **Pilih Akun Google** di **Login Perusahaan** akan langsung membuka jendela pilihan akun Google resmi.
4. Untuk masuk sebagai **Admin Utama**, gunakan akun **`denyakhbar@gmail.com`** beserta kata sandi Admin Utama (dapat diubah kapan saja di **Pusat Pengaturan &rarr; Tema, Logo & Sandi Admin**).

---

### PAKET INSTALASI ANDROID (.APK & PWA)
- Berkas `/downloads/DAR_MultiCorp_Installer.apk` kini berisi biner **APK Android asli yang sudah dikompilasi dan ditandatangani (APK Signature Scheme v2)** untuk `com.aistudio.multicorp.xptqvk` (`minSdk 24` / Android 7.0+).
- Aturan `rewrites` pada `vercel.json` mengecualikan `/downloads/`, `/manifest.json`, `/sw.js`, dan ikon PNG (`icon-192.png`, `icon-512.png`) agar unduhan APK dan instalasi **WebAPK / PWA** di Google Chrome Android berjalan tanpa eror penguraian paket.

