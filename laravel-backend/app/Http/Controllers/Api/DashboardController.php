<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Banner;
use App\Models\Notification;
use Illuminate\Http\Request;

class DashboardController extends Controller
{
    /**
     * Get consolidated dashboard data for the authenticated user.
     */
    public function getDashboard(Request $request)
    {
        $user = $request->user();

        // Load enrolled courses detailed objects
        $enrolledCourses = $user->enrolledCourses()->get()->map(function ($course) {
            return [
                '_id' => (string) $course->id,
                'title' => $course->title,
                'instructor' => $course->instructor,
                'description' => $course->description,
                'price' => $course->price,
                'rating' => $course->rating,
                'duration' => $course->duration,
                'lecturesCount' => $course->lectures_count,
                'isBestSeller' => $course->is_best_seller,
                'enrolledCount' => $course->enrolled_count,
                'coverUrl' => $course->cover_url,
                'category' => $course->category,
            ];
        });

        // Load purchased books detailed objects
        $purchasedBooks = $user->purchasedBooks()->get()->map(function ($book) {
            return [
                '_id' => (string) $book->id,
                'title' => $book->title,
                'author' => (string) $book->author_id,
                'authorName' => $book->author_name,
                'description' => $book->description,
                'price' => $book->price,
                'rating' => $book->rating,
                'reviewsCount' => $book->reviews_count,
                'isBestSeller' => $book->is_best_seller,
                'isEbook' => $book->is_ebook,
                'coverUrl' => $book->cover_url,
                'publishYear' => $book->publish_year,
                'pages' => $book->pages,
                'isbn' => $book->isbn,
            ];
        });

        // Load saved blogs detailed objects
        $savedBlogs = $user->savedBlogs()->get()->map(function ($blog) {
            return [
                '_id' => (string) $blog->id,
                'title' => $blog->title,
                'content' => $blog->content,
                'authorName' => $blog->author_name,
                'publishDate' => $blog->publish_date,
                'coverUrl' => $blog->cover_url,
                'readingTime' => $blog->reading_time,
            ];
        });

        // Retrieve active banners and recent notifications
        $activeBanners = Banner::where('is_active', true)->get()->map(function ($banner) {
            return [
                '_id' => (string) $banner->id,
                'text' => $banner->text,
                'imageUrl' => $banner->image_url,
            ];
        });

        $recentNotifications = Notification::orderBy('created_at', 'desc')->take(5)->get()->map(function ($notif) {
            return [
                '_id' => (string) $notif->id,
                'title' => $notif->title,
                'message' => $notif->message,
                'isRead' => $notif->is_read,
                'createdAt' => $notif->created_at ? $notif->created_at->toISOString() : null,
            ];
        });

        return response()->json([
            'success' => true,
            'user' => [
                '_id' => (string) $user->id,
                'username' => $user->username,
                'email' => $user->email,
                'role' => $user->role,
                'bio' => $user->bio,
                'profilePhotoUrl' => $user->profile_photo_url,
                'notificationPref' => $user->notification_pref,
            ],
            'stats' => [
                'enrolledCoursesCount' => $enrolledCourses->count(),
                'purchasedBooksCount' => $purchasedBooks->count(),
                'savedBlogsCount' => $savedBlogs->count(),
            ],
            'enrolledCourses' => $enrolledCourses,
            'purchasedBooks' => $purchasedBooks,
            'savedBlogs' => $savedBlogs,
            'activeBanners' => $activeBanners,
            'recentNotifications' => $recentNotifications,
        ], 200);
    }
}
