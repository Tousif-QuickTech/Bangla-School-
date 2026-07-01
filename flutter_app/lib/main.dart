import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_animate/flutter_animate.dart';
import 'controllers/bangla_controller.dart';
import 'views/home_view.dart';
import 'views/book_views.dart';
import 'views/course_views.dart';
import 'views/author_views.dart';
import 'views/dashboard_view.dart';
import 'views/auth_view.dart';
import 'views/admin_view.dart';
import 'views/widgets/search_bar.dart';

void main() {
  runApp(
    ChangeNotifierProvider(
      create: (context) => BanglaController(),
      child: const BanglaSchoolApp(),
    ),
  );
}

class BanglaSchoolApp extends StatelessWidget {
  const BanglaSchoolApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final controller = Provider.of<BanglaController>(context);

    return MaterialApp(
      title: 'বাংলা স্কুল',
      debugShowCheckedModeBanner: false,
      themeMode: controller.isDarkTheme ? ThemeMode.dark : ThemeMode.light,
      
      // Warm Premium Literary Light Theme
      theme: ThemeData(
        useMaterial3: true,
        primaryColor: const Color(0xFFF97316), // Vibrant Orange
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFFF97316),
          primary: const Color(0xFFF97316),
          secondary: const Color(0xFFFB923C),
          surface: const Color(0xFFFFFBEB), // Cream background for comfortable reading
          background: const Color(0xFFFFFBEB),
        ),
        cardColor: Colors.white,
        scaffoldBackgroundColor: const Color(0xFFFFFBEB),
        appBarTheme: const AppBarTheme(
          backgroundColor: Color(0xFFFFFBEB),
          elevation: 0,
        ),
      ),

      // Premium Dark Slate Theme (Elegant Slate-900 instead of muddy brown!)
      darkTheme: ThemeData(
        useMaterial3: true,
        primaryColor: const Color(0xFFF97316), // Energetic primary highlight
        colorScheme: const ColorScheme.dark(
          primary: Color(0xFFF97316),
          secondary: Color(0xFFFB923C),
          surface: Color(0xFF1E293B), // Sleek slate-800 cards
          background: Color(0xFF0F172A), // Sleek slate-900 background
        ),
        cardColor: const Color(0xFF1E293B),
        scaffoldBackgroundColor: const Color(0xFF0F172A),
        appBarTheme: const AppBarTheme(
          backgroundColor: Color(0xFF0F172A),
          elevation: 0,
        ),
      ),
      home: const MainNavigationShell(),
    );
  }
}

class MainNavigationShell extends StatelessWidget {
  const MainNavigationShell({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final controller = Provider.of<BanglaController>(context);

    // Return Reader view or full auth if chosen
    if (controller.currentScreen == ActiveScreen.ebookReader && controller.selectedItemId != null) {
      return EbookReaderView(bookId: controller.selectedItemId!);
    }
    if (controller.currentScreen == ActiveScreen.auth) {
      return const AuthView();
    }
    if (controller.currentScreen == ActiveScreen.admin) {
      return const AdminView();
    }

    return Scaffold(
      // --- App Header with custom search bar ---
      appBar: AppBar(
        centerTitle: true,
        title: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            const Icon(Icons.school, color: Color(0xFFF97316), size: 24)
                .animate(onPlay: (controller) => controller.repeat(reverse: true))
                .scale(begin: const Offset(0.92, 0.92), end: const Offset(1.08, 1.08), duration: 1500.ms),
            const SizedBox(width: 8),
            Text(
              "বাংলা স্কুল",
              style: TextStyle(
                fontWeight: FontWeight.black,
                fontSize: 18,
                color: Theme.of(context).colorScheme.primary,
              ),
            ),
          ],
        ),
        actions: [
          IconButton(
            icon: Icon(controller.isDarkTheme ? Icons.light_mode : Icons.dark_mode),
            onPressed: () => controller.isDarkTheme = !controller.isDarkTheme,
          ),
          IconButton(
            icon: Icon(
              controller.userProfile.isLoggedIn ? Icons.account_circle : Icons.account_circle_outlined,
              color: controller.userProfile.isLoggedIn ? Theme.of(context).primaryColor : null,
            ),
            onPressed: () {
              if (controller.userProfile.isLoggedIn) {
                controller.navigateTo(ActiveScreen.dashboard);
              } else {
                controller.navigateTo(ActiveScreen.auth);
              }
            },
          )
        ],
        bottom: controller.currentScreen == ActiveScreen.ebookReader
            ? null
            : const PreferredSize(
                preferredSize: Size.fromHeight(60),
                child: ShadcnPersistentHeaderSearchBar(),
              ),
      ),

      // --- Side Drawer Navigation ---
      drawer: Drawer(
        child: Column(
          children: [
            UserAccountsDrawerHeader(
              decoration: BoxDecoration(color: Theme.of(context).primaryColor),
              accountName: Text(controller.userProfile.username, style: const TextStyle(fontWeight: FontWeight.bold)),
              accountEmail: Text(controller.userProfile.email),
              currentAccountPicture: CircleAvatar(
                backgroundImage: NetworkImage(controller.userProfile.profilePhotoUrl),
              ),
            ),
            ListTile(
              leading: const Icon(Icons.home),
              title: const Text("মূল পাতা (Home)"),
              selected: controller.activeTab == 0 && controller.currentScreen == ActiveScreen.home,
              onTap: () {
                Navigator.pop(context);
                controller.activeTab = 0;
              },
            ),
            ListTile(
              leading: const Icon(Icons.school),
              title: const Text("অনলাইন কোর্সসমূহ"),
              selected: controller.activeTab == 1 && controller.currentScreen == ActiveScreen.courses,
              onTap: () {
                Navigator.pop(context);
                controller.activeTab = 1;
              },
            ),
            ListTile(
              leading: const Icon(Icons.menu_book),
              title: const Text("ডিজিটাল লাইব্রেরি"),
              selected: controller.activeTab == 2 && controller.currentScreen == ActiveScreen.books,
              onTap: () {
                Navigator.pop(context);
                controller.activeTab = 2;
              },
            ),
            ListTile(
              leading: const Icon(Icons.history_edu),
              title: const Text("বরেণ্য সাহিত্যিকবৃন্দ"),
              selected: controller.activeTab == 3 && controller.currentScreen == ActiveScreen.authors,
              onTap: () {
                Navigator.pop(context);
                controller.activeTab = 3;
              },
            ),
          ],
        ),
      ),

      // --- Main View Renderer ---
      body: _renderBody(controller),

      // --- Custom Bottom Navigation Bar ---
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: controller.activeTab,
        onTap: (index) {
          controller.activeTab = index;
        },
        selectedItemColor: Theme.of(context).primaryColor,
        unselectedItemColor: Colors.grey,
        type: BottomNavigationBarType.fixed,
        items: const [
          BottomNavigationBarItem(icon: Icon(Icons.home), label: "মূল পাতা"),
          BottomNavigationBarItem(icon: Icon(Icons.school), label: "কোর্স"),
          BottomNavigationBarItem(icon: Icon(Icons.menu_book), label: "লাইব্রেরি"),
          BottomNavigationBarItem(icon: Icon(Icons.people), label: "লেখকবৃন্দ"),
          BottomNavigationBarItem(icon: Icon(Icons.person), label: "প্রোফাইল"),
        ],
      ),
    );
  }

  Widget _renderBody(BanglaController controller) {
    switch (controller.currentScreen) {
      case ActiveScreen.home:
        return const HomeView();
      case ActiveScreen.books:
        return const BookListView();
      case ActiveScreen.bookDetails:
        if (controller.selectedItemId != null) {
          return BookDetailsView(bookId: controller.selectedItemId!);
        }
        return const BookListView();
      case ActiveScreen.courses:
        return const CourseListView();
      case ActiveScreen.courseDetails:
        if (controller.selectedItemId != null) {
          return CourseDetailsView(courseId: controller.selectedItemId!);
        }
        return const CourseListView();
      case ActiveScreen.authors:
        return const AuthorListView();
      case ActiveScreen.authorDetails:
        if (controller.selectedItemId != null) {
          return AuthorDetailsView(authorId: controller.selectedItemId!);
        }
        return const AuthorListView();
      case ActiveScreen.dashboard:
        return const DashboardView();
      default:
        return const HomeView();
    }
  }
}
