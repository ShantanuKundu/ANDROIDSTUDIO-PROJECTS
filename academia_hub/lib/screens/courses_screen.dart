import 'package:flutter/material.dart';

class CoursesScreen extends StatelessWidget {
  const CoursesScreen({super.key});

  final List<String> courses = const [
    'B.Tech - Computer Engineering',
    'BCA - Computer Applications',
    'BBA - Business Administration',
    'MCA - Computer Applications',
    'MBA - Business Administration',
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Our Courses')),
      body: ListView.builder(
        padding: const EdgeInsets.all(12),
        itemCount: courses.length,
        itemBuilder: (context, index) {
          return Card(
            child: ListTile(
              leading: const Icon(
                Icons.menu_book,
                color: Colors.indigo,
              ),
              title: Text(courses[index]),
              subtitle: const Text('Full-time program'),
            ),
          );
        },
      ),
    );
  }
}