import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_animate/flutter_animate.dart';
import '../controllers/bangla_controller.dart';
import '../models/bangla_models.dart';

class BookListView extends StatelessWidget {
  const BookListView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final controller = Provider.of<BanglaController>(context);

    return Scaffold(
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Text("ডিজিটাল লাইব্রেরি", style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                Text("${controller.books.length}টি বই উপলব্ধ", style: TextStyle(color: Theme.of(context).primaryColor, fontSize: 12)),
              ],
            ),
          ),
          Expanded(
            child: GridView.builder(
              padding: const EdgeInsets.symmetric(horizontal: 16, bottom: 16),
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 2,
                childAspectRatio: 0.65,
                crossAxisSpacing: 12,
                mainAxisSpacing: 12,
              ),
              itemCount: controller.books.length,
              itemBuilder: (context, index) {
                final book = controller.books[index];
                return Container(
                  decoration: BoxDecoration(
                    color: Theme.of(context).cardColor,
                    borderRadius: BorderRadius.circular(16),
                    border: Border.all(color: Theme.of(context).dividerColor.withOpacity(0.08)),
                  ),
                  child: InkWell(
                    onTap: () => controller.navigateTo(ActiveScreen.bookDetails, itemId: book.id),
                    borderRadius: BorderRadius.circular(16),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Expanded(
                          child: ClipRRect(
                            borderRadius: const BorderRadius.vertical(top: Radius.circular(16)),
                            child: Image.network(book.coverUrl, fit: BoxFit.cover, width: double.infinity),
                          ),
                        ),
                        Padding(
                          padding: const EdgeInsets.all(10.0),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                book.title,
                                maxLines: 1,
                                overflow: TextOverflow.ellipsis,
                                style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13),
                              ),
                              const SizedBox(height: 2),
                              Text(
                                book.authorName,
                                style: TextStyle(fontSize: 11, color: Theme.of(context).hintColor),
                              ),
                              const SizedBox(height: 6),
                              Row(
                                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                children: [
                                  Text(
                                    "৳${book.price.toStringAsFixed(0)}",
                                    style: TextStyle(fontWeight: FontWeight.black, color: Theme.of(context).primaryColor),
                                  ),
                                  Container(
                                    padding: const EdgeInsets.symmetric(horizontal: 4, vertical: 2),
                                    decoration: BoxDecoration(color: Colors.green.withOpacity(0.1), borderRadius: BorderRadius.circular(4)),
                                    child: const Text("EBOOK", style: TextStyle(color: Colors.green, fontSize: 8, fontWeight: FontWeight.bold)),
                                  )
                                ],
                              )
                            ],
                          ),
                        )
                      ],
                    ),
                  ).animate().fadeIn(delay: (index * 50).ms, duration: 400.ms),
                );
              },
            ),
          )
        ],
      ),
    );
  }
}

class BookDetailsView extends StatelessWidget {
  final String bookId;
  const BookDetailsView({Key? key, required this.bookId}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final controller = Provider.of<BanglaController>(context);
    final book = controller.books.firstWhere((b) => b.id == bookId, orElse: () => controller.books.first);
    final isPurchased = controller.userProfile.purchasedBooks.contains(book.id);

    return Scaffold(
      appBar: AppBar(
        title: Text(book.title, style: const TextStyle(fontSize: 16)),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => controller.navigateTo(ActiveScreen.books),
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Center(
              child: ClipRRect(
                borderRadius: BorderRadius.circular(16),
                child: Image.network(book.coverUrl, height: 220, width: 150, fit: BoxFit.cover),
              ).animate().scale(duration: 300.ms),
            ),
            const SizedBox(height: 20),
            Text(book.title, style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
            const SizedBox(height: 4),
            Text(book.authorName, style: TextStyle(fontSize: 14, color: Theme.of(context).primaryColor, fontWeight: FontWeight.bold)),
            const SizedBox(height: 12),
            Row(
              children: [
                Icon(Icons.star, color: Colors.amber[700], size: 18),
                const SizedBox(width: 4),
                Text(book.rating.toString(), style: const TextStyle(fontWeight: FontWeight.bold)),
                const SizedBox(width: 4),
                Text("(${book.reviewsCount}টি রিভিউ)", style: TextStyle(color: Theme.of(context).hintColor, fontSize: 12)),
              ],
            ),
            const Divider(height: 24),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                _buildInfoColumn(context, "প্রকাশকাল", "${book.publishYear} খ্রিস্টাব্দ"),
                _buildInfoColumn(context, "মোট পৃষ্ঠা", "${book.pages} পৃষ্ঠা"),
                _buildInfoColumn(context, "ধরন", "ই-বুক"),
              ],
            ),
            const Divider(height: 24),
            const Text("বইয়ের পরিচিতি ও সারসংক্ষেপ", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
            const SizedBox(height: 8),
            Text(book.description, style: TextStyle(height: 1.5, color: Theme.of(context).textTheme.bodyMedium?.color?.withOpacity(0.9))),
            const SizedBox(height: 32),
            SizedBox(
              width: double.infinity,
              height: 50,
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  backgroundColor: Theme.of(context).primaryColor,
                  shape: RoundedCornerShape(12),
                ),
                onPressed: () {
                  if (isPurchased) {
                    controller.navigateTo(ActiveScreen.ebookReader, itemId: book.id);
                  } else {
                    controller.purchaseBook(book.id);
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text("${book.title} লাইব্রেরিতে যুক্ত করা হয়েছে!")),
                    );
                  }
                },
                child: Text(
                  isPurchased ? "ই-বুক পড়ুন" : "৳${book.price.toStringAsFixed(0)} - ই-বুকটি সংগ্রহ করুন",
                  style: const TextStyle(color: Colors.white, fontSize: 15, fontWeight: FontWeight.bold),
                ),
              ),
            )
          ],
        ),
      ),
    );
  }

  Widget _buildInfoColumn(BuildContext context, String title, String value) {
    return Column(
      children: [
        Text(title, style: TextStyle(fontSize: 11, color: Theme.of(context).hintColor)),
        const SizedBox(height: 4),
        Text(value, style: const TextStyle(fontSize: 13, fontWeight: FontWeight.bold)),
      ],
    );
  }
}

class EbookReaderView extends StatelessWidget {
  final String bookId;
  const EbookReaderView({Key? key, required this.bookId}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final controller = Provider.of<BanglaController>(context);
    final book = controller.books.firstWhere((b) => b.id == bookId, orElse: () => controller.books.first);

    return Scaffold(
      appBar: AppBar(
        title: Text(book.title, style: const TextStyle(fontSize: 15)),
        leading: IconButton(
          icon: const Icon(Icons.close),
          onPressed: () => controller.navigateTo(ActiveScreen.bookDetails, itemId: book.id),
        ),
        actions: [
          IconButton(icon: const Icon(Icons.text_fields), onPressed: () {}),
          IconButton(icon: const Icon(Icons.bookmark_outline), onPressed: () {}),
        ],
      ),
      body: Container(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text("অধ্যায় ১: সূচিপত্র ও উপক্রমণিকা", style: TextStyle(fontSize: 12, color: Theme.of(context).primaryColor, fontWeight: FontWeight.bold)),
                const Text("পৃষ্ঠা ১/২৫", style: TextStyle(fontSize: 11)),
              ],
            ),
            const SizedBox(height: 24),
            Expanded(
              child: SingleChildScrollView(
                child: Text(
                  "${book.title} কাব্যগ্রন্হের প্রথম খণ্ড\n\n"
                  "হে মোর চিত্ত, পুণ্য তীর্থে জাগো রে ধীরে\n"
                  "এই ভারতের মহামানবের সাগরতীরে।\n"
                  "হেথায় দাঁড়ায়ে দু বাহু বাড়ায়ে নমি নরদেবতারে,\n"
                  "উদার ছন্দে পরমানন্দে বন্দন করি তাঁরে।\n\n"
                  "মম চিত্তে নিতি নৃত্যে কে যে নাচে--\n"
                  "তাতা থৈথৈ তাতা থৈথৈ তাতা থৈথৈ।\n"
                  "তারি সঙ্গে বাজেরী মৃদঙ্গ মাঝে মাঝে--\n"
                  "তাতা থৈথৈ তাতা থৈথৈ তাতা থৈথৈ।\n\n"
                  "ধ্রুপদী সাহিত্যের এই অনন্য সৃষ্টি বাঙালির চিরকালীন মননের খোরাক জোগায়। প্রতিটি পঙ্ক্তিতে লুকিয়ে আছে আধ্যাত্মিক প্রেম ও প্রকৃতির সাথে আত্মার মেলবন্ধন।",
                  style: const TextStyle(fontSize: 16, height: 1.8, fontFamily: "serif"),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
