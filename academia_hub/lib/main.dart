import 'package:flutter/material.dart';
import 'routes/app_routes.dart';

void main() {
  runApp(const AcademiaHub());
}

class AcademiaHub extends StatelessWidget {
  const AcademiaHub({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Academia Hub',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: Colors.indigo,
        ),
        useMaterial3: true,
      ),
      initialRoute: AppRoutes.home,
      routes: AppRoutes.getRoutes(),
    );
  }
}