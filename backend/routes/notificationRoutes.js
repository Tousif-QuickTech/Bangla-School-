const express = require('express');
const {
  getNotifications,
  createNotification,
} = require('../controllers/notificationController');
const { protect, authorize } = require('../middleware/auth');

const router = express.Router();

router
  .route('/')
  .get(getNotifications)
  .post(protect, authorize('admin'), createNotification);

module.exports = router;
