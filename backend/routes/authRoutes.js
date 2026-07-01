const express = require('express');
const {
  register,
  login,
  getProfile,
  updateProfile,
  enrollInCourse,
  purchaseBook,
  toggleSaveBlog,
} = require('../controllers/authController');
const { protect } = require('../middleware/auth');

const router = express.Router();

router.post('/register', register);
router.post('/login', login);
router.get('/profile', protect, getProfile);
router.put('/profile', protect, updateProfile);
router.post('/enroll/:courseId', protect, enrollInCourse);
router.post('/purchase/:bookId', protect, purchaseBook);
router.post('/save-blog/:blogId', protect, toggleSaveBlog);

module.exports = router;
