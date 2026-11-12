import 'package:flutter/material.dart';
import '../routes/app_routes.dart';
import '../widgets/menu_button.dart';
import 'courses_screen.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Academia Hub'),
        backgroundColor: Colors.indigo,
        foregroundColor: Colors.white,
      ),
      body: Center(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(24),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Icon(
                Icons.school,
                size: 100,
                color: Colors.indigo,
              ),
              const SizedBox(height: 16),
              const Text(
                'Vivekanand Education Society Institute of Technology',
                textAlign: TextAlign.center,
                style: TextStyle(
                  fontSize: 25,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 10),
              const Text(
                'Learn Today, Lead Tomorrow!',
                style: TextStyle(fontSize: 16),
              ),
              const SizedBox(height: 30),

              // Direct navigation (push)
              MenuButton(
                title: 'Courses',
                icon: Icons.menu_book,
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => const CoursesScreen(),
                    ),
                  );
                },
              ),

              // Named-route navigation
              MenuButton(
                title: 'Faculty',
                icon: Icons.people,
                onPressed: () {
                  Navigator.pushNamed(context, AppRoutes.faculty);
                },
              ),

              MenuButton(
                title: 'Contact Us',
                icon: Icons.contact_mail,
                onPressed: () {
                  Navigator.pushNamed(context, AppRoutes.contact);
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}