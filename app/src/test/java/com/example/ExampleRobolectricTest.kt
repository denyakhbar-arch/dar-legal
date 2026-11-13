package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.*
import com.example.data.repository.EnterpriseRepository
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun read_string_from_context() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("DAR MultiCorp", appName)
    }

    @Test
    fun testMultiTenantIsolationBetweenCompanies() {
        val repo = EnterpriseRepository()
        // Default company: comp_1 (DAR Agro Makmur)
        assertEquals("comp_1", repo.currentCompany.value.id)
        val comp1Users = repo.users.value
        assertTrue(comp1Users.all { it.companyId == "comp_1" })
        assertTrue(comp1Users.any { it.name.contains("Hendra Wijaya") })

        // Switch to company 2: comp_2 (DAR Logistik Prima)
        repo.switchCompany("comp_2")
        assertEquals("comp_2", repo.currentCompany.value.id)
        val comp2Users = repo.users.value
        assertTrue(comp2Users.all { it.companyId == "comp_2" })
        assertTrue(comp2Users.any { it.name.contains("Bambang Sudiro") })
        // Verify no leak of comp_1 users
        assertFalse(comp2Users.any { it.name.contains("Hendra Wijaya") })
    }

    @Test
    fun testThemeDownloadAndApply() {
        val repo = EnterpriseRepository()
        assertEquals("theme_blue", repo.activeTheme.value.id)

        // Download theme_gold
        val (downloadSuccess, _) = repo.downloadTheme("theme_gold")
        assertTrue(downloadSuccess)
        val goldTheme = repo.themePresets.value.find { it.id == "theme_gold" }
        assertNotNull(goldTheme)
        assertTrue(goldTheme!!.isDownloaded)

        // Apply theme_gold
        val (applySuccess, _) = repo.applyTheme("theme_gold")
        assertTrue(applySuccess)
        assertEquals("theme_gold", repo.activeTheme.value.id)
        assertTrue(repo.activeTheme.value.isApplied)
    }

    @Test
    fun testDashboardCustomizerAndBroadcasting() {
        val repo = EnterpriseRepository()
        val customConfig = DashboardCustomizerConfig(
            logoUri = "content://media/external/images/media/42",
            logoPosition = LogoPosition.BANNER_TOP,
            isBroadcastingActive = true,
            broadcastingMessage = "Siaran Darurat: Seluruh armada truk wajib konvoi pengawalan laut.",
            broadcastingUrgency = "DARURAT",
            showBroadcastingWidget = true,
            showGoogleWorkspaceHub = true
        )
        val (updateSuccess, _) = repo.updateDashboardConfig(customConfig)
        assertTrue(updateSuccess)
        assertEquals(LogoPosition.BANNER_TOP, repo.currentCompany.value.dashboardConfig.logoPosition)
        assertEquals("DARURAT", repo.currentCompany.value.dashboardConfig.broadcastingUrgency)
        assertEquals("content://media/external/images/media/42", repo.currentCompany.value.dashboardConfig.logoUri)
    }

    @Test
    fun testGoogleFormsManagementForSupplierAndBuyer() {
        val repo = EnterpriseRepository()
        val initialCount = repo.currentCompany.value.googleForms.size

        // Add buyer form
        val (addSuccess, _) = repo.addGoogleForm(
            title = "Formulir Survey Kebutuhan Tonase Buyer Internasional",
            audience = FormAudience.BUYER,
            url = "https://forms.google.com/d/e/survey-buyer-global/viewform",
            desc = "Formulir riset kuota ekspor tahunan negara tujuan."
        )
        assertTrue(addSuccess)
        assertEquals(initialCount + 1, repo.currentCompany.value.googleForms.size)

        val addedForm = repo.currentCompany.value.googleForms.find { it.title.contains("Survey Kebutuhan Tonase") }
        assertNotNull(addedForm)
        assertEquals(FormAudience.BUYER, addedForm!!.audience)

        // Remove form
        val (removeSuccess, _) = repo.removeGoogleForm(addedForm.id)
        assertTrue(removeSuccess)
        assertEquals(initialCount, repo.currentCompany.value.googleForms.size)
    }

    @Test
    fun testGoogleCalendarAndDriveSync() {
        val repo = EnterpriseRepository()

        // Google Calendar Event
        val (calSuccess, _) = repo.addGoogleCalendarEvent(
            title = "Rapat Koordinasi Ekspor Komoditas",
            date = "15 Nov 2026",
            time = "14:00 - 16:00 WIB",
            loc = "Ruang Rapat Direksi & Google Meet",
            meetUrl = "https://meet.google.com/dar-export-meeting"
        )
        assertTrue(calSuccess)
        assertTrue(repo.googleCalendarEvents.value.any { it.title.contains("Koordinasi Ekspor") })

        // Google Drive Doc
        val (driveSuccess, _) = repo.addGoogleDriveDoc(
            title = "Laporan Neraca Laba Rugi Q3 2026",
            type = "Google Sheets",
            sizeOrPages = "14 Halaman",
            url = "https://drive.google.com/file/d/sheets-q3-2026"
        )
        assertTrue(driveSuccess)
        assertTrue(repo.googleDriveDocs.value.any { it.title.contains("Neraca Laba Rugi") })
    }

    @Test
    fun testSettingsFeatureTogglesAndCustomMenu() {
        val repo = EnterpriseRepository()
        // 1. Toggle feature config
        val updatedCfg = CompanyFeaturesConfig(enableVideoMeetings = false, enablePayroll = true)
        val (cfgSuccess, _) = repo.updateFeatureConfig(updatedCfg)
        assertTrue(cfgSuccess)
        assertFalse(repo.currentCompany.value.featuresConfig.enableVideoMeetings)

        // 2. Add leadership structure officer
        val (offSuccess, _) = repo.addLeadershipOfficer("Direktur Keuangan", "Anita Dewi, S.E., Ak.", "Keuangan", "anita@agro.darmulticorp.co.id")
        assertTrue(offSuccess)
        assertTrue(repo.currentCompany.value.leadershipStructure.any { it.officerName == "Anita Dewi, S.E., Ak." })

        // 3. Toggle Plugin
        val (plgSuccess, _) = repo.togglePlugin("plg_wa", false)
        assertTrue(plgSuccess)

        // 4. Create Custom Menu with text, photo, and video
        val (menuSuccess, _) = repo.addCustomCompanyMenu(
            menuTitle = "Instruksi Kerja Standarisasi Lab",
            subMenuTitle = "Prosedur Uji Kadar Air",
            categoryTag = "SOP QA",
            bodyText = "Uji sampel biji kopi menggunakan moisture meter terkalibrasi.",
            photoUrl = "https://images.unsplash.com/photo-sample",
            photoCaption = "Kalibrasi Alat Uji",
            videoUrl = "https://example.com/video.mp4",
            videoTitle = "Video Tutorial Uji Lab"
        )
        assertTrue(menuSuccess)
        assertTrue(repo.currentCompany.value.customMenus.any { it.menuTitle == "Instruksi Kerja Standarisasi Lab" })
    }

    @Test
    fun testTwoTierAdminAppointmentRules() {
        val repo = EnterpriseRepository()
        // Pre-seeded: usr_second_admin is already appointed in comp_1 (1 of 2 slots)
        val initialAppointed = repo.users.value.filter { it.role == UserRole.SECOND_ADMIN || it.isAppointedSecondAdmin }
        assertEquals(1, initialAppointed.size)

        // Fill Slot 2: Appoint usr_tetap_1
        val (success1, _) = repo.appointSecondAdmin("usr_tetap_1")
        assertTrue(success1)
        assertEquals(UserRole.SECOND_ADMIN, repo.users.value.find { it.id == "usr_tetap_1" }?.role)

        // Slot 3: Try to appoint a 3rd admin -> should be rejected because max is 2
        val (successRejected, errorMsg) = repo.appointSecondAdmin("usr_direksi_1")
        assertFalse(successRejected)
        assertTrue(errorMsg.contains("Maksimal 2 orang Admin"))

        // Revoke Slot 1 (usr_second_admin)
        val (revokeSuccess, _) = repo.revokeSecondAdmin("usr_second_admin")
        assertTrue(revokeSuccess)
        assertFalse(repo.users.value.find { it.id == "usr_second_admin" }!!.isAppointedSecondAdmin)

        // Now can appoint usr_direksi_1 into the freed slot
        val (success2, _) = repo.appointSecondAdmin("usr_direksi_1")
        assertTrue(success2)
        assertEquals(UserRole.SECOND_ADMIN, repo.users.value.find { it.id == "usr_direksi_1" }?.role)
    }

    @Test
    fun testDualEmailRoutingAndFullMasterDataCustomization() {
        val repo = EnterpriseRepository()

        // 1. Test Dual Email Routing
        val (emailSuccess, _) = repo.updateCompanyEmailRouting(
            legalEmail = "denyakhbar@gmail.com",
            ownerEmail = "owner@darmulticorp.co.id",
            secretaryEmail = "sekretaris@darmulticorp.co.id"
        )
        assertTrue(emailSuccess)
        assertEquals("denyakhbar@gmail.com", repo.currentCompany.value.legalConsultantEmail)
        assertEquals("owner@darmulticorp.co.id", repo.currentCompany.value.ownerEmail)
        assertEquals("sekretaris@darmulticorp.co.id", repo.currentCompany.value.secretaryEmail)

        // 2. Test Full Master Data Customization by Super Admin
        val (masterSuccess, _) = repo.updateCompanyMasterData(
            name = "PT DAR MultiCorp Nusantara",
            code = "DAR-NUSANTARA",
            sector = "Holding Komoditas Agrikultur & Logistik Ekspor",
            npwp = "01.999.888.7-001.000",
            nib = "9120001112223",
            website = "https://nusantara.darmulticorp.co.id",
            phone = "+62 21 7788 9900",
            legalEmail = "denyakhbar@gmail.com",
            ownerEmail = "owner@nusantara.darmulticorp.co.id",
            secretaryEmail = "sekretaris@nusantara.darmulticorp.co.id",
            workStart = "07:30",
            workEnd = "16:30",
            accentHex = 0xFF0D9488
        )
        assertTrue(masterSuccess)
        assertEquals("PT DAR MultiCorp Nusantara", repo.currentCompany.value.name)
        assertEquals("DAR-NUSANTARA", repo.currentCompany.value.code)
        assertEquals("07:30", repo.currentCompany.value.workHoursStart)
        assertEquals("16:30", repo.currentCompany.value.workHoursEnd)
        assertEquals(0xFF0D9488, repo.currentCompany.value.primaryAccentColorHex)
    }

    @Test
    fun testAttendanceAndPayrollDisbursement() {
        val repo = EnterpriseRepository()
        val slip = repo.payrollSlips.value.first()
        val (paidSuccess, _) = repo.disbursePayroll(slip.id, PaymentPlatform.MIDTRANS)
        assertTrue(paidSuccess)
        val updatedSlip = repo.payrollSlips.value.find { it.id == slip.id }
        assertTrue(updatedSlip!!.isPaid)
    }

    @Test
    fun testAppDownloadHelperGeneratesApkAndIosFiles() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val apkResult = com.example.util.AppDownloadHelper.downloadAndroidApk(context)
        assertTrue(apkResult.success)
        assertEquals("DAR_MultiCorp_Installer.apk", apkResult.fileName)
        assertTrue(apkResult.message.contains("DAR_MultiCorp_Installer.apk"))

        val iosResult = com.example.util.AppDownloadHelper.downloadIosProfile(context)
        assertTrue(iosResult.success)
        assertEquals("DAR_MultiCorp_iOS.mobileconfig", iosResult.fileName)
        assertTrue(iosResult.message.contains("DAR_MultiCorp_iOS.mobileconfig"))
    }

    @Test
    fun testLegalPracticeExclusivityAndLeadCounsel() {
        val repo = EnterpriseRepository()
        val leadAdvocate = repo.advocateTeam.first()
        assertEquals("Deny Akhbar, S.H., M.H.", leadAdvocate.name)
        assertEquals("denyakhbar@gmail.com", repo.currentCompany.value.legalConsultantEmail)
        assertTrue(leadAdvocate.title.contains("Konsultan Hukum"))
    }

    @Test
    fun testDenyAkhbarSuperAdminPasswordAuthentication() {
        val repo = EnterpriseRepository()
        assertEquals("denyakhbar@gmail.com", repo.currentUser.value.email)
        assertEquals(UserRole.SUPER_ADMIN, repo.currentUser.value.role)

        // Wrong password rejected
        val (wrongOk, _) = repo.authenticateSuperAdmin("denyakhbar@gmail.com", "wrongpass")
        assertFalse(wrongOk)
        assertFalse(repo.isSuperAdminPasswordVerified.value)

        // Non-admin email rejected
        val (otherOk, _) = repo.authenticateSuperAdmin("tamu@gmail.com", EnterpriseRepository.DEFAULT_ADMIN_PASSWORD)
        assertFalse(otherOk)

        // Valid Google email + password authenticates as Super Admin
        val (validOk, _) = repo.authenticateSuperAdmin("denyakhbar@gmail.com", EnterpriseRepository.DEFAULT_ADMIN_PASSWORD)
        assertTrue(validOk)
        assertTrue(repo.isSuperAdminPasswordVerified.value)
        assertEquals(UserRole.SUPER_ADMIN, repo.currentUser.value.role)

        // Switching to comp_2 preserves denyakhbar@gmail.com as verified Super Admin
        repo.switchCompany("comp_2")
        assertEquals("comp_2", repo.currentCompany.value.id)
        assertEquals("denyakhbar@gmail.com", repo.currentUser.value.email)
        assertEquals(UserRole.SUPER_ADMIN, repo.currentUser.value.role)

        // Update password works
        val (pwdUpdated, _) = repo.updateSuperAdminPassword(EnterpriseRepository.DEFAULT_ADMIN_PASSWORD, "RahasiaBaru2026")
        assertTrue(pwdUpdated)
        val (reloginOk, _) = repo.authenticateSuperAdmin("denyakhbar@gmail.com", "RahasiaBaru2026")
        assertTrue(reloginOk)
    }
}


