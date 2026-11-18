import 'package:flutter/material.dart';
import '../data/college_data.dart';

class ContactScreen extends StatefulWidget {
  const ContactScreen({super.key});

  @override
  State<ContactScreen> createState() => _ContactScreenState();
}

class _ContactScreenState extends State<ContactScreen> {
  final _formKey = GlobalKey<FormState>();
  final _nameController = TextEditingController();
  final _emailController = TextEditingController();
  final _messageController = TextEditingController();

  @override
  void dispose() {
    _nameController.dispose();
    _emailController.dispose();
    _messageController.dispose();
    super.dispose();
  }

  void _submitEnquiry() {
    if (_formKey.currentState!.validate()) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(
            'Thank you, ${_nameController.text}! Your enquiry has been submitted.',
          ),
          backgroundColor: Colors.green.shade700,
        ),
      );
      _nameController.clear();
      _emailController.clear();
      _messageController.clear();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Contact Us')),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Card(
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(12),
              ),
              child: const Column(
                children: [
                  ListTile(
                    leading: Icon(Icons.location_on, color: Color(0xFF0D47A1)),
                    title: Text('Campus Address'),
                    subtitle: Text(CollegeData.address),
                  ),
                  Divider(height: 1),
                  ListTile(
                    leading: Icon(Icons.phone, color: Color(0xFF0D47A1)),
                    title: Text('Phone'),
                    subtitle: Text(CollegeData.phone),
                  ),
                  Divider(height: 1),
                  ListTile(
                    leading: Icon(Icons.email, color: Color(0xFF0D47A1)),
                    title: Text('Email'),
                    subtitle: Text(CollegeData.email),
                  ),
                  Divider(height: 1),
                  ListTile(
                    leading: Icon(Icons.language, color: Color(0xFF0D47A1)),
                    title: Text('Website'),
                    subtitle: Text(CollegeData.website),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 20),
            const Text(
              'Admission / General Enquiry',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 10),
            Form(
              key: _formKey,
              child: Column(
                children: [
                  TextFormField(
                    controller: _nameController,
                    decoration: const InputDecoration(
                      labelText: 'Full Name',
                      border: OutlineInputBorder(),
                      prefixIcon: Icon(Icons.person),
                    ),
                    validator: (value) => (value == null || value.trim().isEmpty)
                        ? 'Please enter your name'
                        : null,
                  ),
                  const SizedBox(height: 12),
                  TextFormField(
                    controller: _emailController,
                    keyboardType: TextInputType.emailAddress,
                    decoration: const InputDecoration(
                      labelText: 'Email Address',
                      border: OutlineInputBorder(),
                      prefixIcon: Icon(Icons.email),
                    ),
                    validator: (value) =>
                    (value == null || !value.contains('@'))
                        ? 'Enter a valid email address'
                        : null,
                  ),
                  const SizedBox(height: 12),
                  TextFormField(
                    controller: _messageController,
                    maxLines: 3,
                    decoration: const InputDecoration(
                      labelText: 'Your Query',
                      border: OutlineInputBorder(),
                      alignLabelWithHint: true,
                    ),
                    validator: (value) => (value == null || value.trim().isEmpty)
                        ? 'Please enter your query'
                        : null,
                  ),
                  const SizedBox(height: 16),
                  SizedBox(
                    width: double.infinity,
                    height: 48,
                    child: ElevatedButton.icon(
                      style: ElevatedButton.styleFrom(
                        backgroundColor: const Color(0xFF0D47A1),
                        foregroundColor: Colors.white,
                      ),
                      onPressed: _submitEnquiry,
                      icon: const Icon(Icons.send),
                      label: const Text('Submit Enquiry'),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}