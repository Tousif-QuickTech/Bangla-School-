package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

// --- Core App Models ---

data class Author(
    val id: String,
    val name: String,
    val photoUrl: String,
    val bio: String,
    val achievements: String,
    val facebookUrl: String = "",
    val twitterUrl: String = "",
    val websiteUrl: String = ""
) : Serializable

data class Lesson(
    val id: String,
    val title: String,
    val duration: String,
    val isFreePreview: Boolean,
    val videoUrl: String, // Localized or youtube/vimeo simulation
    val textContent: String = ""
) : Serializable

data class Review(
    val id: String,
    val userName: String,
    val rating: Float,
    val comment: String,
    val date: String
) : Serializable

data class Course(
    val id: String,
    val title: String,
    val thumbnail: String,
    val instructor: String,
    val instructorBio: String,
    val price: Double,
    val discountPrice: Double?,
    val description: String,
    val overview: String,
    val duration: String,
    val lessonsCount: Int,
    val lessons: List<Lesson>,
    val reviews: List<Review>,
    val rating: Float = 4.5f
) : Serializable

data class Book(
    val id: String,
    val title: String,
    val authorId: String,
    val authorName: String,
    val coverUrl: String,
    val isbn: String,
    val category: String,
    val price: Double,
    val discountPrice: Double?,
    val isPhysical: Boolean,
    val isEbook: Boolean,
    val description: String,
    val tableOfContents: List<String>,
    val reviews: List<Review>,
    val rating: Float = 4.6f,
    val isBestSeller: Boolean = false,
    val samplePages: List<String> = emptyList() // List of text snippets for preview
) : Serializable

data class BlogCommentReply(
    val id: String,
    val username: String,
    val content: String,
    val createdAt: String,
    val likesCount: Int = 0,
    val likedBy: List<String> = emptyList()
) : java.io.Serializable

data class BlogComment(
    val id: String,
    val username: String,
    val content: String,
    val createdAt: String,
    val likesCount: Int = 0,
    val likedBy: List<String> = emptyList(),
    val replies: List<BlogCommentReply> = emptyList()
) : java.io.Serializable

data class BlogPost(
    val id: String,
    val title: String,
    val authorName: String,
    val publishDate: String,
    val excerpt: String,
    val content: String,
    val coverUrl: String,
    val readingTime: String,
    val comments: List<BlogComment> = emptyList()
) : Serializable

// --- Room Database Entities for Local State Persistence ---

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String = "current_user",
    val name: String,
    val email: String,
    val phone: String,
    val isLoggedIn: Boolean = false,
    val notificationCount: Int = 3
)

@Entity(tableName = "course_enrollments")
data class CourseEnrollmentEntity(
    @PrimaryKey val courseId: String,
    val title: String,
    val thumbnail: String,
    val instructor: String,
    val progressPercent: Int = 0,
    val completedLessons: String = "", // Comma-separated lesson IDs
    val enrollmentDate: Long = System.currentTimeMillis()
)

@Entity(tableName = "purchased_books")
data class PurchasedBookEntity(
    @PrimaryKey val bookId: String,
    val title: String,
    val authorName: String,
    val coverUrl: String,
    val purchaseDate: Long = System.currentTimeMillis(),
    val isEbook: Boolean = true,
    val lastReadPage: Int = 0
)

@Entity(tableName = "wishlist")
data class WishlistItemEntity(
    @PrimaryKey val itemId: String,
    val title: String,
    val image: String,
    val price: Double,
    val type: String // "course" or "book"
)

@Entity(tableName = "order_history")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val date: String,
    val itemsSummary: String,
    val totalAmount: Double,
    val paymentMethod: String,
    val status: String // "সফল" (Success), "পেন্ডিং" (Pending), "ব্যর্থ" (Failed)
)

@Entity(tableName = "book_bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bookId: String,
    val pageNumber: Int,
    val note: String,
    val createdTime: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false
)
