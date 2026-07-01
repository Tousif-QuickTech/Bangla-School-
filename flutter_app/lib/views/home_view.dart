import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_animate/flutter_animate.dart';
import '../controllers/bangla_controller.dart';
import '../models/bangla_models.dart';

class HomeView extends StatelessWidget {
  const HomeView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final controller = Provider.of<BanglaController>(context);

    return SingleChildScrollView(
      padding: const EdgeInsets.only(bottom: 32),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Banner Carousel
          _buildHeroSlider(context, controller).animate().fadeIn(duration: 500.ms).slideY(begin: 0.1, end: 0.0),
          const SizedBox(height: 16),

          // Quick Access Buttons
          _buildQuickAccess(context, controller).animate().fadeIn(delay: 100.ms, duration: 500.ms),
          const SizedBox(height: 24),

          // Courses Section
          _buildSectionHeader(
            context,
            "জনপ্রিয় কোর্সসমূহ",
            "আপনার সাহিত্য চর্চাকে সমৃদ্ধ করতে প্রথিতযশা শিক্ষকদের নির্দেশনায় ভর্তি হোন।",
            () => controller.activeTab = 1,
          ),
          _buildCoursesSlider(context, controller),
          const SizedBox(height: 24),

          // Best Seller Books Section
          _buildSectionHeader(
            context,
            "বেস্ট সেলার বইসমূহ",
            "সবচেয়ে বেশি পঠিত ও সমাদৃত সাহিত্যের সেরা মলাটগুলো বেছে নিন।",
            () => controller.activeTab = 2,
          ),
          _buildBooksSlider(context, controller, true),
          const SizedBox(height: 24),

          // Popular Authors Section
          _buildSectionHeader(
            context,
            "বরেণ্য লেখকবৃন্দ",
            "যাঁদের অবিস্মরণীয় সৃষ্টিতে বাংলা সাহিত্য বিশ্বদরবারে অনন্য উচ্চতায় পৌঁছেছে।",
            () => controller.activeTab = 3,
          ),
          _buildAuthorsSlider(context, controller),
        ],
      ).animate().fadeIn(duration: 400.ms),
    );
  }

  Widget _buildHeroSlider(BuildContext context, BanglaController controller) {
    return SizedBox(
      height: 190,
      child: PageView.builder(
        itemCount: controller.banners.length,
        itemBuilder: (context, index) {
          final bannerText = controller.banners[index];
          String title = bannerText;
          String subtitle = "";
          if (bannerText.contains("ছাড়!")) {
            title = "বিশেষ সাহিত্য অফার!";
            subtitle = bannerText;
          } else if (bannerText.contains("ভর্তি")) {
            title = "নতুন কোর্সে ভর্তি চলছে!";
            subtitle = bannerText;
          } else {
            title = "বাংলা ডিজিটাল ই-লাইব্রেরি";
            subtitle = bannerText;
          }

          // Use the high quality generated placeholders matching the theme
          final List<String> imageUrls = [
            "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=600",
            "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=600",
            "https://images.unsplash.com/photo-1457369804613-52c61a468e7d?w=600"
          ];

          return Container(
            margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(16),
              boxShadow: [
                BoxShadow(color: Colors.black12, blurRadius: 6, offset: const Offset(0, 3))
              ],
            ),
            child: ClipRRect(
              borderRadius: BorderRadius.circular(16),
              child: Stack(
                fit: StackFit.expand,
                children: [
                  Image.network(
                    imageUrls[index % imageUrls.length],
                    fit: BoxFit.cover,
                  ),
                  Container(
                    decoration: BoxDecoration(
                      gradient: LinearGradient(
                        begin: Alignment.topCenter,
                        end: Alignment.bottomCenter,
                        colors: [
                          Colors.black.withOpacity(0.2),
                          Colors.black.withOpacity(0.85),
                        ],
                      ),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.end,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Container(
                          padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                          decoration: BoxDecoration(
                            color: Theme.of(context).primaryColor,
                            borderRadius: BorderRadius.circular(6),
                          ),
                          child: const Text(
                            "ঘোষণা",
                            style: TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold),
                          ),
                        ),
                        const SizedBox(height: 6),
                        Text(
                          title,
                          style: const TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold),
                        ),
                        const SizedBox(height: 4),
                        Text(
                          subtitle,
                          maxLines: 2,
                          overflow: TextOverflow.ellipsis,
                          style: const TextStyle(color: Colors.white70, fontSize: 11),
                        ),
                      ],
                    ),
                  )
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  Widget _buildQuickAccess(BuildContext context, BanglaController controller) {
    final items = [
      {"label": "হোম", "tab": 0, "icon": Icons.home_outlined},
      {"label": "কোর্সসমূহ", "tab": 1, "icon": Icons.school_outlined},
      {"label": "লাইব্রেরি", "tab": 2, "icon": Icons.auto_stories_outlined},
      {"label": "লেখকবৃন্দ", "tab": 3, "icon": Icons.history_edu_outlined},
    ];

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: items.map((item) {
          final isSelected = controller.activeTab == item["tab"];
          return InkWell(
            onTap: () => controller.activeTab = item["tab"] as int,
            borderRadius: BorderRadius.circular(12),
            child: AnimatedContainer(
              duration: 200.ms,
              width: 80,
              padding: const EdgeInsets.symmetric(vertical: 12),
              decoration: BoxDecoration(
                color: isSelected
                    ? Theme.of(context).primaryColor.withOpacity(0.1)
                    : Theme.of(context).cardColor,
                borderRadius: BorderRadius.circular(12),
                border: Border.all(
                  color: isSelected
                      ? Theme.of(context).primaryColor
                      : Theme.of(context).dividerColor.withOpacity(0.1),
                ),
              ),
              child: Column(
                children: [
                  Icon(
                    item["icon"] as IconData,
                    color: isSelected ? Theme.of(context).primaryColor : Theme.of(context).hintColor,
                    size: 24,
                  ),
                  const SizedBox(height: 6),
                  Text(
                    item["label"] as String,
                    style: TextStyle(
                      fontSize: 12,
                      fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
                      color: isSelected ? Theme.of(context).primaryColor : Theme.of(context).textTheme.bodyMedium?.color,
                    ),
                  ),
                ],
              ),
            ),
          );
        }).toList(),
      ),
    );
  }

  Widget _buildSectionHeader(BuildContext context, String title, String subtitle, VoidCallback onSeeAll) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 12.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                title,
                style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
              TextButton(
                onPressed: onSeeAll,
                child: Row(
                  children: [
                    Text("সব দেখুন", style: TextStyle(color: Theme.of(context).primaryColor, fontSize: 13)),
                    Icon(Icons.chevron_right, size: 16, color: Theme.of(context).primaryColor),
                  ],
                ),
              ),
            ],
          ),
          Text(
            subtitle,
            style: TextStyle(fontSize: 11, color: Theme.of(context).hintColor.withOpacity(0.8)),
          ),
        ],
      ),
    );
  }

  Widget _buildCoursesSlider(BuildContext context, BanglaController controller) {
    return SizedBox(
      height: 250,
      child: ListView.builder(
        scrollDirection: Axis.horizontal,
        padding: const EdgeInsets.symmetric(horizontal: 12),
        itemCount: controller.courses.length,
        itemBuilder: (context, index) {
          final course = controller.courses[index];
          return Container(
            width: 280,
            margin: const EdgeInsets.symmetric(horizontal: 4, vertical: 8),
            decoration: BoxDecoration(
              color: Theme.of(context).cardColor,
              borderRadius: BorderRadius.circular(12),
              border: Border.all(color: Theme.of(context).dividerColor.withOpacity(0.08)),
              boxShadow: [
                BoxShadow(color: Colors.black12.withOpacity(0.05), blurRadius: 4, offset: const Offset(0, 2))
              ],
            ),
            child: InkWell(
              onTap: () => controller.navigateTo(ActiveScreen.courseDetails, itemId: course.id),
              borderRadius: BorderRadius.circular(12),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  ClipRRect(
                    borderRadius: const BorderRadius.vertical(top: Radius.circular(12)),
                    child: Image.network(
                      course.coverUrl,
                      height: 120,
                      width: double.infinity,
                      fit: BoxFit.cover,
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.all(12.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          course.category,
                          style: TextStyle(fontSize: 9, color: Theme.of(context).primaryColor, fontWeight: FontWeight.bold),
                        ),
                        const SizedBox(height: 4),
                        Text(
                          course.title,
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                          style: const TextStyle(fontSize: 14, fontWeight: FontWeight.bold),
                        ),
                        const SizedBox(height: 4),
                        Text(
                          course.instructor,
                          style: TextStyle(fontSize: 11, color: Theme.of(context).hintColor),
                        ),
                        const SizedBox(height: 8),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text(
                              "৳${course.price.toStringAsFixed(0)}",
                              style: TextStyle(fontSize: 14, fontWeight: FontWeight.black, color: Theme.of(context).primaryColor),
                            ),
                            Row(
                              children: [
                                const Icon(Icons.star, color: Colors.amber, size: 14),
                                const SizedBox(width: 2),
                                Text(course.rating.toString(), style: const TextStyle(fontSize: 11, fontWeight: FontWeight.bold)),
                              ],
                            )
                          ],
                        )
                      ],
                    ),
                  )
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  Widget _buildBooksSlider(BuildContext context, BanglaController controller, bool isBestSellerOnly) {
    final filtered = isBestSellerOnly ? controller.books.where((b) => b.isBestSeller).toList() : controller.books;
    return SizedBox(
      height: 240,
      child: ListView.builder(
        scrollDirection: Axis.horizontal,
        padding: const EdgeInsets.symmetric(horizontal: 12),
        itemCount: filtered.length,
        itemBuilder: (context, index) {
          final book = filtered[index];
          return Container(
            width: 150,
            margin: const EdgeInsets.symmetric(horizontal: 4, vertical: 8),
            decoration: BoxDecoration(
              color: Theme.of(context).cardColor,
              borderRadius: BorderRadius.circular(12),
              border: Border.all(color: Theme.of(context).dividerColor.withOpacity(0.08)),
            ),
            child: InkWell(
              onTap: () => controller.navigateTo(ActiveScreen.bookDetails, itemId: book.id),
              borderRadius: BorderRadius.circular(12),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  ClipRRect(
                    borderRadius: const BorderRadius.vertical(top: Radius.circular(12)),
                    child: Image.network(
                      book.coverUrl,
                      height: 120,
                      width: double.infinity,
                      fit: BoxFit.cover,
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          book.title,
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                          style: const TextStyle(fontSize: 12, fontWeight: FontWeight.bold),
                        ),
                        const SizedBox(height: 2),
                        Text(
                          book.authorName,
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                          style: TextStyle(fontSize: 10, color: Theme.of(context).hintColor),
                        ),
                        const SizedBox(height: 6),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text(
                              "৳${book.price.toStringAsFixed(0)}",
                              style: TextStyle(fontSize: 12, fontWeight: FontWeight.bold, color: Theme.of(context).primaryColor),
                            ),
                            Container(
                              padding: const EdgeInsets.symmetric(horizontal: 4, vertical: 2),
                              decoration: BoxDecoration(color: Colors.green.withOpacity(0.1), borderRadius: BorderRadius.circular(4)),
                              child: const Text("ই-বুক", style: TextStyle(color: Colors.green, fontSize: 8, fontWeight: FontWeight.bold)),
                            )
                          ],
                        )
                      ],
                    ),
                  )
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  Widget _buildAuthorsSlider(BuildContext context, BanglaController controller) {
    return SizedBox(
      height: 160,
      child: ListView.builder(
        scrollDirection: Axis.horizontal,
        padding: const EdgeInsets.symmetric(horizontal: 12),
        itemCount: controller.authors.length,
        itemBuilder: (context, index) {
          final author = controller.authors[index];
          return Container(
            width: 140,
            margin: const EdgeInsets.symmetric(horizontal: 4, vertical: 8),
            child: InkWell(
              onTap: () => controller.navigateTo(ActiveScreen.authorDetails, itemId: author.id),
              borderRadius: BorderRadius.circular(12),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CircleAvatar(
                    radius: 35,
                    backgroundImage: NetworkImage(author.photoUrl),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    author.name,
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                    style: const TextStyle(fontSize: 13, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 2),
                  Text(
                    "${author.booksCount}টি প্রকাশনা",
                    style: TextStyle(fontSize: 10, color: Theme.of(context).hintColor),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}
