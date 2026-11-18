import 'package:flutter/material.dart';

class DetailScreen extends StatelessWidget {
  final String appBarTitle;
  final String headerTitle;
  final String headerSubtitle;
  final IconData icon;
  final String? imagePath;
  final Map<String, String> details;

  const DetailScreen({
    super.key,
    required this.appBarTitle,
    required this.headerTitle,
    required this.headerSubtitle,
    required this.icon,
    this.imagePath,
    required this.details,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(appBarTitle)),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Card(
              color: const Color(0xFF0D47A1),
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(16),
              ),
              child: Padding(
                padding: const EdgeInsets.all(20.0),
                child: Column(
                  children: [
                    if (imagePath != null)
                      CircleAvatar(
                        radius: 42,
                        backgroundColor: Colors.white,
                        child: ClipOval(
                          child: Image.asset(
                            imagePath!,
                            width: 80,
                            height: 80,
                            fit: BoxFit.cover,
                            errorBuilder: (context, error, stackTrace) => Icon(
                              icon,
                              size: 48,
                              color: const Color(0xFF0D47A1),
                            ),
                          ),
                        ),
                      )
                    else
                      Icon(icon, size: 48, color: Colors.amberAccent),
                    const SizedBox(height: 12),
                    Text(
                      headerTitle,
                      textAlign: TextAlign.center,
                      style: const TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                        color: Colors.white,
                      ),
                    ),
                    const SizedBox(height: 6),
                    Text(
                      headerSubtitle,
                      textAlign: TextAlign.center,
                      style: const TextStyle(
                        fontSize: 14,
                        color: Colors.white70,
                      ),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 16),
            ...details.entries.map(
                  (entry) => Card(
                margin: const EdgeInsets.only(bottom: 10),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Padding(
                  padding: const EdgeInsets.all(14.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        entry.key,
                        style: TextStyle(
                          fontSize: 12,
                          fontWeight: FontWeight.bold,
                          color: Colors.blue.shade900,
                        ),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        entry.value,
                        style: const TextStyle(fontSize: 15),
                      ),
                    ],
                  ),
                ),
              ),
            ),
            const SizedBox(height: 12),
            ElevatedButton.icon(
              onPressed: () => Navigator.pop(context),
              icon: const Icon(Icons.arrow_back),
              label: const Text('Go Back'),
            ),
          ],
        ),
      ),
    );
  }
}