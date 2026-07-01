<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Notification;
use Illuminate\Http\Request;

class NotificationController extends Controller
{
    /**
     * Get list of all system notifications
     */
    public function index()
    {
        $notifications = Notification::orderBy('created_at', 'desc')->get()->map(function ($notif) {
            return [
                '_id' => (string) $notif->id,
                'title' => $notif->title,
                'message' => $notif->message,
                'isRead' => $notif->is_read,
                'createdAt' => $notif->created_at ? $notif->created_at->toISOString() : null,
            ];
        });

        return response()->json([
            'success' => true,
            'notifications' => $notifications
        ], 200);
    }

    /**
     * Store new notification (used by admin panel)
     */
    public function store(Request $request)
    {
        $request->validate([
            'title' => 'required|string',
            'message' => 'required|string'
        ]);

        $notification = Notification::create([
            'title' => $request->title,
            'message' => $request->message,
            'is_read' => false
        ]);

        return response()->json([
            'success' => true,
            'notification' => [
                '_id' => (string) $notification->id,
                'title' => $notification->title,
                'message' => $notification->message,
                'isRead' => $notification->is_read,
                'createdAt' => $notification->created_at ? $notification->created_at->toISOString() : null
            ]
        ], 201);
    }
}
