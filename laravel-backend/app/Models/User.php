<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Laravel\Sanctum\HasApiTokens;

class User extends Authenticatable
{
    use HasApiTokens, HasFactory, Notifiable;

    /**
     * The attributes that are mass assignable.
     *
     * @var array<int, string>
     */
    protected $fillable = [
        'username',
        'email',
        'password',
        'role',
        'phone',
        'bio',
        'profile_photo_url',
        'notification_pref',
    ];

    /**
     * The attributes that should be hidden for serialization.
     *
     * @var array<int, string>
     */
    protected $hidden = [
        'password',
        'remember_token',
    ];

    /**
     * The attributes that should be cast.
     *
     * @var array<string, string>
     */
    protected $casts = [
        'notification_pref' => 'boolean',
        'password' => 'hashed',
    ];

    /**
     * Enrolled Courses Relationship
     */
    public function enrolledCourses()
    {
        return $this->belongsToMany(Course::class, 'course_user');
    }

    /**
     * Purchased Books Relationship
     */
    public function purchasedBooks()
    {
        return $this->belongsToMany(Book::class, 'book_user');
    }

    /**
     * Saved Blogs Relationship
     */
    public function savedBlogs()
    {
        return $this->belongsToMany(Blog::class, 'blog_user_saved');
    }

    /**
     * Liked Blogs Relationship
     */
    public function likedBlogs()
    {
        return $this->belongsToMany(Blog::class, 'blog_user_liked');
    }
}
