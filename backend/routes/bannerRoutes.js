const express = require('express');
const {
  getBanners,
  createBanner,
} = require('../controllers/bannerController');
const { protect, authorize } = require('../middleware/auth');

const router = express.Router();

router
  .route('/')
  .get(getBanners)
  .post(protect, authorize('admin'), createBanner);

module.exports = router;
