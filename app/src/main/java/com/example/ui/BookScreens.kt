package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*

@Composable
fun BookListScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    // Advanced filtering
    val filteredBooks = viewModel.books.filter { book ->
        val matchesSearch = book.title.contains(viewModel.bookSearchQuery) || book.authorName.contains(viewModel.bookSearchQuery)
        val matchesCategory = viewModel.bookCategoryFilter == "সব" || book.category == viewModel.bookCategoryFilter
        val matchesAuthor = viewModel.bookAuthorFilter == "সব" || book.authorName.contains(viewModel.bookAuthorFilter)
        val matchesPrice = (book.discountPrice ?: book.price) <= viewModel.bookMaxPriceFilter
        matchesSearch && matchesCategory && matchesAuthor && matchesPrice
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "বইয়ের লাইব্রেরি ও মেলা",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "ফিজিক্যাল বই ঘরে বসেই ডেলিভারি নিন অথবা ই-বুক সংস্করণে সাথে সাথেই পড়া শুরু করুন।",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(14.dp))

        // Search text field
        OutlinedTextField(
            value = viewModel.bookSearchQuery,
            onValueChange = { viewModel.bookSearchQuery = it },
            placeholder = { Text("বই বা লেখকের নাম দিয়ে খুঁজুন...", fontSize = 12.sp) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category Quick filter
        val categories = listOf("সব", "উপন্যাস", "কাব্যগ্রন্থ", "কাব্য সংকলন")
        ScrollableTabRow(
            selectedTabIndex = categories.indexOf(viewModel.bookCategoryFilter).coerceAtLeast(0),
            edgePadding = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            categories.forEach { cat ->
                Tab(
                    selected = viewModel.bookCategoryFilter == cat,
                    onClick = { viewModel.bookCategoryFilter = cat },
                    text = { Text(cat, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Author filter options and price range sliders
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("লেখক দিয়ে ফিল্টার:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Row {
                        val authors = listOf("সব", "রবীন্দ্রনাথ", "নজরুল", "শরৎচন্দ্র", "বিভূতিভূষণ")
                        var expanded by remember { mutableStateOf(false) }
                        Box {
                            TextButton(onClick = { expanded = true }) {
                                Text("${viewModel.bookAuthorFilter} ▾", fontSize = 11.sp)
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                authors.forEach { authName ->
                                    DropdownMenuItem(
                                        text = { Text(authName, fontSize = 12.sp) },
                                        onClick = {
                                            viewModel.bookAuthorFilter = authName
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("সর্বোচ্চ মূল্য: ৳${viewModel.bookMaxPriceFilter.toInt()}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Slider(
                        value = viewModel.bookMaxPriceFilter,
                        onValueChange = { viewModel.bookMaxPriceFilter = it },
                        valueRange = 100f..500f,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredBooks.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("কোনো বই খুঁজে পাওয়া যায়নি!", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredBooks) { book ->
                    ShadcnBookCard(
                        book = book,
                        onClick = { viewModel.navigateTo(Screen.BookDetails(book.id)) },
                        onAddToCartClick = { viewModel.addToCart(book) },
                        isAddedToCart = viewModel.isItemInCart(book.id)
                    )
                }
            }
        }
    }
}

@Composable
fun BookDetailsScreen(
    bookId: String,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val book = viewModel.books.find { it.id == bookId }

    if (book == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("বইটি খুঁজে পাওয়া যায়নি!")
        }
        return
    }

    val purchasedList by viewModel.purchasedBooks.collectAsState()
    val isEbookPurchased = purchasedList.any { it.bookId == book.id }
    val userProfile by viewModel.userProfile.collectAsState()

    var showPaymentSheet by remember { mutableStateOf(false) }
    var buyIsPhysicalChoice by remember { mutableStateOf(false) }

    var showSampleDialog by remember { mutableStateOf(false) }
    var activeSamplePageIndex by remember { mutableStateOf(0) }

    // Custom review fields
    var reviewerName by remember { mutableStateOf("") }
    var reviewText by remember { mutableStateOf("") }
    var reviewRating by remember { mutableStateOf(5f) }
    var reviewStatus by remember { mutableStateOf("") }

    if (showPaymentSheet) {
        PaymentSheet(
            itemName = "${book.title} (${if (buyIsPhysicalChoice) "ফিজিক্যাল বই" else "ই-বুক"})",
            price = book.discountPrice ?: book.price,
            onPaymentSuccess = { paymentMethod ->
                viewModel.buyBook(book, buyIsPhysicalChoice, paymentMethod)
                showPaymentSheet = false
            },
            onDismiss = { showPaymentSheet = false }
        )
    }

    // "নমুনা পড়ুন" (Read Sample) dialog
    if (showSampleDialog) {
        AlertDialog(
            onDismissRequest = { showSampleDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("বইয়ের নমুনা পৃষ্ঠা: ${book.title}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { showSampleDialog = false }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color(0xFFFCF6ED), RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "পৃষ্ঠা ${activeSamplePageIndex + 1} / ${book.samplePages.size}",
                        fontSize = 11.sp,
                        color = Color(0xFF5F1900),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (book.samplePages.isNotEmpty()) book.samplePages[activeSamplePageIndex] else "নমুনা পৃষ্ঠা পাওয়া যায়নি!",
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Serif,
                        lineHeight = 22.sp,
                        color = Color(0xFF2B1600),
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { if (activeSamplePageIndex > 0) activeSamplePageIndex-- },
                            enabled = activeSamplePageIndex > 0,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("পূর্ববর্তী", fontSize = 11.sp)
                        }
                        Button(
                            onClick = { if (activeSamplePageIndex < book.samplePages.size - 1) activeSamplePageIndex++ },
                            enabled = activeSamplePageIndex < book.samplePages.size - 1,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("পরবর্তী", fontSize = 11.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSampleDialog = false }) {
                    Text("বন্ধ করুন", fontSize = 12.sp)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(130.dp)
                    .shadow(4.dp, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
            ) {
                MockImage(name = book.coverUrl, title = book.title, subtitle = book.authorName, height = 180)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = book.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "লেখক: ${book.authorName}", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "ISBN: ${book.isbn}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "ক্যাটেগরি: ${book.category}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)

                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color(0xFFF1C40F), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${book.rating}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "(${book.reviews.size} রিভিউ)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (book.discountPrice != null) {
                        Text(
                            text = "৳${book.price.toInt()}",
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.bodySmall.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "৳${book.discountPrice.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                    } else {
                        Text(text = "৳${book.price.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }

        // Action Options Area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Ebook Option
            if (book.isEbook) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.TabletAndroid, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("ই-বুক সংস্করণ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        if (isEbookPurchased) {
                            Button(
                                onClick = { viewModel.navigateTo(Screen.EbookReader(book.id)) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B8A3E)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("পড়ুন", fontSize = 11.sp)
                            }
                        } else {
                            Button(
                                onClick = {
                                    if (userProfile?.isLoggedIn == true) {
                                        buyIsPhysicalChoice = false
                                        showPaymentSheet = true
                                    } else {
                                        viewModel.navigateTo(Screen.Auth)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("কিনুন", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // Physical Book Option
            if (book.isPhysical) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.LocalShipping, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("ফিজিক্যাল মলাট", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                if (userProfile?.isLoggedIn == true) {
                                    buyIsPhysicalChoice = true
                                    showPaymentSheet = true
                                } else {
                                    viewModel.navigateTo(Screen.Auth)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("অর্ডার করুন", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Read Sample Button
        if (book.samplePages.isNotEmpty()) {
            OutlinedButton(
                onClick = {
                    activeSamplePageIndex = 0
                    showSampleDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Book, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("নমুনা পড়ুন (ফ্রি প্রিভিউ)")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Details tabs / sections
        Column(modifier = Modifier.padding(16.dp)) {
            Text("বইয়ের বিবরণ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = book.description, fontSize = 13.sp, lineHeight = 20.sp, color = MaterialTheme.colorScheme.onSurface)

            Spacer(modifier = Modifier.height(16.dp))
            Text("সূচিপত্র", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            book.tableOfContents.forEach { chapter ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                ) {
                    Text(text = chapter, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(10.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Reviews list
            Text("পাঠকের প্রতিক্রিয়া ও রিভিউ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(10.dp))

            // Submit review form
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth().border(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("আপনার পাঠক রিভিউ লিখুন", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("আপনার রেটিং: ", fontSize = 11.sp)
                        (1..5).forEach { star ->
                            IconButton(onClick = { reviewRating = star.toFloat() }, modifier = Modifier.size(24.dp)) {
                                Icon(
                                    imageVector = if (reviewRating >= star) Icons.Default.Star else Icons.Default.StarBorder,
                                    contentDescription = null,
                                    tint = Color(0xFFF1C40F),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = reviewerName,
                        onValueChange = { reviewerName = it },
                        placeholder = { Text("আপনার নাম লিখুন", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        placeholder = { Text("আপনার পঠন অনুভূতি লিখুন...", fontSize = 11.sp) },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (reviewStatus.isNotEmpty()) {
                        Text(reviewStatus, color = Color(0xFF2B8A3E), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Button(
                        onClick = {
                            if (reviewerName.isNotEmpty() && reviewText.isNotEmpty()) {
                                viewModel.addReview(book.id, false, reviewRating, reviewText, reviewerName)
                                reviewStatus = "ধন্যবাদ! আপনার পাঠক রিভিউটি সফলভাবে প্রকাশিত হয়েছে।"
                                reviewerName = ""
                                reviewText = ""
                            } else {
                                reviewStatus = "সব ঘর পূরণ করুন।"
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("জমা দিন", fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            book.reviews.forEach { r ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).border(0.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = r.userName, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Row {
                                (1..5).forEach { star ->
                                    Icon(
                                        imageVector = if (r.rating >= star) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = null,
                                        tint = Color(0xFFF1C40F),
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = r.comment, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Related Books
            Text("সম্পর্কিত অন্যান্য বইসমূহ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(10.dp))
            val related = viewModel.books.filter { it.id != book.id }
            BooksHorizontalSlider(books = related, onBookClick = { viewModel.navigateTo(Screen.BookDetails(it)) }, viewModel = viewModel)
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

// --- Premium E-Book Reader Screen ---
@Composable
fun EbookReaderScreen(
    bookId: String,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val book = viewModel.books.find { it.id == bookId }

    if (book == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("বইটি খুঁজে পাওয়া যায়নি!")
        }
        return
    }

    var currentPageIndex by remember { mutableStateOf(viewModel.getEbookProgressPage(book.id)) }
    var showBookmarksPane by remember { mutableStateOf(false) }
    var bookmarkMemo by remember { mutableStateOf("") }
    var isBookmarkedMsg by remember { mutableStateOf("") }

    val bookmarks by viewModel.getBookmarks(book.id).collectAsState(initial = emptyList())

    // Apply reader themes dynamically
    val readerBackground = if (viewModel.readerNightMode) Color(0xFF1E1E1E) else Color(0xFFFCF6ED)
    val readerTextColor = if (viewModel.readerNightMode) Color(0xFFE2C439) else Color(0xFF2B1600) // Night: Amber, Day: Charcoal

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(readerBackground)
    ) {
        // Reader Header Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                viewModel.updateLastReadPage(book.id, currentPageIndex)
                viewModel.navigateBack()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Exit Reader")
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = book.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = "পৃষ্ঠা ${currentPageIndex + 1} / ${book.samplePages.size}", fontSize = 10.sp)
            }

            // Zoom out
            IconButton(
                onClick = { if (viewModel.readerZoomLevel > 80) viewModel.readerZoomLevel -= 10 },
                enabled = viewModel.readerZoomLevel > 80
            ) {
                Icon(imageVector = Icons.Default.RemoveCircleOutline, contentDescription = "Zoom Out")
            }
            // Zoom in
            IconButton(
                onClick = { if (viewModel.readerZoomLevel < 180) viewModel.readerZoomLevel += 10 },
                enabled = viewModel.readerZoomLevel < 180
            ) {
                Icon(imageVector = Icons.Default.AddCircleOutline, contentDescription = "Zoom In")
            }

            // Night mode toggle
            IconButton(onClick = { viewModel.readerNightMode = !viewModel.readerNightMode }) {
                Icon(
                    imageVector = if (viewModel.readerNightMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Theme Toggle"
                )
            }

            // Bookmarks menu
            IconButton(onClick = { showBookmarksPane = !showBookmarksPane }) {
                Icon(
                    imageVector = if (bookmarks.any { it.pageNumber == currentPageIndex }) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmarks"
                )
            }
        }

        if (showBookmarksPane) {
            // Bookmark Management Pane
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("বুকমার্ক ও নোটবুক", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        IconButton(onClick = { showBookmarksPane = false }, modifier = Modifier.size(24.dp)) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    // Add Bookmark row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = bookmarkMemo,
                            onValueChange = { bookmarkMemo = it },
                            placeholder = { Text("একটি সংক্ষিপ্ত টীকা লিখুন (যেমন: চমৎকার লাইন)", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Button(
                            onClick = {
                                viewModel.addBookmark(book.id, currentPageIndex, bookmarkMemo.ifEmpty { "পৃষ্ঠা ${currentPageIndex + 1}" })
                                isBookmarkedMsg = "বুকমার্ক সংরক্ষণ করা হয়েছে!"
                                bookmarkMemo = ""
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("যোগ করুন", fontSize = 11.sp)
                        }
                    }

                    if (isBookmarkedMsg.isNotEmpty()) {
                        Text(isBookmarkedMsg, color = Color(0xFF2B8A3E), fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // List bookmarks
                    if (bookmarks.isEmpty()) {
                        Text("কোনো বুকমার্ক সংরক্ষিত নেই।", fontSize = 11.sp)
                    } else {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(bookmarks) { b ->
                                SuggestionChip(
                                    onClick = {
                                        currentPageIndex = b.pageNumber
                                        showBookmarksPane = false
                                    },
                                    label = { Text("পৃষ্ঠা ${b.pageNumber + 1}: ${b.note}", fontSize = 10.sp) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Reader Text Canvas
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = if (book.samplePages.isNotEmpty()) book.samplePages[currentPageIndex] else "পৃষ্ঠা সামগ্রী পাওয়া যায়নি।",
                color = readerTextColor,
                fontSize = (15 * (viewModel.readerZoomLevel / 100f)).sp,
                fontFamily = FontFamily.Serif,
                lineHeight = (24 * (viewModel.readerZoomLevel / 100f)).sp,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            )
        }

        // Reader Navigation Buttons Bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { if (currentPageIndex > 0) currentPageIndex-- },
                enabled = currentPageIndex > 0,
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("পূর্ববর্তী")
            }

            Text(
                text = "${currentPageIndex + 1} / ${book.samplePages.size}",
                color = readerTextColor.copy(alpha = 0.8f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = { if (currentPageIndex < book.samplePages.size - 1) currentPageIndex++ },
                enabled = currentPageIndex < book.samplePages.size - 1,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("পরবর্তী")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
            }
        }
    }
}
