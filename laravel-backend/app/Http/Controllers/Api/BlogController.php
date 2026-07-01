<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Blog;
use App\Models\Comment;
use App\Models\CommentReply;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class BlogController extends Controller
{
    /**
     * Display a listing of the blogs.
     */
    public function index()
    {
        $blogs = Blog::with([
            'likedBy',
            'comments.user',
            'comments.likedBy',
            'comments.replies.user',
            'comments.replies.likedBy'
        ])->get()->map(function ($blog) {
            return [
                '_id' => (string) $blog->id,
                'title' => $blog->title,
                'content' => $blog->content,
                'authorName' => $blog->author_name,
                'publishDate' => $blog->publish_date,
                'coverUrl' => $blog->cover_url,
                'readingTime' => $blog->reading_time,
                'likedBy' => $blog->likedBy->pluck('id')->map(fn($id) => (string)$id),
                'comments' => $commentList = $blog->comments->map(function ($comment) {
                    return [
                        '_id' => (string) $comment->id,
                        'username' => $comment->user ? $comment->user->username : 'অজানা পাঠক',
                        'content' => $comment->content,
                        'createdAt' => $comment->created_at ? $comment->created_at->toISOString() : null,
                        'likesCount' => $comment->likedBy->count(),
                        'likedBy' => $comment->likedBy->pluck('id')->map(fn($id) => (string)$id),
                        'replies' => $comment->replies->map(function ($reply) {
                            return [
                                '_id' => (string) $reply->id,
                                'username' => $reply->user ? $reply->user->username : 'অজানা পাঠক',
                                'content' => $reply->content,
                                'createdAt' => $reply->created_at ? $reply->created_at->toISOString() : null,
                                'likesCount' => $reply->likedBy->count(),
                                'likedBy' => $reply->likedBy->pluck('id')->map(fn($id) => (string)$id),
                            ];
                        })->values()->toArray(),
                    ];
                })->values()->toArray(),
            ];
        });

        return response()->json([
            'success' => true,
            'count' => $blogs->count(),
            'blogs' => $blogs
        ], 200);
    }

    /**
     * Store a newly created blog.
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'title' => 'required|string|max:255',
            'content' => 'required|string',
            'authorName' => 'required|string',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'error' => $validator->errors()->first()
            ], 400);
        }

        $blog = Blog::create($request->all());

        return response()->json([
            'success' => true,
            'blog' => [
                '_id' => (string) $blog->id,
                'title' => $blog->title,
                'content' => $blog->content,
                'authorName' => $blog->author_name,
                'publishDate' => $blog->publish_date,
                'coverUrl' => $blog->cover_url,
                'readingTime' => $blog->reading_time,
                'likedBy' => [],
                'comments' => [],
            ]
        ], 201);
    }

    /**
     * Display the specified blog.
     */
    public function show($id)
    {
        $blog = Blog::with([
            'likedBy',
            'comments.user',
            'comments.likedBy',
            'comments.replies.user',
            'comments.replies.likedBy'
        ])->find($id);

        if (!$blog) {
            return response()->json([
                'success' => false,
                'error' => 'ব্লগ পাওয়া যায়নি'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'blog' => [
                '_id' => (string) $blog->id,
                'title' => $blog->title,
                'content' => $blog->content,
                'authorName' => $blog->author_name,
                'publishDate' => $blog->publish_date,
                'coverUrl' => $blog->cover_url,
                'readingTime' => $blog->reading_time,
                'likedBy' => $blog->likedBy->pluck('id')->map(fn($id) => (string)$id),
                'comments' => $blog->comments->map(function ($comment) {
                    return [
                        '_id' => (string) $comment->id,
                        'username' => $comment->user ? $comment->user->username : 'অজানা পাঠক',
                        'content' => $comment->content,
                        'createdAt' => $comment->created_at ? $comment->created_at->toISOString() : null,
                        'likesCount' => $comment->likedBy->count(),
                        'likedBy' => $comment->likedBy->pluck('id')->map(fn($id) => (string)$id),
                        'replies' => $comment->replies->map(function ($reply) {
                            return [
                                '_id' => (string) $reply->id,
                                'username' => $reply->user ? $reply->user->username : 'অজানা পাঠক',
                                'content' => $reply->content,
                                'createdAt' => $reply->created_at ? $reply->created_at->toISOString() : null,
                                'likesCount' => $reply->likedBy->count(),
                                'likedBy' => $reply->likedBy->pluck('id')->map(fn($id) => (string)$id),
                            ];
                        })->values()->toArray(),
                    ];
                })->values()->toArray(),
            ]
        ], 200);
    }

    /**
     * Toggle like state on a blog post
     */
    public function likeBlog(Request $request, $id)
    {
        $blog = Blog::find($id);

        if (!$blog) {
            return response()->json([
                'success' => false,
                'error' => 'ব্লগ পাওয়া যায়নি'
            ], 404);
        }

        $user = $request->user();
        if ($blog->likedBy()->where('user_id', $user->id)->exists()) {
            $blog->likedBy()->detach($user->id);
        } else {
            $blog->likedBy()->attach($user->id);
        }

        return response()->json([
            'success' => true,
            'blog' => [
                '_id' => (string) $blog->id,
                'title' => $blog->title,
                'content' => $blog->content,
                'authorName' => $blog->author_name,
                'publishDate' => $blog->publish_date,
                'coverUrl' => $blog->cover_url,
                'readingTime' => $blog->reading_time,
                'likedBy' => $blog->likedBy()->pluck('users.id')->map(fn($id) => (string)$id),
            ]
        ], 200);
    }

    /**
     * Toggle save state on a blog post
     */
    public function saveBlog(Request $request, $id)
    {
        $blog = Blog::find($id);

        if (!$blog) {
            return response()->json([
                'success' => false,
                'error' => 'ব্লগ পাওয়া যায়নি'
            ], 404);
        }

        $user = $request->user();
        if ($blog->savedBy()->where('user_id', $user->id)->exists()) {
            $blog->savedBy()->detach($user->id);
            $saved = false;
        } else {
            $blog->savedBy()->attach($user->id);
            $saved = true;
        }

        return response()->json([
            'success' => true,
            'saved' => $saved,
            'savedBlogs' => $user->savedBlogs()->pluck('blogs.id')->map(fn($uid) => (string)$uid)
        ], 200);
    }

    /**
     * Store a comment on a blog post.
     */
    public function comment(Request $request, $id)
    {
        $blog = Blog::find($id);

        if (!$blog) {
            return response()->json([
                'success' => false,
                'error' => 'ব্লগ পাওয়া যায়নি'
            ], 404);
        }

        $validator = Validator::make($request->all(), [
            'content' => 'required|string|min:1',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'error' => $validator->errors()->first()
            ], 400);
        }

        $user = $request->user();

        $comment = $blog->comments()->create([
            'user_id' => $user->id,
            'content' => $request->input('content'),
        ]);

        return response()->json([
            'success' => true,
            'comment' => [
                '_id' => (string) $comment->id,
                'username' => $user->username,
                'content' => $comment->content,
                'createdAt' => $comment->created_at ? $comment->created_at->toISOString() : null,
            ],
            'message' => 'মন্তব্যটি সফলভাবে যুক্ত হয়েছে'
        ], 201);
    }

    /**
     * Toggle like state on a comment.
     */
    public function likeComment(Request $request, $commentId)
    {
        $comment = Comment::find($commentId);

        if (!$comment) {
            return response()->json([
                'success' => false,
                'error' => 'মন্তব্য পাওয়া যায়নি'
            ], 404);
        }

        $user = $request->user();
        if ($comment->likedBy()->where('user_id', $user->id)->exists()) {
            $comment->likedBy()->detach($user->id);
            $liked = false;
        } else {
            $comment->likedBy()->attach($user->id);
            $liked = true;
        }

        return response()->json([
            'success' => true,
            'liked' => $liked,
            'likesCount' => $comment->likedBy()->count(),
            'likedBy' => $comment->likedBy()->pluck('users.id')->map(fn($id) => (string)$id)
        ], 200);
    }

    /**
     * Store a reply on a comment.
     */
    public function replyComment(Request $request, $commentId)
    {
        $comment = Comment::find($commentId);

        if (!$comment) {
            return response()->json([
                'success' => false,
                'error' => 'মন্তব্য পাওয়া যায়নি'
            ], 404);
        }

        $validator = Validator::make($request->all(), [
            'content' => 'required|string|min:1',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'error' => $validator->errors()->first()
            ], 400);
        }

        $user = $request->user();

        $reply = $comment->replies()->create([
            'user_id' => $user->id,
            'content' => $request->input('content'),
        ]);

        return response()->json([
            'success' => true,
            'reply' => [
                '_id' => (string) $reply->id,
                'username' => $user->username,
                'content' => $reply->content,
                'createdAt' => $reply->created_at ? $reply->created_at->toISOString() : null,
                'likesCount' => 0,
                'likedBy' => []
            ],
            'message' => 'প্রতিউত্তরটি সফলভাবে যুক্ত হয়েছে'
        ], 201);
    }

    /**
     * Toggle like state on a reply.
     */
    public function likeReply(Request $request, $replyId)
    {
        $reply = CommentReply::find($replyId);

        if (!$reply) {
            return response()->json([
                'success' => false,
                'error' => 'প্রতিউত্তর পাওয়া যায়নি'
            ], 404);
        }

        $user = $request->user();
        if ($reply->likedBy()->where('user_id', $user->id)->exists()) {
            $reply->likedBy()->detach($user->id);
            $liked = false;
        } else {
            $reply->likedBy()->attach($user->id);
            $liked = true;
        }

        return response()->json([
            'success' => true,
            'liked' => $liked,
            'likesCount' => $reply->likedBy()->count(),
            'likedBy' => $reply->likedBy()->pluck('users.id')->map(fn($id) => (string)$id)
        ], 200);
    }

    /**
     * Remove the specified blog.
     */
    public function destroy($id)
    {
        $blog = Blog::find($id);

        if (!$blog) {
            return response()->json([
                'success' => false,
                'error' => 'ব্লগ পাওয়া যায়নি'
            ], 404);
        }

        $blog->delete();

        return response()->json([
            'success' => true,
            'message' => 'ব্লগ সফলভাবে মুছে ফেলা হয়েছে'
        ], 200);
    }
}
