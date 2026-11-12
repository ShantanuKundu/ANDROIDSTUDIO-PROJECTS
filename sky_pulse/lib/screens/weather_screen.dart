import 'package:flutter/material.dart';
import '../models/weather_model.dart';
import '../services/weather_service.dart';

class WeatherScreen extends StatefulWidget {
  const WeatherScreen({super.key});

  @override
  State<WeatherScreen> createState() => _WeatherScreenState();
}

class _WeatherScreenState extends State<WeatherScreen> {
  final WeatherService _weatherService = WeatherService();

  String _selectedState = 'Maharashtra';
  String _selectedCity = 'Mumbai';

  Future<WeatherModel>? _weatherFuture;

  @override
  void initState() {
    super.initState();
    _loadSavedLocation();
  }

  Future<void> _loadSavedLocation() async {
    try {
      final location = await _weatherService.getSavedLocation();
      setState(() {
        _selectedState = location['state'] ?? 'Maharashtra';
        _selectedCity = location['city'] ?? 'Mumbai';
        _weatherFuture = _weatherService.fetchWeather(_selectedState, _selectedCity);
      });
    } catch (_) {
      setState(() {
        _weatherFuture = _weatherService.fetchWeather(_selectedState, _selectedCity);
      });
    }
  }

  void _fetchWeather() {
    setState(() {
      _weatherFuture = _weatherService.fetchWeather(_selectedState, _selectedCity);
    });
  }

  Future<void> _saveAsDefault() async {
    await _weatherService.saveDefaultLocation(_selectedState, _selectedCity);
    if (mounted) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('Default location saved as $_selectedCity, $_selectedState'),
        ),
      );
    }
  }

  String _getWeatherCondition(int code) {
    switch (code) {
      case 0:
        return 'Clear Sky';
      case 1:
        return 'Mainly Clear';
      case 2:
        return 'Partly Cloudy';
      case 3:
        return 'Overcast';
      case 45:
      case 48:
        return 'Foggy';
      case 51:
      case 53:
      case 55:
        return 'Drizzle';
      case 61:
      case 63:
      case 65:
        return 'Rainy';
      case 71:
      case 73:
      case 75:
        return 'Snowy';
      case 80:
      case 81:
      case 82:
        return 'Rain Showers';
      case 95:
      case 96:
      case 99:
        return 'Thunderstorm';
      default:
        return 'Weather Code: $code';
    }
  }

  IconData _getWeatherIcon(int code) {
    switch (code) {
      case 0:
      case 1:
        return Icons.wb_sunny;
      case 2:
      case 3:
        return Icons.cloud;
      case 45:
      case 48:
        return Icons.cloud_queue;
      case 51:
      case 53:
      case 55:
      case 61:
      case 63:
      case 65:
      case 80:
      case 81:
      case 82:
        return Icons.umbrella;
      case 71:
      case 73:
      case 75:
        return Icons.ac_unit;
      case 95:
      case 96:
      case 99:
        return Icons.flash_on;
      default:
        return Icons.wb_cloudy;
    }
  }

  @override
  Widget build(BuildContext context) {
    final states = WeatherService.indiaData.keys.toList()..sort();
    final cities = (WeatherService.indiaData[_selectedState]?.keys.toList() ?? [])..sort();

    return Scaffold(
      appBar: AppBar(
        title: const Text('SkyPulse Weather'),
        centerTitle: true,
        actions: [
          IconButton(
            icon: const Icon(Icons.bookmark_add_outlined),
            tooltip: 'Save as Default Location',
            onPressed: _saveAsDefault,
          ),
          IconButton(
            icon: const Icon(Icons.refresh),
            tooltip: 'Refresh Weather',
            onPressed: _fetchWeather,
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            Card(
              elevation: 2,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  children: [
                    DropdownButtonFormField<String>(
                      key: ValueKey(_selectedState),
                      initialValue: _selectedState,
                      decoration: const InputDecoration(
                        labelText: 'Select State / UT',
                        border: OutlineInputBorder(),
                      ),
                      isExpanded: true,
                      items: states.map((state) {
                        return DropdownMenuItem<String>(
                          value: state,
                          child: Text(state),
                        );
                      }).toList(),
                      onChanged: (newState) {
                        if (newState != null && newState != _selectedState) {
                          setState(() {
                            _selectedState = newState;
                            final availableCities = WeatherService.indiaData[_selectedState]?.keys.toList() ?? [];
                            _selectedCity = availableCities.isNotEmpty ? availableCities.first : '';
                          });
                          _fetchWeather();
                        }
                      },
                    ),
                    const SizedBox(height: 12),
                    DropdownButtonFormField<String>(
                      key: ValueKey('$_selectedState-$_selectedCity'),
                      initialValue: _selectedCity.isNotEmpty && cities.contains(_selectedCity) ? _selectedCity : (cities.isNotEmpty ? cities.first : null),
                      decoration: const InputDecoration(
                        labelText: 'Select City',
                        border: OutlineInputBorder(),
                      ),
                      isExpanded: true,
                      items: cities.map((city) {
                        return DropdownMenuItem<String>(
                          value: city,
                          child: Text(city),
                        );
                      }).toList(),
                      onChanged: (newCity) {
                        if (newCity != null && newCity != _selectedCity) {
                          setState(() {
                            _selectedCity = newCity;
                          });
                          _fetchWeather();
                        }
                      },
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 20),
            Expanded(
              child: _weatherFuture == null
                  ? const Center(child: CircularProgressIndicator())
                  : FutureBuilder<WeatherModel>(
                      future: _weatherFuture,
                      builder: (context, snapshot) {
                        if (snapshot.connectionState == ConnectionState.waiting) {
                          return const Center(child: CircularProgressIndicator());
                        } else if (snapshot.hasError) {
                          return Center(
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                const Icon(Icons.error_outline, size: 48, color: Colors.red),
                                const SizedBox(height: 8),
                                Text(
                                  'Error: ${snapshot.error}',
                                  textAlign: TextAlign.center,
                                  style: const TextStyle(color: Colors.red),
                                ),
                                const SizedBox(height: 16),
                                ElevatedButton.icon(
                                  onPressed: _fetchWeather,
                                  icon: const Icon(Icons.refresh),
                                  label: const Text('Retry'),
                                ),
                              ],
                            ),
                          );
                        } else if (snapshot.hasData) {
                          final weather = snapshot.data!;
                          return SingleChildScrollView(
                            child: Card(
                              elevation: 4,
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(16),
                              ),
                              child: Container(
                                padding: const EdgeInsets.all(24.0),
                                width: double.infinity,
                                child: Column(
                                  mainAxisSize: MainAxisSize.min,
                                  children: [
                                    Icon(
                                      _getWeatherIcon(weather.weatherCode),
                                      size: 80,
                                      color: Theme.of(context).colorScheme.primary,
                                    ),
                                    const SizedBox(height: 16),
                                    Text(
                                      weather.cityName,
                                      style: Theme.of(context).textTheme.headlineMedium?.copyWith(
                                            fontWeight: FontWeight.bold,
                                          ),
                                      textAlign: TextAlign.center,
                                    ),
                                    const SizedBox(height: 8),
                                    Text(
                                      _getWeatherCondition(weather.weatherCode),
                                      style: Theme.of(context).textTheme.titleLarge?.copyWith(
                                            color: Colors.grey[700],
                                          ),
                                    ),
                                    const SizedBox(height: 24),
                                    Row(
                                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                                      children: [
                                        _buildDetailItem(
                                          context,
                                          icon: Icons.thermostat,
                                          label: 'Temperature',
                                          value: '${weather.temperature}°C',
                                        ),
                                        _buildDetailItem(
                                          context,
                                          icon: Icons.air,
                                          label: 'Wind Speed',
                                          value: '${weather.windSpeed} km/h',
                                        ),
                                      ],
                                    ),
                                  ],
                                ),
                              ),
                            ),
                          );
                        }
                        return const Center(child: Text('No weather data available'));
                      },
                    ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildDetailItem(
    BuildContext context, {
    required IconData icon,
    required String label,
    required String value,
  }) {
    return Column(
      children: [
        Icon(icon, size: 32, color: Theme.of(context).colorScheme.primary),
        const SizedBox(height: 4),
        Text(
          label,
          style: Theme.of(context).textTheme.bodySmall?.copyWith(color: Colors.grey[600]),
        ),
        const SizedBox(height: 2),
        Text(
          value,
          style: Theme.of(context).textTheme.titleMedium?.copyWith(
                fontWeight: FontWeight.bold,
              ),
        ),
      ],
    );
  }
}
