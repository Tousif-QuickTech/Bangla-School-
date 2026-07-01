class Book {
  final String id;
  final String title;
  final String authorId;
  final String authorName;
  final String description;
  final double price;
  final double rating;
  final int reviewsCount;
  final bool isBestSeller;
  final bool isEbook;
  final String coverUrl;
  final int publishYear;
  final int pages;

  Book({
    required this.id,
    required this.title,
    required this.authorId,
    required this.authorName,
    required this.description,
    required this.price,
    required this.rating,
    required this.reviewsCount,
    required this.isBestSeller,
    required this.isEbook,
    required this.coverUrl,
    required this.publishYear,
    required this.pages,
  });
}

class Course {
  final String id;
  final String title;
  final String instructor;
  final String description;
  final double price;
  final double rating;
  final String duration;
  final int lecturesCount;
  final bool isBestSeller;
  final int enrolledCount;
  final String coverUrl;
  final String category;

  Course({
    required this.id,
    required this.title,
    required this.instructor,
    required this.description,
    required this.price,
    required this.rating,
    required this.duration,
    required this.lecturesCount,
    required this.isBestSeller,
    required this.enrolledCount,
    required this.coverUrl,
    required this.category,
  });
}

class Author {
  final String id;
  final String name;
  final String bio;
  final String photoUrl;
  final String achievements;
  final String works;
  final int booksCount;

  Author({
    required this.id,
    required this.name,
    required this.bio,
    required this.photoUrl,
    required this.achievements,
    required this.works,
    required this.booksCount,
  });
}

class UserProfile {
  String username;
  String email;
  bool isLoggedIn;
  String bio;
  String profilePhotoUrl;
  List<String> enrolledCourses;
  List<String> purchasedBooks;
  List<String> savedBlogs;
  bool notificationPref;
  String joinedDate;

  UserProfile({
    required this.username,
    required this.email,
    required this.isLoggedIn,
    required this.bio,
    required this.profilePhotoUrl,
    required this.enrolledCourses,
    required this.purchasedBooks,
    required this.savedBlogs,
    required this.notificationPref,
    required this.joinedDate,
  });
}

class Blog {
  final String id;
  final String title;
  final String author;
  final String date;
  final String readTime;
  final String content;
  final String category;
  int likes;

  Blog({
    required this.id,
    required this.title,
    required this.author,
    required this.date,
    required this.readTime,
    required this.content,
    required this.category,
    required this.likes,
  });
}

class NotificationItem {
  final String id;
  final String title;
  final String message;
  final String time;

  NotificationItem({
    required this.id,
    required this.title,
    required this.message,
    required this.time,
  });
}
