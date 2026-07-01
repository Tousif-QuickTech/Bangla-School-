import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_animate/flutter_animate.dart';
import '../controllers/bangla_controller.dart';
import '../models/bangla_models.dart';

class CourseListView extends StatelessWidget {
  const CourseListView({Key? key}) : super(key: key);

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
                const Text("অনলাইন কোর্সসমূহ", style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                Text("${controller.courses.length}টি কোর্স উপলব্ধ", style: TextStyle(color: Theme.of(context).primaryColor, fontSize: 12)),
              ],
            ),
          ),
          Expanded(
            child: ListView.builder(
              padding: const EdgeInsets.symmetric(horizontal: 16, bottom: 16),
              itemCount: controller.courses.length,
              itemBuilder: (context, index) {
                final course = controller.courses[index];
                return Container(
                  margin: const EdgeInsets.only(bottom: 12),
                  decoration: BoxDecoration(
                    color: Theme.of(context).cardColor,
                    borderRadius: BorderRadius.circular(16),
                    border: Border.all(color: Theme.of(context).dividerColor.withOpacity(0.08)),
                  ),
                  child: InkWell(
                    onTap: () => controller.navigateTo(ActiveScreen.courseDetails, itemId: course.id),
                    borderRadius: BorderRadius.circular(16),
                    child: Row(
                      children: [
                        ClipRRect(
                          borderRadius: const BorderRadius.horizontal(left: Radius.circular(16)),
                          child: Image.network(
                            course.coverUrl,
                            height: 110,
                            width: 110,
                            fit: BoxFit.cover,
                          ),
                        ),
                        Expanded(
                          child: Padding(
                            padding: const EdgeInsets.all(12.0),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  course.category,
                                  style: TextStyle(fontSize: 10, color: Theme.of(context).primaryColor, fontWeight: FontWeight.bold),
                                ),
                                const SizedBox(height: 4),
                                Text(
                                  course.title,
                                  maxLines: 1,
                                  overflow: TextOverflow.ellipsis,
                                  style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14),
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
                                      style: TextStyle(fontWeight: FontWeight.black, color: Theme.of(context).primaryColor, fontSize: 13),
                                    ),
                                    Row(
                                      children: [
                                        const Icon(Icons.star, color: Colors.amber, size: 12),
                                        const SizedBox(width: 2),
                                        Text(course.rating.toString(), style: const TextStyle(fontSize: 11, fontWeight: FontWeight.bold)),
                                      ],
                                    )
                                  ],
                                )
                              ],
                            ),
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

class CourseDetailsView extends StatelessWidget {
  final String courseId;
  const CourseDetailsView({Key? key, required this.courseId}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final controller = Provider.of<BanglaController>(context);
    final course = controller.courses.firstWhere((c) => c.id == courseId, orElse: () => controller.courses.first);
    final isEnrolled = controller.userProfile.enrolledCourses.contains(course.id);

    return Scaffold(
      appBar: AppBar(
        title: Text(course.title, style: const TextStyle(fontSize: 16)),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => controller.navigateTo(ActiveScreen.courses),
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            ClipRRect(
              borderRadius: BorderRadius.circular(16),
              child: Image.network(course.coverUrl, height: 180, width: double.infinity, fit: BoxFit.cover),
            ).animate().fadeIn(duration: 300.ms),
            const SizedBox(height: 20),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
              decoration: BoxDecoration(
                color: Theme.of(context).primaryColor.withOpacity(0.1),
                borderRadius: BorderRadius.circular(6),
              ),
              child: Text(
                course.category,
                style: TextStyle(color: Theme.of(context).primaryColor, fontSize: 10, fontWeight: FontWeight.bold),
              ),
            ),
            const SizedBox(height: 12),
            Text(course.title, style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
            const SizedBox(height: 6),
            Text("প্রশিক্ষক: ${course.instructor}", style: TextStyle(fontSize: 14, color: Theme.of(context).hintColor, fontWeight: FontWeight.bold)),
            const SizedBox(height: 12),
            Row(
              children: [
                Icon(Icons.star, color: Colors.amber[700], size: 18),
                const SizedBox(width: 4),
                Text(course.rating.toString(), style: const TextStyle(fontWeight: FontWeight.bold)),
                const SizedBox(width: 12),
                Icon(Icons.people_outline, color: Theme.of(context).hintColor, size: 18),
                const SizedBox(width: 4),
                Text("${course.enrolledCount} জন শিক্ষার্থী ভর্তি হয়েছেন", style: TextStyle(color: Theme.of(context).hintColor, fontSize: 12)),
              ],
            ),
            const Divider(height: 24),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                _buildInfoColumn(context, "মোট সময়কাল", course.duration),
                _buildInfoColumn(context, "মোট লেকচার", "${course.lecturesCount}টি ভিডিও"),
                _buildInfoColumn(context, "অ্যাক্সেস", "আজীবন"),
              ],
            ),
            const Divider(height: 24),
            const Text("কোর্স পরিচিতি ও রূপরেখা", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
            const SizedBox(height: 8),
            Text(course.description, style: TextStyle(height: 1.5, color: Theme.of(context).textTheme.bodyMedium?.color?.withOpacity(0.9))),
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
                  if (isEnrolled) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text("আপনি ইতিপূর্বে এই কোর্সে ভর্তি হয়েছেন!")),
                    );
                  } else {
                    controller.enrollInCourse(course.id);
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text("${course.title} কোর্সে ভর্তি সফল হয়েছে!")),
                    );
                  }
                },
                child: Text(
                  isEnrolled ? "আপনি এই কোর্সে যুক্ত আছেন" : "৳${course.price.toStringAsFixed(0)} - কোর্সে ভর্তি হোন",
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
