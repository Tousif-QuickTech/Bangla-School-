const express = require('express');
const cors = require('cors');
const dotenv = require('dotenv');
const connectDB = require('./config/db');

// Load environment variables
dotenv.config();

// Connect to MongoDB
connectDB();

const app = express();

// Body parser
app.use(express.json());

// Enable CORS
app.use(cors());

// Simple logging middleware
app.use((req, res, next) => {
  console.log(`${req.method} ${req.originalUrl}`);
  next();
});

// Mount routes
app.use('/api/auth', require('./routes/authRoutes'));
app.use('/api/books', require('./routes/bookRoutes'));
app.use('/api/courses', require('./routes/courseRoutes'));
app.use('/api/authors', require('./routes/authorRoutes'));
app.use('/api/blogs', require('./routes/blogRoutes'));
app.use('/api/notifications', require('./routes/notificationRoutes'));
app.use('/api/banners', require('./routes/bannerRoutes'));

// Welcome/Root Route
app.get('/', (req, res) => {
  res.json({
    message: 'Welcome to Bangla School API Server',
    status: 'Running',
    documentation: 'Refer to /backend/README.md for usage and endpoints detail.',
  });
});

// Error handling middleware
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(err.status || 500).json({
    success: false,
    error: err.message || 'Server Error',
  });
});

const PORT = process.env.PORT || 5000;

const server = app.listen(PORT, () => {
  console.log(`Server running in development mode on port ${PORT}`);
});

// Handle unhandled promise rejections
process.on('unhandledRejection', (err, promise) => {
  console.log(`Error: ${err.message}`);
  // Close server & exit process
  server.close(() => process.exit(1));
});
