import 'package:flutter_test/flutter_test.dart';
import 'package:sky_pulse/main.dart';

void main() {
  testWidgets('SkyPulse app smoke test', (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(const SkyPulseApp());

    // Verify that app title exists
    expect(find.text('SkyPulse Weather'), findsOneWidget);
  });
}
