<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Comment extends Model
{
    use HasFactory;

    protected $fillable = [
        'user_id',
        'blog_id',
        'content',
    ];

    /**
     * User who left this comment
     */
    public function user()
    {
        return $this->belongsTo(User::class);
    }

    /**
     * Blog post this comment belongs to
     */
    public function blog()
    {
        return $this->belongsTo(Blog::class);
    }

    /**
     * Replies for this comment
     */
    public function replies()
    {
        return $this->hasMany(CommentReply::class);
    }

    /**
     * Users who liked this comment
     */
    public function likedBy()
    {
        return $this->belongsToMany(User::class, 'comment_user_liked');
    }
}
