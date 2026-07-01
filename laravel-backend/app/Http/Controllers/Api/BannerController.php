<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Banner;
use Illuminate\Http\Request;

class BannerController extends Controller
{
    /**
     * Get list of active promotional banners
     */
    public function index()
    {
        $banners = Banner::where('is_active', true)->get()->map(function ($banner) {
            return [
                '_id' => (string) $banner->id,
                'text' => $banner->text,
                'imageUrl' => $banner->image_url,
                'isActive' => $banner->is_active,
            ];
        });

        return response()->json([
            'success' => true,
            'banners' => $banners
        ], 200);
    }

    /**
     * Store new promotional banner
     */
    public function store(Request $request)
    {
        $request->validate([
            'text' => 'required|string',
        ]);

        $banner = Banner::create([
            'text' => $request->text,
            'image_url' => $request->image_url,
            'is_active' => $request->has('is_active') ? $request->is_active : true
        ]);

        return response()->json([
            'success' => true,
            'banner' => [
                '_id' => (string) $banner->id,
                'text' => $banner->text,
                'imageUrl' => $banner->image_url,
                'isActive' => $banner->is_active,
            ]
        ], 201);
    }
}
