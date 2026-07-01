package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 'current_user' LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfileEntity)

    @Query("DELETE FROM user_profile WHERE id = 'current_user'")
    suspend fun clearUserProfile()
}

@Dao
interface CourseEnrollmentDao {
    @Query("SELECT * FROM course_enrollments ORDER BY enrollmentDate DESC")
    fun getEnrolledCourses(): Flow<List<CourseEnrollmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun enrollCourse(enrollment: CourseEnrollmentEntity)

    @Query("UPDATE course_enrollments SET progressPercent = :progress, completedLessons = :completed WHERE courseId = :courseId")
    suspend fun updateProgress(courseId: String, progress: Int, completed: String)

    @Query("SELECT * FROM course_enrollments WHERE courseId = :courseId LIMIT 1")
    suspend fun getEnrollment(courseId: String): CourseEnrollmentEntity?

    @Query("DELETE FROM course_enrollments")
    suspend fun clear()
}

@Dao
interface PurchasedBookDao {
    @Query("SELECT * FROM purchased_books ORDER BY purchaseDate DESC")
    fun getPurchasedBooks(): Flow<List<PurchasedBookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun purchaseBook(book: PurchasedBookEntity)

    @Query("UPDATE purchased_books SET lastReadPage = :page WHERE bookId = :bookId")
    suspend fun updateLastReadPage(bookId: String, page: Int)

    @Query("SELECT * FROM purchased_books WHERE bookId = :bookId LIMIT 1")
    suspend fun getPurchasedBook(bookId: String): PurchasedBookEntity?

    @Query("DELETE FROM purchased_books")
    suspend fun clear()
}

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist")
    fun getWishlistItems(): Flow<List<WishlistItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(item: WishlistItemEntity)

    @Query("DELETE FROM wishlist WHERE itemId = :itemId")
    suspend fun removeFromWishlist(itemId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE itemId = :itemId LIMIT 1)")
    fun isInWishlist(itemId: String): Flow<Boolean>

    @Query("DELETE FROM wishlist")
    suspend fun clear()
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM order_history ORDER BY orderId DESC")
    fun getOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrder(order: OrderEntity)

    @Query("DELETE FROM order_history")
    suspend fun clear()
}

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM book_bookmarks WHERE bookId = :bookId ORDER BY pageNumber ASC")
    fun getBookmarks(bookId: String): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM book_bookmarks WHERE id = :id")
    suspend fun deleteBookmark(id: Int)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM user_notifications ORDER BY id DESC")
    fun getNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNotification(notification: NotificationEntity)

    @Query("UPDATE user_notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query("DELETE FROM user_notifications")
    suspend fun clear()
}

@Database(
    entities = [
        UserProfileEntity::class,
        CourseEnrollmentEntity::class,
        PurchasedBookEntity::class,
        WishlistItemEntity::class,
        OrderEntity::class,
        BookmarkEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun courseEnrollmentDao(): CourseEnrollmentDao
    abstract fun purchasedBookDao(): PurchasedBookDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun orderDao(): OrderDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bangla_sahitya_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
