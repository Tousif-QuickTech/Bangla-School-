<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Blog extends Model
{
    use HasFactory;

    protected $fillable = [
        'title',
        'content',
        'author_name',
        'publish_date',
        'cover_url',
        'reading_time',
    ];

    /**
     * Users who saved this blog
     */
    public function savedBy()
    {
        return $this->belongsToMany(User::class, 'blog_user_saved');
    }

    /**
     * Users who liked this blog
     */
    public function likedBy()
    {
        return $this->belongsToMany(User::class, 'blog_user_liked');
    }

    /**
     * Comments for this blog post
     */
    public function comments()
    {
        return $this->hasMany(Comment::class);
    }
}
