<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Course extends Model
{
    use HasFactory;

    protected $fillable = [
        'title',
        'instructor',
        'description',
        'price',
        'rating',
        'duration',
        'lectures_count',
        'is_best_seller',
        'enrolled_count',
        'cover_url',
        'category',
    ];

    protected $casts = [
        'is_best_seller' => 'boolean',
        'price' => 'double',
        'rating' => 'float',
    ];

    /**
     * Users enrolled in this course
     */
    public function users()
    {
        return $this->belongsToMany(User::class, 'course_user');
    }
}
