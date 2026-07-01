import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_animate/flutter_animate.dart';
import '../controllers/bangla_controller.dart';
import '../models/bangla_models.dart';

class DashboardView extends StatelessWidget {
  const DashboardView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final controller = Provider.of<BanglaController>(context);
    final user = controller.userProfile;

    if (!user.isLoggedIn) {
      return Center(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(Icons.lock_outline, size: 64, color: Theme.of(context).primaryColor),
              const SizedBox(height: 16),
              const Text("লগইন প্রয়োজন", style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
              const SizedBox(height: 8),
              const Text("আপনার ড্যাশবোর্ড ও প্রোফাইল দেখতে দয়া করে লগইন করুন।", textAlign: TextAlign.center, style: TextStyle(fontSize: 12)),
              const SizedBox(height: 20),
              ElevatedButton(
                style: ElevatedButton.styleFrom(backgroundColor: Theme.of(context).primaryColor),
                onPressed: () => controller.navigateTo(ActiveScreen.auth),
                child: const Text("লগইন স্ক্রিনে যান", style: TextStyle(color: Colors.white)),
              )
            ],
          ),
        ),
      );
    }

    return SingleChildScrollView(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Profile Header card
          Container(
            padding: const EdgeInsets.all(16),
            decoration: BoxDecoration(
              color: Theme.of(context).cardColor,
              borderRadius: BorderRadius.circular(16),
              border: Border.all(color: Theme.of(context).dividerColor.withOpacity(0.08)),
            ),
            child: Row(
              children: [
                CircleAvatar(
                  radius: 35,
                  backgroundImage: NetworkImage(user.profilePhotoUrl),
                ),
                const SizedBox(width: 16),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(user.username, style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                      Text(user.email, style: TextStyle(fontSize: 12, color: Theme.of(context).hintColor)),
                      const SizedBox(height: 4),
                      Text("যোগদান: ${user.joinedDate}", style: TextStyle(fontSize: 10, color: Theme.of(context).primaryColor, fontWeight: FontWeight.bold)),
                    ],
                  ),
                ),
                IconButton(
                  icon: const Icon(Icons.logout, color: Colors.redAccent),
                  onPressed: () {
                    controller.userLogout();
                    ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("সফলভাবে লগআউট করা হয়েছে।")));
                  },
                )
              ],
            ),
          ).animate().fadeIn(duration: 400.ms),

          const SizedBox(height: 24),

          // Enrolled Courses List
          const Text("আমার যুক্ত হওয়া কোর্সসমূহ", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
          const SizedBox(height: 8),
          if (user.enrolledCourses.isEmpty)
            _buildEmptyState(context, "কোনো কোর্স যুক্ত নেই")
          else
            ListView.builder(
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
              itemCount: user.enrolledCourses.length,
              itemBuilder: (context, index) {
                final courseId = user.enrolledCourses[index];
                final course = controller.courses.firstWhere((c) => c.id == courseId, orElse: () => controller.courses.first);
                return Card(
                  margin: const EdgeInsets.only(bottom: 8),
                  child: ListTile(
                    leading: Image.network(course.coverUrl, width: 50, height: 50, fit: BoxFit.cover),
                    title: Text(course.title, style: const TextStyle(fontSize: 13, fontWeight: FontWeight.bold)),
                    subtitle: Text(course.instructor, style: const TextStyle(fontSize: 11)),
                    trailing: Icon(Icons.arrow_forward_ios, size: 12, color: Theme.of(context).primaryColor),
                    onTap: () => controller.navigateTo(ActiveScreen.courseDetails, itemId: course.id),
                  ),
                );
              },
            ),

          const SizedBox(height: 24),

          // Purchased Books List
          const Text("আমার সংগ্রহীত ই-বুকসমূহ", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
          const SizedBox(height: 8),
          if (user.purchasedBooks.isEmpty)
            _buildEmptyState(context, "কোনো বই সংগ্রহে নেই")
          else
            ListView.builder(
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
              itemCount: user.purchasedBooks.length,
              itemBuilder: (context, index) {
                final bookId = user.purchasedBooks[index];
                final book = controller.books.firstWhere((b) => b.id == bookId, orElse: () => controller.books.first);
                return Card(
                  margin: const EdgeInsets.only(bottom: 8),
                  child: ListTile(
                    leading: Image.network(book.coverUrl, width: 40, height: 50, fit: BoxFit.cover),
                    title: Text(book.title, style: const TextStyle(fontSize: 13, fontWeight: FontWeight.bold)),
                    subtitle: Text(book.authorName, style: const TextStyle(fontSize: 11)),
                    trailing: ElevatedButton(
                      style: ElevatedButton.styleFrom(backgroundColor: Theme.of(context).primaryColor, padding: const EdgeInsets.symmetric(horizontal: 12)),
                      onPressed: () => controller.navigateTo(ActiveScreen.ebookReader, itemId: book.id),
                      child: const Text("পড়ুন", style: TextStyle(color: Colors.white, fontSize: 11)),
                    ),
                  ),
                );
              },
            ),

          const SizedBox(height: 24),

          // Quick Settings
          const Text("অ্যাপ্লিকেশন সেটিংস", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
          const SizedBox(height: 8),
          SwitchListTile(
            title: const Text("ডার্ক থিম মোড সক্রিয় করুন", style: TextStyle(fontSize: 13)),
            subtitle: const Text("চোখের সুরক্ষায় ডার্ক মোড অন করুন", style: TextStyle(fontSize: 11)),
            value: controller.isDarkTheme,
            activeColor: Theme.of(context).primaryColor,
            onChanged: (val) => controller.isDarkTheme = val,
          ),
          SwitchListTile(
            title: const Text("পুশ নোটিফিকেশন", style: TextStyle(fontSize: 13)),
            subtitle: const Text("নতুন কোর্স ও বইয়ের আপডেট নোটিফিকেশন পান", style: TextStyle(fontSize: 11)),
            value: user.notificationPref,
            activeColor: Theme.of(context).primaryColor,
            onChanged: (val) => controller.updateProfile(user.username, user.bio, user.email, val),
          ),
        ],
      ),
    );
  }

  Widget _buildEmptyState(BuildContext context, String message) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(24),
      decoration: BoxDecoration(
        color: Theme.of(context).cardColor,
        borderRadius: BorderRadius.circular(12),
      ),
      child: Center(
        child: Text(message, style: TextStyle(fontSize: 12, color: Theme.of(context).hintColor)),
      ),
    );
  }
}
