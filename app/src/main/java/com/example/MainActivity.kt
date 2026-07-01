package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.animation.core.*
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = viewModel.isDarkTheme) {
                var showSplashScreen by remember { mutableStateOf(true) }

                if (showSplashScreen) {
                    SplashScreen(
                        isDarkTheme = viewModel.isDarkTheme,
                        onFinished = { showSplashScreen = false }
                    )
                } else {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    val currentScreen = viewModel.navigationStack.lastOrNull() ?: Screen.Home
                    val userProfile by viewModel.userProfile.collectAsState()

                    // Intercept Android System Back Press
                    BackHandler(enabled = viewModel.navigationStack.size > 1) {
                        viewModel.navigateBack()
                    }

                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "বাংলা সাহিত্য",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                            Text(
                                text = "সংস্কৃতি ও সাহিত্য শিক্ষা",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(12.dp))

                            // Drawer Items
                            val drawerItems = listOf(
                                Triple(Screen.Home, "হোম (প্রধান পাতা)", Icons.Default.Home),
                                Triple(Screen.Courses, "কোর্সসমূহ", Icons.Default.School),
                                Triple(Screen.Books, "লাইব্রেরি ও বইমেলা", Icons.Default.Book),
                                Triple(Screen.Authors, "বরেণ্য লেখকবৃন্দ", Icons.Default.Portrait),
                                Triple(Screen.Blog, "সাহিত্য ব্লগ ও প্রবন্ধ", Icons.Default.Article),
                                Triple(Screen.AboutUs, "আমাদের সম্পর্কে", Icons.Default.Info),
                                Triple(Screen.ContactUs, "যোগাযোগ করুন", Icons.Default.ContactSupport),
                                Triple(if (userProfile?.isLoggedIn == true) Screen.Dashboard else Screen.Auth, "আমার ড্যাশবোর্ড", Icons.Default.Dashboard)
                            )

                            drawerItems.forEach { (targetScreen, label, icon) ->
                                NavigationDrawerItem(
                                    icon = { Icon(imageVector = icon, contentDescription = label) },
                                    label = { Text(label, fontSize = 13.sp, fontWeight = FontWeight.Bold) },
                                    selected = currentScreen == targetScreen,
                                    onClick = {
                                        scope.launch { drawerState.close() }
                                        viewModel.navigateTo(targetScreen)
                                    },
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            if (currentScreen !is Screen.EbookReader) {
                                Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                                    CenterAlignedTopAppBar(
                                        title = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                                                val scale by infiniteTransition.animateFloat(
                                                    initialValue = 0.94f,
                                                    targetValue = 1.06f,
                                                    animationSpec = infiniteRepeatable(
                                                        animation = tween(1500, easing = FastOutSlowInEasing),
                                                        repeatMode = RepeatMode.Reverse
                                                    ),
                                                    label = "scale"
                                                )
                                                Image(
                                                    painter = painterResource(id = R.drawable.ic_brand_logo_new_1782817994864),
                                                    contentDescription = "Logo",
                                                    modifier = Modifier
                                                        .size(28.dp)
                                                        .clip(CircleShape)
                                                        .graphicsLayer {
                                                            scaleX = scale
                                                            scaleY = scale
                                                        }
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "বাংলা স্কুল",
                                                    fontWeight = FontWeight.Black,
                                                    fontSize = 17.sp,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        },
                                        navigationIcon = {
                                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                                            }
                                        },
                                        actions = {
                                            // Dark Mode Switcher
                                            IconButton(onClick = { viewModel.isDarkTheme = !viewModel.isDarkTheme }) {
                                                Icon(
                                                    imageVector = if (viewModel.isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                                    contentDescription = "Toggle Dark Mode"
                                                )
                                            }

                                            // Dashboard Direct shortcut
                                            IconButton(onClick = {
                                                if (userProfile?.isLoggedIn == true) {
                                                    viewModel.navigateTo(Screen.Dashboard)
                                                } else {
                                                    viewModel.navigateTo(Screen.Auth)
                                                }
                                            }) {
                                                Icon(
                                                    imageVector = if (userProfile?.isLoggedIn == true) Icons.Filled.AccountCircle else Icons.Outlined.AccountCircle,
                                                    contentDescription = "Profile",
                                                    tint = if (userProfile?.isLoggedIn == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        },
                                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                            containerColor = MaterialTheme.colorScheme.surface
                                        )
                                    )
                                    ShadcnPersistentHeaderSearchBar(viewModel = viewModel)
                                    
                                    // Live Server Connection Banner
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                if (viewModel.isLiveServerConnected) 
                                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f) 
                                                else 
                                                    MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)
                                            )
                                            .clickable { viewModel.syncWithLiveServer() }
                                            .padding(vertical = 6.dp, horizontal = 16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (viewModel.isLiveServerConnected) 
                                                        Color(0xFF2B8A3E) 
                                                    else 
                                                        Color(0xFFC92A2A)
                                                )
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = viewModel.serverStatusMessage,
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (viewModel.isLiveServerConnected) 
                                                MaterialTheme.colorScheme.onPrimaryContainer 
                                            else {
                                                if (viewModel.isDarkTheme) Color(0xFFFF8787) else Color(0xFFC92A2A)
                                            }
                                        )
                                        if (viewModel.isSyncingServerData) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(10.dp),
                                                strokeWidth = 1.5.dp,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        } else {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Icon(
                                                imageVector = Icons.Default.Refresh,
                                                contentDescription = "Re-sync",
                                                modifier = Modifier.size(12.dp),
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                        thickness = 1.dp
                                    )
                                }
                            }
                        },
                        bottomBar = {
                            if (currentScreen !is Screen.EbookReader) {
                                NavigationBar {
                                    val bottomItems = listOf(
                                        Triple("home", "হোম", Icons.Default.Home),
                                        Triple("courses", "কোর্স", Icons.Default.School),
                                        Triple("books", "বইসমূহ", Icons.Default.AutoStories),
                                        Triple("authors", "লেখকবৃন্দ", Icons.Default.Groups),
                                        Triple("dashboard", "ড্যাশবোর্ড", Icons.Default.Dashboard)
                                    )

                                    bottomItems.forEach { (route, label, icon) ->
                                        val isSelected = when (route) {
                                            "home" -> currentScreen is Screen.Home
                                            "courses" -> currentScreen is Screen.Courses || currentScreen is Screen.CourseDetails
                                            "books" -> currentScreen is Screen.Books || currentScreen is Screen.BookDetails
                                            "authors" -> currentScreen is Screen.Authors || currentScreen is Screen.AuthorDetails
                                            "dashboard" -> currentScreen is Screen.Dashboard || currentScreen is Screen.Auth
                                            else -> false
                                        }

                                        NavigationBarItem(
                                            selected = isSelected,
                                            onClick = { viewModel.selectBottomTab(route) },
                                            icon = { Icon(imageVector = icon, contentDescription = label) },
                                            label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                                        )
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        // Screen Routing Handler with beautiful smooth crossfade transition animations
                        Crossfade(
                            targetState = currentScreen,
                            animationSpec = tween(350),
                            label = "screen_transition",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) { targetScreen ->
                            when (targetScreen) {
                                is Screen.Home -> HomeScreen(viewModel = viewModel)
                                is Screen.Courses -> CourseListScreen(viewModel = viewModel)
                                is Screen.CourseDetails -> CourseDetailsScreen(
                                    courseId = targetScreen.courseId,
                                    viewModel = viewModel
                                )
                                is Screen.Books -> BookListScreen(viewModel = viewModel)
                                is Screen.BookDetails -> BookDetailsScreen(
                                    bookId = targetScreen.bookId,
                                    viewModel = viewModel
                                )
                                is Screen.EbookReader -> EbookReaderScreen(
                                    bookId = targetScreen.bookId,
                                    viewModel = viewModel
                                )
                                is Screen.Authors -> AuthorListScreen(viewModel = viewModel)
                                is Screen.AuthorDetails -> AuthorDetailsScreen(
                                    authorId = targetScreen.authorId,
                                    viewModel = viewModel
                                )
                                is Screen.Blog -> BlogScreen(viewModel = viewModel)
                                is Screen.AboutUs -> AboutUsScreen(viewModel = viewModel)
                                is Screen.ContactUs -> ContactUsScreen(viewModel = viewModel)
                                is Screen.Dashboard -> DashboardScreen(viewModel = viewModel)
                                is Screen.Auth -> AuthScreen(viewModel = viewModel)
                                is Screen.AdminPanel -> AdminPanelScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
}

@Composable
fun SplashScreen(isDarkTheme: Boolean, onFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    val scale = animateFloatAsState(
        targetValue = if (startAnimation) 1.1f else 0.5f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "logo_scale"
    )

    val alpha = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "logo_alpha"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(1800) // Immersive delay
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkTheme) Color(0xFF121212) else Color(0xFFFAF9F6)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_brand_logo_new_1782817994864),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .graphicsLayer(
                        scaleX = scale.value,
                        scaleY = scale.value,
                        alpha = alpha.value
                    )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "বাংলা স্কুল",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.graphicsLayer(alpha = alpha.value)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "সংস্কৃতি ও সাহিত্য শিক্ষা",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.graphicsLayer(alpha = alpha.value)
            )
            Spacer(modifier = Modifier.height(48.dp))
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp,
                modifier = Modifier
                    .size(36.dp)
                    .graphicsLayer(alpha = alpha.value)
            )
        }
    }
}
