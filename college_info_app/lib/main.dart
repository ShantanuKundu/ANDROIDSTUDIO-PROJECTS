import 'package:flutter/material.dart';
import 'screens/home_screen.dart';
import 'screens/courses_screen.dart';
import 'screens/faculty_screen.dart';
import 'screens/contact_screen.dart';

void main() {
  runApp(const CollegeInfoApp());
}

class CollegeInfoApp extends StatelessWidget {
  const CollegeInfoApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'College Information App',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFF0D47A1)),
        useMaterial3: true,
        appBarTheme: const AppBarTheme(
          backgroundColor: Color(0xFF0D47A1),
          foregroundColor: Colors.white,
          centerTitle: true,
        ),
      ),
      // Named-Route Navigation Configuration
      initialRoute: '/',
      routes: {
        '/': (context) => const HomeScreen(),
        '/courses': (context) => const CoursesScreen(),
        '/faculty': (context) => const FacultyScreen(),
        '/contact': (context) => const ContactScreen(),
      },
    );
  }
}