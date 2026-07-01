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
        Schema::create('courses', function (Blueprint $table) {
            $table->id();
            $table->string('title');
            $table->string('instructor');
            $table->text('description');
            $table->decimal('price', 8, 2);
            $table->float('rating')->default(4.8);
            $table->string('duration');
            $table->integer('lectures_count');
            $table->boolean('is_best_seller')->default(false);
            $table->integer('enrolled_count')->default(0);
            $table->string('cover_url')->nullable();
            $table->string('category');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('courses');
    }
};
