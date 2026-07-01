package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlin.math.absoluteValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import coil.compose.AsyncImage

@Composable
fun AnimatedEntrance(
    delayMillis: Int,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)) +
                slideInVertically(
                    initialOffsetY = { 40 },
                    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                )
    ) {
        content()
    }
}

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // --- Dynamic Hero Banner Slider ---
        AnimatedEntrance(delayMillis = 50) {
            HeroBannerSlider(banners = viewModel.banners, onActionClick = { viewModel.selectBottomTab(it) })
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Call To Action: Quick Access Row ---
        AnimatedEntrance(delayMillis = 150) {
            QuickAccessRow(onNavigate = { viewModel.selectBottomTab(it) })
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Featured Courses Slider ---
        AnimatedEntrance(delayMillis = 250) {
            Column {
                SectionHeader(title = "জনপ্রিয় কোর্সসমূহ", subtitle = "আপনার সাহিত্য চর্চাকে সমৃদ্ধ করতে প্রথিতযশা শিক্ষকদের নির্দেশনায় ভর্তি হোন।", onSeeAll = { viewModel.selectBottomTab("courses") })
                FeaturedCoursesSlider(courses = viewModel.courses, onCourseClick = { viewModel.navigateTo(Screen.CourseDetails(it)) }, viewModel = viewModel)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Best Selling Books Slider ---
        AnimatedEntrance(delayMillis = 350) {
            Column {
                SectionHeader(title = "বেস্ট সেলার বইসমূহ", subtitle = "সবচেয়ে বেশি পঠিত ও সমাদৃত সাহিত্যের সেরা মলাটগুলো বেছে নিন।", onSeeAll = { viewModel.selectBottomTab("books") })
                BooksHorizontalSlider(books = viewModel.books.filter { it.isBestSeller }, onBookClick = { viewModel.navigateTo(Screen.BookDetails(it)) }, viewModel = viewModel)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Popular E-Books Slider ---
        AnimatedEntrance(delayMillis = 450) {
            Column {
                SectionHeader(title = "জনপ্রিয় ই-বুকসমূহ", subtitle = "যেকোনো স্থানে আপনার ফোনেই পড়ুন সেরা ই-বুকগুলো।", onSeeAll = { viewModel.selectBottomTab("books") })
                BooksHorizontalSlider(books = viewModel.books.filter { it.isEbook }, onBookClick = { viewModel.navigateTo(Screen.BookDetails(it)) }, viewModel = viewModel)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Featured Authors Slider ---
        AnimatedEntrance(delayMillis = 550) {
            Column {
                SectionHeader(title = "বরেণ্য লেখকবৃন্দ", subtitle = "যাঁদের অবিস্মরণীয় সৃষ্টিতে বাংলা সাহিত্য বিশ্বদরবারে অনন্য উচ্চতায় পৌঁছেছে।", onSeeAll = { viewModel.selectBottomTab("authors") })
                AuthorsHorizontalSlider(authors = viewModel.authors, onAuthorClick = { viewModel.navigateTo(Screen.AuthorDetails(it)) })
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Student Testimonials ---
        AnimatedEntrance(delayMillis = 650) {
            StudentTestimonialsSection()
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Latest Blog Articles ---
        AnimatedEntrance(delayMillis = 750) {
            Column {
                SectionHeader(title = "সাহিত্য ব্লগ ও প্রবন্ধ", subtitle = "গবেষণামূলক লেখা ও সাহিত্যের বিভিন্ন যুগের বিবর্তন নিয়ে পড়ুন।", onSeeAll = { viewModel.selectBottomTab("blog") })
                LatestBlogsSection(blogs = viewModel.blogs, onBlogClick = { viewModel.navigateTo(Screen.Blog) })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Newsletter ---
        AnimatedEntrance(delayMillis = 850) {
            NewsletterSection()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Footer ---
        AnimatedEntrance(delayMillis = 950) {
            FooterSection(onNavigate = { viewModel.selectBottomTab(it) })
        }
    }
}

data class PromoBanner(
    val title: String,
    val subtitle: String,
    val badge: String,
    val buttonText: String,
    val targetTab: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val gradientColors: List<Color>,
    val imageRes: Int
)

@Composable
fun HeroBannerSlider(
    banners: List<String>,
    onActionClick: (String) -> Unit
) {
    if (banners.isEmpty()) return

    val promoBanners = remember(banners) {
        banners.mapIndexed { index, text ->
            val title: String
            val subtitle: String
            val badge: String
            val buttonText: String
            val targetTab: String
            val icon = when (index % 3) {
                0 -> Icons.Default.Celebration
                1 -> Icons.Default.AutoAwesome
                else -> Icons.Default.MenuBook
            }
            val gradient = when (index % 3) {
                0 -> listOf(Color(0xFFF97316), Color(0xFFDC2626)) // Vibrant Orange-Red (Bangla School Primary)
                1 -> listOf(Color(0xFF2563EB), Color(0xFF4F46E5)) // Royal Blue to Indigo
                else -> listOf(Color(0xFFEAB308), Color(0xFFD97706)) // Amber to Dark Orange
            }
            val imageRes = when (index % 3) {
                0 -> com.example.R.drawable.img_carousel_books_1_1782808327015
                1 -> com.example.R.drawable.img_carousel_corner_2_1782808347662
                else -> com.example.R.drawable.img_carousel_shelves_3_1782808365474
            }

            if (text.contains("অফার") || text.contains("ছাড়")) {
                title = text.substringBefore("কুপন:").trim()
                subtitle = if (text.contains("কুপন:")) "কুপন কোড: " + text.substringAfter("কুপন:").trim() else "আজই ভর্তি হোন!"
                badge = "সীমিত সময়ের অফার"
                buttonText = "ভর্তি হোন"
                targetTab = "courses"
            } else if (text.contains("রবীন্দ্রনাথ") || text.contains("কোর্স")) {
                title = text
                subtitle = "প্রথিতযশা শিক্ষকদের সাথে বাংলা সাহিত্যচর্চা।"
                badge = "নতুন কোর্স"
                buttonText = "কোর্সসমূহ দেখুন"
                targetTab = "courses"
            } else if (text.contains("বইমেলা") || text.contains("বই")) {
                title = text
                subtitle = "সেরা ই-বুক সংগ্রহশালা আপনার হাতের মুঠোয়।"
                badge = "বইমেলা ধামাকা"
                buttonText = "বই দেখুন"
                targetTab = "books"
            } else {
                title = text
                subtitle = "সবার জন্য বাংলা স্কুল - বাংলা সাহিত্য ই-লার্নিং"
                badge = "বিশেষ ঘোষণা"
                buttonText = "বিস্তারিত জানুন"
                targetTab = "courses"
            }
            
            PromoBanner(title, subtitle, badge, buttonText, targetTab, icon, gradient, imageRes)
        }
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { promoBanners.size }
    )

    // Auto-scroll effect
    LaunchedEffect(key1 = promoBanners) {
        if (promoBanners.isNotEmpty()) {
            while (true) {
                delay(4500)
                val nextPage = (pagerState.currentPage + 1) % promoBanners.size
                pagerState.animateScrollToPage(
                    page = nextPage,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
            pageSpacing = 12.dp
        ) { page ->
            val banner = promoBanners[page]
            
            // Framer Motion style page slide offset calculations
            val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
            val scale = 1f - (pageOffset.absoluteValue * 0.12f).coerceIn(0f, 1f)
            val alpha = 1f - (pageOffset.absoluteValue * 0.6f).coerceIn(0f, 1f)
            val rotation = pageOffset * -10f
            val translationX = pageOffset * 50.dp.value

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                        rotationY = rotation
                        this.translationX = translationX
                    }
                    .shadow(
                        elevation = if (pagerState.currentPage == page) 6.dp else 1.dp,
                        shape = RoundedCornerShape(16.dp),
                        clip = false
                    )
                    .clickable { onActionClick(banner.targetTab) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    androidx.compose.foundation.Image(
                        painter = androidx.compose.ui.res.painterResource(id = banner.imageRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                    
                    // Rich dark modern overlay gradient for premium literature reading
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.25f),
                                        Color.Black.copy(alpha = 0.8f)
                                    )
                                )
                            )
                    )

                    // Decorative Abstract Background Wave/Circle for premium branding feel
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = Color.White.copy(alpha = 0.05f),
                            radius = size.width * 0.4f,
                            center = Offset(size.width * 0.9f, size.height * 0.1f)
                        )
                        drawCircle(
                            color = Color.White.copy(alpha = 0.03f),
                            radius = size.width * 0.25f,
                            center = Offset(size.width * 0.1f, size.height * 0.9f)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Badge and Icon
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Badge with subtle scale/breathing animation
                            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                            val badgeScale by infiniteTransition.animateFloat(
                                initialValue = 0.95f,
                                targetValue = 1.05f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(1200, easing = FastOutSlowInEasing),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "badgeScale"
                            )

                            Box(
                                modifier = Modifier
                                    .graphicsLayer {
                                        scaleX = badgeScale
                                        scaleY = badgeScale
                                    }
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White.copy(alpha = 0.22f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = banner.badge,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Icon(
                                imageVector = banner.icon,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // Banner Texts (Title & Subtitle)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = banner.title,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 22.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = banner.subtitle,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White.copy(alpha = 0.9f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Action Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { onActionClick(banner.targetTab) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = banner.gradientColors[0]
                                ),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .height(30.dp)
                                    .shadow(2.dp, RoundedCornerShape(20.dp))
                            ) {
                                Text(
                                    text = banner.buttonText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Pager indicator dots (smooth transition sizes & alpha values)
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            promoBanners.forEachIndexed { i, _ ->
                val active = pagerState.currentPage == i
                val width by animateDpAsState(
                    targetValue = if (active) 16.dp else 6.dp,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "dotWidth"
                )
                val alpha by animateFloatAsState(
                    targetValue = if (active) 1f else 0.4f,
                    label = "dotAlpha"
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(height = 6.dp, width = width)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = alpha)
                        )
                )
            }
        }
    }
}

@Composable
fun QuickAccessRow(onNavigate: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val menuItems = listOf(
            Triple("courses", "কোর্সসমূহ", Icons.Default.School),
            Triple("books", "ই-বইসমূহ", Icons.Default.AutoStories),
            Triple("authors", "লেখকবৃন্দ", Icons.Default.Groups),
            Triple("blog", "ব্লগ ও আর্টিকেল", Icons.Default.PostAdd)
        )

        menuItems.forEach { (route, label, icon) ->
            Card(
                onClick = { onNavigate(route) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .shadow(1.dp, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String,
    onSeeAll: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "সব দেখুন ➔",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onSeeAll() }
            )
        }
        if (subtitle.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun FeaturedCoursesSlider(
    courses: List<Course>,
    onCourseClick: (String) -> Unit,
    viewModel: MainViewModel? = null
) {
    val wishlist by if (viewModel != null) viewModel.wishlistItems.collectAsState() else remember { mutableStateOf(emptyList()) }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(courses) { course ->
            val isFav = wishlist.any { it.itemId == course.id }
            ShadcnCourseCard(
                course = course,
                onClick = { onCourseClick(course.id) },
                isFavorite = isFav,
                onWishlistClick = if (viewModel != null) {
                    {
                        viewModel.toggleWishlist(
                            itemId = course.id,
                            title = course.title,
                            image = course.thumbnail,
                            price = course.discountPrice ?: course.price,
                            type = "course"
                        )
                    }
                } else null,
                modifier = Modifier.width(260.dp)
            )
        }
    }
}

@Composable
fun BooksHorizontalSlider(
    books: List<Book>,
    onBookClick: (String) -> Unit,
    viewModel: MainViewModel
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(books) { book ->
            ShadcnBookCard(
                book = book,
                onClick = { onBookClick(book.id) },
                onAddToCartClick = { viewModel.addToCart(book) },
                isAddedToCart = viewModel.isItemInCart(book.id)
            )
        }
    }
}

@Composable
fun AuthorsHorizontalSlider(
    authors: List<Author>,
    onAuthorClick: (String) -> Unit
) {
    if (authors.isEmpty()) return

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { authors.size }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 16.dp
        ) { page ->
            val author = authors[page]
            
            // Framer Motion inspired scale/rotate/opacity card transition calculations
            val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
            val scale = 1f - (pageOffset.absoluteValue * 0.12f).coerceIn(0f, 1f)
            val alpha = 1f - (pageOffset.absoluteValue * 0.5f).coerceIn(0f, 1f)
            val rotation = pageOffset * -8f
            val translationX = pageOffset * 25.dp.value

            Card(
                onClick = { onAuthorClick(author.id) },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                        rotationY = rotation
                        this.translationX = translationX
                    }
                    .border(
                        width = 1.dp,
                        color = if (pagerState.currentPage == page) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                        } else {
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        },
                        shape = RoundedCornerShape(24.dp)
                    )
                    .shadow(
                        elevation = if (pagerState.currentPage == page) 8.dp else 1.dp,
                        shape = RoundedCornerShape(24.dp),
                        clip = false
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = if (pagerState.currentPage == page) {
                                    listOf(
                                        MaterialTheme.colorScheme.surface,
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.12f)
                                    )
                                } else {
                                    listOf(
                                        MaterialTheme.colorScheme.surface,
                                        MaterialTheme.colorScheme.surface
                                    )
                                }
                            )
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Author Portrait with colorful active glowing aura
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.sweepGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary,
                                        MaterialTheme.colorScheme.tertiary,
                                        MaterialTheme.colorScheme.primary
                                    )
                                )
                            )
                            .padding(3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = author.photoUrl,
                            contentDescription = author.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Bio, name, achievements info
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = author.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = author.bio,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            lineHeight = 15.sp,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "বরেণ্য সাহিত্যিক",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "প্রোফাইল দেখুন",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Pagination Dots
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            authors.forEachIndexed { i, _ ->
                val active = pagerState.currentPage == i
                val width by animateDpAsState(
                    targetValue = if (active) 14.dp else 6.dp,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "authorDotWidth"
                )
                val alpha by animateFloatAsState(
                    targetValue = if (active) 1f else 0.4f,
                    label = "authorDotAlpha"
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(height = 6.dp, width = width)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = alpha)
                        )
                )
            }
        }
    }
}

@Composable
fun StudentTestimonialsSection() {
    val testimonials = listOf(
        Pair("অধ্যাপক ড. সাজেদুল করিমের রবীন্দ্রনাথের কবিতা কোর্সটি বাংলা সাহিত্যকে ভালোবাসতে এক নতুন চোখ দান করেছে। অ্যাপের অডিও-ভিডিও কোয়ালিটি চমৎকার!", "তানজিল হাসান (শিক্ষার্থী, বাংলা বিভাগ)"),
        Pair("গীতাঞ্জলি ও সঞ্চয়িতা বইয়ের ডিজিটাল রিডারটি বুকমার্ক ও নাইট মোড থাকায় চমৎকার লেগেছে। রাতবেলা পড়তে চোখের কোনো কষ্ট হয় না।", "সুমাইয়া মেহজাবিন (সাহিত্য অনুরাগী)")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "শিক্ষার্থীদের অনুভূতির কথা",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        testimonials.forEach { (quote, student) ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(imageVector = Icons.Default.FormatQuote, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = quote,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "- $student",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}

@Composable
fun LatestBlogsSection(
    blogs: List<BlogPost>,
    onBlogClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        blogs.take(2).forEach { blog ->
            Card(
                onClick = onBlogClick,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .shadow(1.dp, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(72.dp).clip(RoundedCornerShape(8.dp))) {
                        MockImage(name = blog.coverUrl, title = "", height = 72)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = blog.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = blog.authorName, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = blog.publishDate, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = blog.excerpt, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

// --- Dynamic Blog Page ---
@Composable
fun BlogScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var activeBlog by remember { mutableStateOf<BlogPost?>(null) }

    if (activeBlog != null) {
        val currentBlog = viewModel.blogs.find { it.id == activeBlog!!.id } ?: activeBlog!!
        var commentText by remember { mutableStateOf("") }
        val userProfile by viewModel.userProfile.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        // Individual Article Details View
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            IconButton(onClick = { activeBlog = null }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ফিরে যান", fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = currentBlog.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, lineHeight = 26.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(4.dp))
                Text(currentBlog.authorName, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(16.dp))
                Icon(imageVector = Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(4.dp))
                Text("${currentBlog.publishDate} • ${currentBlog.readingTime}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(16.dp))
            MockImage(name = currentBlog.coverUrl, title = currentBlog.title, height = 200)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = currentBlog.content,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Justify
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(16.dp))

            // Comments Header
            Text(
                text = "মন্তব্যসমূহ (${currentBlog.comments.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Comments List
            if (currentBlog.comments.isEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "এখনো কোনো মন্তব্য করা হয়নি। প্রথম মন্তব্যটি আপনিই করুন!",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                var replyingToCommentId by remember { mutableStateOf<String?>(null) }
                var replyText by remember { mutableStateOf("") }

                currentBlog.comments.forEach { comment ->
                    val isCommentLiked = comment.likedBy.contains(userProfile?.id ?: "") || comment.likedBy.contains("me")

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = comment.username,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = comment.createdAt,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = comment.content,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 19.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Comment Actions: Like & Reply
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Like Button
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            if (userProfile?.isLoggedIn == true) {
                                                viewModel.likeBlogComment(currentBlog.id, comment.id)
                                            } else {
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("পছন্দ করতে অনুগ্রহ করে আগে লগইন করুন।")
                                                }
                                            }
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isCommentLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Like",
                                        tint = if (isCommentLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (comment.likesCount > 0) "${comment.likesCount}" else "পছন্দ",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isCommentLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                // Reply Button
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            if (userProfile?.isLoggedIn == true) {
                                                if (replyingToCommentId == comment.id) {
                                                    replyingToCommentId = null
                                                } else {
                                                    replyingToCommentId = comment.id
                                                    replyText = ""
                                                }
                                            } else {
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("প্রতিউত্তর দিতে অনুগ্রহ করে আগে লগইন করুন।")
                                                }
                                            }
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Reply,
                                        contentDescription = "Reply",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "উত্তর দিন",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Reply Input field
                            if (replyingToCommentId == comment.id) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = replyText,
                                        onValueChange = { replyText = it },
                                        placeholder = { Text("আপনার প্রতিউত্তর লিখুন...", fontSize = 11.sp) },
                                        modifier = Modifier.weight(1f),
                                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
                                        maxLines = 2,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = {
                                            if (replyText.trim().isNotEmpty()) {
                                                viewModel.replyBlogComment(currentBlog.id, comment.id, replyText) { success, msg ->
                                                    if (success) {
                                                        replyText = ""
                                                        replyingToCommentId = null
                                                    }
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(msg ?: "ত্রুটি ঘটেছে।")
                                                    }
                                                }
                                            }
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp)
                                    ) {
                                        Text("পাঠান", fontSize = 11.sp)
                                    }
                                }
                            }

                            // Nested Replies list rendering
                            if (comment.replies.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                                Spacer(modifier = Modifier.height(8.dp))

                                comment.replies.forEach { reply ->
                                    val isReplyLiked = reply.likedBy.contains(userProfile?.id ?: "") || reply.likedBy.contains("me")

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 16.dp, top = 6.dp, bottom = 6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.SubdirectoryArrowRight,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                            modifier = Modifier.size(16.dp).padding(top = 2.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                                .padding(10.dp)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = reply.username,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = reply.createdAt,
                                                    fontSize = 9.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = reply.content,
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                lineHeight = 17.sp
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            // Reply Like action
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .clickable {
                                                        if (userProfile?.isLoggedIn == true) {
                                                            viewModel.likeBlogCommentReply(currentBlog.id, comment.id, reply.id)
                                                        } else {
                                                            coroutineScope.launch {
                                                                snackbarHostState.showSnackbar("পছন্দ করতে অনুগ্রহ করে আগে লগইন করুন।")
                                                            }
                                                        }
                                                    }
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (isReplyLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                                    contentDescription = "Like Reply",
                                                    tint = if (isReplyLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = if (reply.likesCount > 0) "${reply.likesCount}" else "পছন্দ",
                                                    fontSize = 10.sp,
                                                    color = if (isReplyLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Comment Input Section
            if (userProfile?.isLoggedIn == true) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    label = { Text("আপনার মন্তব্য লিখুন...", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp),
                    maxLines = 3,
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (commentText.trim().isNotEmpty()) {
                            viewModel.addBlogComment(currentBlog.id, commentText) { success, msg ->
                                if (success) {
                                    commentText = ""
                                }
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(msg ?: "ত্রুটি ঘটেছে।")
                                }
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("মন্তব্য প্রকাশ করুন", fontSize = 12.sp)
                }
            } else {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "মন্তব্য পোস্ট করতে আপনার অ্যাকাউন্টে লগইন থাকা আবশ্যক।",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.navigateTo(Screen.Auth) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("এখনই লগইন করুন", fontSize = 12.sp)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            SnackbarHost(hostState = snackbarHostState)
            Spacer(modifier = Modifier.height(30.dp))
            FooterSection(onNavigate = { viewModel.selectBottomTab(it) })
        }
    } else {
        // Articles Listing View
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("সাহিত্য ব্লগ ও প্রবন্ধাবলী", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(4.dp))
                Text("বাংলা সাহিত্য এবং বিভিন্ন কালপঞ্জীর গভীর প্রবন্ধসমূহ পড়ুন।", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(viewModel.blogs) { blog ->
                Card(
                    onClick = { activeBlog = blog },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column {
                        MockImage(name = blog.coverUrl, title = blog.title, height = 130)
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = blog.title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(blog.authorName, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(blog.publishDate, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = blog.excerpt,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text(text = blog.readingTime, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                                Text("বিস্তারিত পড়ুন ➔", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                FooterSection(onNavigate = { viewModel.selectBottomTab(it) })
            }
        }
    }
}

// --- About Us Screen ---
@Composable
fun AboutUsScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("আমাদের সম্পর্কে", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "\"বাংলা সাহিত্য\" অ্যাপের লক্ষ্য বাংলা সাহিত্য ও ঐতিহ্যকে প্রযুক্তির আলোয় বিশ্বজুড়ে ছড়িয়ে দেওয়া।",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        MockImage(name = "about_hero", title = "বাংলা সাহিত্য লার্নিং প্ল্যাটফর্ম", height = 180)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "আমাদের মূল মিশন:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))

        val missions = listOf(
            "১. রবীন্দ্রনাথ, নজরুল, শরৎচন্দ্র থেকে শুরু করে সকল ক্লাসিক ও সমকালীন বাংলা সাহিত্য সহজ উপায়ে শিক্ষার্থীদের কাছে পৌঁছে দেওয়া।",
            "২. একটি চমৎকার মোবাইল-অপটিমাইজড ই-বুক রিডারের মাধ্যমে কম খরচে বা বিনামূল্যে বই পড়ার সুযোগ করে দেওয়া।",
            "৩. স্বনামধন্য বিশ্ববিদ্যালয়ের শিক্ষকদের নির্দেশনায় চমৎকার ভিডিও লেকচারের মাধ্যমে কবিতার মূল ভাবনা বিশ্লেষণ কোর্স প্রদান করা।"
        )

        missions.forEach { mission ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = mission,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        FooterSection(onNavigate = { viewModel.selectBottomTab(it) })
    }
}

// --- Contact Us Screen ---
@Composable
fun ContactUsScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var submitSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("যোগাযোগ করুন", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(4.dp))
        Text("আপনার যেকোনো জিজ্ঞাসা, পরামর্শ বা সহযোগিতা পেতে নিচের ফর্মটির মাধ্যমে আমাদের জানান।", fontSize = 13.sp, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(20.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (submitSuccess) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF2B8A3E).copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFF2B8A3E), RoundedCornerShape(8.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2B8A3E), modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("বার্তাটি পাঠানো হয়েছে!", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF2B8A3E))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("আমরা শীঘ্রই আপনার সাথে যোগাযোগ করব। ধন্যবাদ!", fontSize = 12.sp, textAlign = TextAlign.Center)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        submitSuccess = false
                        name = ""
                        email = ""
                        message = ""
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("নতুন বার্তা লিখুন")
                    }
                } else {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("আপনার নাম লিখুন") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("ইমেইল অ্যাড্রেস") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("আপনার বার্তা/মতামত") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (name.isNotEmpty() && email.isNotEmpty() && message.isNotEmpty()) {
                                submitSuccess = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("বার্তা পাঠান", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        FooterSection(onNavigate = { viewModel.selectBottomTab(it) })
    }
}
