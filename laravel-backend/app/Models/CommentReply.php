<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class CommentReply extends Model
{
    use HasFactory;

    protected $table = 'comment_replies';

    protected $fillable = [
        'comment_id',
        'user_id',
        'content',
    ];

    /**
     * Get the comment that owns the reply.
     */
    public function comment()
    {
        return $this->belongsTo(Comment::class);
    }

    /**
     * Get the user who made the reply.
     */
    public function user()
    {
        return $this->belongsTo(User::class);
    }

    /**
     * Users who liked this reply.
     */
    public function likedBy()
    {
        return $this->belongsToMany(User::class, 'reply_user_liked', 'comment_reply_id', 'user_id');
    }
}
