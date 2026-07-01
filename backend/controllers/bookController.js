const Book = require('../models/Book');
const Author = require('../models/Author');

// @desc    Get all books
// @route   GET /api/books
// @access  Public
exports.getBooks = async (req, res) => {
  try {
    const books = await Book.find().populate('author', 'name photoUrl');
    res.status(200).json({ success: true, count: books.length, books });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// @desc    Get single book
// @route   GET /api/books/:id
// @access  Public
exports.getBook = async (req, res) => {
  try {
    const book = await Book.findById(req.params.id).populate('author', 'name bio photoUrl achievements works');
    if (!book) {
      return res.status(404).json({ success: false, error: 'Book not found' });
    }
    res.status(200).json({ success: true, book });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// @desc    Create new book
// @route   POST /api/books
// @access  Private/Admin
exports.createBook = async (req, res) => {
  try {
    const { author: authorId } = req.body;

    // Verify Author exists
    const author = await Author.findById(authorId);
    if (!author) {
      return res.status(404).json({ success: false, error: 'Author not found' });
    }

    // Set authorName automatically
    req.body.authorName = author.name;

    const book = await Book.create(req.body);

    // Increment author booksCount
    author.booksCount += 1;
    await author.save();

    res.status(201).json({ success: true, book });
  } catch (error) {
    res.status(400).json({ success: false, error: error.message });
  }
};

// @desc    Update book
// @route   PUT /api/books/:id
// @access  Private/Admin
exports.updateBook = async (req, res) => {
  try {
    let book = await Book.findById(req.params.id);
    if (!book) {
      return res.status(404).json({ success: false, error: 'Book not found' });
    }

    // If author is updated, verify author and update authorName
    if (req.body.author && req.body.author !== book.author.toString()) {
      const author = await Author.findById(req.body.author);
      if (!author) {
        return res.status(404).json({ success: false, error: 'Author not found' });
      }
      req.body.authorName = author.name;

      // Adjust booksCounts
      await Author.findByIdAndUpdate(book.author, { $inc: { booksCount: -1 } });
      await Author.findByIdAndUpdate(req.body.author, { $inc: { booksCount: 1 } });
    }

    book = await Book.findByIdAndUpdate(req.params.id, req.body, {
      new: true,
      runValidators: true,
    });

    res.status(200).json({ success: true, book });
  } catch (error) {
    res.status(400).json({ success: false, error: error.message });
  }
};

// @desc    Delete book
// @route   DELETE /api/books/:id
// @access  Private/Admin
exports.deleteBook = async (req, res) => {
  try {
    const book = await Book.findById(req.params.id);
    if (!book) {
      return res.status(404).json({ success: false, error: 'Book not found' });
    }

    // Decrement author booksCount
    await Author.findByIdAndUpdate(book.author, { $inc: { booksCount: -1 } });

    await book.deleteOne();

    res.status(200).json({ success: true, message: 'Book deleted successfully' });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};
