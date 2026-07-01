<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Book;
use App\Models\Author;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class BookController extends Controller
{
    /**
     * Search for books by title, author name, or ISBN.
     */
    public function search(Request $request)
    {
        $query = $request->query('q');

        if (!$query) {
            return response()->json([
                'success' => false,
                'error' => 'অনুগ্রহ করে সার্চ কিওয়ার্ড প্রদান করুন'
            ], 400);
        }

        $books = Book::where('title', 'LIKE', "%{$query}%")
            ->orWhere('author_name', 'LIKE', "%{$query}%")
            ->orWhere('isbn', 'LIKE', "%{$query}%")
            ->get()
            ->map(function ($book) {
                return [
                    '_id' => (string) $book->id,
                    'title' => $book->title,
                    'author' => (string) $book->author_id,
                    'authorName' => $book->author_name,
                    'description' => $book->description,
                    'price' => $book->price,
                    'rating' => $book->rating,
                    'reviewsCount' => $book->reviews_count,
                    'isBestSeller' => $book->is_best_seller,
                    'isEbook' => $book->is_ebook,
                    'coverUrl' => $book->cover_url,
                    'publishYear' => $book->publish_year,
                    'pages' => $book->pages,
                    'isbn' => $book->isbn,
                ];
            });

        return response()->json([
            'success' => true,
            'count' => $books->count(),
            'books' => $books
        ], 200);
    }

    /**
     * Display a listing of the books.
     */
    public function index()
    {
        $books = Book::all()->map(function ($book) {
            return [
                '_id' => (string) $book->id,
                'title' => $book->title,
                'author' => (string) $book->author_id,
                'authorName' => $book->author_name,
                'description' => $book->description,
                'price' => $book->price,
                'rating' => $book->rating,
                'reviewsCount' => $book->reviews_count,
                'isBestSeller' => $book->is_best_seller,
                'isEbook' => $book->is_ebook,
                'coverUrl' => $book->cover_url,
                'publishYear' => $book->publish_year,
                'pages' => $book->pages,
                'isbn' => $book->isbn,
            ];
        });

        return response()->json([
            'success' => true,
            'count' => $books->count(),
            'books' => $books
        ], 200);
    }

    /**
     * Store a newly created book.
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'title' => 'required|string|max:255',
            'author_id' => 'required|integer',
            'description' => 'required|string',
            'price' => 'required|numeric',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'error' => $validator->errors()->first()
            ], 400);
        }

        $author = Author::find($request->author_id);
        if (!$author) {
            return response()->json([
                'success' => false,
                'error' => 'নির্বাচিত লেখক পাওয়া যায়নি'
            ], 400);
        }

        $bookData = $request->all();
        $bookData['author_name'] = $author->name;
        $book = Book::create($bookData);

        // Update books count in author
        $author->increment('books_count');

        return response()->json([
            'success' => true,
            'book' => [
                '_id' => (string) $book->id,
                'title' => $book->title,
                'author' => (string) $book->author_id,
                'authorName' => $book->author_name,
                'description' => $book->description,
                'price' => $book->price,
                'rating' => $book->rating,
                'reviewsCount' => $book->reviews_count,
                'isBestSeller' => $book->is_best_seller,
                'isEbook' => $book->is_ebook,
                'coverUrl' => $book->cover_url,
                'publishYear' => $book->publish_year,
                'pages' => $book->pages,
                'isbn' => $book->isbn,
            ]
        ], 201);
    }

    /**
     * Display the specified book.
     */
    public function show($id)
    {
        $book = Book::find($id);

        if (!$book) {
            return response()->json([
                'success' => false,
                'error' => 'বই পাওয়া যায়নি'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'book' => [
                '_id' => (string) $book->id,
                'title' => $book->title,
                'author' => (string) $book->author_id,
                'authorName' => $book->author_name,
                'description' => $book->description,
                'price' => $book->price,
                'rating' => $book->rating,
                'reviewsCount' => $book->reviews_count,
                'isBestSeller' => $book->is_best_seller,
                'isEbook' => $book->is_ebook,
                'coverUrl' => $book->cover_url,
                'publishYear' => $book->publish_year,
                'pages' => $book->pages,
                'isbn' => $book->isbn,
            ]
        ], 200);
    }

    /**
     * Update the specified book.
     */
    public function update(Request $request, $id)
    {
        $book = Book::find($id);

        if (!$book) {
            return response()->json([
                'success' => false,
                'error' => 'বই পাওয়া যায়নি'
            ], 404);
        }

        $book->update($request->all());

        return response()->json([
            'success' => true,
            'book' => [
                '_id' => (string) $book->id,
                'title' => $book->title,
                'author' => (string) $book->author_id,
                'authorName' => $book->author_name,
                'description' => $book->description,
                'price' => $book->price,
                'rating' => $book->rating,
                'reviewsCount' => $book->reviews_count,
                'isBestSeller' => $book->is_best_seller,
                'isEbook' => $book->is_ebook,
                'coverUrl' => $book->cover_url,
                'publishYear' => $book->publish_year,
                'pages' => $book->pages,
                'isbn' => $book->isbn,
            ]
        ], 200);
    }

    /**
     * Remove the specified book.
     */
    public function destroy($id)
    {
        $book = Book::find($id);

        if (!$book) {
            return response()->json([
                'success' => false,
                'error' => 'বই পাওয়া যায়নি'
            ], 404);
        }

        $author = Author::find($book->author_id);
        $book->delete();

        if ($author) {
            $author->decrement('books_count');
        }

        return response()->json([
            'success' => true,
            'message' => 'বই সফলভাবে মুছে ফেলা হয়েছে'
        ], 200);
    }
}
