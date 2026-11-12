import 'package:flutter/material.dart';

class FacultyScreen extends StatelessWidget {
  const FacultyScreen({super.key});

  final List<Map<String, String>> faculty = const [
  {
  'name': 'Dr. Amit Sharma',
  'department': 'Computer Engineering',
  },
  {
  'name': 'Prof. Priya Verma',
  'department': 'Information Technology',
  },
  {
  'name': 'Prof. Rahul Mehta',
  'department': 'Management Studies',
  },
  {
  'name': 'Dr. Neha Patel',
  'department': 'Applied Sciences',
  },
  ];

  @override
  Widget build(BuildContext context) {
  return Scaffold(
  appBar: AppBar(title: const Text('Our Faculty')),
  body: ListView.builder(
  padding: const EdgeInsets.all(12),
  itemCount: faculty.length,
  itemBuilder: (context, index) {
  return Card(
  child: ListTile(
  leading: const CircleAvatar(
  child: Icon(Icons.person),
  ),
  title: Text(faculty[index]['name']!),
  subtitle: Text(
  faculty[index]['department']!,
  ),
  ),
  );
  },
  ),
  );
  }
}