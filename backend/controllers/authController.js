const User = require('../models/User');
const Course = require('../models/Course');
const Book = require('../models/Book');
const Blog = require('../models/Blog');
const jwt = require('jsonwebtoken');

// Helper to sign JWT Token
const getSignedToken = (id) => {
  return jwt.sign({ id }, process.env.JWT_SECRET, {
    expiresIn: process.env.JWT_EXPIRE,
  });
};

// @desc    Register user
// @route   POST /api/auth/register
// @access  Public
exports.register = async (req, res) => {
  try {
    const { username, email, password } = req.body;

    // Check if user exists
    const userExists = await User.findOne({ email });
    if (userExists) {
      return res.status(400).json({ success: false, error: 'Email already registered' });
    }

    // Create user
    const user = await User.create({
      username,
      email,
      password,
    });

    // Create token
    const token = getSignedToken(user._id);

    res.status(201).json({
      success: true,
      token,
      user: {
        id: user._id,
        username: user.username,
        email: user.email,
        role: user.role,
        bio: user.bio,
        profilePhotoUrl: user.profilePhotoUrl,
        enrolledCourses: user.enrolledCourses,
        purchasedBooks: user.purchasedBooks,
        savedBlogs: user.savedBlogs,
        notificationPref: user.notificationPref,
      },
    });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// @desc    Login user
// @route   POST /api/auth/login
// @access  Public
exports.login = async (req, res) => {
  try {
    const { email, password } = req.body;

    // Validate email & password
    if (!email || !password) {
      return res.status(400).json({ success: false, error: 'Please provide email and password' });
    }

    // Check for user
    const user = await User.findOne({ email }).select('+password');
    if (!user) {
      return res.status(401).json({ success: false, error: 'Invalid credentials' });
    }

    // Check if password matches
    const isMatch = await user.matchPassword(password);
    if (!isMatch) {
      return res.status(401).json({ success: false, error: 'Invalid credentials' });
    }

    // Create token
    const token = getSignedToken(user._id);

    res.status(200).json({
      success: true,
      token,
      user: {
        id: user._id,
        username: user.username,
        email: user.email,
        role: user.role,
        bio: user.bio,
        profilePhotoUrl: user.profilePhotoUrl,
        enrolledCourses: user.enrolledCourses,
        purchasedBooks: user.purchasedBooks,
        savedBlogs: user.savedBlogs,
        notificationPref: user.notificationPref,
      },
    });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// @desc    Get current logged in user profile
// @route   GET /api/auth/profile
// @access  Private
exports.getProfile = async (req, res) => {
  try {
    const user = await User.findById(req.user.id)
      .populate('enrolledCourses')
      .populate('purchasedBooks')
      .populate('savedBlogs');

    res.status(200).json({
      success: true,
      user,
    });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// @desc    Update user profile settings
// @route   PUT /api/auth/profile
// @access  Private
exports.updateProfile = async (req, res) => {
  try {
    const fieldsToUpdate = {
      username: req.body.username,
      bio: req.body.bio,
      profilePhotoUrl: req.body.profilePhotoUrl,
      notificationPref: req.body.notificationPref,
    };

    // Remove undefined fields
    Object.keys(fieldsToUpdate).forEach(
      (key) => fieldsToUpdate[key] === undefined && delete fieldsToUpdate[key]
    );

    const user = await User.findByIdAndUpdate(req.user.id, fieldsToUpdate, {
      new: true,
      runValidators: true,
    });

    res.status(200).json({
      success: true,
      user,
    });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// @desc    Enroll user in a course
// @route   POST /api/auth/enroll/:courseId
// @access  Private
exports.enrollInCourse = async (req, res) => {
  try {
    const courseId = req.params.courseId;

    // Check if course exists
    const course = await Course.findById(courseId);
    if (!course) {
      return res.status(404).json({ success: false, error: 'Course not found' });
    }

    const user = await User.findById(req.user.id);

    // Check if already enrolled
    if (user.enrolledCourses.includes(courseId)) {
      return res.status(400).json({ success: false, error: 'Already enrolled in this course' });
    }

    user.enrolledCourses.push(courseId);
    await user.save();

    // Increment enrolledCount in Course
    course.enrolledCount += 1;
    await course.save();

    res.status(200).json({
      success: true,
      message: 'Successfully enrolled in course',
      enrolledCourses: user.enrolledCourses,
    });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// @desc    Purchase an e-book
// @route   POST /api/auth/purchase/:bookId
// @access  Private
exports.purchaseBook = async (req, res) => {
  try {
    const bookId = req.params.bookId;

    // Check if book exists
    const book = await Book.findById(bookId);
    if (!book) {
      return res.status(404).json({ success: false, error: 'Book not found' });
    }

    const user = await User.findById(req.user.id);

    // Check if already purchased
    if (user.purchasedBooks.includes(bookId)) {
      return res.status(400).json({ success: false, error: 'Already purchased this book' });
    }

    user.purchasedBooks.push(bookId);
    await user.save();

    res.status(200).json({
      success: true,
      message: 'Successfully purchased e-book',
      purchasedBooks: user.purchasedBooks,
    });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// @desc    Toggle bookmark/save a blog post
// @route   POST /api/auth/save-blog/:blogId
// @access  Private
exports.toggleSaveBlog = async (req, res) => {
  try {
    const blogId = req.params.blogId;

    // Check if blog exists
    const blog = await Blog.findById(blogId);
    if (!blog) {
      return res.status(404).json({ success: false, error: 'Blog post not found' });
    }

    const user = await User.findById(req.user.id);
    const index = user.savedBlogs.indexOf(blogId);

    let saved = false;
    if (index > -1) {
      // Remove bookmark
      user.savedBlogs.splice(index, 1);
    } else {
      // Add bookmark
      user.savedBlogs.push(blogId);
      saved = true;
    }

    await user.save();

    res.status(200).json({
      success: true,
      saved,
      savedBlogs: user.savedBlogs,
    });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};
