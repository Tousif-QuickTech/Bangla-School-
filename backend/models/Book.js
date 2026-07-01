const mongoose = require('mongoose');

const BookSchema = new mongoose.Schema({
  title: {
    type: String,
    required: [true, 'Please add a book title'],
    trim: true,
  },
  author: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Author',
    required: [true, 'Please associate an Author'],
  },
  authorName: {
    type: String,
    required: [true, 'Please add the author name as string'],
  },
  description: {
    type: String,
    required: [true, 'Please add a book description'],
  },
  price: {
    type: Number,
    required: [true, 'Please add a price in BDT'],
    default: 0.0,
  },
  rating: {
    type: Number,
    min: 0,
    max: 5,
    default: 4.5,
  },
  reviewsCount: {
    type: Number,
    default: 0,
  },
  isBestSeller: {
    type: Boolean,
    default: false,
  },
  isEbook: {
    type: Boolean,
    default: true,
  },
  coverUrl: {
    type: String,
    default: 'https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=200',
  },
  publishYear: {
    type: Number,
    required: [true, 'Please add the publish year'],
  },
  pages: {
    type: Number,
    required: [true, 'Please add the number of pages'],
  },
  createdAt: {
    type: Date,
    default: Date.now,
  },
});

module.exports = mongoose.model('Book', BookSchema);
