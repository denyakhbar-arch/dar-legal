package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CustomCompanyMenu
import com.example.data.repository.EnterpriseRepository
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val repository = remember { EnterpriseRepository() }
            val activeTheme by repository.activeTheme.collectAsStateWithLifecycle()
            MyApplicationTheme(activeTheme = activeTheme) {
                MainAppNavHost(repository = repository)
            }
        }
    }
}

@Composable
fun MainAppNavHost(
    repository: EnterpriseRepository = remember { EnterpriseRepository() }
) {
    var currentScreen by remember { mutableStateOf("dashboard") }
    var selectedCustomMenu by remember { mutableStateOf<CustomCompanyMenu?>(null) }

    val activeTheme by repository.activeTheme.collectAsStateWithLifecycle()
    val themePresets by repository.themePresets.collectAsStateWithLifecycle()
    val currentCompany by repository.currentCompany.collectAsStateWithLifecycle()
    val companies by repository.companies.collectAsStateWithLifecycle()
    val currentUser by repository.currentUser.collectAsStateWithLifecycle()
    val users by repository.users.collectAsStateWithLifecycle()
    val attendanceList by repository.attendanceList.collectAsStateWithLifecycle()
    val liveGps by repository.liveGps.collectAsStateWithLifecycle()
    val dailyReports by repository.dailyReports.collectAsStateWithLifecycle()
    val documents by repository.documents.collectAsStateWithLifecycle()
    val payrollSlips by repository.payrollSlips.collectAsStateWithLifecycle()
    val chatMessages by repository.chatMessages.collectAsStateWithLifecycle()
    val videoMeetings by repository.videoMeetings.collectAsStateWithLifecycle()
    val companyInbox by repository.companyInbox.collectAsStateWithLifecycle()
    val partnerSubmissions by repository.partnerSubmissions.collectAsStateWithLifecycle()
    val products by repository.products.collectAsStateWithLifecycle()
    val posts by repository.posts.collectAsStateWithLifecycle()
    val consultationBookings by repository.consultationBookings.collectAsStateWithLifecycle()
    val legalAidRequests by repository.legalAidRequests.collectAsStateWithLifecycle()
    val legalPartnerships by repository.legalPartnerships.collectAsStateWithLifecycle()
    val calendarEvents by repository.googleCalendarEvents.collectAsStateWithLifecycle()
    val driveDocs by repository.googleDriveDocs.collectAsStateWithLifecycle()
    val isSuperAdminVerified by repository.isSuperAdminPasswordVerified.collectAsStateWithLifecycle()

    // Android System Back Button Handler
    BackHandler(enabled = currentScreen != "dashboard") {
        currentScreen = "dashboard"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(activeTheme.bgHex),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = CorporateBlue,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("main_bottom_nav")
            ) {
                NavigationBarItem(
                    selected = currentScreen == "dashboard",
                    onClick = { currentScreen = "dashboard" },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Utama", fontSize = 10.sp) },
                    modifier = Modifier.testTag("nav_item_dashboard")
                )
                NavigationBarItem(
                    selected = currentScreen == "attendance_gps",
                    onClick = { currentScreen = "attendance_gps" },
                    icon = { Icon(Icons.Default.Fingerprint, contentDescription = "Presensi") },
                    label = { Text("Presensi", fontSize = 10.sp) },
                    modifier = Modifier.testTag("nav_item_attendance")
                )
                NavigationBarItem(
                    selected = currentScreen == "communication",
                    onClick = { currentScreen = "communication" },
                    icon = { Icon(Icons.Default.Forum, contentDescription = "Rapat & Chat") },
                    label = { Text("Rapat", fontSize = 10.sp) },
                    modifier = Modifier.testTag("nav_item_communication")
                )
                NavigationBarItem(
                    selected = currentScreen == "supplier_buyer",
                    onClick = { currentScreen = "supplier_buyer" },
                    icon = { Icon(Icons.Default.Handshake, contentDescription = "Mitra") },
                    label = { Text("Mitra", fontSize = 10.sp) },
                    modifier = Modifier.testTag("nav_item_partner")
                )
                NavigationBarItem(
                    selected = currentScreen == "settings",
                    onClick = { currentScreen = "settings" },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Pengaturan") },
                    label = { Text("Pengaturan", fontSize = 10.sp) },
                    modifier = Modifier.testTag("nav_item_settings")
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color(activeTheme.bgHex)
        ) {
            when (currentScreen) {
                "dashboard" -> {
                    DashboardScreen(
                        currentCompany = currentCompany,
                        companies = companies,
                        currentUser = currentUser,
                        users = users,
                        calendarEvents = calendarEvents,
                        driveDocs = driveDocs,
                        onSelectCompany = { repository.switchCompany(it) },
                        onSelectUser = { repository.switchCurrentUser(it) },
                        isSuperAdminVerified = isSuperAdminVerified,
                        onAuthenticateSuperAdmin = { email, pwd -> repository.authenticateSuperAdmin(email, pwd) },
                        onUpdateSuperAdminPassword = { oldPwd, newPwd -> repository.updateSuperAdminPassword(oldPwd, newPwd) },
                        onNavigate = { currentScreen = it },
                        onOpenLegalPortal = { currentScreen = "legal_consultant" },
                        onOpenCustomMenu = { menu ->
                            selectedCustomMenu = menu
                            currentScreen = "custom_menu_detail"
                        },
                        onUpdateDashboardConfig = { repository.updateDashboardConfig(it) },
                        onUpdateCompanyLogo = { uri, pos -> repository.updateCompanyLogo(uri, pos) },
                        onUpdateBroadcastingAnnouncement = { msg, urg, act -> repository.updateBroadcastingAnnouncement(msg, urg, act) },
                        onAddCalendarEvent = { title, date, time, loc, meet -> repository.addGoogleCalendarEvent(title, date, time, loc, meet) }
                    )
                }

                "settings" -> {
                    SettingsScreen(
                        currentCompany = currentCompany,
                        currentUser = currentUser,
                        users = users,
                        themePresets = themePresets,
                        activeTheme = activeTheme,
                        onDownloadTheme = { repository.downloadTheme(it) },
                        onApplyTheme = { repository.applyTheme(it) },
                        onUpdateDashboardConfig = { repository.updateDashboardConfig(it) },
                        onUpdateCompanyLogo = { uri, pos -> repository.updateCompanyLogo(uri, pos) },
                        onUpdateBroadcastingAnnouncement = { msg, urg, act -> repository.updateBroadcastingAnnouncement(msg, urg, act) },
                        onUpdateEmailRouting = { legal, owner, sec -> repository.updateCompanyEmailRouting(legal, owner, sec) },
                        onUpdateMasterData = { name, code, sector, npwp, nib, web, phone, legal, owner, sec, start, end, accent ->
                            repository.updateCompanyMasterData(name, code, sector, npwp, nib, web, phone, legal, owner, sec, start, end, accent)
                        },
                        onUpdateFeatures = { repository.updateFeatureConfig(it) },
                        onUpdateCompanyDesign = { name, sector, web, phone, hex ->
                            repository.updateCompanyDesign(name, sector, web, phone, hex)
                        },
                        onAddOfficer = { title, name, dept, email ->
                            repository.addLeadershipOfficer(title, name, dept, email)
                        },
                        onRemoveOfficer = { repository.removeLeadershipOfficer(it) },
                        onTogglePlugin = { plgId, enable ->
                            repository.togglePlugin(plgId, enable)
                        },
                        onInstallPlugin = { name, dev, ver, desc, cat, ep ->
                            repository.installNewPlugin(name, dev, ver, desc, cat, ep)
                        },
                        onSavePluginConfig = { plgId, ep, key ->
                            repository.savePluginConfig(plgId, ep, key)
                        },
                        onCreateCustomMenu = { mTitle, subTitle, catTag, text, photo, caption, video, vTitle ->
                            repository.addCustomCompanyMenu(mTitle, subTitle, catTag, text, photo, caption, video, vTitle)
                        },
                        onDeleteCustomMenu = { repository.deleteCustomCompanyMenu(it) },
                        onOpenCustomMenu = { menu ->
                            selectedCustomMenu = menu
                            currentScreen = "custom_menu_detail"
                        },
                        onBack = { currentScreen = "dashboard" }
                    )
                }

                "custom_menu_detail" -> {
                    selectedCustomMenu?.let { cMenu ->
                        CustomMenuDetailScreen(
                            customMenu = cMenu,
                            onBack = { currentScreen = "dashboard" }
                        )
                    } ?: run {
                        currentScreen = "dashboard"
                    }
                }

                "admin_mgmt" -> {
                    AdminManagementScreen(
                        currentUser = currentUser,
                        users = users,
                        company = currentCompany,
                        onAppointSecondAdmin = { repository.appointSecondAdmin(it) },
                        onRevokeSecondAdmin = { repository.revokeSecondAdmin(it) },
                        onInviteEmployee = { name, email, role, pos, branch, phone, wage ->
                            repository.inviteEmployee(name, email, role, pos, branch, phone, wage)
                        },
                        onRemoveEmployee = { repository.removeEmployee(it) },
                        onToggleInboxAccess = { userId, allowed ->
                            repository.toggleUserInboxAccess(userId, allowed)
                        },
                        onUpdateEmailRouting = { legal, owner, sec ->
                            repository.updateCompanyEmailRouting(legal, owner, sec)
                        },
                        onBack = { currentScreen = "dashboard" }
                    )
                }

                "attendance_gps" -> {
                    AttendanceGpsScreen(
                        currentUser = currentUser,
                        attendanceList = attendanceList,
                        liveGps = liveGps,
                        onCheckIn = { note ->
                            repository.recordCheckIn(note)
                        },
                        onCheckOut = {
                            repository.recordCheckOut()
                        },
                        onBack = { currentScreen = "dashboard" }
                    )
                }

                "daily_reports" -> {
                    DailyReportScreen(
                        currentUser = currentUser,
                        dailyReports = dailyReports,
                        documents = documents,
                        onSubmitReport = { title, done, obstacles, plan, fileName ->
                            repository.submitDailyReport(title, done, obstacles, plan, fileName)
                        },
                        onClearStorage = { repository.clearStorageCache() },
                        onBack = { currentScreen = "dashboard" }
                    )
                }

                "payroll" -> {
                    PayrollScreen(
                        currentUser = currentUser,
                        payrollSlips = payrollSlips,
                        onDisbursePayroll = { slipId, platform ->
                            repository.disbursePayroll(slipId, platform)
                        },
                        onBack = { currentScreen = "dashboard" }
                    )
                }

                "communication" -> {
                    CommunicationHubScreen(
                        currentUser = currentUser,
                        users = users,
                        company = currentCompany,
                        chatMessages = chatMessages,
                        videoMeetings = videoMeetings,
                        companyInbox = companyInbox,
                        onSendMessage = { channel, branchId, text, voiceSec ->
                            repository.sendChatMessage(channel, branchId, text, voiceSec)
                        },
                        onBack = { currentScreen = "dashboard" }
                    )
                }

                "supplier_buyer" -> {
                    SupplierBuyerPortalScreen(
                        currentUser = currentUser,
                        googleForms = currentCompany.googleForms,
                        submissions = partnerSubmissions,
                        onAddGoogleForm = { title, audience, url, desc ->
                            repository.addGoogleForm(title, audience, url, desc)
                        },
                        onRemoveGoogleForm = { repository.removeGoogleForm(it) },
                        onSubmitForm = { formType, partnerRole, fullName, companyName, nik, npwp, email, phone, address, commodity, price, volume, terms, sig ->
                            repository.submitPartnerForm(formType, partnerRole, fullName, companyName, nik, npwp, email, phone, address, commodity, price, volume, terms, sig)
                        },
                        onBack = { currentScreen = "dashboard" }
                    )
                }

                "marketing_catalog" -> {
                    MarketingCatalogScreen(
                        currentCompany = currentCompany,
                        currentUser = currentUser,
                        products = products,
                        posts = posts,
                        onCreatePost = { title, content, tag ->
                            repository.createPost(title, content, tag)
                        },
                        onBack = { currentScreen = "dashboard" }
                    )
                }

                "web_browser_preview" -> {
                    WebBrowserPreviewScreen(
                        currentCompany = currentCompany,
                        onAuthenticateSuperAdmin = { email, pwd -> repository.authenticateSuperAdmin(email, pwd) },
                        onNavigateToModule = { currentScreen = it },
                        onBack = { currentScreen = "dashboard" }
                    )
                }

                "legal_consultant" -> {
                    LegalConsultantScreen(
                        advocateTeam = repository.advocateTeam,
                        testimonials = repository.testimonials,
                        consultationBookings = consultationBookings,
                        legalAidRequests = legalAidRequests,
                        legalPartnerships = legalPartnerships,
                        onBookConsultation = { name, org, email, phone, topic, date, brief ->
                            repository.bookLegalConsultation(name, org, email, phone, topic, date, brief)
                        },
                        onSubmitLegalAid = { name, contact, cat, desc ->
                            repository.submitLegalAidRequest(name, contact, cat, desc)
                        },
                        onSubmitPartnership = { name, firm, loc, area, note ->
                            repository.submitLegalPartnership(name, firm, loc, area, note)
                        },
                        onBack = { currentScreen = "dashboard" }
                    )
                }
            }
        }
    }
}
