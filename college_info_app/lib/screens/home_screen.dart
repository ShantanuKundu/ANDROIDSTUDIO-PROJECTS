import 'package:flutter/material.dart';
import '../data/college_data.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('College Info Portal')),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Card(
              elevation: 2,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(16),
              ),
              child: Padding(
                padding: const EdgeInsets.all(20.0),
                child: Column(
                  children: [
                    Image.asset(
                      'assets/images/college_logo.jpg',
                      width: 100,
                      height: 100,
                      fit: BoxFit.contain,
                    ),
                    const SizedBox(height: 12),
                    const Text(
                      CollegeData.collegeName,
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.bold,
                        color: Color(0xFF0D47A1),
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'Affiliated to University of Mumbai • NAAC Accredited',
                      textAlign: TextAlign.center,
                      style: TextStyle(fontSize: 13, color: Colors.grey[700]),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 20),
            _buildMenuCard(
              context,
              title: 'Courses',
              subtitle: 'Explore UG & PG academic programs, intake & eligibility',
              icon: Icons.menu_book,
              color: Colors.blue.shade800,
              routeName: '/courses',
            ),
            const SizedBox(height: 12),
            _buildMenuCard(
              context,
              title: 'Faculty',
              subtitle: 'View department professors, qualifications & specializations',
              icon: Icons.people,
              color: Colors.teal.shade700,
              routeName: '/faculty',
            ),
            const SizedBox(height: 12),
            _buildMenuCard(
              context,
              title: 'Contact',
              subtitle: 'Campus address, helpline & admission enquiry form',
              icon: Icons.contact_mail,
              color: Colors.amber.shade800,
              routeName: '/contact',
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildMenuCard(
      BuildContext context, {
        required String title,
        required String subtitle,
        required IconData icon,
        required Color color,
        required String routeName,
      }) {
    return Card(
      elevation: 3,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
      child: ListTile(
        contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
        leading: CircleAvatar(
          radius: 24,
          backgroundColor: color.withOpacity(0.15),
          child: Icon(icon, size: 26, color: color),
        ),
        title: Text(
          title,
          style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
        ),
        subtitle: Padding(
          padding: const EdgeInsets.only(top: 4),
          child: Text(subtitle, style: const TextStyle(fontSize: 12)),
        ),
        trailing: const Icon(Icons.arrow_forward_ios, size: 16),
        onTap: () => Navigator.pushNamed(context, routeName),
      ),
    );
  }
}