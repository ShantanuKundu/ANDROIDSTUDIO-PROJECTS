import 'package:flutter/material.dart';
import '../data/college_data.dart';
import 'detail_screen.dart';

class CoursesScreen extends StatelessWidget {
  const CoursesScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Academic Courses')),
      body: ListView.builder(
        padding: const EdgeInsets.all(12),
        itemCount: CollegeData.courses.length,
        itemBuilder: (context, index) {
          final course = CollegeData.courses[index];
          return Card(
            margin: const EdgeInsets.symmetric(vertical: 6),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12),
            ),
            child: ListTile(
              contentPadding: const EdgeInsets.all(14),
              leading: CircleAvatar(
                backgroundColor: const Color(0xFF0D47A1),
                foregroundColor: Colors.white,
                child: Text(
                  course.code.split('-').last,
                  style: const TextStyle(fontSize: 11, fontWeight: FontWeight.bold),
                ),
              ),
              title: Text(
                course.title,
                style: const TextStyle(fontWeight: FontWeight.bold),
              ),
              subtitle: Padding(
                padding: const EdgeInsets.only(top: 4),
                child: Text('${course.duration} • Intake: ${course.intake}'),
              ),
              trailing: const Icon(Icons.arrow_forward_ios, size: 16),
              // Direct Navigation using MaterialPageRoute
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => DetailScreen(
                      appBarTitle: course.code,
                      headerTitle: course.title,
                      headerSubtitle: course.department,
                      icon: Icons.school,
                      details: {
                        'Duration': course.duration,
                        'Sanctioned Intake': '${course.intake} Seats',
                        'Eligibility': course.eligibility,
                        'Program Overview': course.description,
                      },
                    ),
                  ),
                );
              },
            ),
          );
        },
      ),
    );
  }
}