package com.example.data.network

import com.example.data.*
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

// --- Auth Requests & Responses ---
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class AuthResponse(
    val success: Boolean,
    val token: String? = null,
    val user: NetworkUser? = null,
    val error: String? = null
)

data class NetworkUser(
    @Json(name = "_id") val id: String? = null,
    val username: String,
    val email: String,
    val role: String,
    val bio: String? = null,
    val profilePhotoUrl: String? = null,
    val enrolledCourses: List<String> = emptyList(),
    val purchasedBooks: List<String> = emptyList(),
    val savedBlogs: List<String> = emptyList()
)

// --- Author ---
data class AuthorResponse(
    val success: Boolean,
    val author: NetworkAuthor? = null,
    val error: String? = null
)

data class AuthorListResponse(
    val success: Boolean,
    val count: Int? = null,
    val authors: List<NetworkAuthor>? = null,
    val error: String? = null
)

data class NetworkAuthor(
    @Json(name = "_id") val id: String,
    val name: String,
    val bio: String,
    val photoUrl: String,
    val achievements: String? = null,
    val works: String? = null,
    val booksCount: Int = 0
)

// --- Book ---
data class BookResponse(
    val success: Boolean,
    val book: NetworkBook? = null,
    val error: String? = null
)

data class BookListResponse(
    val success: Boolean,
    val count: Int? = null,
    val books: List<NetworkBook>? = null,
    val error: String? = null
)

data class NetworkBook(
    @Json(name = "_id") val id: String,
    val title: String,
    val author: Any? = null, // Can be string id or populated NetworkAuthor map
    val authorName: String? = null,
    val description: String,
    val price: Double,
    val rating: Float = 4.5f,
    val reviewsCount: Int = 0,
    val isBestSeller: Boolean = false,
    val isEbook: Boolean = true,
    val coverUrl: String? = null,
    val publishYear: Int? = null,
    val pages: Int? = null,
    val isbn: String? = null
)

// --- Course ---
data class CourseResponse(
    val success: Boolean,
    val course: NetworkCourse? = null,
    val error: String? = null
)

data class CourseListResponse(
    val success: Boolean,
    val count: Int? = null,
    val courses: List<NetworkCourse>? = null,
    val error: String? = null
)

data class NetworkCourse(
    @Json(name = "_id") val id: String,
    val title: String,
    val instructor: String,
    val description: String,
    val price: Double,
    val rating: Float = 4.8f,
    val duration: String,
    val lecturesCount: Int,
    val isBestSeller: Boolean = false,
    val enrolledCount: Int = 0,
    val coverUrl: String? = null,
    val category: String
)

// --- Blog ---
data class BlogResponse(
    val success: Boolean,
    val blog: NetworkBlog? = null,
    val error: String? = null
)

data class BlogListResponse(
    val success: Boolean,
    val count: Int? = null,
    val blogs: List<NetworkBlog>? = null,
    val error: String? = null
)

data class NetworkCommentReply(
    @Json(name = "_id") val id: String,
    val username: String,
    val content: String,
    val createdAt: String? = null,
    val likesCount: Int? = null,
    val likedBy: List<String>? = null
)

data class NetworkComment(
    @Json(name = "_id") val id: String,
    val username: String,
    val content: String,
    val createdAt: String? = null,
    val likesCount: Int? = null,
    val likedBy: List<String>? = null,
    val replies: List<NetworkCommentReply>? = null
)

data class NetworkBlog(
    @Json(name = "_id") val id: String,
    val title: String,
    val content: String,
    val authorName: String,
    val publishDate: String? = null,
    val coverUrl: String? = null,
    val likedBy: List<String> = emptyList(),
    val readingTime: String? = null,
    val comments: List<NetworkComment>? = null
)

data class CommentRequest(
    val content: String
)

data class CommentResponse(
    val success: Boolean,
    val comment: NetworkComment? = null,
    val message: String? = null,
    val error: String? = null
)

data class ReplyRequest(
    val content: String
)

data class ReplyResponse(
    val success: Boolean,
    val reply: NetworkCommentReply? = null,
    val message: String? = null,
    val error: String? = null
)

data class LikeActionResponse(
    val success: Boolean,
    val liked: Boolean? = null,
    val likesCount: Int? = null,
    val likedBy: List<String>? = null,
    val error: String? = null
)

// --- Banner ---
data class BannerListResponse(
    val success: Boolean,
    val banners: List<NetworkBanner>? = null,
    val error: String? = null
)

data class NetworkBanner(
    @Json(name = "_id") val id: String,
    val text: String,
    val imageUrl: String? = null,
    val isActive: Boolean = true
)

// --- Notification ---
data class NotificationListResponse(
    val success: Boolean,
    val notifications: List<NetworkNotification>? = null,
    val error: String? = null
)

data class NetworkNotification(
    @Json(name = "_id") val id: String,
    val title: String,
    val message: String,
    val isRead: Boolean = false,
    val createdAt: String? = null
)

// --- Retrofit Service API Interface ---
interface BanglaSchoolApi {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("api/auth/profile")
    suspend fun getProfile(@Header("Authorization") token: String): AuthResponse

    @GET("api/authors")
    suspend fun getAuthors(): AuthorListResponse

    @GET("api/authors/{id}")
    suspend fun getAuthor(@Path("id") id: String): AuthorResponse

    @GET("api/books")
    suspend fun getBooks(): BookListResponse

    @GET("api/books/{id}")
    suspend fun getBook(@Path("id") id: String): BookResponse

    @GET("api/courses")
    suspend fun getCourses(): CourseListResponse

    @GET("api/courses/{id}")
    suspend fun getCourse(@Path("id") id: String): CourseResponse

    @GET("api/blogs")
    suspend fun getBlogs(): BlogListResponse

    @POST("api/blogs/{id}/like")
    suspend fun likeBlog(@Header("Authorization") token: String, @Path("id") id: String): BlogResponse

    @POST("api/blogs/{id}/comment")
    suspend fun addComment(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: CommentRequest
    ): CommentResponse

    @POST("api/comments/{commentId}/like")
    suspend fun likeComment(
        @Header("Authorization") token: String,
        @Path("commentId") commentId: String
    ): LikeActionResponse

    @POST("api/comments/{commentId}/reply")
    suspend fun replyComment(
        @Header("Authorization") token: String,
        @Path("commentId") commentId: String,
        @Body request: ReplyRequest
    ): ReplyResponse

    @POST("api/replies/{replyId}/like")
    suspend fun likeReply(
        @Header("Authorization") token: String,
        @Path("replyId") replyId: String
    ): LikeActionResponse

    @GET("api/banners")
    suspend fun getBanners(): BannerListResponse

    @GET("api/notifications")
    suspend fun getNotifications(): NotificationListResponse
}

// --- Retrofit Singleton Client ---
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:5000/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .writeTimeout(3, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    val api: BanglaSchoolApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(BanglaSchoolApi::class.java)
    }
}

// --- Domain Mapping Converters ---
fun NetworkAuthor.toDomain(): Author {
    return Author(
        id = this.id,
        name = this.name,
        photoUrl = this.photoUrl.ifEmpty { "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=300" },
        bio = this.bio,
        achievements = this.achievements ?: "",
        facebookUrl = "",
        twitterUrl = ""
    )
}

fun NetworkBook.toDomain(): Book {
    return Book(
        id = this.id,
        title = this.title,
        authorId = if (this.author is Map<*, *>) (this.author["_id"] as? String) ?: "" else this.author?.toString() ?: "",
        authorName = this.authorName ?: "অজ্ঞাত লেখক",
        coverUrl = this.coverUrl ?: "book_gitanjali",
        isbn = this.isbn ?: "978-984-555-102-1",
        category = "উপন্যাস",
        price = this.price,
        discountPrice = this.price * 0.8,
        isPhysical = true,
        isEbook = this.isEbook,
        description = this.description,
        tableOfContents = listOf("১. প্রথম পরিচ্ছেদ", "২. দ্বিতীয় পরিচ্ছেদ", "৩. শেষ পরিচ্ছেদ"),
        reviews = emptyList(),
        rating = this.rating,
        isBestSeller = this.isBestSeller,
        samplePages = listOf("নমুনা পৃষ্ঠা ১: বাংলা সাহিত্যের চিরন্তন বাণী।", "নমুনা পৃষ্ঠা ২: জীবন ও দর্শনের চিরকালীন রূপ।")
    )
}

fun NetworkCourse.toDomain(): Course {
    return Course(
        id = this.id,
        title = this.title,
        thumbnail = "course_novels",
        instructor = this.instructor,
        instructorBio = "প্রতিষ্ঠিত বাংলা সাহিত্য গবেষক ও শিক্ষক।",
        price = this.price,
        discountPrice = this.price * 0.75,
        description = this.description,
        overview = "কোর্সের পরিচিতি ও বাংলা সাহিত্যের গভীর আলোচনা।",
        duration = this.duration,
        lessonsCount = this.lecturesCount,
        lessons = listOf(
            Lesson("les_1", "১. পরিচিতি ও সাহিত্যমূল্য", "৩০:০০", true, "https://www.w3schools.com/html/movie.mp4")
        ),
        reviews = emptyList(),
        rating = this.rating
    )
}

fun NetworkCommentReply.toDomain(): BlogCommentReply {
    return BlogCommentReply(
        id = this.id,
        username = this.username,
        content = this.content,
        createdAt = this.createdAt ?: "৩০ জুন, ২০২৬",
        likesCount = this.likesCount ?: 0,
        likedBy = this.likedBy ?: emptyList()
    )
}

fun NetworkComment.toDomain(): BlogComment {
    return BlogComment(
        id = this.id,
        username = this.username,
        content = this.content,
        createdAt = this.createdAt ?: "৩০ জুন, ২০২৬",
        likesCount = this.likesCount ?: 0,
        likedBy = this.likedBy ?: emptyList(),
        replies = this.replies?.map { it.toDomain() } ?: emptyList()
    )
}

fun NetworkBlog.toDomain(): BlogPost {
    return BlogPost(
        id = this.id,
        title = this.title,
        authorName = this.authorName,
        publishDate = this.publishDate ?: "২৯ জুন, ২০২৬",
        excerpt = if (this.content.length > 120) this.content.substring(0, 120) + "..." else this.content,
        content = this.content,
        coverUrl = this.coverUrl ?: "blog_novel",
        readingTime = this.readingTime ?: "৫ মিনিট পাঠ",
        comments = this.comments?.map { it.toDomain() } ?: emptyList()
    )
}
