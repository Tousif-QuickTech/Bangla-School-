const mongoose = require('mongoose');

const NotificationSchema = new mongoose.Schema({
  title: {
    type: String,
    required: [true, 'Please add a notification title'],
  },
  message: {
    type: String,
    required: [true, 'Please add a notification message'],
  },
  time: {
    type: String,
    required: [true, 'Please specify the relative time display string'],
  },
  user: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    default: null, // null means it is a broadcast notification for all users
  },
  createdAt: {
    type: Date,
    default: Date.now,
  },
});

module.exports = mongoose.model('Notification', NotificationSchema);
