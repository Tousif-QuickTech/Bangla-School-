<?php

use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\AuthorController;
use App\Http\Controllers\Api\BookController;
use App\Http\Controllers\Api\CourseController;
use App\Http\Controllers\Api\BlogController;
use App\Http\Controllers\Api\NotificationController;
use App\Http\Controllers\Api\BannerController;
use App\Http\Controllers\Api\DashboardController;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "api" middleware group. Make something great!
|
*/

// --- Public Authentication Routes ---
Route::post('/auth/register', [AuthController::class, 'register']);
Route::post('/auth/login', [AuthController::class, 'login']);

// --- Authenticated User Routes ---
Route::middleware('auth:sanctum')->group(function () {
    Route::get('/auth/profile', [AuthController::class, 'getProfile']);
    Route::post('/auth/logout', [AuthController::class, 'logout']);
    
    // Dashboard Data Retrieval (consolidated statistics, user details, courses & books)
    Route::get('/dashboard', [DashboardController::class, 'getDashboard']);
    
    // Blog likes & saves (requires authentication)
    Route::post('/blogs/{id}/like', [BlogController::class, 'likeBlog']);
    Route::post('/blogs/{id}/save', [BlogController::class, 'saveBlog']);
    Route::post('/blogs/{id}/comment', [BlogController::class, 'comment']);
    
    // Comment likes & replies
    Route::post('/comments/{commentId}/like', [BlogController::class, 'likeComment']);
    Route::post('/comments/{commentId}/reply', [BlogController::class, 'replyComment']);
    Route::post('/replies/{replyId}/like', [BlogController::class, 'likeReply']);
});

// --- Public / General Resource Routes ---
Route::get('/books/search', [BookController::class, 'search']); // Book searching (defined before Resource to avoid conflict)
Route::apiResource('authors', AuthorController::class);
Route::apiResource('books', BookController::class);
Route::apiResource('courses', CourseController::class); // Course listings (index, show, store, update, destroy)
Route::apiResource('blogs', BlogController::class);

Route::get('/banners', [BannerController::class, 'index']);
Route::post('/banners', [BannerController::class, 'store']);

Route::get('/notifications', [NotificationController::class, 'index']);
Route::post('/notifications', [NotificationController::class, 'store']);
