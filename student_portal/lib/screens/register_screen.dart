import 'package:flutter/material.dart';
import '../models/student.dart';
import '../utils/validators.dart';

class RegisterScreen extends StatefulWidget {
  const RegisterScreen({super.key});

  @override
  State<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  final _formKey = GlobalKey<FormState>();

  final _nameController = TextEditingController();
  final _emailController = TextEditingController();
  final _mobileController = TextEditingController();
  final _passwordController = TextEditingController();
  final _dobController = TextEditingController();

  bool _obscurePassword = true;

  // Dropdown state
  String? _selectedCourse;
  final List<String> _courses = [
    'MCA',
    'B.E. Computer Engineering',
    'B.E. Information Technology',
    'B.Sc. IT',
    'M.Sc. Computer Science',
  ];

  // Radio button state
  String _selectedGender = 'Male';

  // Checkbox state
  final Map<String, bool> _skills = {
    'Flutter & Dart': false,
    'Python': false,
    'Java': false,
    'SQL / Databases': false,
  };
  bool _acceptedTerms = false;

  @override
  void dispose() {
    _nameController.dispose();
    _emailController.dispose();
    _mobileController.dispose();
    _passwordController.dispose();
    _dobController.dispose();
    super.dispose();
  }

  Future<void> _pickDate() async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime(2002, 1, 1),
      firstDate: DateTime(1985),
      lastDate: DateTime.now(),
    );

    if (picked != null) {
      setState(() {
        final day = picked.day.toString().padLeft(2, '0');
        final month = picked.month.toString().padLeft(2, '0');
        _dobController.text = '$day/$month/${picked.year}';
      });
    }
  }

  void _submitRegistration() {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    final selectedSkills = _skills.entries
        .where((entry) => entry.value)
        .map((entry) => entry.key)
        .toList();

    if (selectedSkills.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please select at least one skill')),
      );
      return;
    }

    if (!_acceptedTerms) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please accept the Terms & Conditions')),
      );
      return;
    }

    final newStudent = Student(
      fullName: _nameController.text.trim(),
      email: _emailController.text.trim(),
      mobile: _mobileController.text.trim(),
      password: _passwordController.text,
      course: _selectedCourse!,
      gender: _selectedGender,
      dateOfBirth: _dobController.text,
      skills: selectedSkills,
    );

    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(
        content: Text('Registration successful! Please log in.'),
        backgroundColor: Colors.green,
      ),
    );

    // Return the newly created Student object back to LoginScreen
    Navigator.pop(context, newStudent);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Student Registration'),
        centerTitle: true,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              // 1. Full Name
              TextFormField(
                controller: _nameController,
                textCapitalization: TextCapitalization.words,
                decoration: const InputDecoration(
                  labelText: 'Full Name',
                  prefixIcon: Icon(Icons.person),
                  border: OutlineInputBorder(),
                ),
                validator: AppValidators.validateName,
              ),
              const SizedBox(height: 16),

              // 2. Email
              TextFormField(
                controller: _emailController,
                keyboardType: TextInputType.emailAddress,
                decoration: const InputDecoration(
                  labelText: 'Email Address',
                  prefixIcon: Icon(Icons.email),
                  border: OutlineInputBorder(),
                ),
                validator: AppValidators.validateEmail,
              ),
              const SizedBox(height: 16),

              // 3. Mobile Number
              TextFormField(
                controller: _mobileController,
                keyboardType: TextInputType.phone,
                maxLength: 10,
                decoration: const InputDecoration(
                  labelText: 'Mobile Number',
                  prefixIcon: Icon(Icons.phone_android),
                  prefixText: '+91 ',
                  border: OutlineInputBorder(),
                ),
                validator: AppValidators.validateMobile,
              ),
              const SizedBox(height: 16),

              // 4. Password
              TextFormField(
                controller: _passwordController,
                obscureText: _obscurePassword,
                decoration: InputDecoration(
                  labelText: 'Password',
                  prefixIcon: const Icon(Icons.lock),
                  border: const OutlineInputBorder(),
                  suffixIcon: IconButton(
                    icon: Icon(
                      _obscurePassword
                          ? Icons.visibility_off
                          : Icons.visibility,
                    ),
                    onPressed: () {
                      setState(() {
                        _obscurePassword = !_obscurePassword;
                      });
                    },
                  ),
                ),
                validator: AppValidators.validatePassword,
              ),
              const SizedBox(height: 16),

              // 5. Dropdown (Course)
              DropdownButtonFormField<String>(
                value: _selectedCourse,
                decoration: const InputDecoration(
                  labelText: 'Select Course / Branch',
                  prefixIcon: Icon(Icons.school),
                  border: OutlineInputBorder(),
                ),
                items: _courses.map((course) {
                  return DropdownMenuItem(
                    value: course,
                    child: Text(course),
                  );
                }).toList(),
                onChanged: (value) {
                  setState(() {
                    _selectedCourse = value;
                  });
                },
                validator: (value) =>
                value == null ? 'Please select your course' : null,
              ),
              const SizedBox(height: 16),

              // 6. Date Picker (Date of Birth)
              TextFormField(
                controller: _dobController,
                readOnly: true,
                onTap: _pickDate,
                decoration: const InputDecoration(
                  labelText: 'Date of Birth',
                  hintText: 'DD/MM/YYYY',
                  prefixIcon: Icon(Icons.calendar_today),
                  border: OutlineInputBorder(),
                ),
                validator: (value) => (value == null || value.isEmpty)
                    ? 'Please select your date of birth'
                    : null,
              ),
              const SizedBox(height: 20),

              // 7. Radio Buttons (Gender)
              const Text(
                'Gender',
                style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
              ),
              Row(
                children: ['Male', 'Female', 'Other'].map((gender) {
                  return Expanded(
                    child: RadioListTile<String>(
                      contentPadding: EdgeInsets.zero,
                      title: Text(gender, style: const TextStyle(fontSize: 14)),
                      value: gender,
                      groupValue: _selectedGender,
                      onChanged: (value) {
                        setState(() {
                          _selectedGender = value!;
                        });
                      },
                    ),
                  );
                }).toList(),
              ),
              const SizedBox(height: 12),

              // 8. Checkboxes (Technical Skills)
              const Text(
                'Technical Skills',
                style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
              ),
              ..._skills.keys.map((skill) {
                return CheckboxListTile(
                  contentPadding: EdgeInsets.zero,
                  title: Text(skill),
                  value: _skills[skill],
                  controlAffinity: ListTileControlAffinity.leading,
                  onChanged: (bool? checked) {
                    setState(() {
                      _skills[skill] = checked ?? false;
                    });
                  },
                );
              }),
              const Divider(),

              // Terms & Conditions Checkbox
              CheckboxListTile(
                contentPadding: EdgeInsets.zero,
                title: const Text(
                  'I confirm that the above details are accurate.',
                  style: TextStyle(fontSize: 14),
                ),
                value: _acceptedTerms,
                controlAffinity: ListTileControlAffinity.leading,
                onChanged: (bool? checked) {
                  setState(() {
                    _acceptedTerms = checked ?? false;
                  });
                },
              ),
              const SizedBox(height: 20),

              // Register Button
              ElevatedButton(
                onPressed: _submitRegistration,
                style: ElevatedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  backgroundColor:
                  _acceptedTerms ? const Color(0xFF0D47A1) : Colors.grey[300],
                  foregroundColor:
                  _acceptedTerms ? Colors.white : Colors.black54,
                ),
                child: const Text(
                  'Register',
                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}