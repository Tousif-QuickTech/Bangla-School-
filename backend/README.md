# Bangla School Backend API

A production-grade, highly structured Node.js / Express RESTful API server integrated with MongoDB (Mongoose) to support all mobile features of the **Bangla School** Flutter application.

## Features Built
- **Full Authentication System**: Uses JWT (JSON Web Tokens) with standard encryption (bcryptjs) for secure registration and login.
- **Relational Seeding Engine**: Includes real classical and modern Bengali literature records, academic courses, authors, essays, banners, and notifications.
- **Transactions & Actions**: Handles course enrollment, e-book purchasing, blog likes, and bookmarks.
- **Role-Based Guarding**: Protects administrative endpoints (POST/PUT/DELETE of courses and books) to only allow users with the `'admin'` role.

---

## Directory Structure
```text
/backend
├── config/
│   └── db.js                 # MongoDB connection wrapper
├── controllers/
│   ├── authController.js     # Signup, login, profile, buy book, enroll
│   ├── authorController.js   # Author directory management
│   ├── bannerController.js   # Top carousels & banners
│   ├── blogController.js     # Literary articles, liking & unliking
│   ├── bookController.js     # Book catalog CRUD
│   ├── courseController.js   # Course catalogue CRUD
│   └── notificationController.js # App notifications and updates
├── middleware/
│   └── auth.js               # JWT check & Role verification guards
├── models/
│   ├── Author.js             # Author schema
│   ├── Banner.js             # Banner slide schema
│   ├── Blog.js               # Article schema
│   ├── Book.js               # Book schema
│   ├── Course.js             # Course schema
│   ├── Notification.js       # Notification schema
│   └── User.js               # User schema, bcrypt salting & methods
├── routes/                   # Routing mounts
│   ├── authRoutes.js
│   ├── authorRoutes.js
│   ├── bannerRoutes.js
│   ├── blogRoutes.js
│   ├── bookRoutes.js
│   ├── courseRoutes.js
│   └── notificationRoutes.js
├── scripts/
│   └── seed.js               # Relational data seed script
├── .env                      # Database & Port credentials
├── .env.example              # Sample environment file
├── package.json              # Express / Mongoose dependencies
└── server.js                 # Main server entrypoint
```

---

## Prerequisites
1. **Node.js** installed on your system (v16+ recommended).
2. **MongoDB** running locally (`mongodb://localhost:27017/bangla_school`) OR a remote **MongoDB Atlas** Connection string.

---

## Setup & Running Guide

### 1. Install Dependencies
Navigate to the backend directory and run:
```bash
cd backend
npm install
```

### 2. Configure Environment Variables
A `.env` file has been created. If using MongoDB Atlas, replace the `MONGO_URI` with your connection string:
```env
PORT=5000
MONGO_URI=mongodb://localhost:27017/bangla_school
JWT_SECRET=your_super_secret_jwt_key_change_me_in_production
JWT_EXPIRE=30d
```

### 3. Seed the Database
Populate the database with high-quality classical Bengali literature, academic courses, authors, and users:
```bash
npm run seed
```
This seeds Rabindranath Tagore, Kazi Nazrul Islam, Sarat Chandra Chattopadhyay, classic books (Gitanjali, Agnibeena, Srikanta), premium courses, articles, and two test accounts:
* **Admin Account**: `admin@banglaschool.com` | Password: `password123`
* **Regular Account**: `user@banglaschool.com` | Password: `password123`

### 4. Run the Server
* **Production Mode**:
  ```bash
  npm start
  ```
* **Development Mode (Auto-restart via nodemon)**:
  ```bash
  npm run dev
  ```
The API server will listen at `http://localhost:5000`.

---

## API Documentation

### 1. Authentication & Profiles (`/api/auth`)

| Method | Endpoint | Access | Description | Request Body / Parameters |
|:---|:---|:---|:---|:---|
| **POST** | `/api/auth/register` | Public | Sign up a new user | `{ "username": "Your Name", "email": "user@example.com", "password": "password123" }` |
| **POST** | `/api/auth/login` | Public | Login and obtain JWT token | `{ "email": "user@example.com", "password": "password123" }` |
| **GET** | `/api/auth/profile` | Private | Retrieve current user profile, including enrolled courses and purchased books | Header: `Authorization: Bearer <JWT_TOKEN>` |
| **PUT** | `/api/auth/profile` | Private | Update user display profile settings | Header: `Authorization: Bearer <JWT_TOKEN>` <br> Body: `{ "username": "New Name", "bio": "New bio info...", "profilePhotoUrl": "https://url.com" }` |
| **POST** | `/api/auth/enroll/:courseId` | Private | Enroll current user in an academic course | Header: `Authorization: Bearer <JWT_TOKEN>` |
| **POST** | `/api/auth/purchase/:bookId` | Private | Purchase an e-book for current user | Header: `Authorization: Bearer <JWT_TOKEN>` |
| **POST** | `/api/auth/save-blog/:blogId` | Private | Toggle bookmark/save a blog post | Header: `Authorization: Bearer <JWT_TOKEN>` |

---

### 2. Books (`/api/books`)

| Method | Endpoint | Access | Description | Request Body / Parameters |
|:---|:---|:---|:---|:---|
| **GET** | `/api/books` | Public | Get all books in catalog | None |
| **GET** | `/api/books/:id` | Public | Get detailed details of a book | `id` parameter |
| **POST** | `/api/books` | Admin | Add a new book (Auto-maps Author Name) | Header: `Authorization: Bearer <ADMIN_JWT_TOKEN>` <br> Body: `{ "title": "...", "author": "AUTHOR_OBJECT_ID", "description": "...", "price": 250, "publishYear": 1910, "pages": 150 }` |
| **PUT** | `/api/books/:id` | Admin | Edit existing book metadata | Header: `Authorization: Bearer <ADMIN_JWT_TOKEN>` <br> Body: `{ "price": 300 }` |
| **DELETE** | `/api/books/:id` | Admin | Delete book from collection | Header: `Authorization: Bearer <ADMIN_JWT_TOKEN>` |

---

### 3. Courses (`/api/courses`)

| Method | Endpoint | Access | Description | Request Body / Parameters |
|:---|:---|:---|:---|:---|
| **GET** | `/api/courses` | Public | Get all academic/literary courses | None |
| **GET** | `/api/courses/:id` | Public | Get detailed syllabus of a course | `id` parameter |
| **POST** | `/api/courses` | Admin | Publish a new learning course | Header: `Authorization: Bearer <ADMIN_JWT_TOKEN>` <br> Body: `{ "title": "...", "instructor": "...", "description": "...", "price": 950, "duration": "10 Hours", "lecturesCount": 8, "category": "Grammar" }` |
| **PUT** | `/api/courses/:id` | Admin | Edit course details | Header: `Authorization: Bearer <ADMIN_JWT_TOKEN>` |
| **DELETE** | `/api/courses/:id` | Admin | Remove course from listing | Header: `Authorization: Bearer <ADMIN_JWT_TOKEN>` |

---

### 4. Authors (`/api/authors`)

| Method | Endpoint | Access | Description | Request Body / Parameters |
|:---|:---|:---|:---|:---|
| **GET** | `/api/authors` | Public | Get all Bengali writer profiles | None |
| **GET** | `/api/authors/:id` | Public | Get detailed profile/works of an author | `id` parameter |
| **POST** | `/api/authors` | Admin | Create a writer profile | Header: `Authorization: Bearer <ADMIN_JWT_TOKEN>` <br> Body: `{ "name": "...", "bio": "...", "achievements": "..." }` |
| **PUT** | `/api/authors/:id` | Admin | Edit writer profile details | Header: `Authorization: Bearer <ADMIN_JWT_TOKEN>` |
| **DELETE** | `/api/authors/:id` | Admin | Delete writer profile | Header: `Authorization: Bearer <ADMIN_JWT_TOKEN>` |

---

### 5. Blogs & Articles (`/api/blogs`)

| Method | Endpoint | Access | Description | Request Body / Parameters |
|:---|:---|:---|:---|:---|
| **GET** | `/api/blogs` | Public | Get all literary blogs | None |
| **GET** | `/api/blogs/:id` | Public | Read full content of a blog | `id` parameter |
| **POST** | `/api/blogs` | Admin | Publish a new blog/article | Header: `Authorization: Bearer <ADMIN_JWT_TOKEN>` <br> Body: `{ "title": "...", "author": "...", "content": "...", "readTime": "5 Min", "category": "Essay", "date": "15 June, 2026" }` |
| **POST** | `/api/blogs/:id/like` | Private | Toggle Like on an article (Checks duplicate clicks) | Header: `Authorization: Bearer <JWT_TOKEN>` |
| **PUT** | `/api/blogs/:id` | Admin | Edit blog content | Header: `Authorization: Bearer <ADMIN_JWT_TOKEN>` |
| **DELETE** | `/api/blogs/:id` | Admin | Delete blog post | Header: `Authorization: Bearer <ADMIN_JWT_TOKEN>` |

---

### 6. Dynamic Hero Banners & Notifications (`/api/banners` & `/api/notifications`)

| Method | Endpoint | Access | Description | Request Body / Parameters |
|:---|:---|:---|:---|:---|
| **GET** | `/api/banners` | Public | Get active top-screen slideshow banners | None |
| **POST** | `/api/banners` | Admin | Create a new top slideshow banner | Header: `Authorization: Bearer <ADMIN_JWT_TOKEN>` <br> Body: `{ "title": "...", "text": "...", "imageUrl": "..." }` |
| **GET** | `/api/notifications`| Public/Private| Get announcement notifications (Includes personalized if token provided) | Optional Header: `Authorization: Bearer <JWT_TOKEN>` |
| **POST** | `/api/notifications`| Admin | Post a broadcast system notification | Header: `Authorization: Bearer <ADMIN_JWT_TOKEN>` <br> Body: `{ "title": "...", "message": "...", "time": "Just now" }` |
