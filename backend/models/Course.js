const mongoose = require('mongoose');

const CourseSchema = new mongoose.Schema({
  title: {
    type: String,
    required: [true, 'Please add a course title'],
    trim: true,
  },
  instructor: {
    type: String,
    required: [true, 'Please add an instructor name'],
  },
  description: {
    type: String,
    required: [true, 'Please add a course description'],
  },
  price: {
    type: Number,
    required: [true, 'Please add a course price in BDT'],
    default: 0.0,
  },
  rating: {
    type: Number,
    min: 0,
    max: 5,
    default: 4.8,
  },
  duration: {
    type: String,
    required: [true, 'Please specify course duration (e.g., 24 hours)'],
  },
  lecturesCount: {
    type: Number,
    required: [true, 'Please specify the number of lectures'],
  },
  isBestSeller: {
    type: Boolean,
    default: false,
  },
  enrolledCount: {
    type: Number,
    default: 0,
  },
  coverUrl: {
    type: String,
    default: 'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=200',
  },
  category: {
    type: String,
    required: [true, 'Please specify the course category'],
  },
  createdAt: {
    type: Date,
    default: Date.now,
  },
});

module.exports = mongoose.model('Course', CourseSchema);
