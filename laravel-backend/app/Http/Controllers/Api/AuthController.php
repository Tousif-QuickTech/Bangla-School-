<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;

class AuthController extends Controller
{
    /**
     * Register a new user
     */
    public function register(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'username' => 'required|string|max:255',
            'email' => 'required|string|email|max:255|unique:users',
            'password' => 'required|string|min:6',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'error' => $validator->errors()->first()
            ], 400);
        }

        $user = User::create([
            'username' => $request->username,
            'email' => $request->email,
            'password' => Hash::make($request->password),
            'role' => 'user',
        ]);

        $token = $user->createToken('auth_token')->plainTextToken;

        return response()->json([
            'success' => true,
            'token' => $token,
            'user' => [
                '_id' => (string) $user->id,
                'username' => $user->username,
                'email' => $user->email,
                'role' => $user->role,
                'bio' => $user->bio,
                'profilePhotoUrl' => $user->profile_photo_url,
                'enrolledCourses' => $user->enrolledCourses()->pluck('courses.id')->map(fn($id) => (string)$id),
                'purchasedBooks' => $user->purchasedBooks()->pluck('books.id')->map(fn($id) => (string)$id),
                'savedBlogs' => $user->savedBlogs()->pluck('blogs.id')->map(fn($id) => (string)$id),
            ]
        ], 201);
    }

    /**
     * Login user and return token
     */
    public function login(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'email' => 'required|string|email',
            'password' => 'required|string',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'error' => $validator->errors()->first()
            ], 400);
        }

        $user = User::where('email', $request->email)->first();

        if (!$user || !Hash::check($request->password, $user->password)) {
            return response()->json([
                'success' => false,
                'error' => 'ভুল ইমেইল বা পাসওয়ার্ড'
            ], 401);
        }

        $token = $user->createToken('auth_token')->plainTextToken;

        return response()->json([
            'success' => true,
            'token' => $token,
            'user' => [
                '_id' => (string) $user->id,
                'username' => $user->username,
                'email' => $user->email,
                'role' => $user->role,
                'bio' => $user->bio,
                'profilePhotoUrl' => $user->profile_photo_url,
                'enrolledCourses' => $user->enrolledCourses()->pluck('courses.id')->map(fn($id) => (string)$id),
                'purchasedBooks' => $user->purchasedBooks()->pluck('books.id')->map(fn($id) => (string)$id),
                'savedBlogs' => $user->savedBlogs()->pluck('blogs.id')->map(fn($id) => (string)$id),
            ]
        ], 200);
    }

    /**
     * Get user profile details
     */
    public function getProfile(Request $request)
    {
        $user = $request->user();

        return response()->json([
            'success' => true,
            'user' => [
                '_id' => (string) $user->id,
                'username' => $user->username,
                'email' => $user->email,
                'role' => $user->role,
                'bio' => $user->bio,
                'profilePhotoUrl' => $user->profile_photo_url,
                'enrolledCourses' => $user->enrolledCourses()->pluck('courses.id')->map(fn($id) => (string)$id),
                'purchasedBooks' => $user->purchasedBooks()->pluck('books.id')->map(fn($id) => (string)$id),
                'savedBlogs' => $user->savedBlogs()->pluck('blogs.id')->map(fn($id) => (string)$id),
            ]
        ], 200);
    }

    /**
     * Logout user (revoke token)
     */
    public function logout(Request $request)
    {
        $request->user()->currentAccessToken()->delete();

        return response()->json([
            'success' => true,
            'message' => 'সফলভাবে লগআউট করা হয়েছে'
        ], 200);
    }
}
