<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('books', function (Blueprint $table) {
            $table->id();
            $table->string('title');
            $table->foreignId('author_id')->nullable()->constrained('authors')->onDelete('set null');
            $table->string('author_name');
            $table->text('description');
            $table->decimal('price', 8, 2);
            $table->float('rating')->default(4.5);
            $table->integer('reviews_count')->default(0);
            $table->boolean('is_best_seller')->default(false);
            $table->boolean('is_ebook')->default(true);
            $table->string('cover_url')->nullable();
            $table->integer('publish_year')->nullable();
            $table->integer('pages')->nullable();
            $table->string('isbn')->nullable();
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('books');
    }
};
