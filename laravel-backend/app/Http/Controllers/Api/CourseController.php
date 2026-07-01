<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Course;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class CourseController extends Controller
{
    /**
     * Display a listing of the courses with search and filtering.
     */
    public function index(Request $request)
    {
        $query = Course::query();

        // Search by title or instructor
        if ($search = $request->query('q')) {
            $query->where(function ($q) use ($search) {
                $q->where('title', 'LIKE', "%{$search}%")
                  ->orWhere('instructor', 'LIKE', "%{$search}%");
            });
        }

        // Filter by category
        if ($category = $request->query('category')) {
            $query->where('category', $category);
        }

        // Filter by is_best_seller
        if ($request->has('best_seller')) {
            $query->where('is_best_seller', $request->boolean('best_seller'));
        }

        $courses = $query->get()->map(function ($course) {
            return [
                '_id' => (string) $course->id,
                'title' => $course->title,
                'instructor' => $course->instructor,
                'description' => $course->description,
                'price' => $course->price,
                'rating' => $course->rating,
                'duration' => $course->duration,
                'lecturesCount' => $course->lectures_count,
                'isBestSeller' => $course->is_best_seller,
                'enrolledCount' => $course->enrolled_count,
                'coverUrl' => $course->cover_url,
                'category' => $course->category,
            ];
        });

        return response()->json([
            'success' => true,
            'count' => $courses->count(),
            'courses' => $courses
        ], 200);
    }

    /**
     * Store a newly created course.
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'title' => 'required|string|max:255',
            'instructor' => 'required|string',
            'description' => 'required|string',
            'price' => 'required|numeric',
            'duration' => 'required|string',
            'lectures_count' => 'required|integer',
            'category' => 'required|string',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'error' => $validator->errors()->first()
            ], 400);
        }

        $course = Course::create($request->all());

        return response()->json([
            'success' => true,
            'course' => [
                '_id' => (string) $course->id,
                'title' => $course->title,
                'instructor' => $course->instructor,
                'description' => $course->description,
                'price' => $course->price,
                'rating' => $course->rating,
                'duration' => $course->duration,
                'lecturesCount' => $course->lectures_count,
                'isBestSeller' => $course->is_best_seller,
                'enrolledCount' => $course->enrolled_count,
                'coverUrl' => $course->cover_url,
                'category' => $course->category,
            ]
        ], 201);
    }

    /**
     * Display the specified course.
     */
    public function show($id)
    {
        $course = Course::find($id);

        if (!$course) {
            return response()->json([
                'success' => false,
                'error' => 'কোর্স পাওয়া যায়নি'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'course' => [
                '_id' => (string) $course->id,
                'title' => $course->title,
                'instructor' => $course->instructor,
                'description' => $course->description,
                'price' => $course->price,
                'rating' => $course->rating,
                'duration' => $course->duration,
                'lecturesCount' => $course->lectures_count,
                'isBestSeller' => $course->is_best_seller,
                'enrolledCount' => $course->enrolled_count,
                'coverUrl' => $course->cover_url,
                'category' => $course->category,
            ]
        ], 200);
    }

    /**
     * Update the specified course.
     */
    public function update(Request $request, $id)
    {
        $course = Course::find($id);

        if (!$course) {
            return response()->json([
                'success' => false,
                'error' => 'কোর্স পাওয়া যায়নি'
            ], 404);
        }

        $course->update($request->all());

        return response()->json([
            'success' => true,
            'course' => [
                '_id' => (string) $course->id,
                'title' => $course->title,
                'instructor' => $course->instructor,
                'description' => $course->description,
                'price' => $course->price,
                'rating' => $course->rating,
                'duration' => $course->duration,
                'lecturesCount' => $course->lectures_count,
                'isBestSeller' => $course->is_best_seller,
                'enrolledCount' => $course->enrolled_count,
                'coverUrl' => $course->cover_url,
                'category' => $course->category,
            ]
        ], 200);
    }

    /**
     * Remove the specified course.
     */
    public function destroy($id)
    {
        $course = Course::find($id);

        if (!$course) {
            return response()->json([
                'success' => false,
                'error' => 'কোর্স পাওয়া যায়নি'
            ], 404);
        }

        $course->delete();

        return response()->json([
            'success' => true,
            'message' => 'কোর্স সফলভাবে মুছে ফেলা হয়েছে'
        ], 200);
    }
}
