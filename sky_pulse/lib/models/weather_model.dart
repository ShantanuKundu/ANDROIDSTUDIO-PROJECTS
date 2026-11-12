class WeatherModel {
  final String cityName;
  final double temperature;
  final double windSpeed;
  final int weatherCode;


  WeatherModel({
    required this.cityName,
    required this.temperature,
    required this.windSpeed,
    required this.weatherCode,
  });


  factory WeatherModel.fromJson(Map<String, dynamic> json, String cityName) {
    final current = json['current_weather'];
    return WeatherModel(
      cityName: cityName,
      temperature: (current['temperature'] as num).toDouble(),
      windSpeed: (current['windspeed'] as num).toDouble(),
      weatherCode: current['weathercode'] as int,
    );
  }
}
