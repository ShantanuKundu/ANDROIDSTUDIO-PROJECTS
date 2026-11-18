import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:provider/provider.dart';
import 'package:todo_app/main.dart';
import 'package:todo_app/providers/task_provider.dart';

void main() {
  testWidgets('TaskFlow app smoke test', (WidgetTester tester) async {
    await tester.pumpWidget(
      ChangeNotifierProvider(
        create: (_) => TaskProvider(),
        child: const TaskFlowApp(),
      ),
    );

    expect(find.text('TaskFlow'), findsOneWidget);
    expect(find.byIcon(Icons.add), findsOneWidget);
  });
}