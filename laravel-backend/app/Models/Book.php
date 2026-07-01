<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Book extends Model
{
    use HasFactory;

    protected $fillable = [
        'title',
        'author_id',
        'author_name',
        'description',
        'price',
        'rating',
        'reviews_count',
        'is_best_seller',
        'is_ebook',
        'cover_url',
        'publish_year',
        'pages',
        'isbn',
    ];

    protected $casts = [
        'is_best_seller' => 'boolean',
        'is_ebook' => 'boolean',
        'price' => 'double',
        'rating' => 'float',
    ];

    /**
     * Author Relationship
     */
    public function author()
    {
        return $this->belongsTo(Author::class);
    }

    /**
     * Users who purchased this book
     */
    public function users()
    {
        return $this->belongsToMany(User::class, 'book_user');
    }
}
