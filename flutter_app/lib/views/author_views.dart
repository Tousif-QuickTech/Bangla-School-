import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_animate/flutter_animate.dart';
import '../controllers/bangla_controller.dart';
import '../models/bangla_models.dart';

class AuthorListView extends StatelessWidget {
  const AuthorListView({Key? key}) : super(key: key);

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
                const Text("বরেণ্য বাঙালি সাহিত্যিকবৃন্দ", style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                Text("${controller.authors.length} জন সাহিত্যিক", style: TextStyle(color: Theme.of(context).primaryColor, fontSize: 12)),
              ],
            ),
          ),
          Expanded(
            child: ListView.builder(
              padding: const EdgeInsets.symmetric(horizontal: 16, bottom: 16),
              itemCount: controller.authors.length,
              itemBuilder: (context, index) {
                final author = controller.authors[index];
                return Container(
                  margin: const EdgeInsets.only(bottom: 12),
                  decoration: BoxDecoration(
                    color: Theme.of(context).cardColor,
                    borderRadius: BorderRadius.circular(16),
                    border: Border.all(color: Theme.of(context).dividerColor.withOpacity(0.08)),
                  ),
                  child: InkWell(
                    onTap: () => controller.navigateTo(ActiveScreen.authorDetails, itemId: author.id),
                    borderRadius: BorderRadius.circular(16),
                    child: Padding(
                      padding: const EdgeInsets.all(12.0),
                      child: Row(
                        children: [
                          CircleAvatar(
                            radius: 35,
                            backgroundImage: NetworkImage(author.photoUrl),
                          ),
                          const SizedBox(width: 16),
                          Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  author.name,
                                  style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
                                ),
                                const SizedBox(height: 4),
                                Text(
                                  author.bio,
                                  maxLines: 2,
                                  overflow: TextOverflow.ellipsis,
                                  style: TextStyle(fontSize: 11, color: Theme.of(context).hintColor),
                                ),
                                const SizedBox(height: 6),
                                Text(
                                  "${author.booksCount}টি ধ্রুপদী প্রকাশনা",
                                  style: TextStyle(fontSize: 10, color: Theme.of(context).primaryColor, fontWeight: FontWeight.bold),
                                ),
                              ],
                            ),
                          )
                        ],
                      ),
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

class AuthorDetailsView extends StatelessWidget {
  final String authorId;
  const AuthorDetailsView({Key? key, required this.authorId}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final controller = Provider.of<BanglaController>(context);
    final author = controller.authors.firstWhere((a) => a.id == authorId, orElse: () => controller.authors.first);

    return Scaffold(
      appBar: AppBar(
        title: Text(author.name, style: const TextStyle(fontSize: 16)),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => controller.navigateTo(ActiveScreen.authors),
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Center(
              child: Column(
                children: [
                  CircleAvatar(
                    radius: 55,
                    backgroundImage: NetworkImage(author.photoUrl),
                  ).animate().scale(duration: 300.ms),
                  const SizedBox(height: 12),
                  Text(author.name, style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
                  const SizedBox(height: 4),
                  Text("বরেণ্য বাঙালি সাহিত্যিক", style: TextStyle(fontSize: 12, color: Theme.of(context).primaryColor, fontWeight: FontWeight.bold)),
                ],
              ),
            ),
            const Divider(height: 32),
            const Text("সাহিত্যিক জীবনী", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
            const SizedBox(height: 8),
            Text(author.bio, style: const TextStyle(height: 1.5, fontSize: 13)),
            const SizedBox(height: 20),
            const Text("প্রধান অর্জন ও পুরস্কারসমূহ", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
            const SizedBox(height: 8),
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: Theme.of(context).primaryColor.withOpacity(0.06),
                borderRadius: BorderRadius.circular(12),
              ),
              child: Text(author.achievements, style: const TextStyle(fontSize: 13, height: 1.4)),
            ),
            const SizedBox(height: 20),
            const Text("উল্লেখযোগ্য কালজয়ী সাহিত্যকর্ম", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
            const SizedBox(height: 8),
            Text(author.works, style: const TextStyle(fontSize: 14, height: 1.4, fontWeight: FontWeight.bold)),
            const SizedBox(height: 24),
          ],
        ),
      ),
    );
  }
}
