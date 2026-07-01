<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Author extends Model
{
    use HasFactory;

    protected $fillable = [
        'name',
        'bio',
        'photo_url',
        'achievements',
        'works',
        'books_count',
    ];

    /**
     * Books Relationship
     */
    public function books()
    {
        return $this->hasMany(Book::class);
    }
}
