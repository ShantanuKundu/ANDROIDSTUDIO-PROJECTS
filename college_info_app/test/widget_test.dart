import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:college_info_app/main.dart';

void main() {
  testWidgets('College Info App loads Home screen and navigates properly',
          (WidgetTester tester) async {
        // Build our app and trigger a frame
        await tester.pumpWidget(const CollegeInfoApp());

        // Verify Home Screen loads with college title and navigation cards
        expect(find.text('College Info Portal'), findsOneWidget);
        expect(find.text('Courses'), findsOneWidget);
        expect(find.text('Faculty'), findsOneWidget);

        // Test Named-Route Navigation: Tap the 'Courses' card
        await tester.tap(find.text('Courses'));
        await tester.pumpAndSettle();

        // Verify navigation to Courses Screen
        expect(find.text('Academic Courses'), findsOneWidget);
        expect(find.text('Master of Computer Applications'), findsOneWidget);

        // Test Direct Navigation: Tap on the MCA course item
        await tester.tap(find.text('Master of Computer Applications'));
        await tester.pumpAndSettle();

        // Verify navigation to Detail Screen
        expect(find.text('Duration'), findsOneWidget);
        expect(find.text('2 Years (4 Semesters)'), findsOneWidget);
      });
}