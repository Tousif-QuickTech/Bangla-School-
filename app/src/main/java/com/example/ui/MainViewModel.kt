package com.example.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.data.network.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.util.UUID

// Define our App's custom type-safe screen structure for stable navigation.
sealed class Screen {
    object Home : Screen()
    object Courses : Screen()
    object Books : Screen()
    object Authors : Screen()
    object Blog : Screen()
    object AboutUs : Screen()
    object ContactUs : Screen()
    object Dashboard : Screen()
    object Auth : Screen()
    object AdminPanel : Screen()
    data class CourseDetails(val courseId: String) : Screen()
    data class BookDetails(val bookId: String) : Screen()
    data class AuthorDetails(val authorId: String) : Screen()
    data class EbookReader(val bookId: String) : Screen()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(application)

    // --- State-based Backstack Navigation ---
    val navigationStack = mutableStateListOf<Screen>(Screen.Home)

    fun navigateTo(screen: Screen) {
        if (navigationStack.lastOrNull() != screen) {
            navigationStack.add(screen)
        }
    }

    fun navigateBack(): Boolean {
        if (navigationStack.size > 1) {
            navigationStack.removeAt(navigationStack.size - 1)
            return true
        }
        return false
    }

    // --- Live Server State & Synchronization ---
    var isLiveServerConnected by mutableStateOf(false)
    var isSyncingServerData by mutableStateOf(false)
    var serverStatusMessage by mutableStateOf("অফলাইন মোড (লোকাল ডেটা)")
    var authToken by mutableStateOf("")

    fun syncWithLiveServer() {
        if (isSyncingServerData) return
        viewModelScope.launch {
            isSyncingServerData = true
            serverStatusMessage = "লাইভ সার্ভারের সাথে সংযুক্ত হচ্ছে..."
            try {
                // Try fetching authors
                val authorResponse = RetrofitClient.api.getAuthors()
                if (authorResponse.success && authorResponse.authors != null) {
                    val domainAuthors = authorResponse.authors.map { it.toDomain() }
                    if (domainAuthors.isNotEmpty()) {
                        authors.clear()
                        authors.addAll(domainAuthors)
                    }
                }

                // Try fetching books
                val bookResponse = RetrofitClient.api.getBooks()
                if (bookResponse.success && bookResponse.books != null) {
                    val domainBooks = bookResponse.books.map { it.toDomain() }
                    if (domainBooks.isNotEmpty()) {
                        books.clear()
                        books.addAll(domainBooks)
                    }
                }

                // Try fetching courses
                val courseResponse = RetrofitClient.api.getCourses()
                if (courseResponse.success && courseResponse.courses != null) {
                    val domainCourses = courseResponse.courses.map { it.toDomain() }
                    if (domainCourses.isNotEmpty()) {
                        courses.clear()
                        courses.addAll(domainCourses)
                    }
                }

                // Try fetching blogs
                val blogResponse = RetrofitClient.api.getBlogs()
                if (blogResponse.success && blogResponse.blogs != null) {
                    val domainBlogs = blogResponse.blogs.map { it.toDomain() }
                    if (domainBlogs.isNotEmpty()) {
                        blogs.clear()
                        blogs.addAll(domainBlogs)
                    }
                }

                // Try fetching banners
                try {
                    val bannerResponse = RetrofitClient.api.getBanners()
                    if (bannerResponse.success && bannerResponse.banners != null) {
                        val domainBanners = bannerResponse.banners.filter { it.isActive }.map { it.text }
                        if (domainBanners.isNotEmpty()) {
                            banners.clear()
                            banners.addAll(domainBanners)
                        }
                    }
                } catch (e: Exception) {
                    // Ignore banner failures gracefully
                }

                isLiveServerConnected = true
                serverStatusMessage = "লাইভ সার্ভার সংযুক্ত (রিয়েল-টাইম ডেটা)"
                repository.addNotification("সার্ভার সিঙ্ক সফল!", "আপনার অ্যাপটি সফলভাবে লাইভ ডেটাবেজের সাথে যুক্ত হয়েছে।")
            } catch (e: Exception) {
                isLiveServerConnected = false
                serverStatusMessage = "সার্ভার সংযোগ ব্যর্থ। লোকাল অফলাইন ডেটা ব্যবহার করা হচ্ছে।"
            } finally {
                isSyncingServerData = false
            }
        }
    }

    // --- Dynamic Application Catalogs (Support Admin Modifications) ---
    var courses = mutableStateListOf<Course>()
    var isCoursesLoading by mutableStateOf(false)
    
    fun refreshCourses() {
        if (isCoursesLoading) return
        viewModelScope.launch {
            isCoursesLoading = true
            delay(1200)
            isCoursesLoading = false
        }
    }
    var books = mutableStateListOf<Book>()
    var authors = mutableStateListOf<Author>()
    var blogs = mutableStateListOf<BlogPost>()
    var banners = mutableStateListOf<String>()
    var cartItems = mutableStateListOf<Book>()

    // --- Search & Filters ---
    var courseSearchQuery by mutableStateOf("")
    var bookSearchQuery by mutableStateOf("")
    var bookCategoryFilter by mutableStateOf("সব")
    var bookAuthorFilter by mutableStateOf("সব")
    var bookMaxPriceFilter by mutableStateOf(500.0f)

    // --- UI State: Cart, Checkout & Reviews ---
    var isDarkTheme by mutableStateOf(false)
    var activeCourseTab by mutableStateOf("পরিচিতি") // পরিচিতি, সিলেবাস, ফ্রি ভিডিও, পেইড ভিডিও, টেক্সট কনটেন্ট, রিভিউ
    var activeDashboardTab by mutableStateOf("আমার কোর্সসমূহ") // আমার কোর্সসমূহ, আমার বইসমূহ, চলমান কোর্স, সম্পন্ন কোর্স, Order History, Payment History, Wishlist, Profile Management, Password Change, Notifications

    // --- Active Reader Preferences ---
    var readerZoomLevel by mutableStateOf(100) // 100%, 120%, 140%, 160%
    var readerNightMode by mutableStateOf(false)

    // --- Auth states ---
    var authEmailPhone by mutableStateOf("")
    var authPassword by mutableStateOf("")
    var isRegisterMode by mutableStateOf(false)
    var authOTPCode by mutableStateOf("")
    var showOTPVerification by mutableStateOf(false)
    var loginError by mutableStateOf("")

    // --- Admin state variables ---
    var adminSelectedCategory by mutableStateOf("courses") // courses, books, authors, banners, blogs, orders, payments, reviews, analytics

    // --- Room Database Observers ---
    val userProfile: StateFlow<UserProfileEntity?> = repository.userDao.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val enrolledCourses: StateFlow<List<CourseEnrollmentEntity>> = repository.courseEnrollmentDao.getEnrolledCourses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val purchasedBooks: StateFlow<List<PurchasedBookEntity>> = repository.purchasedBookDao.getPurchasedBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistItems: StateFlow<List<WishlistItemEntity>> = repository.wishlistDao.getWishlistItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orderHistory: StateFlow<List<OrderEntity>> = repository.orderDao.getOrders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationEntity>> = repository.notificationDao.getNotifications()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Load initial mock datasets from repo
        courses.addAll(repository.courses)
        books.addAll(repository.books)
        authors.addAll(repository.authors)
        blogs.addAll(repository.blogs)
        banners.addAll(listOf(
            "নতুন বছর অফার: সকল সাহিত্য কোর্সে ৫০% পর্যন্ত ছাড়! কুপন: NOBO2026",
            "বিশ্বকবি রবীন্দ্রনাথ ঠাকুরের কাব্যজগৎ নিয়ে নতুন কোর্স এখন লাইভ!",
            "বইমেলা ধামাকা: সেরা ৫টি ই-বুক কিনুন এবং বিনামূল্যে পান ১টি ফিজিক্যাল বই!"
        ))

        // Prepopulate database with initial state if empty
        viewModelScope.launch {
            if (repository.userDao.getUserProfile().firstOrNull() == null) {
                // Initialize default logged out state or standard notifications
                repository.addNotification("স্বাগতম!", "বাংলা সাহিত্য ই-লার্নিং অ্যাপে আপনাকে স্বাগতম। আধুনিক উপায়ে বাংলা সাহিত্যচর্চা শুরু করুন!",)
                repository.addNotification("মেগা ছাড়!", "উপন্যাস ও কবিতার ই-বুকগুলোতে চলছে আকর্ষণীয় ছাড়। বিস্তারিত জানতে বইসমূহ সেকশনে যান।")
            }
        }

        // Trigger synchronization with Live Express Backend Server
        syncWithLiveServer()
    }

    // --- Navigation Shortcuts ---
    fun selectBottomTab(route: String) {
        val screen = when (route) {
            "home" -> Screen.Home
            "courses" -> Screen.Courses
            "books" -> Screen.Books
            "authors" -> Screen.Authors
            "blog" -> Screen.Blog
            "about" -> Screen.AboutUs
            "contact" -> Screen.ContactUs
            "dashboard" -> {
                if (userProfile.value?.isLoggedIn == true) Screen.Dashboard else Screen.Auth
            }
            else -> Screen.Home
        }
        navigateTo(screen)
    }

    // --- Authentication Actions ---
    fun handleLogin() {
        if (authEmailPhone.isEmpty() || authPassword.isEmpty()) {
            loginError = "দয়া করে ইমেইল/ফোন নম্বর এবং পাসওয়ার্ড পূরণ করুন।"
            return
        }
        if (authPassword.length < 4) {
            loginError = "পাসওয়ার্ড কমপক্ষে ৪ অক্ষরের হতে হবে।"
            return
        }

        // Trigger OTP verification flow as requested
        showOTPVerification = true
        loginError = ""
    }

    fun verifyOTP() {
        if (authOTPCode == "1234" || authOTPCode.length == 4) {
            viewModelScope.launch {
                val name = if (isRegisterMode) "নতুন শিক্ষার্থী" else "তওসিফ আহমেদ"
                var emailVal = if (authEmailPhone.contains("@")) authEmailPhone else "user_${UUID.randomUUID().toString().substring(0, 4)}@domain.com"
                var finalName = name

                // Attempt server login / register if live server connection is active
                if (isLiveServerConnected) {
                    try {
                        if (isRegisterMode) {
                            val regResponse = RetrofitClient.api.register(
                                RegisterRequest(
                                    username = name,
                                    email = emailVal,
                                    password = authPassword
                                )
                            )
                            if (regResponse.success && regResponse.token != null) {
                                authToken = regResponse.token
                                finalName = regResponse.user?.username ?: name
                            }
                        } else {
                            val logResponse = RetrofitClient.api.login(
                                LoginRequest(
                                    email = authEmailPhone.ifEmpty { "student@banglaschool.com" },
                                    password = authPassword
                                )
                            )
                            if (logResponse.success && logResponse.token != null) {
                                authToken = logResponse.token
                                finalName = logResponse.user?.username ?: name
                                emailVal = logResponse.user?.email ?: emailVal
                            }
                        }
                    } catch (e: Exception) {
                        // Fail gracefully, use local fallback offline auth
                    }
                }

                val profile = UserProfileEntity(
                    name = finalName,
                    email = emailVal,
                    phone = if (!authEmailPhone.contains("@")) authEmailPhone else "01700000000",
                    isLoggedIn = true,
                    notificationCount = 3
                )
                repository.userDao.saveUserProfile(profile)
                repository.addNotification("লগইন সফল", "স্বাগতম, $finalName! আপনি সফলভাবে লগইন করেছেন।")
                showOTPVerification = false
                isRegisterMode = false
                authOTPCode = ""
                authPassword = ""
                // Redirect to dashboard
                navigateTo(Screen.Dashboard)
            }
        } else {
            loginError = "ভুল ওটিপি (OTP) কোড! (পরীক্ষার জন্য '1234' দিন)"
        }
    }

    fun handleSocialLogin(platform: String) {
        viewModelScope.launch {
            val profile = UserProfileEntity(
                name = "$platform ইউজার",
                email = "social.user@gmail.com",
                phone = "01888888888",
                isLoggedIn = true,
                notificationCount = 1
            )
            repository.userDao.saveUserProfile(profile)
            repository.addNotification("সামাজিক লগইন সফল", "স্বাগতম, ${profile.name}! আপনি $platform-এর মাধ্যমে সফলভাবে লগইন করেছেন।")
            navigateTo(Screen.Dashboard)
        }
    }

    fun handleLogout() {
        viewModelScope.launch {
            repository.userDao.clearUserProfile()
            repository.courseEnrollmentDao.clear()
            repository.purchasedBookDao.clear()
            repository.wishlistDao.clear()
            repository.orderDao.clear()
            repository.notificationDao.clear()
            authEmailPhone = ""
            navigateTo(Screen.Home)
        }
    }

    fun saveProfile(name: String, email: String, phone: String) {
        viewModelScope.launch {
            val current = userProfile.value
            val updated = UserProfileEntity(
                name = name,
                email = email,
                phone = phone,
                isLoggedIn = true,
                notificationCount = current?.notificationCount ?: 0
            )
            repository.userDao.saveUserProfile(updated)
            repository.addNotification("প্রোফাইল আপডেট", "আপনার প্রোফাইল তথ্য সফলভাবে আপডেট করা হয়েছে।")
        }
    }

    // --- Course & Book Enrollment / Purchase ---
    fun enrollInCourse(course: Course) {
        viewModelScope.launch {
            repository.enrollInCourse(course)
        }
    }

    fun buyBook(book: Book, isPhysical: Boolean, paymentMethod: String) {
        viewModelScope.launch {
            repository.purchaseBook(book, isPhysical, paymentMethod)
        }
    }

    // --- Wishlist Management ---
    fun toggleWishlist(itemId: String, title: String, image: String, price: Double, type: String) {
        viewModelScope.launch {
            val currentList = wishlistItems.value
            val existing = currentList.find { it.itemId == itemId }
            if (existing != null) {
                repository.wishlistDao.removeFromWishlist(itemId)
            } else {
                repository.wishlistDao.addToWishlist(WishlistItemEntity(itemId, title, image, price, type))
            }
        }
    }

    fun isItemInWishlist(itemId: String): Boolean {
        return wishlistItems.value.any { it.itemId == itemId }
    }

    // --- Cart Management ---
    fun addToCart(book: Book) {
        if (!cartItems.any { it.id == book.id }) {
            cartItems.add(book)
        }
    }

    fun removeFromCart(bookId: String) {
        cartItems.removeAll { it.id == bookId }
    }

    fun isItemInCart(bookId: String): Boolean {
        return cartItems.any { it.id == bookId }
    }

    fun clearCart() {
        cartItems.clear()
    }

    // --- Course Progress Tracker ---
    fun completeLesson(courseId: String, lessonId: String) {
        viewModelScope.launch {
            val enrollment = repository.courseEnrollmentDao.getEnrollment(courseId) ?: return@launch
            val completedList = enrollment.completedLessons.split(",").filter { it.isNotEmpty() }.toMutableList()
            if (!completedList.contains(lessonId)) {
                completedList.add(lessonId)
            }
            val course = courses.find { it.id == courseId } ?: return@launch
            val newPercent = ((completedList.size.toFloat() / course.lessons.size) * 100).toInt()
            repository.courseEnrollmentDao.updateProgress(courseId, newPercent, completedList.joinToString(","))
        }
    }

    fun isLessonCompleted(courseId: String, lessonId: String): Boolean {
        val enrollment = enrolledCourses.value.find { it.courseId == courseId } ?: return false
        return enrollment.completedLessons.split(",").contains(lessonId)
    }

    // --- Book Reading Bookmarks & Zoom ---
    fun addBookmark(bookId: String, pageNumber: Int, note: String) {
        viewModelScope.launch {
            repository.bookmarkDao.addBookmark(BookmarkEntity(bookId = bookId, pageNumber = pageNumber, note = note))
        }
    }

    fun getBookmarks(bookId: String): Flow<List<BookmarkEntity>> {
        return repository.bookmarkDao.getBookmarks(bookId)
    }

    fun updateLastReadPage(bookId: String, page: Int) {
        viewModelScope.launch {
            repository.purchasedBookDao.updateLastReadPage(bookId, page)
        }
    }

    fun getEbookProgressPage(bookId: String): Int {
        return purchasedBooks.value.find { it.bookId == bookId }?.lastReadPage ?: 0
    }

    // --- Add Course / Book Custom Review ---
    fun addReview(itemId: String, isCourse: Boolean, rating: Float, comment: String, name: String) {
        val newReview = Review(
            id = UUID.randomUUID().toString(),
            userName = name,
            rating = rating,
            comment = comment,
            date = "২৯ জুন, ২০২৬"
        )
        if (isCourse) {
            val idx = courses.indexOfFirst { it.id == itemId }
            if (idx != -1) {
                val old = courses[idx]
                val updatedReviews = old.reviews.toMutableList().apply { add(newReview) }
                val newRating = updatedReviews.map { it.rating }.average().toFloat()
                courses[idx] = old.copy(reviews = updatedReviews, rating = newRating)
            }
        } else {
            val idx = books.indexOfFirst { it.id == itemId }
            if (idx != -1) {
                val old = books[idx]
                val updatedReviews = old.reviews.toMutableList().apply { add(newReview) }
                val newRating = updatedReviews.map { it.rating }.average().toFloat()
                books[idx] = old.copy(reviews = updatedReviews, rating = newRating)
            }
        }
    }

    // --- Password Change Action ---
    fun changePassword(old: String, new: String): Boolean {
        if (old.isNotEmpty() && new.length >= 4) {
            viewModelScope.launch {
                repository.addNotification("পাসওয়ার্ড পরিবর্তন সফল", "আপনার অ্যাকাউন্ট নিরাপত্তা পাসওয়ার্ডটি পরিবর্তন করা হয়েছে।")
            }
            return true
        }
        return false
    }

    fun markNotificationAsRead(id: String) {
        viewModelScope.launch {
            repository.notificationDao.markAsRead(id)
        }
    }

    // --- Admin Panel Operations ---
    fun adminAddCourse(title: String, instructor: String, price: Double, discountPrice: Double?, desc: String) {
        val newCourse = Course(
            id = "course_admin_${UUID.randomUUID().toString().substring(0, 4)}",
            title = title,
            thumbnail = "course_novels",
            instructor = instructor,
            instructorBio = "প্রতিষ্ঠিত বাংলা গবেষক ও শিক্ষাবিদ।",
            price = price,
            discountPrice = discountPrice,
            description = desc,
            overview = "কোর্সের ভূমিকা ও সাহিত্যের গভীর পাঠ আলোচনা।",
            duration = "৮ ঘণ্টা (১৬টি লেকচার)",
            lessonsCount = 16,
            lessons = listOf(
                Lesson("l_ad_1", "১. প্রাথমিক পরিচিতি ও সাহিত্যমূল্য", "৩০:০০", true, "https://www.w3schools.com/html/movie.mp4")
            ),
            reviews = emptyList()
        )
        courses.add(newCourse)
        viewModelScope.launch {
            repository.addNotification("নতুন কোর্স যুক্ত হয়েছে", "এডমিন প্যানেল থেকে '${title}' কোর্সটি সাফল্যের সাথে প্রকাশ করা হয়েছে।")
        }
    }

    fun adminAddBook(title: String, authorName: String, isbn: String, category: String, price: Double, discountPrice: Double?, isEbook: Boolean, isPhysical: Boolean, desc: String) {
        val newBook = Book(
            id = "book_admin_${UUID.randomUUID().toString().substring(0, 4)}",
            title = title,
            authorId = "author_rabindranath",
            authorName = authorName,
            coverUrl = "book_banalata_sen",
            isbn = isbn,
            category = category,
            price = price,
            discountPrice = discountPrice,
            isPhysical = isPhysical,
            isEbook = isEbook,
            description = desc,
            tableOfContents = listOf("১. সূচনা অনুচ্ছেদ", "২. মধ্য পর্ব", "৩. সমাপ্তি"),
            reviews = emptyList(),
            samplePages = listOf("নমুনা পৃষ্ঠা ১: বাংলা সাহিত্যের অমূল্য অনুভূতি।", "নমুনা পৃষ্ঠা ২: জীবন ও দর্শনের অপূর্ব চিত্রশালা।")
        )
        books.add(newBook)
        viewModelScope.launch {
            repository.addNotification("নতুন বই যুক্ত হয়েছে", "এডমিন প্যানেল থেকে '${title}' বইটি সফলভাবে যুক্ত হয়েছে।")
        }
    }

    fun adminAddAuthor(name: String, bio: String, achievements: String) {
        val newAuthor = Author(
            id = "author_admin_${UUID.randomUUID().toString().substring(0, 4)}",
            name = name,
            photoUrl = "https://images.unsplash.com/photo-1506880018603-83d5b814b5a6?auto=format&fit=crop&q=80&w=300",
            bio = bio,
            achievements = achievements
        )
        authors.add(newAuthor)
        viewModelScope.launch {
            repository.addNotification("নতুন লেখক যুক্ত হয়েছে", "লেখকবৃন্দ তালিকায় '${name}' যুক্ত করা হয়েছে।")
        }
    }

    fun adminAddBanner(text: String) {
        banners.add(text)
    }

    fun adminAddBlog(title: String, authorName: String, content: String) {
        val newBlog = BlogPost(
            id = "blog_admin_${UUID.randomUUID().toString().substring(0, 4)}",
            title = title,
            authorName = authorName,
            publishDate = "২৯ জুন, ২০২৬",
            excerpt = if (content.length > 80) content.substring(0, 80) + "..." else content,
            content = content,
            coverUrl = "blog_novel",
            readingTime = "৪ মিনিট পাঠ",
            comments = emptyList()
        )
        blogs.add(newBlog)
    }

    fun addBlogComment(blogId: String, content: String, onComplete: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                if (authToken.isEmpty()) {
                    onComplete(false, "মন্তব্য করতে দয়া করে আগে লগইন করুন।")
                    return@launch
                }
                val response = RetrofitClient.api.addComment(
                    token = "Bearer $authToken",
                    id = blogId,
                    request = CommentRequest(content = content)
                )
                if (response.success && response.comment != null) {
                    val index = blogs.indexOfFirst { it.id == blogId }
                    if (index != -1) {
                        val currentBlog = blogs[index]
                        val updatedComments = currentBlog.comments + response.comment.toDomain()
                        blogs[index] = currentBlog.copy(comments = updatedComments)
                    }
                    onComplete(true, response.message ?: "মন্তব্য সফলভাবে যোগ হয়েছে")
                } else {
                    onComplete(false, response.error ?: "মন্তব্য যোগ করা যায়নি।")
                }
            } catch (e: Exception) {
                val index = blogs.indexOfFirst { it.id == blogId }
                if (index != -1) {
                    val currentBlog = blogs[index]
                    val localUserProfile = userProfile.value
                    val fallbackName = localUserProfile?.name ?: "অজানা পাঠক"
                    val fallbackComment = BlogComment(
                        id = "local_comment_${System.currentTimeMillis()}",
                        username = fallbackName,
                        content = content,
                        createdAt = "এখন"
                    )
                    val updatedComments = currentBlog.comments + fallbackComment
                    blogs[index] = currentBlog.copy(comments = updatedComments)
                    onComplete(true, "মন্তব্য সফলভাবে যোগ হয়েছে (অফলাইন মোড)")
                } else {
                    onComplete(false, "ত্রুটি: ${e.message}")
                }
            }
        }
    }

    fun likeBlogComment(blogId: String, commentId: String) {
        viewModelScope.launch {
            try {
                if (authToken.isEmpty()) return@launch
                val response = RetrofitClient.api.likeComment(
                    token = "Bearer $authToken",
                    commentId = commentId
                )
                if (response.success) {
                    val blogIndex = blogs.indexOfFirst { it.id == blogId }
                    if (blogIndex != -1) {
                        val currentBlog = blogs[blogIndex]
                        val updatedComments = currentBlog.comments.map { comment ->
                            if (comment.id == commentId) {
                                comment.copy(
                                    likesCount = response.likesCount ?: comment.likesCount,
                                    likedBy = response.likedBy ?: comment.likedBy
                                )
                            } else comment
                        }
                        blogs[blogIndex] = currentBlog.copy(comments = updatedComments)
                    }
                }
            } catch (e: Exception) {
                val blogIndex = blogs.indexOfFirst { it.id == blogId }
                if (blogIndex != -1) {
                    val currentBlog = blogs[blogIndex]
                    val updatedComments = currentBlog.comments.map { comment ->
                        if (comment.id == commentId) {
                            val isLiked = comment.likedBy.contains("me")
                            val newLikedBy = if (isLiked) comment.likedBy - "me" else comment.likedBy + "me"
                            val newLikesCount = if (isLiked) comment.likesCount - 1 else comment.likesCount + 1
                            comment.copy(likesCount = newLikesCount, likedBy = newLikedBy)
                        } else comment
                    }
                    blogs[blogIndex] = currentBlog.copy(comments = updatedComments)
                }
            }
        }
    }

    fun replyBlogComment(blogId: String, commentId: String, content: String, onComplete: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                if (authToken.isEmpty()) {
                    onComplete(false, "মন্তব্য করতে দয়া করে আগে লগইন করুন।")
                    return@launch
                }
                val response = RetrofitClient.api.replyComment(
                    token = "Bearer $authToken",
                    commentId = commentId,
                    request = ReplyRequest(content = content)
                )
                if (response.success && response.reply != null) {
                    val blogIndex = blogs.indexOfFirst { it.id == blogId }
                    if (blogIndex != -1) {
                        val currentBlog = blogs[blogIndex]
                        val updatedComments = currentBlog.comments.map { comment ->
                            if (comment.id == commentId) {
                                comment.copy(replies = comment.replies + response.reply.toDomain())
                            } else comment
                        }
                        blogs[blogIndex] = currentBlog.copy(comments = updatedComments)
                    }
                    onComplete(true, response.message ?: "প্রতিউত্তর সফলভাবে যোগ হয়েছে")
                } else {
                    onComplete(false, response.error ?: "প্রতিউত্তর যোগ করা যায়নি।")
                }
            } catch (e: Exception) {
                val blogIndex = blogs.indexOfFirst { it.id == blogId }
                if (blogIndex != -1) {
                    val currentBlog = blogs[blogIndex]
                    val localUserProfile = userProfile.value
                    val fallbackName = localUserProfile?.name ?: "অজানা পাঠক"
                    val fallbackReply = BlogCommentReply(
                        id = "local_reply_${System.currentTimeMillis()}",
                        username = fallbackName,
                        content = content,
                        createdAt = "এখন"
                    )
                    val updatedComments = currentBlog.comments.map { comment ->
                        if (comment.id == commentId) {
                            comment.copy(replies = comment.replies + fallbackReply)
                        } else comment
                    }
                    blogs[blogIndex] = currentBlog.copy(comments = updatedComments)
                    onComplete(true, "প্রতিউত্তর সফলভাবে যোগ হয়েছে (অফলাইন মোড)")
                } else {
                    onComplete(false, "ত্রুটি: ${e.message}")
                }
            }
        }
    }

    fun likeBlogCommentReply(blogId: String, commentId: String, replyId: String) {
        viewModelScope.launch {
            try {
                if (authToken.isEmpty()) return@launch
                val response = RetrofitClient.api.likeReply(
                    token = "Bearer $authToken",
                    replyId = replyId
                )
                if (response.success) {
                    val blogIndex = blogs.indexOfFirst { it.id == blogId }
                    if (blogIndex != -1) {
                        val currentBlog = blogs[blogIndex]
                        val updatedComments = currentBlog.comments.map { comment ->
                            if (comment.id == commentId) {
                                val updatedReplies = comment.replies.map { reply ->
                                    if (reply.id == replyId) {
                                        reply.copy(
                                            likesCount = response.likesCount ?: reply.likesCount,
                                            likedBy = response.likedBy ?: reply.likedBy
                                        )
                                    } else reply
                                }
                                comment.copy(replies = updatedReplies)
                            } else comment
                        }
                        blogs[blogIndex] = currentBlog.copy(comments = updatedComments)
                    }
                }
            } catch (e: Exception) {
                val blogIndex = blogs.indexOfFirst { it.id == blogId }
                if (blogIndex != -1) {
                    val currentBlog = blogs[blogIndex]
                    val updatedComments = currentBlog.comments.map { comment ->
                        if (comment.id == commentId) {
                            val updatedReplies = comment.replies.map { reply ->
                                if (reply.id == replyId) {
                                    val isLiked = reply.likedBy.contains("me")
                                    val newLikedBy = if (isLiked) reply.likedBy - "me" else reply.likedBy + "me"
                                    val newLikesCount = if (isLiked) reply.likesCount - 1 else reply.likesCount + 1
                                    reply.copy(likesCount = newLikesCount, likedBy = newLikedBy)
                                } else reply
                            }
                            comment.copy(replies = updatedReplies)
                        } else comment
                    }
                    blogs[blogIndex] = currentBlog.copy(comments = updatedComments)
                }
            }
        }
    }
}
