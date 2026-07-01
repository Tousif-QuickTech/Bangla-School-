# 🇧🇩 Bangla School - Laravel & MySQL API Backend

This is the production-ready, relational database backend for the **Bangla School** mobile application (supporting both Android Jetpack Compose and Flutter). It is rewritten from Node.js/Express to **Laravel 10** using a **MySQL** relational database schema, delivering scalable, standard REST APIs for users, authors, books, courses, blogs, banners, and notifications.

---

## 🛠️ Tech Stack & Architecture

- **Framework**: Laravel 10 (PHP 8.1+)
- **Database**: MySQL (relational schemas with foreign keys & cascade constraints)
- **Authentication**: Laravel Sanctum (Token-based API auth)
- **Data Hydration**: Full Database Seeding containing complete, rich Bengali literature, courses, and blog data.

---

## 📁 Project Structure

```bash
laravel-backend/
├── app/
│   ├── Http/Controllers/Api/       # REST API Controllers (Auth, Books, Courses, etc.)
│   └── Models/                     # Eloquent Models with Relational Mappings
├── database/
│   ├── migrations/                 # MySQL Schema Definitions with Foreign Keys
│   └── seeders/                    # Complete Bengali content database seed
├── routes/
│   └── api.php                     # Route definitions for all endpoints
├── .env.example                    # Template environment file
└── composer.json                   # PHP Dependencies listing
```

---

## 🚀 Step-by-Step Installation & Setup

Follow these simple instructions to launch the backend locally and connect it with your MySQL server:

### 1. Prerequisites
Ensure you have the following installed on your machine:
- **PHP 8.1** or newer
- **Composer** (PHP Package Manager)
- **MySQL Server** (local or remote)

### 2. Install PHP Dependencies
Navigate into the `laravel-backend` directory and run composer installation:
```bash
cd laravel-backend
composer install
```

### 3. Setup Your Environment File
Duplicate the `.env.example` file and rename it to `.env`:
```bash
cp .env.example .env
```

### 4. Create the MySQL Database
Log into your MySQL client (e.g., phpMyAdmin, TablePlus, or command line) and create a database named `bangla_school`:
```sql
CREATE DATABASE bangla_school CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 5. Configure Database in `.env`
Open the `.env` file and input your local MySQL database configurations:
```env
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=bangla_school
DB_USERNAME=root
DB_PASSWORD=your_mysql_password_here
```

### 6. Run Migrations & Seed Data
Build the tables and automatically seed them with rich, complete Bengali content (authors, books, courses, blogs, and users):
```bash
php artisan migrate --seed
```

### 7. Generate App Security Key
Generate the Laravel app cryptographic key:
```bash
php artisan key:generate
```

### 8. Run the Server
Launch the development server on port `5000` (which is configured in your Android Retrofit client):
```bash
php artisan serve --host=0.0.0.0 --port=5000
```
Your server will now be live at: **`http://localhost:5000`**

---

## 🧪 Seeding Details (Pre-configured Users)

Once seeded, you can test the APIs and login using these pre-configured user credentials:

| Role | Email | Password | Details |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin@banglaschool.com` | `password123` | Full access to create/update resources. |
| **Regular User** | `user@banglaschool.com` | `password123` | Comes pre-registered with courses, books, and saved blogs. |

---

## 📡 API Endpoints Documentation

All routes are prefixed with `/api`. Below are the active endpoints:

### 🔐 Authentication (`api/auth/...`)
- **`POST /api/auth/register`**: Registers a new student.
- **`POST /api/auth/login`**: Authenticates and returns a secure Sanctum Token.
- **`GET /api/auth/profile`** *(Protected)*: Returns active user details, enrolled courses, and purchased books.
- **`POST /api/auth/logout`** *(Protected)*: Destroys the active authentication token.

### ✍️ Authors (`api/authors/...`)
- **`GET /api/authors`**: Get list of all legendary Bengali authors.
- **`GET /api/authors/{id}`**: Get specific author biography and works.
- **`POST /api/authors`**: Add a new author (Admin only).
- **`PUT /api/authors/{id}`**: Update biography details.
- **`DELETE /api/authors/{id}`**: Remove an author.

### 📚 Books (`api/books/...`)
- **`GET /api/books`**: List all available books and prices.
- **`GET /api/books/{id}`**: Get detailed book review, description, and pages.
- **`POST /api/books`**: Create new book catalog item.
- **`PUT /api/books/{id}`**: Edit book price/metadata.
- **`DELETE /api/books/{id}`**: Remove book item.

### 🎓 Courses (`api/courses/...`)
- **`GET /api/courses`**: Fetch list of specialized literature, grammar, and pronunciation courses.
- **`GET /api/courses/{id}`**: Get full course details, syllabus outline, and rating.
- **`POST /api/courses`**: Publish a new course.

### 📰 Blogs & Community (`api/blogs/...`)
- **`GET /api/blogs`**: Get list of all literary essays and community blogs.
- **`POST /api/blogs/{id}/like`** *(Protected)*: Toggle liking a blog post.
- **`POST /api/blogs/{id}/save`** *(Protected)*: Toggle saving a blog post.

### 📢 Promotional Banners & Notifications
- **`GET /api/banners`**: Get list of active slideshow banners.
- **`GET /api/notifications`**: Retrieve real-time push announcements.
