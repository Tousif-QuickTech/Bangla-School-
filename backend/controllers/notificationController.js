const Notification = require('../models/Notification');

// @desc    Get all notifications (broadcasts + personalized)
// @route   GET /api/notifications
// @access  Public/Private
exports.getNotifications = async (req, res) => {
  try {
    let query = { user: null }; // Start with broadcast notifications

    // If logged in, fetch broadcast notifications AND user-specific notifications
    if (req.headers.authorization) {
      const jwt = require('jsonwebtoken');
      const User = require('../models/User');
      try {
        const token = req.headers.authorization.split(' ')[1];
        const decoded = jwt.verify(token, process.env.JWT_SECRET);
        query = {
          $or: [{ user: null }, { user: decoded.id }],
        };
      } catch (err) {
        // Fallback to only public notifications if token is invalid
      }
    }

    const notifications = await Notification.find(query).sort({ createdAt: -1 });
    res.status(200).json({ success: true, count: notifications.length, notifications });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// @desc    Create new notification
// @route   POST /api/notifications
// @access  Private/Admin
exports.createNotification = async (req, res) => {
  try {
    const notification = await Notification.create(req.body);
    res.status(201).json({ success: true, notification });
  } catch (error) {
    res.status(400).json({ success: false, error: error.message });
  }
};
