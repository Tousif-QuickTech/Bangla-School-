const mongoose = require('mongoose');

const BlogSchema = new mongoose.Schema({
  title: {
    type: String,
    required: [true, 'Please add a blog title'],
    trim: true,
  },
  author: {
    type: String,
    required: [true, 'Please add an author name'],
  },
  date: {
    type: String,
    required: [true, 'Please specify the publication date (e.g., ১৫ মে, ২০২৬)'],
  },
  readTime: {
    type: String,
    required: [true, 'Please specify reading time'],
  },
  content: {
    type: String,
    required: [true, 'Please add blog content'],
  },
  category: {
    type: String,
    required: [true, 'Please specify blog category'],
  },
  likes: {
    type: Number,
    default: 0,
  },
  likedBy: [
    {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'User',
    },
  ],
  createdAt: {
    type: Date,
    default: Date.now,
  },
});

module.exports = mongoose.model('Blog', BlogSchema);
