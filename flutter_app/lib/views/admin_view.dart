import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_animate/flutter_animate.dart';
import '../controllers/bangla_controller.dart';
import '../models/bangla_models.dart';

class AdminView extends StatefulWidget {
  const AdminView({Key? key}) : super(key: key);

  @override
  State<AdminView> createState() => _AdminViewState();
}

class _AdminViewState extends State<AdminView> {
  final _courseTitleController = TextEditingController();
  final _courseInstructorController = TextEditingController();
  final _coursePriceController = TextEditingController();

  final _bookTitleController = TextEditingController();
  final _bookAuthorController = TextEditingController();
  final _bookPriceController = TextEditingController();

  @override
  void dispose() {
    _courseTitleController.dispose();
    _courseInstructorController.dispose();
    _coursePriceController.dispose();
    _bookTitleController.dispose();
    _bookAuthorController.dispose();
    _bookPriceController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final controller = Provider.of<BanglaController>(context);

    return Scaffold(
      appBar: AppBar(
        title: const Text("অ্যাডমিন প্যানেল", style: TextStyle(fontSize: 16)),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => controller.navigateTo(ActiveScreen.home),
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              "অ্যাডমিন কন্ট্রোল প্যানেল",
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const Text(
              "বাংলা সাহিত্য প্ল্যাটফর্ম ডেটা ও ব্যবহারকারী পরিচালনা করুন।",
              style: TextStyle(fontSize: 11, color: Colors.grey),
            ),
            const SizedBox(height: 24),

            // Quick Stats Row
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                _buildStatCard(context, "মোট বই", "${controller.books.length}টি", Icons.book),
                _buildStatCard(context, "মোট কোর্স", "${controller.courses.length}টি", Icons.school),
                _buildStatCard(context, "মোট লেখক", "${controller.authors.length} জন", Icons.person),
              ],
            ).animate().fadeIn(duration: 400.ms),

            const SizedBox(height: 24),

            // Form: Add Course
            _buildAdminSection(
              context,
              "নতুন প্রিমিয়াম কোর্স তৈরি করুন",
              [
                TextField(
                  controller: _courseTitleController,
                  style: const TextStyle(fontSize: 13),
                  decoration: const InputDecoration(labelText: "কোর্স শিরোনাম"),
                ),
                const SizedBox(height: 8),
                TextField(
                  controller: _courseInstructorController,
                  style: const TextStyle(fontSize: 13),
                  decoration: const InputDecoration(labelText: "শিক্ষকের নাম"),
                ),
                const SizedBox(height: 8),
                TextField(
                  controller: _coursePriceController,
                  style: const TextStyle(fontSize: 13),
                  keyboardType: TextInputType.number,
                  decoration: const InputDecoration(labelText: "মূল্য (৳)"),
                ),
                const SizedBox(height: 12),
                ElevatedButton(
                  style: ElevatedButton.styleFrom(backgroundColor: Theme.of(context).primaryColor),
                  onPressed: () {
                    final title = _courseTitleController.text.trim();
                    final instructor = _courseInstructorController.text.trim();
                    final price = double.tryParse(_coursePriceController.text.trim()) ?? 0.0;

                    if (title.isEmpty || instructor.isEmpty) {
                      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("দয়া করে সমস্ত তথ্য প্রদান করুন!")));
                      return;
                    }

                    controller.addCourse(Course(
                      id: "c_${DateTime.now().millisecondsSinceEpoch}",
                      title: title,
                      instructor: instructor,
                      description: "রবীন্দ্র ও নজরুলের পাশাপাশি উনিশ শতকের আধুনিক বাঙালি লেখকদের উপন্যাস ও দর্শনের উপর গভীর একাডেমি কোর্স।",
                      price: price,
                      rating: 5.0,
                      duration: "১২ ঘণ্টা",
                      lecturesCount: 8,
                      isBestSeller: true,
                      enrolledCount: 1,
                      coverUrl: "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=200",
                      category: "বাঙালি সাহিত্য",
                    ));

                    _courseTitleController.clear();
                    _courseInstructorController.clear();
                    _coursePriceController.clear();

                    ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("নতুন সাহিত্য কোর্স সফলভাবে লাইভ করা হয়েছে!")));
                  },
                  child: const Text("কোর্স লঞ্চ করুন", style: TextStyle(color: Colors.white)),
                )
              ],
            ),

            const SizedBox(height: 24),

            // Form: Add Book
            _buildAdminSection(
              context,
              "লাইব্রেরিতে নতুন বই যুক্ত করুন",
              [
                TextField(
                  controller: _bookTitleController,
                  style: const TextStyle(fontSize: 13),
                  decoration: const InputDecoration(labelText: "বইয়ের নাম"),
                ),
                const SizedBox(height: 8),
                TextField(
                  controller: _bookAuthorController,
                  style: const TextStyle(fontSize: 13),
                  decoration: const InputDecoration(labelText: "লেখকের নাম"),
                ),
                const SizedBox(height: 8),
                TextField(
                  controller: _bookPriceController,
                  style: const TextStyle(fontSize: 13),
                  keyboardType: TextInputType.number,
                  decoration: const InputDecoration(labelText: "ই-বুক মূল্য (৳)"),
                ),
                const SizedBox(height: 12),
                ElevatedButton(
                  style: ElevatedButton.styleFrom(backgroundColor: Theme.of(context).primaryColor),
                  onPressed: () {
                    final title = _bookTitleController.text.trim();
                    final author = _bookAuthorController.text.trim();
                    final price = double.tryParse(_bookPriceController.text.trim()) ?? 0.0;

                    if (title.isEmpty || author.isEmpty) {
                      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("দয়া করে সম্পূর্ণ তথ্য প্রদান করুন!")));
                      return;
                    }

                    controller.addBook(Book(
                      id: "b_${DateTime.now().millisecondsSinceEpoch}",
                      title: title,
                      authorId: "a1",
                      authorName: author,
                      description: "বাঙালি ঐতিহ্য এবং ধ্রুপদী মননের কালজয়ী সংগ্রহশালা থেকে সংগৃহীত সেরা উপন্যাস ও কাব্য সংকলন।",
                      price: price,
                      rating: 4.8,
                      reviewsCount: 1,
                      isBestSeller: false,
                      isEbook: true,
                      coverUrl: "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=200",
                      publishYear: 2026,
                      pages: 180,
                    ));

                    _bookTitleController.clear();
                    _bookAuthorController.clear();
                    _bookPriceController.clear();

                    ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("নতুন বই লাইব্রেরিতে যুক্ত হয়েছে!")));
                  },
                  child: const Text("ই-বুক প্রকাশ করুন", style: TextStyle(color: Colors.white)),
                )
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildStatCard(BuildContext context, String title, String value, IconData icon) {
    return Expanded(
      child: Card(
        child: Padding(
          padding: const EdgeInsets.all(12.0),
          child: Column(
            children: [
              Icon(icon, color: Theme.of(context).primaryColor, size: 20),
              const SizedBox(height: 6),
              Text(title, style: TextStyle(fontSize: 11, color: Theme.of(context).hintColor)),
              const SizedBox(height: 2),
              Text(value, style: const TextStyle(fontSize: 15, fontWeight: FontWeight.bold)),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildAdminSection(BuildContext context, String sectionTitle, List<Widget> children) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Theme.of(context).cardColor,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: Theme.of(context).dividerColor.withOpacity(0.08)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Text(sectionTitle, style: TextStyle(fontSize: 14, fontWeight: FontWeight.bold, color: Theme.of(context).primaryColor)),
          const Divider(height: 20),
          ...children,
        ],
      ),
    );
  }
}
