<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Author;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class AuthorController extends Controller
{
    /**
     * Display a listing of the authors.
     */
    public function index()
    {
        $authors = Author::all()->map(function ($author) {
            return [
                '_id' => (string) $author->id,
                'name' => $author->name,
                'bio' => $author->bio,
                'photoUrl' => $author->photo_url,
                'achievements' => $author->achievements,
                'works' => $author->works,
                'booksCount' => $author->books_count ?: $author->books()->count()
            ];
        });

        return response()->json([
            'success' => true,
            'count' => $authors->count(),
            'authors' => $authors
        ], 200);
    }

    /**
     * Store a newly created author.
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'name' => 'required|string|max:255',
            'bio' => 'required|string',
            'photoUrl' => 'nullable|string',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'error' => $validator->errors()->first()
            ], 400);
        }

        $author = Author::create($request->all());

        return response()->json([
            'success' => true,
            'author' => [
                '_id' => (string) $author->id,
                'name' => $author->name,
                'bio' => $author->bio,
                'photoUrl' => $author->photo_url,
                'achievements' => $author->achievements,
                'works' => $author->works,
                'booksCount' => 0
            ]
        ], 201);
    }

    /**
     * Display the specified author.
     */
    public function show($id)
    {
        $author = Author::find($id);

        if (!$author) {
            return response()->json([
                'success' => false,
                'error' => 'লেখক পাওয়া যায়নি'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'author' => [
                '_id' => (string) $author->id,
                'name' => $author->name,
                'bio' => $author->bio,
                'photoUrl' => $author->photo_url,
                'achievements' => $author->achievements,
                'works' => $author->works,
                'booksCount' => $author->books_count ?: $author->books()->count()
            ]
        ], 200);
    }

    /**
     * Update the specified author.
     */
    public function update(Request $request, $id)
    {
        $author = Author::find($id);

        if (!$author) {
            return response()->json([
                'success' => false,
                'error' => 'লেখক পাওয়া যায়নি'
            ], 404);
        }

        $author->update($request->all());

        return response()->json([
            'success' => true,
            'author' => [
                '_id' => (string) $author->id,
                'name' => $author->name,
                'bio' => $author->bio,
                'photoUrl' => $author->photo_url,
                'achievements' => $author->achievements,
                'works' => $author->works,
                'booksCount' => $author->books_count ?: $author->books()->count()
            ]
        ], 200);
    }

    /**
     * Remove the specified author.
     */
    public function destroy($id)
    {
        $author = Author::find($id);

        if (!$author) {
            return response()->json([
                'success' => false,
                'error' => 'লেখক পাওয়া যায়নি'
            ], 404);
        }

        $author->delete();

        return response()->json([
            'success' => true,
            'message' => 'লেখক সফলভাবে মুছে ফেলা হয়েছে'
        ], 200);
    }
}
