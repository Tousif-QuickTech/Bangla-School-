const Author = require('../models/Author');

// @desc    Get all authors
// @route   GET /api/authors
// @access  Public
exports.getAuthors = async (req, res) => {
  try {
    const authors = await Author.find();
    res.status(200).json({ success: true, count: authors.length, authors });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// @desc    Get single author
// @route   GET /api/authors/:id
// @access  Public
exports.getAuthor = async (req, res) => {
  try {
    const author = await Author.findById(req.params.id);
    if (!author) {
      return res.status(404).json({ success: false, error: 'Author not found' });
    }
    res.status(200).json({ success: true, author });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// @desc    Create new author
// @route   POST /api/authors
// @access  Private/Admin
exports.createAuthor = async (req, res) => {
  try {
    const author = await Author.create(req.body);
    res.status(201).json({ success: true, author });
  } catch (error) {
    res.status(400).json({ success: false, error: error.message });
  }
};

// @desc    Update author
// @route   PUT /api/authors/:id
// @access  Private/Admin
exports.updateAuthor = async (req, res) => {
  try {
    const author = await Author.findByIdAndUpdate(req.params.id, req.body, {
      new: true,
      runValidators: true,
    });
    if (!author) {
      return res.status(404).json({ success: false, error: 'Author not found' });
    }
    res.status(200).json({ success: true, author });
  } catch (error) {
    res.status(400).json({ success: false, error: error.message });
  }
};

// @desc    Delete author
// @route   DELETE /api/authors/:id
// @access  Private/Admin
exports.deleteAuthor = async (req, res) => {
  try {
    const author = await Author.findById(req.params.id);
    if (!author) {
      return res.status(404).json({ success: false, error: 'Author not found' });
    }
    await author.deleteOne();
    res.status(200).json({ success: true, message: 'Author deleted successfully' });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};
