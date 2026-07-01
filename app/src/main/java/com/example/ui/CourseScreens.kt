package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*

@Composable
fun CourseListScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val filteredCourses = viewModel.courses.filter {
        it.title.contains(viewModel.courseSearchQuery) || it.instructor.contains(viewModel.courseSearchQuery)
    }
    val wishlist by viewModel.wishlistItems.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "rotate")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "আমাদের কোর্সসমূহ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(
                onClick = { viewModel.refreshCourses() },
                enabled = !viewModel.isCoursesLoading
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.graphicsLayer {
                        rotationZ = if (viewModel.isCoursesLoading) rotationAngle else 0f
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "দেশের সেরা সাহিত্য শিক্ষকদের চমৎকার লেকচারগুলোর মাধ্যমে বাংলা সাহিত্যচর্চা করুন সহজ পদ্ধতিতে।",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = viewModel.courseSearchQuery,
            onValueChange = { viewModel.courseSearchQuery = it },
            placeholder = { Text("কোর্স বা শিক্ষকের নাম দিয়ে খুঁজুন...", fontSize = 13.sp) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                if (viewModel.courseSearchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.courseSearchQuery = "" }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isCoursesLoading) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(3) {
                    ShadcnCourseCardSkeleton()
                }
            }
        } else if (filteredCourses.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.SentimentDissatisfied, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("কোনো কোর্স খুঁজে পাওয়া যায়নি!", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredCourses) { course ->
                    val isFav = wishlist.any { it.itemId == course.id }
                    ShadcnCourseCard(
                        course = course,
                        onClick = { viewModel.navigateTo(Screen.CourseDetails(course.id)) },
                        isFavorite = isFav,
                        onWishlistClick = {
                            viewModel.toggleWishlist(
                                itemId = course.id,
                                title = course.title,
                                image = course.thumbnail,
                                price = course.discountPrice ?: course.price,
                                type = "course"
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CourseDetailsScreen(
    courseId: String,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val course = viewModel.courses.find { it.id == courseId }

    if (course == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("কোর্সটি খুঁজে পাওয়া যায়নি!")
        }
        return
    }

    // Check enrollment status reactively
    val enrollments by viewModel.enrolledCourses.collectAsState()
    val isEnrolled = enrollments.any { it.courseId == course.id }
    val userProfile by viewModel.userProfile.collectAsState()

    var showPaymentSheet by remember { mutableStateOf(false) }
    var activePlayingVideoTitle by remember { mutableStateOf("") }
    var activePlayingVideoUrl by remember { mutableStateOf("") }
    var activePlayingVideoDuration by remember { mutableStateOf("") }
    var isVideoPlaying by remember { mutableStateOf(false) }

    // Custom review inputs
    var userReviewName by remember { mutableStateOf("") }
    var userReviewComment by remember { mutableStateOf("") }
    var userReviewRating by remember { mutableStateOf(5f) }
    var reviewAddedMessage by remember { mutableStateOf("") }

    if (showPaymentSheet) {
        PaymentSheet(
            itemName = course.title,
            price = course.discountPrice ?: course.price,
            onPaymentSuccess = {
                viewModel.enrollInCourse(course)
                showPaymentSheet = false
            },
            onDismiss = { showPaymentSheet = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Course cover
        MockImage(name = course.thumbnail, title = course.title, subtitle = course.instructor, height = 180)

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = course.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, lineHeight = 26.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // Author and ratings
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFF1C40F), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${course.rating}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "(${course.reviews.size}টি রিভিউ)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = course.duration, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Price and Enroll Box
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("কোর্স ফি:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (course.discountPrice != null) {
                                Text(
                                    text = "৳${course.price.toInt()}",
                                    fontSize = 13.sp,
                                    style = MaterialTheme.typography.bodySmall.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "৳${course.discountPrice.toInt()}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            } else {
                                Text(
                                    text = "৳${course.price.toInt()}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    if (isEnrolled) {
                        Button(
                            onClick = {
                                viewModel.activeDashboardTab = "আমার কোর্সসমূহ"
                                viewModel.navigateTo(Screen.Dashboard)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B8A3E))
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("এনরোল করা আছে", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = {
                                if (userProfile?.isLoggedIn == true) {
                                    showPaymentSheet = true
                                } else {
                                    viewModel.navigateTo(Screen.Auth)
                                }
                            }
                        ) {
                            Text("ভর্তি হোন ➔", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Custom Video Player View ---
            if (activePlayingVideoTitle.isNotEmpty()) {
                Text(
                    text = "ভিডিও প্লেয়ার:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                CustomVideoPlayer(
                    title = activePlayingVideoTitle,
                    duration = activePlayingVideoDuration,
                    isPlaying = isVideoPlaying,
                    onTogglePlay = { isVideoPlaying = !isVideoPlaying }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // --- Tab Selection Bar ---
            val tabs = listOf("পরিচিতি", "সিলেবাস", "ফ্রি ভিডিও", "পেইড ভিডিও", "টেক্সট কনটেন্ট", "রিভিউ")
            ScrollableTabRow(
                selectedTabIndex = tabs.indexOf(viewModel.activeCourseTab).coerceAtLeast(0),
                edgePadding = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEach { tab ->
                    Tab(
                        selected = viewModel.activeCourseTab == tab,
                        onClick = { viewModel.activeCourseTab = tab },
                        text = { Text(tab, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Tab Contents ---
            when (viewModel.activeCourseTab) {
                "পরিচিতি" -> {
                    Column {
                        Text(text = "কোর্স পরিচিতি", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = course.description, fontSize = 13.sp, lineHeight = 20.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "যা যা শিখবেন", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = course.overview, fontSize = 13.sp, lineHeight = 20.sp)

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "শিক্ষকের পরিচিতি", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(24.dp)).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                                    Icon(imageVector = Icons.Default.Portrait, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = course.instructor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = course.instructorBio, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }

                "সিলেবাস" -> {
                    Column {
                        Text(text = "বিস্তারিত সিলেবাস ও লেকচার সূচী", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        course.lessons.forEachIndexed { idx, lesson ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .border(0.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(MaterialTheme.colorScheme.primary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "${idx + 1}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = lesson.title, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(text = "সময়কাল: ${lesson.duration}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    if (lesson.isFreePreview) {
                                        SuggestionChip(
                                            onClick = {},
                                            label = { Text("ফ্রি প্রিভিউ", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                                        )
                                    } else {
                                        Icon(imageVector = if (isEnrolled) Icons.Default.LockOpen else Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                        }
                    }
                }

                "ফ্রি ভিডিও" -> {
                    val freeVideos = course.lessons.filter { it.isFreePreview }
                    Column {
                        Text(text = "ফ্রি ডেমো লেকচারসমূহ", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "ভর্তি হওয়ার পূর্বে শিক্ষকের পড়ানোর ধরণ দেখে নিন সম্পূর্ণ বিনামূল্যে।", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(10.dp))

                        freeVideos.forEach { lesson ->
                            Card(
                                onClick = {
                                    activePlayingVideoTitle = lesson.title
                                    activePlayingVideoUrl = lesson.videoUrl
                                    activePlayingVideoDuration = lesson.duration
                                    isVideoPlaying = true
                                },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = lesson.title, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text(text = "সময়কাল: ${lesson.duration}", fontSize = 10.sp)
                                    }
                                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
                                }
                            }
                        }
                    }
                }

                "পেইড ভিডিও" -> {
                    if (isEnrolled) {
                        val paidVideos = course.lessons.filter { !it.isFreePreview }
                        Column {
                            Text(text = "প্রিমিয়াম লেকচার সূচী", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2B8A3E))
                            Spacer(modifier = Modifier.height(8.dp))
                            paidVideos.forEach { lesson ->
                                val completed = viewModel.isLessonCompleted(course.id, lesson.id)

                                Card(
                                    onClick = {
                                        activePlayingVideoTitle = lesson.title
                                        activePlayingVideoUrl = lesson.videoUrl
                                        activePlayingVideoDuration = lesson.duration
                                        isVideoPlaying = true
                                    },
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .border(
                                            1.dp,
                                            if (completed) Color(0xFF2B8A3E) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                            RoundedCornerShape(8.dp)
                                        )
                                ) {
                                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (completed) Icons.Default.CheckCircle else Icons.Default.PlayCircleOutline,
                                            contentDescription = null,
                                            tint = if (completed) Color(0xFF2B8A3E) else MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = lesson.title, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Text(text = "সময়কাল: ${lesson.duration}", fontSize = 10.sp)
                                        }
                                        Checkbox(
                                            checked = completed,
                                            onCheckedChange = {
                                                viewModel.completeLesson(course.id, lesson.id)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // Locked Screen
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "পেইড লেকচারগুলো লক করা রয়েছে!", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "এই কোর্সের পূর্ণাঙ্গ প্রিমিয়াম লেকচারগুলো দেখতে এখনই ভর্তি সম্পন্ন করুন।",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    if (userProfile?.isLoggedIn == true) {
                                        showPaymentSheet = true
                                    } else {
                                        viewModel.navigateTo(Screen.Auth)
                                    }
                                }
                            ) {
                                Text("কোর্সে ভর্তি হোন")
                            }
                        }
                    }
                }

                "টেক্সট কনটেন্ট" -> {
                    if (isEnrolled) {
                        Column {
                            Text(text = "লেকচার নোট ও লিখিত পড়াশোনা", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(10.dp))
                            course.lessons.forEach { lesson ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(text = lesson.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = if (lesson.textContent.isNotEmpty()) lesson.textContent else "এই লেকচারের বিস্তারিত বিবরণ ও নোটে রয়েছে অসাধারণ গবেষণামূলক সমাধান। আপনার সাহিত্য পঠনকে এগিয়ে নিতে নোটটি সম্পূর্ণ পড়ুন।",
                                            fontSize = 12.sp,
                                            lineHeight = 18.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "লিখিত লেকচার নোটগুলো লক করা আছে!", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "কোর্সে ভর্তি হওয়ার পর সকল লেকচারের লিখিত পিডিএফ এবং নোট সরাসরি আনলক হবে।",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                "রিভিউ" -> {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "শিক্ষার্থীদের রিভিউ ও মতামত", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color(0xFFF1C40F), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(text = "${course.rating}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        // Review submission Form
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth().border(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("নিজে একটি রিভিউ লিখুন", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(8.dp))

                                // Rating selection stars
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("আপনার রেটিং: ", fontSize = 12.sp)
                                    (1..5).forEach { star ->
                                        IconButton(
                                            onClick = { userReviewRating = star.toFloat() },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (userReviewRating >= star) Icons.Default.Star else Icons.Default.StarBorder,
                                                contentDescription = "$star Stars",
                                                tint = Color(0xFFF1C40F),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))

                                OutlinedTextField(
                                    value = userReviewName,
                                    onValueChange = { userReviewName = it },
                                    placeholder = { Text("আপনার নাম লিখুন", fontSize = 11.sp) },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = userReviewComment,
                                    onValueChange = { userReviewComment = it },
                                    placeholder = { Text("আপনার মতামত ও অভিজ্ঞতা লিখুন...", fontSize = 11.sp) },
                                    minLines = 2,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                if (reviewAddedMessage.isNotEmpty()) {
                                    Text(text = reviewAddedMessage, color = Color(0xFF2B8A3E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(6.dp))
                                }

                                Button(
                                    onClick = {
                                        if (userReviewName.isNotEmpty() && userReviewComment.isNotEmpty()) {
                                            viewModel.addReview(course.id, true, userReviewRating, userReviewComment, userReviewName)
                                            reviewAddedMessage = "ধন্যবাদ! আপনার রিভিউটি সফলভাবে গৃহীত হয়েছে।"
                                            userReviewComment = ""
                                            userReviewName = ""
                                        } else {
                                            reviewAddedMessage = "দয়া করে নাম ও মতামত ঘর পূরণ করুন।"
                                        }
                                    },
                                    modifier = Modifier.align(Alignment.End),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("রিভিউ জমা দিন", fontSize = 11.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Review List
                        if (course.reviews.isEmpty()) {
                            Text("এই কোর্সে এখনো কোনো রিভিউ দেওয়া হয়নি। প্রথম রিভিউটি আপনিই লিখুন!", fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))
                        } else {
                            course.reviews.forEach { r ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).border(0.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = r.userName, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Row {
                                                (1..5).forEach { star ->
                                                    Icon(
                                                        imageVector = if (r.rating >= star) Icons.Default.Star else Icons.Default.StarBorder,
                                                        contentDescription = null,
                                                        tint = Color(0xFFF1C40F),
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(text = r.date, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = r.comment, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
