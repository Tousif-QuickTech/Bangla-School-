const mongoose = require('mongoose');

const BannerSchema = new mongoose.Schema({
  text: {
    type: String,
    required: [true, 'Please add the banner text message'],
  },
  title: {
    type: String,
    required: [true, 'Please add a banner title'],
  },
  subtitle: {
    type: String,
    default: '',
  },
  imageUrl: {
    type: String,
    required: [true, 'Please specify a background hero image URL'],
  },
  createdAt: {
    type: Date,
    default: Date.now,
  },
});

module.exports = mongoose.model('Banner', BannerSchema);
