import 'package:flutter/material.dart';
import '../data/college_data.dart';
import 'detail_screen.dart';

class FacultyScreen extends StatelessWidget {
  const FacultyScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Faculty Directory')),
      body: ListView.builder(
        padding: const EdgeInsets.all(12),
        itemCount: CollegeData.facultyList.length,
        itemBuilder: (context, index) {
          final faculty = CollegeData.facultyList[index];
          return Card(
            margin: const EdgeInsets.symmetric(vertical: 6),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12),
            ),
            child: ListTile(
              contentPadding: const EdgeInsets.all(14),
              leading: CircleAvatar(
                radius: 26,
                backgroundColor: Colors.teal.shade700,
                child: ClipOval(
                  child: Image.asset(
                    faculty.imagePath,
                    width: 52,
                    height: 52,
                    fit: BoxFit.cover,
                    errorBuilder: (context, error, stackTrace) =>
                    const Icon(Icons.person, color: Colors.white),
                  ),
                ),
              ),
              title: Text(
                faculty.name,
                style: const TextStyle(fontWeight: FontWeight.bold),
              ),
              subtitle: Padding(
                padding: const EdgeInsets.only(top: 4),
                child: Text('${faculty.designation}\n${faculty.department}'),
              ),
              isThreeLine: true,
              trailing: const Icon(Icons.arrow_forward_ios, size: 16),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => DetailScreen(
                      appBarTitle: 'Faculty Profile',
                      headerTitle: faculty.name,
                      headerSubtitle: faculty.designation,
                      icon: Icons.person_pin,
                      imagePath: faculty.imagePath,
                      details: {
                        'Department': faculty.department,
                        'Qualification': faculty.qualification,
                        'Specialization': faculty.specialization,
                        'Official Email': faculty.email,
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