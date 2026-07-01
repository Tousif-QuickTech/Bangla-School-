package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*

// --- Authentication UI Screen ---
@Composable
fun AuthScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "বাংলা সাহিত্য",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "শিক্ষা ও ই-বুক লার্নিং প্ল্যাটফর্ম",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (viewModel.showOTPVerification) {
                // --- OTP Verification Layout ---
                Text("ওটিপি (OTP) কোড যাচাইকরণ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "পরীক্ষার জন্য যেকোনো ৪ সংখ্যার কোড লিখুন (অথবা '1234'):",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = viewModel.authOTPCode,
                    onValueChange = { if (it.length <= 4) viewModel.authOTPCode = it },
                    label = { Text("৪ ডিজিটের ওটিপি কোড") },
                    singleLine = true,
                    leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                if (viewModel.loginError.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = viewModel.loginError, color = MaterialTheme.colorScheme.error, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { viewModel.verifyOTP() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("ভেরিফাই করুন", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))
                TextButton(onClick = { viewModel.showOTPVerification = false }) {
                    Text("ফিরে যান", fontSize = 12.sp)
                }
            } else {
                // --- Standard Login/Register Layout ---
                Text(
                    text = if (viewModel.isRegisterMode) "নতুন অ্যাকাউন্ট তৈরি করুন" else "আপনার অ্যাকাউন্টে লগইন করুন",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = viewModel.authEmailPhone,
                    onValueChange = { viewModel.authEmailPhone = it },
                    label = { Text(if (viewModel.isRegisterMode) "ইমেইল বা মোবাইল নম্বর" else "ইমেইল / মোবাইল নম্বর") },
                    singleLine = true,
                    leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = viewModel.authPassword,
                    onValueChange = { viewModel.authPassword = it },
                    label = { Text("পাসওয়ার্ড") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = { Icon(imageVector = Icons.Default.VpnKey, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                if (viewModel.loginError.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = viewModel.loginError, color = MaterialTheme.colorScheme.error, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { viewModel.handleLogin() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(if (viewModel.isRegisterMode) "রেজিস্ট্রেশন করুন" else "লগইন করুন", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Toggle Login / Register
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = if (viewModel.isRegisterMode) "ইতিমধ্যে অ্যাকাউন্ট আছে?" else "নতুন শিক্ষার্থী?", fontSize = 12.sp)
                    TextButton(onClick = { viewModel.isRegisterMode = !viewModel.isRegisterMode }) {
                        Text(text = if (viewModel.isRegisterMode) "লগইন করুন" else "রেজিস্ট্রেশন করুন", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(12.dp))

                Text("অন্যান্য সোশ্যাল লগইন মাধ্যম:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(10.dp))

                // Social buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = { viewModel.handleSocialLogin("Google") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                    ) {
                        Text("Google", fontSize = 12.sp)
                    }
                    OutlinedButton(
                        onClick = { viewModel.handleSocialLogin("Facebook") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    ) {
                        Text("Facebook", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// --- Student Dashboard UI Screen ---
@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val enrolledCourses by viewModel.enrolledCourses.collectAsState()
    val purchasedBooks by viewModel.purchasedBooks.collectAsState()
    val orders by viewModel.orderHistory.collectAsState()
    val wishlist by viewModel.wishlistItems.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Profile inputs
    var profileNameInput by remember(userProfile) { mutableStateOf(userProfile?.name ?: "তওসিফ আহমেদ") }
    var profilePhoneInput by remember(userProfile) { mutableStateOf(userProfile?.phone ?: "01712345678") }
    var profileEmailInput by remember(userProfile) { mutableStateOf(userProfile?.email ?: "tousif@domain.com") }
    var profileSuccessMsg by remember { mutableStateOf("") }

    // Password change inputs
    var oldPassInput by remember { mutableStateOf("") }
    var newPassInput by remember { mutableStateOf("") }
    var passChangeMsg by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Dashboard Top Header Profile Area - Elegant Minimalistic design
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                )
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Modern Glass-morphic Styled Avatar with subtle borders
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            ),
                            shape = CircleShape
                        )
                        .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userProfile?.name?.trim()?.take(1) ?: "ত",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.width(18.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userProfile?.name ?: "তওসিফ আহমেদ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = userProfile?.email ?: "tousif@domain.com",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }

                IconButton(
                    onClick = { viewModel.handleLogout() },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), CircleShape)
                        .size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Log out",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Horizontal scrolling Tab Selectors
        val tabs = listOf(
            "আমার কোর্সসমূহ",
            "আমার বইসমূহ",
            "চলমান কোর্স",
            "সম্পন্ন কোর্স",
            "অর্ডার ইতিহাস",
            "পেমেন্ট হিস্ট্রি",
            "উইশলিস্ট",
            "প্রোফাইল আপডেট",
            "পাসওয়ার্ড পরিবর্তন",
            "বিজ্ঞপ্তি"
        )

        ScrollableTabRow(
            selectedTabIndex = tabs.indexOf(viewModel.activeDashboardTab).coerceAtLeast(0),
            edgePadding = 16.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEach { t ->
                Tab(
                    selected = viewModel.activeDashboardTab == t,
                    onClick = { viewModel.activeDashboardTab = t },
                    text = { Text(t, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Render Active Dashboard Tab Content
        Column(modifier = Modifier.padding(16.dp)) {
            when (viewModel.activeDashboardTab) {
                "আমার কোর্সসমূহ" -> {
                    Text("আপনার ভর্তি হওয়া কোর্সসমূহ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(10.dp))
                    if (enrolledCourses.isEmpty()) {
                        EmptyStatePrompt("আপনি এখনো কোনো কোর্সে ভর্তি হননি। চমৎকার কোর্সসমূহ দেখতে কোর্স ট্যাব ব্রাউজ করুন।")
                    } else {
                        enrolledCourses.forEach { c ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .shadow(1.dp, RoundedCornerShape(10.dp)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(8.dp))) {
                                        MockImage(name = c.thumbnail, title = "", height = 56)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = c.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text(text = c.instructor, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            LinearProgressIndicator(
                                                progress = { c.progressPercent / 100f },
                                                color = Color(0xFF2B8A3E),
                                                trackColor = Color(0xFFE2E2E2),
                                                modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp))
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("${c.progressPercent}% সম্পন্ন", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = { viewModel.navigateTo(Screen.CourseDetails(c.courseId)) },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                    ) {
                                        Text("পড়ুন", fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                "আমার বইসমূহ" -> {
                    Text("আপনার ই-বুক সেলফ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(10.dp))
                    if (purchasedBooks.isEmpty()) {
                        EmptyStatePrompt("আপনার ই-বুক সেলফ খালি রয়েছে। পড়ার জন্য যেকোনো একটি ই-বুক ক্রয় করুন।")
                    } else {
                        purchasedBooks.forEach { b ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .shadow(1.dp, RoundedCornerShape(10.dp)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(8.dp))) {
                                        MockImage(name = b.coverUrl, title = "", height = 56)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = b.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text(text = b.authorName, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(text = "সর্বশেষ পড়া পৃষ্ঠা: ${b.lastReadPage + 1}", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
                                    }
                                    Button(
                                        onClick = { viewModel.navigateTo(Screen.EbookReader(b.bookId)) },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B8A3E))
                                    ) {
                                        Text("পড়ুন", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                "চলমান কোর্স" -> {
                    val ongoing = enrolledCourses.filter { it.progressPercent < 100 }
                    Text("চলমান কোর্সসমূহ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(10.dp))
                    if (ongoing.isEmpty()) {
                        EmptyStatePrompt("বর্তমানে কোনো চলমান কোর্স নেই।")
                    } else {
                        ongoing.forEach { c ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = c.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                                    Text("${c.progressPercent}% সম্পন্ন", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                "সম্পন্ন কোর্স" -> {
                    val completed = enrolledCourses.filter { it.progressPercent == 100 }
                    Text("সম্পন্ন করা কোর্সসমূহ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2B8A3E))
                    Spacer(modifier = Modifier.height(10.dp))
                    if (completed.isEmpty()) {
                        EmptyStatePrompt("আপনার সম্পন্ন করা কোর্সের সংখ্যা ০। লেকচারগুলো ১০০% দেখে সার্টিফিকেট পেতে চেষ্টা করুন!")
                    } else {
                        completed.forEach { c ->
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = c.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                                    Icon(imageVector = Icons.Default.Verified, contentDescription = "Completed", tint = Color(0xFF2B8A3E))
                                }
                            }
                        }
                    }
                }

                "অর্ডার ইতিহাস" -> {
                    Text("আপনার ক্রয়ের অর্ডার ও ট্রানজেকশন সূচী", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(10.dp))
                    if (orders.isEmpty()) {
                        EmptyStatePrompt("আপনার ক্রয়ের কোনো বিবরণ পাওয়া যায়নি।")
                    } else {
                        orders.forEach { o ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).border(0.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = "আইডি: ${o.orderId}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text(text = o.status, color = Color(0xFF2B8A3E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = o.itemsSummary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = "তারিখ: ${o.date}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(text = "মূল্য: ৳${o.totalAmount.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                        }
                    }
                }

                "পেমেন্ট হিস্ট্রি" -> {
                    Text("পেমেন্ট হিস্ট্রি", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(10.dp))
                    if (orders.isEmpty()) {
                        EmptyStatePrompt("কোনো পেমেন্ট বিবরণ পাওয়া যায়নি।")
                    } else {
                        orders.forEach { o ->
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Column {
                                        Text("মাধ্যম: ${o.paymentMethod}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text("ট্রানজেকশন ID: ${o.orderId}", fontSize = 10.sp)
                                    }
                                    Text("৳${o.totalAmount.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFF2B8A3E))
                                }
                            }
                        }
                    }
                }

                "উইশলিস্ট" -> {
                    Text("আপনার সংরক্ষিত উইশলিস্ট", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(10.dp))
                    if (wishlist.isEmpty()) {
                        EmptyStatePrompt("আপনার উইশলিস্টটি খালি রয়েছে।")
                    } else {
                        wishlist.forEach { item ->
                            Card(
                                onClick = {
                                    if (item.type == "course") {
                                        viewModel.navigateTo(Screen.CourseDetails(item.itemId))
                                    } else {
                                        viewModel.navigateTo(Screen.BookDetails(item.itemId))
                                    }
                                },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).shadow(1.dp, RoundedCornerShape(8.dp))
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(6.dp))) {
                                        MockImage(name = item.image, title = "", height = 48)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = item.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text(text = if (item.type == "course") "কোর্স" else "বই", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    IconButton(onClick = { viewModel.toggleWishlist(item.itemId, "", "", 0.0, "") }) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                        }
                    }
                }

                "প্রোফাইল আপডেট" -> {
                    Text("আপনার প্রোফাইল তথ্য পরিবর্তন করুন", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = profileNameInput,
                        onValueChange = { profileNameInput = it },
                        label = { Text("নাম লিখুন") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = profileEmailInput,
                        onValueChange = { profileEmailInput = it },
                        label = { Text("ইমেইল অ্যাড্রেস") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = profilePhoneInput,
                        onValueChange = { profilePhoneInput = it },
                        label = { Text("মোবাইল নম্বর") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (profileSuccessMsg.isNotEmpty()) {
                        Text(profileSuccessMsg, color = Color(0xFF2B8A3E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Button(
                        onClick = {
                            if (profileNameInput.isNotEmpty() && profileEmailInput.isNotEmpty() && profilePhoneInput.isNotEmpty()) {
                                viewModel.saveProfile(profileNameInput, profileEmailInput, profilePhoneInput)
                                profileSuccessMsg = "প্রোফাইল তথ্য সফলভাবে সংরক্ষণ করা হয়েছে!"
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("প্রোফাইল আপডেট করুন")
                    }
                }

                "পাসওয়ার্ড পরিবর্তন" -> {
                    Text("অ্যাকাউন্ট পাসওয়ার্ড পরিবর্তন", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = oldPassInput,
                        onValueChange = { oldPassInput = it },
                        label = { Text("বর্তমান পাসওয়ার্ড লিখুন") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newPassInput,
                        onValueChange = { newPassInput = it },
                        label = { Text("নতুন পাসওয়ার্ড তৈরি করুন") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (passChangeMsg.isNotEmpty()) {
                        Text(passChangeMsg, color = if (passChangeMsg.contains("সফল")) Color(0xFF2B8A3E) else MaterialTheme.colorScheme.error, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Button(
                        onClick = {
                            if (viewModel.changePassword(oldPassInput, newPassInput)) {
                                passChangeMsg = "পাসওয়ার্ড সফলভাবে পরিবর্তন হয়েছে!"
                                oldPassInput = ""
                                newPassInput = ""
                            } else {
                                passChangeMsg = "ভুল তথ্য! পাসওয়ার্ড অবশ্যই ৪ অক্ষরের বেশি হতে হবে।"
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("পাসওয়ার্ড সেভ করুন")
                    }
                }

                "বিজ্ঞপ্তি" -> {
                    Text("আপনার বিজ্ঞপ্তি বক্স", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(10.dp))
                    if (notifications.isEmpty()) {
                        EmptyStatePrompt("আপনার কোনো নোটিফিকেশন নেই।")
                    } else {
                        notifications.forEach { n ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = if (n.isRead) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        viewModel.markNotificationAsRead(n.id)
                                    }
                            ) {
                                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (n.isRead) Icons.Default.MailOutline else Icons.Default.MarkEmailUnread,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(text = n.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text(text = n.message, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(text = n.time, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun EmptyStatePrompt(message: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = Icons.Default.FolderOpen, contentDescription = null, modifier = Modifier.size(36.dp), tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = message, fontSize = 12.sp, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// --- Dynamic Admin Panel Screen ---
@Composable
fun AdminPanelScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    // Input parameters for adding elements
    var cTitle by remember { mutableStateOf("") }
    var cInstructor by remember { mutableStateOf("") }
    var cPrice by remember { mutableStateOf("") }
    var cDiscount by remember { mutableStateOf("") }
    var cDesc by remember { mutableStateOf("") }

    var bTitle by remember { mutableStateOf("") }
    var bAuthor by remember { mutableStateOf("") }
    var bIsbn by remember { mutableStateOf("") }
    var bCategory by remember { mutableStateOf("উপন্যাস") }
    var bPrice by remember { mutableStateOf("") }
    var bDiscount by remember { mutableStateOf("") }
    var bIsEbook by remember { mutableStateOf(true) }
    var bIsPhysical by remember { mutableStateOf(false) }
    var bDesc by remember { mutableStateOf("") }

    var aName by remember { mutableStateOf("") }
    var aBio by remember { mutableStateOf("") }
    var aAchievements by remember { mutableStateOf("") }

    var bannerText by remember { mutableStateOf("") }

    var actionMsg by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("অ্যাডমিন কন্ট্রোল প্যানেল", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("বাংলা সাহিত্য প্ল্যাটফর্ম ডেটা ও ব্যবহারকারী পরিচালনা করুন।", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(16.dp))

        // Analytics Cards Overview
        Row(modifier = Modifier.fillMaxWidth()) {
            AdminStatCard("মোট শিক্ষার্থী", "১,৫৪০", Icons.Default.Groups, modifier = Modifier.weight(1f))
            AdminStatCard("মোট রাজস্ব", "৳৮৫,৪০০", Icons.Default.MonetizationOn, modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            AdminStatCard("বিক্রিত বই", "৪২০টি", Icons.Default.Book, modifier = Modifier.weight(1f))
            AdminStatCard("কোর্স ভর্তি", "৭২০টি", Icons.Default.School, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selector navigation row
        val adminTabs = listOf(
            Pair("courses", "কোর্স ম্যানেজ"),
            Pair("books", "বই ম্যানেজ"),
            Pair("authors", "লেখক ম্যানেজ"),
            Pair("banners", "ব্যানার")
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            adminTabs.forEach { (key, label) ->
                FilterChip(
                    selected = viewModel.adminSelectedCategory == key,
                    onClick = {
                        viewModel.adminSelectedCategory = key
                        actionMsg = ""
                    },
                    label = { Text(label, fontSize = 11.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (actionMsg.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2B8A3E).copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(text = actionMsg, color = Color(0xFF2B8A3E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Action Form Panels
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                when (viewModel.adminSelectedCategory) {
                    "courses" -> {
                        Text("নতুন প্রিমিয়াম কোর্স তৈরি করুন", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(value = cTitle, onValueChange = { cTitle = it }, label = { Text("কোর্সের নাম/শিরোনাম") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = cInstructor, onValueChange = { cInstructor = it }, label = { Text("শিক্ষকের নাম") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            OutlinedTextField(value = cPrice, onValueChange = { cPrice = it }, label = { Text("মূল্য (৳)") }, modifier = Modifier.weight(1f).padding(end = 4.dp))
                            OutlinedTextField(value = cDiscount, onValueChange = { cDiscount = it }, label = { Text("ডিসকাউন্ট মূল্য (৳)") }, modifier = Modifier.weight(1f).padding(start = 4.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = cDesc, onValueChange = { cDesc = it }, label = { Text("কোর্সের বিস্তারিত বিবরণ") }, minLines = 3, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                if (cTitle.isNotEmpty() && cInstructor.isNotEmpty() && cPrice.isNotEmpty()) {
                                    viewModel.adminAddCourse(cTitle, cInstructor, cPrice.toDoubleOrNull() ?: 1000.0, cDiscount.toDoubleOrNull(), cDesc)
                                    actionMsg = "সফল! নতুন কোর্স '${cTitle}' যুক্ত করা হয়েছে এবং শিক্ষার্থীদের ড্যাশবোর্ডে লাইভ দেওয়া হয়েছে।"
                                    cTitle = ""
                                    cInstructor = ""
                                    cPrice = ""
                                    cDiscount = ""
                                    cDesc = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("নতুন কোর্স প্রকাশ করুন")
                        }
                    }

                    "books" -> {
                        Text("লাইব্রেরিতে নতুন বই যুক্ত করুন", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(value = bTitle, onValueChange = { bTitle = it }, label = { Text("বইয়ের নাম/শিরোনাম") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = bAuthor, onValueChange = { bAuthor = it }, label = { Text("লেখকের নাম") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            OutlinedTextField(value = bIsbn, onValueChange = { bIsbn = it }, label = { Text("ISBN কোড") }, modifier = Modifier.weight(1.2f).padding(end = 4.dp))
                            OutlinedTextField(value = bCategory, onValueChange = { bCategory = it }, label = { Text("ক্যাটেগরি") }, modifier = Modifier.weight(0.8f).padding(start = 4.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            OutlinedTextField(value = bPrice, onValueChange = { bPrice = it }, label = { Text("মূল্য (৳)") }, modifier = Modifier.weight(1f).padding(end = 4.dp))
                            OutlinedTextField(value = bDiscount, onValueChange = { bDiscount = it }, label = { Text("ডিসকাউন্ট (৳)") }, modifier = Modifier.weight(1f).padding(start = 4.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = bIsEbook, onCheckedChange = { bIsEbook = it })
                            Text("ই-বুক সমর্থন", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(16.dp))
                            Checkbox(checked = bIsPhysical, onCheckedChange = { bIsPhysical = it })
                            Text("ফিজিক্যাল কপি সমর্থন", fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = bDesc, onValueChange = { bDesc = it }, label = { Text("বইয়ের সারসংক্ষেপ") }, minLines = 3, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                if (bTitle.isNotEmpty() && bAuthor.isNotEmpty() && bPrice.isNotEmpty()) {
                                    viewModel.adminAddBook(bTitle, bAuthor, bIsbn, bCategory, bPrice.toDoubleOrNull() ?: 200.0, bDiscount.toDoubleOrNull(), bIsEbook, bIsPhysical, bDesc)
                                    actionMsg = "সফল! নতুন বই '${bTitle}' লাইব্রেরিতে যুক্ত করা হয়েছে।"
                                    bTitle = ""
                                    bAuthor = ""
                                    bIsbn = ""
                                    bPrice = ""
                                    bDiscount = ""
                                    bDesc = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("লাইব্রেরিতে বই জমা দিন")
                        }
                    }

                    "authors" -> {
                        Text("বরেণ্য লেখক প্রোফাইল যোগ করুন", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(value = aName, onValueChange = { aName = it }, label = { Text("লেখকের নাম") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = aAchievements, onValueChange = { aAchievements = it }, label = { Text("পুরস্কার ও অর্জনসমূহ") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = aBio, onValueChange = { aBio = it }, label = { Text("লেখকের জীবনী") }, minLines = 4, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                if (aName.isNotEmpty() && aBio.isNotEmpty()) {
                                    viewModel.adminAddAuthor(aName, aBio, aAchievements)
                                    actionMsg = "সফল! বরেণ্য লেখক '${aName}' এর সাহিত্য জীবনী সফলভাবে ডাটাবেজে প্রকাশিত হয়েছে।"
                                    aName = ""
                                    aBio = ""
                                    aAchievements = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("লেখক প্রোফাইল সংরক্ষণ করুন")
                        }
                    }

                    "banners" -> {
                        Text("মোবাইল অ্যাপের নতুন ব্যানার ও অফার ঘোষণা", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(value = bannerText, onValueChange = { bannerText = it }, placeholder = { Text("যেমন: বিশেষ ঘোষণা: রবীন্দ্রজয়ন্তী উপলক্ষে সকল বইয়ে ৩০% ছাড়!") }, minLines = 2, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                if (bannerText.isNotEmpty()) {
                                    viewModel.adminAddBanner(bannerText)
                                    actionMsg = "সফল! প্রমোশন ব্যানারটি স্লাইডারে লাইভ দেওয়া হয়েছে।"
                                    bannerText = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("ব্যানার পাবলিশ করুন")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = modifier
            .padding(4.dp)
            .shadow(0.5.dp, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
        }
    }
}
