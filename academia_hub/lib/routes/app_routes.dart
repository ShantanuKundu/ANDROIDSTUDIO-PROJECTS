import 'package:flutter/material.dart';
import '../screens/home_screen.dart';
import '../screens/faculty_screen.dart';
import '../screens/contact_screen.dart';

class AppRoutes {
  static const String home = '/';
  static const String faculty = '/faculty';
  static const String contact = '/contact';

  static Map<String, WidgetBuilder> getRoutes() {
    return {
      home: (context) => const HomeScreen(),
      faculty: (context) => const FacultyScreen(),
      contact: (context) => const ContactScreen(),
    };
  }
}