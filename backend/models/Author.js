const mongoose = require('mongoose');

const AuthorSchema = new mongoose.Schema({
  name: {
    type: String,
    required: [true, 'Please add the author name'],
    trim: true,
  },
  bio: {
    type: String,
    required: [true, 'Please add a biography'],
  },
  photoUrl: {
    type: String,
    default: 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150',
  },
  achievements: {
    type: String,
    default: '',
  },
  works: {
    type: String,
    default: '',
  },
  booksCount: {
    type: Number,
    default: 0,
  },
  createdAt: {
    type: Date,
    default: Date.now,
  },
});

module.exports = mongoose.model('Author', AuthorSchema);
