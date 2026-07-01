import 'package:flutter/material.dart';
import '../models/bangla_models.dart';

enum ActiveScreen {
  home,
  books,
  bookDetails,
  courses,
  courseDetails,
  authors,
  authorDetails,
  blog,
  dashboard,
  auth,
  admin,
  ebookReader,
}

class BanglaController extends ChangeNotifier {
  // Theme State
  bool _isDarkTheme = false;
  bool get isDarkTheme => _isDarkTheme;
  set isDarkTheme(bool val) {
    _isDarkTheme = val;
    notifyListeners();
  }

  // Navigation State
  ActiveScreen _currentScreen = ActiveScreen.home;
  ActiveScreen get currentScreen => _currentScreen;

  String? _selectedItemId;
  String? get selectedItemId => _selectedItemId;

  void navigateTo(ActiveScreen screen, {String? itemId}) {
    _currentScreen = screen;
    _selectedItemId = itemId;
    notifyListeners();
  }

  // Active Bottom Bar / Drawer selection
  int _activeTab = 0; // 0: Home, 1: Courses, 2: Books, 3: Authors, 4: Blog
  int get activeTab => _activeTab;
  set activeTab(int index) {
    _activeTab = index;
    if (index == 0) _currentScreen = ActiveScreen.home;
    if (index == 1) _currentScreen = ActiveScreen.courses;
    if (index == 2) _currentScreen = ActiveScreen.books;
    if (index == 3) _currentScreen = ActiveScreen.authors;
    if (index == 4) _currentScreen = ActiveScreen.blog;
    notifyListeners();
  }

  // Mock Repository Databases
  final List<Book> _books = [];
  final List<Course> _courses = [];
  final List<Author> _authors = [];
  final List<Blog> _blogs = [];
  final List<String> _banners = [];
  final List<NotificationItem> _notifications = [];

  UserProfile _userProfile = UserProfile(
    username: "তৌসিফ আহমেদ",
    email: "tousif@gmail.com",
    isLoggedIn: true,
    bio: "বাংলা সাহিত্য ও রেনেসাঁ ইতিহাসের একজন আগ্রহী পাঠক।",
    profilePhotoUrl: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
    enrolledCourses: ["c1"],
    purchasedBooks: ["b1"],
    savedBlogs: ["bl1"],
    notificationPref: true,
    joinedDate: "৩০ জুন, ২০২৬",
  );

  List<Book> get books => List.unmodifiable(_books);
  List<Course> get courses => List.unmodifiable(_courses);
  List<Author> get authors => List.unmodifiable(_authors);
  List<Blog> get blogs => List.unmodifiable(_blogs);
  List<String> get banners => List.unmodifiable(_banners);
  List<NotificationItem> get notifications => List.unmodifiable(_notifications);
  UserProfile get userProfile => _userProfile;

  BanglaController() {
    _loadInitialData();
  }

  void _loadInitialData() {
    // Populate Banner Carousel
    _banners.addAll([
      "অগ্রিম বুকিং ছাড়! বিশ্বকবি রবীন্দ্রনাথ ঠাকুরের শ্রেষ্ঠ উপন্যাসসংগ্রহ কিনুন ১৫% ছাড়ে। কুপন: RABINDRA15",
      "নতুন ভর্তি চলছে! লালন সাঁই ও বাউল দর্শন কোর্সে আজই অংশ নিয়ে উন্মোচন করুন আধ্যাত্মিক সুধা।",
      "বাংলা ই-বুক মহোৎসব! ২৫০+ ধ্রুপদী সাহিত্যকর্ম ডাউনলোড করে অফলাইনে পড়ার অনন্য সুযোগ!",
    ]);

    // Populate Authors
    _authors.addAll([
      Author(
        id: "a1",
        name: "রবীন্দ্রনাথ ঠাকুর",
        bio: "১৮৬১ - ১৯৪১। বিশ্বকবি ও প্রথম অ-ইউরোপীয় নোবেল বিজয়ী সাহিত্যিক। গীতাঞ্জলি কাব্যের জন্য ১৯১৩ সালে তিনি সাহিত্যের নোবেল লাভ করেন। বাংলা সাহিত্যের প্রতিটি শাখায় তাঁর স্বচ্ছন্দ পদচারণা ছিল কালজয়ী।",
        photoUrl: "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150",
        achievements: "সাহিত্যে নোবেল পুরস্কার (১৯১৩), কলকাতা বিশ্ববিদ্যালয়ের সম্মানসূচক ডি.লিট ডিগ্রিসহ বিশ্বব্যাপী অসংখ্য সর্বোচ্চ সম্মাননা।",
        works: "গীতাঞ্জলি, গোরা, ঘরের বাইরে, চোখের বালি, সোনার তরী, ডাকঘর, রক্তকরবী।",
        booksCount: 15,
      ),
      Author(
        id: "a2",
        name: "কাজী নজরুল ইসলাম",
        bio: "১৮৯৯ - ১৯৭৬। বাংলাদেশের জাতীয় কবি এবং নিপীড়িতের কণ্ঠস্বর। 'বিদ্রোহী' কবিতার স্রষ্টা ও সাম্যবাদী চেতনার এক প্রদীপ্ত নক্ষত্র। তাঁর অগ্নিবীণা কাব্য ও নজরুল গীতি আজো বাঙালির প্রধান অনুপ্রেরণা।",
        photoUrl: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
        achievements: "বাংলাদেশের জাতীয় কবি খেতাব, একুশে পদক (১৯৭৬), ভারতের পদ্মভূষণ সম্মাননা।",
        works: "অগ্নিবীণা, বিষের বাঁশী, দোলন-চাঁপা, রিক্তের বেদন, বাঁধন-হারা, শিউলিমালা।",
        booksCount: 12,
      ),
      Author(
        id: "a3",
        name: "শরৎচন্দ্র চট্টোপাধ্যায়",
        bio: "১৮৭৬ - ১৯৩৮। অপরাজেয় কথাশিল্পী যিনি সাধারণ বাঙালি সমাজের যাপিত জীবনকে দরদী লেখনীতে বিশ্বের দরবারে তুলে ধরেছেন। তাঁর উপন্যাসগুলো সর্বাধিক অনূদিত ও চলচ্চিত্রায়িত হয়েছে।",
        photoUrl: "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150",
        achievements: "কলকাতা বিশ্ববিদ্যালয়ের জগততারিণী স্বর্ণপদক, সমাজ বিশ্লেষণের অনন্য স্বীকৃতি।",
        works: "দেবদাস, চরিত্রহীন, শ্রীকান্ত, গৃহদাহ, পথের দাবী, দেবদাস, পরিণীতা।",
        booksCount: 18,
      ),
    ]);

    // Populate Books
    _books.addAll([
      Book(
        id: "b1",
        title: "গীতাঞ্জলি (কাব্যগ্রন্থ)",
        authorId: "a1",
        authorName: "রবীন্দ্রনাথ ঠাকুর",
        description: "যে আধ্যাত্মিক সুধা ও গভীর কাব্যিক সৌন্দর্যের জন্য রবীন্দ্রনাথ নোবেল পুরস্কার লাভ করেন, তাঁর ১৫৭টি অমর গানের সংকলন এই গীতাঞ্জলি। ভক্তি, প্রেম ও প্রকৃতির অপূর্ব মেলবন্ধন ঘটেছে এখানে।",
        price: 180.0,
        rating: 4.9,
        reviewsCount: 312,
        isBestSeller: true,
        isEbook: true,
        coverUrl: "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=200",
        publishYear: 1910,
        pages: 168,
      ),
      Book(
        id: "b2",
        title: "অগ্নিবীণা (কাব্যগ্রন্থ)",
        authorId: "a2",
        authorName: "কাজী নজরুল ইসলাম",
        description: "কাজী নজরুল ইসলামের প্রথম ও অন্যতম সেরা কাব্যগ্রন্থ। এতে 'বিদ্রোহী', 'প্রলয়োল্লাস' ও 'কামাল পাশা'-র মতো দুর্দান্ত সব বিপ্লবধর্মী ও উদ্দীপনামূলক কবিতা অন্তর্ভুক্ত রয়েছে।",
        price: 150.0,
        rating: 4.8,
        reviewsCount: 220,
        isBestSeller: true,
        isEbook: true,
        coverUrl: "https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=200",
        publishYear: 1922,
        pages: 124,
      ),
      Book(
        id: "b3",
        title: "শ্রীকান্ত (উপন্যাস)",
        authorId: "a3",
        authorName: "শরৎচন্দ্র চট্টোপাধ্যায়",
        description: "শরৎচন্দ্র চট্টোপাধ্যায়ের একটি আত্মজৈবনিক চার খণ্ডের শ্রেষ্ঠ উপন্যাস। শ্রীকান্ত নামের এক পরিব্রাজক যুবকের ঘুরে বেড়ানো এবং বৈচিত্র্যময় গ্রামীণ বাঙালি সমাজকে ফুটিয়ে তোলার অনন্য উপাখ্যান।",
        price: 240.0,
        rating: 4.7,
        reviewsCount: 175,
        isBestSeller: false,
        isEbook: true,
        coverUrl: "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=200",
        publishYear: 1917,
        pages: 350,
      ),
    ]);

    // Populate Courses
    _courses.addAll([
      Course(
        id: "c1",
        title: "রবীন্দ্র সাহিত্য ও আধুনিক চেতনা",
        instructor: "ড. আনিসুজ্জামান",
        description: "রবীন্দ্রনাথ ঠাকুরের ছোটগল্প, উপন্যাস এবং গীতিনাট্যগুলোর অন্তর্নিহিত আধুনিক দর্শন ও বাঙালি মননে তাঁর প্রভাব বিস্তারিত পর্যালোচনা করা হবে এই ১৬ সপ্তাহের মাস্টারক্লাসে।",
        price: 999.0,
        rating: 4.9,
        duration: "২৪ ঘণ্টা (১২টি লেকচার)",
        lecturesCount: 12,
        isBestSeller: true,
        enrolledCount: 1450,
        coverUrl: "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=200",
        category: "আধুনিক সাহিত্য",
      ),
      Course(
        id: "c2",
        title: "নজরুলের সাম্যবাদ ও বাঙালি বিদ্রোহ",
        instructor: "অধ্যাপক সিরাজুল ইসলাম চৌধুরী",
        description: "নজরুলের অগ্নিঝরা সাহিত্যকর্ম, তাঁর রাজনৈতিক প্রবন্ধ এবং ঔপনিবেশিক শক্তির বিরুদ্ধে তাঁর আজীবন আপসহীন সংগ্রাম বিশ্লেষণ। সাহিত্যপ্রেমীদের জন্য একটি কালজয়ী একাডেমি কোর্স।",
        price: 850.0,
        rating: 4.7,
        duration: "১৮ ঘণ্টা (১০টি লেকচার)",
        lecturesCount: 10,
        isBestSeller: false,
        enrolledCount: 890,
        coverUrl: "https://images.unsplash.com/photo-1434030216411-0b793f4b4173?w=200",
        category: "বিদ্রোহী কবিতা",
      ),
    ]);

    // Populate Blogs
    _blogs.addAll([
      Blog(
        id: "bl1",
        title: "বাঙালি মধ্যবিত্তের বিকাশ ও বাংলা উপন্যাসের উত্থান",
        author: "রফিকুল্লাহ হাসান",
        date: "১৫ মে, ২০২৬",
        readTime: "৮ মিনিট",
        content: "উনবিংশ শতাব্দীতে কলকাতার বুদ্ধিবৃত্তিক জাগরণ তথা বেঙ্গল রেনেসাঁর হাত ধরে বাংলা গদ্যের ভিত্তিপ্রস্তর স্থাপিত হয়। ঈশ্বরচন্দ্র বিদ্যাসাগর, বঙ্কিমচন্দ্র চট্টোপাধ্যায়ের মাধ্যমে উপন্যাসের যাত্রা শুরু।",
        category: "সাহিত্য ইতিহাস",
        likes: 125,
      ),
      Blog(
        id: "bl2",
        title: "নজরুলের গানে ভারতীয় শাস্ত্রীয় সঙ্গীতের প্রভাব ও রাগ মিশ্রণ",
        author: "ড. মৈত্রেয়ী রায়",
        date: "২৮ মে, ২০২৬",
        readTime: "১২ মিনিট",
        content: "নজরুল তাঁর গানের সুর বিন্যাসে ঠুমরি, গজল এবং ধ্রুপদ রাগের অসাধারণ সমন্বয় ঘটিয়েছেন। তিনি প্রায় ১৮টিরও বেশি নতুন রাগ আবিষ্কার ও সংযোজন করেছেন বাংলা সঙ্গীতজগতে।",
        category: "নজরুল গীতি",
        likes: 98,
      ),
    ]);

    // Populate Notifications
    _notifications.addAll([
      NotificationItem(
        id: "n1",
        title: "নতুন কোর্স উন্মুক্ত!",
        message: "বরেণ্য কথাসাহিত্যিক শরৎচন্দ্রের উপন্যাস বিশ্লেষণ ও সমাজ বাস্তবতা কোর্সটি আজ থেকেই শুরু হয়েছে।",
        time: "১ ঘণ্টা আগে",
      ),
      NotificationItem(
        id: "n2",
        title: "ঈদ ধামাকা অফার",
        message: "সবচেয়ে বেশি বিক্রিত ৩টি ই-বুকে পেয়ে যান ফ্ল্যাট ২৫% ডিসকাউন্ট! কুপন: EID25",
        time: "১ দিন আগে",
      ),
    ]);
  }

  // Business Logic Methods
  void enrollInCourse(String courseId) {
    if (!_userProfile.enrolledCourses.contains(courseId)) {
      _userProfile.enrolledCourses.add(courseId);
      _notifications.insert(0, NotificationItem(
        id: "en_${DateTime.now().millisecondsSinceEpoch}",
        title: "কোর্সে সফল রেজিস্ট্রেশন!",
        message: "আপনি সফলভাবে নতুন কোর্সে ভর্তি হয়েছেন। প্রথম লেকচার শুরু করুন।",
        time: "এইমাত্র",
      ));
      notifyListeners();
    }
  }

  void purchaseBook(String bookId) {
    if (!_userProfile.purchasedBooks.contains(bookId)) {
      _userProfile.purchasedBooks.add(bookId);
      _notifications.insert(0, NotificationItem(
        id: "pb_${DateTime.now().millisecondsSinceEpoch}",
        title: "বই ক্রয় সফল!",
        message: "আপনার ই-বুক লাইব্রেরিতে নতুন বইটি সফলভাবে যুক্ত হয়েছে।",
        time: "এইমাত্র",
      ));
      notifyListeners();
    }
  }

  void toggleSaveBlog(String blogId) {
    if (_userProfile.savedBlogs.contains(blogId)) {
      _userProfile.savedBlogs.remove(blogId);
    } else {
      _userProfile.savedBlogs.add(blogId);
    }
    notifyListeners();
  }

  void addCourse(Course course) {
    _courses.add(course);
    _notifications.insert(0, NotificationItem(
      id: "ac_${DateTime.now().millisecondsSinceEpoch}",
      title: "নতুন কোর্স যুক্ত করা হয়েছে",
      message: "${course.title} কোর্সটি সফলভাবে প্ল্যাটফর্মে উন্মুক্ত হয়েছে।",
      time: "এইমাত্র",
    ));
    notifyListeners();
  }

  void addBook(Book book) {
    _books.add(book);
    _notifications.insert(0, NotificationItem(
      id: "ab_${DateTime.now().millisecondsSinceEpoch}",
      title: "নতুন বই সংযোজন!",
      message: "${book.title} বইটি প্ল্যাটফর্মের ই-লাইব্রেরিতে যুক্ত করা হয়েছে।",
      time: "এইমাত্র",
    ));
    notifyListeners();
  }

  void addAuthor(Author author) {
    _authors.add(author);
    notifyListeners();
  }

  void addBanner(String banner) {
    _banners.add(banner);
    notifyListeners();
  }

  void updateProfile(String username, String bio, String email, bool notif) {
    _userProfile.username = username;
    _userProfile.bio = bio;
    _userProfile.email = email;
    _userProfile.notificationPref = notif;
    notifyListeners();
  }

  void userLogout() {
    _userProfile.isLoggedIn = false;
    _userProfile.enrolledCourses.clear();
    _userProfile.purchasedBooks.clear();
    notifyListeners();
  }

  void userLogin(String email, String pass) {
    _userProfile.email = email;
    _userProfile.username = email.split('@')[0];
    _userProfile.isLoggedIn = true;
    notifyListeners();
  }
}
