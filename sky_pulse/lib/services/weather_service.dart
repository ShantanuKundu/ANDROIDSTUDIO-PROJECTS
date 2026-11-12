import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../models/weather_model.dart';


class WeatherService {
  // Comprehensive map covering all 28 States & 8 Union Territories
  static const Map<String, Map<String, Map<String, double>>> indiaData = {
    // --- 28 STATES ---
    'Andhra Pradesh': {
      'Visakhapatnam': {'lat': 17.6868, 'lon': 83.2185},
      'Vijayawada': {'lat': 16.5062, 'lon': 80.6480},
      'Tirupati': {'lat': 13.6288, 'lon': 79.4192},
      'Guntur': {'lat': 16.3067, 'lon': 80.4365},
    },
    'Arunachal Pradesh': {
      'Itanagar': {'lat': 27.0844, 'lon': 93.6053},
      'Tawang': {'lat': 27.5861, 'lon': 91.8594},
      'Pasighat': {'lat': 28.0667, 'lon': 95.3333},
    },
    'Assam': {
      'Guwahati': {'lat': 26.1445, 'lon': 91.7362},
      'Silchar': {'lat': 24.8333, 'lon': 92.7789},
      'Dibrugarh': {'lat': 27.4728, 'lon': 94.9120},
      'Jorhat': {'lat': 26.7509, 'lon': 94.2037},
    },
    'Bihar': {
      'Patna': {'lat': 25.5941, 'lon': 85.1376},
      'Gaya': {'lat': 24.7914, 'lon': 85.0002},
      'Muzaffarpur': {'lat': 26.1209, 'lon': 85.3647},
      'Bhagalpur': {'lat': 25.2425, 'lon': 87.0135},
    },
    'Chhattisgarh': {
      'Raipur': {'lat': 21.2514, 'lon': 81.6296},
      'Bhilai': {'lat': 21.1938, 'lon': 81.3509},
      'Bilaspur': {'lat': 22.0797, 'lon': 82.1391},
    },
    'Goa': {
      'Panaji': {'lat': 15.4909, 'lon': 73.8278},
      'Margao': {'lat': 15.2832, 'lon': 73.9862},
      'Vasco da Gama': {'lat': 15.3959, 'lon': 73.8157},
    },
    'Gujarat': {
      'Ahmedabad': {'lat': 23.0225, 'lon': 72.5714},
      'Surat': {'lat': 21.1702, 'lon': 72.8311},
      'Vadodara': {'lat': 22.3072, 'lon': 73.1812},
      'Rajkot': {'lat': 22.3039, 'lon': 70.8022},
    },
    'Haryana': {
      'Gurugram': {'lat': 28.4595, 'lon': 77.0266},
      'Faridabad': {'lat': 28.4089, 'lon': 77.3178},
      'Panipat': {'lat': 29.3909, 'lon': 76.9635},
      'Ambala': {'lat': 30.3782, 'lon': 76.7767},
    },
    'Himachal Pradesh': {
      'Shimla': {'lat': 31.1048, 'lon': 77.1734},
      'Dharamshala': {'lat': 32.2190, 'lon': 76.3234},
      'Manali': {'lat': 32.2432, 'lon': 77.1892},
    },
    'Jharkhand': {
      'Ranchi': {'lat': 23.3441, 'lon': 85.3096},
      'Jamshedpur': {'lat': 22.8046, 'lon': 86.2029},
      'Dhanbad': {'lat': 23.7957, 'lon': 86.4304},
    },
    'Karnataka': {
      'Bengaluru': {'lat': 12.9716, 'lon': 77.5946},
      'Mysuru': {'lat': 12.2958, 'lon': 76.6394},
      'Hubballi': {'lat': 15.3647, 'lon': 75.1240},
      'Mangaluru': {'lat': 12.9141, 'lon': 74.8560},
    },
    'Kerala': {
      'Thiruvananthapuram': {'lat': 8.5241, 'lon': 76.9366},
      'Kochi': {'lat': 9.9312, 'lon': 76.2673},
      'Kozhikode': {'lat': 11.2588, 'lon': 75.7804},
    },
    'Madhya Pradesh': {
      'Bhopal': {'lat': 23.2599, 'lon': 77.4126},
      'Indore': {'lat': 22.7196, 'lon': 75.8577},
      'Gwalior': {'lat': 26.2183, 'lon': 78.1828},
      'Jabalpur': {'lat': 23.1815, 'lon': 79.9864},
    },
    'Maharashtra': {
      'Mumbai': {'lat': 19.0760, 'lon': 72.8777},
      'Pune': {'lat': 18.5204, 'lon': 73.8567},
      'Nagpur': {'lat': 21.1458, 'lon': 79.0882},
      'Nashik': {'lat': 19.9975, 'lon': 73.7898},
      'Chhatrapati Sambhajinagar': {'lat': 19.8762, 'lon': 75.3433},
    },
    'Manipur': {
      'Imphal': {'lat': 24.8170, 'lon': 93.9368},
      'Churachandpur': {'lat': 24.3333, 'lon': 93.6833},
    },
    'Meghalaya': {
      'Shillong': {'lat': 25.5788, 'lon': 91.8933},
      'Tura': {'lat': 25.5141, 'lon': 90.2032},
    },
    'Mizoram': {
      'Aizawl': {'lat': 23.7271, 'lon': 92.7176},
      'Lunglei': {'lat': 22.8833, 'lon': 92.7333},
    },
    'Nagaland': {
      'Kohima': {'lat': 25.6751, 'lon': 94.1086},
      'Dimapur': {'lat': 25.9060, 'lon': 93.7271},
    },
    'Odisha': {
      'Bhubaneswar': {'lat': 20.2961, 'lon': 85.8245},
      'Cuttack': {'lat': 20.4625, 'lon': 85.8828},
      'Rourkela': {'lat': 22.2604, 'lon': 84.8536},
      'Puri': {'lat': 19.8135, 'lon': 85.8312},
    },
    'Punjab': {
      'Ludhiana': {'lat': 30.9010, 'lon': 75.8573},
      'Amritsar': {'lat': 31.6340, 'lon': 74.8723},
      'Jalandhar': {'lat': 31.3260, 'lon': 75.5762},
      'Patiala': {'lat': 30.3398, 'lon': 76.3869},
    },
    'Rajasthan': {
      'Jaipur': {'lat': 26.9124, 'lon': 75.7873},
      'Jodhpur': {'lat': 26.2389, 'lon': 73.0243},
      'Udaipur': {'lat': 24.5854, 'lon': 73.7125},
      'Kota': {'lat': 25.2138, 'lon': 75.8648},
    },
    'Sikkim': {
      'Gangtok': {'lat': 27.3389, 'lon': 88.6065},
      'Namchi': {'lat': 27.1667, 'lon': 88.3500},
    },
    'Tamil Nadu': {
      'Chennai': {'lat': 13.0827, 'lon': 80.2707},
      'Coimbatore': {'lat': 11.0168, 'lon': 76.9558},
      'Madurai': {'lat': 9.9252, 'lon': 78.1198},
      'Tiruchirappalli': {'lat': 10.7905, 'lon': 78.7047},
    },
    'Telangana': {
      'Hyderabad': {'lat': 17.3850, 'lon': 78.4867},
      'Warangal': {'lat': 17.9689, 'lon': 79.5941},
      'Nizamabad': {'lat': 18.6725, 'lon': 78.0941},
    },
    'Tripura': {
      'Agartala': {'lat': 23.8315, 'lon': 91.2868},
      'Dharmanagar': {'lat': 24.3667, 'lon': 92.1667},
    },
    'Uttar Pradesh': {
      'Lucknow': {'lat': 26.8467, 'lon': 80.9462},
      'Kanpur': {'lat': 26.4499, 'lon': 80.3319},
      'Varanasi': {'lat': 25.3176, 'lon': 82.9739},
      'Agra': {'lat': 27.1767, 'lon': 78.0081},
      'Noida': {'lat': 28.5355, 'lon': 77.3910},
    },
    'Uttarakhand': {
      'Dehradun': {'lat': 30.3165, 'lon': 78.0322},
      'Haridwar': {'lat': 29.9457, 'lon': 78.1642},
      'Nainital': {'lat': 29.3803, 'lon': 79.4636},
    },
    'West Bengal': {
      'Kolkata': {'lat': 22.5726, 'lon': 88.3639},
      'Siliguri': {'lat': 26.7271, 'lon': 88.3953},
      'Durgapur': {'lat': 23.5204, 'lon': 87.3119},
    },


    // --- 8 UNION TERRITORIES ---
    'Andaman & Nicobar Islands': {
      'Port Blair': {'lat': 11.6233, 'lon': 92.7265},
    },
    'Chandigarh': {
      'Chandigarh': {'lat': 30.7333, 'lon': 76.7794},
    },
    'Dadra & Nagar Haveli and Daman & Diu': {
      'Daman': {'lat': 20.3974, 'lon': 72.8328},
      'Silvassa': {'lat': 20.2763, 'lon': 73.0083},
      'Diu': {'lat': 20.7144, 'lon': 70.9822},
    },
    'Delhi (UT)': {
      'New Delhi': {'lat': 28.6139, 'lon': 77.2090},
      'North Delhi': {'lat': 28.7041, 'lon': 77.1025},
      'South Delhi': {'lat': 28.5355, 'lon': 77.2500},
    },
    'Jammu & Kashmir': {
      'Srinagar': {'lat': 34.0837, 'lon': 74.7973},
      'Jammu': {'lat': 32.7266, 'lon': 74.8570},
      'Anantnag': {'lat': 33.7311, 'lon': 75.1485},
    },
    'Ladakh': {
      'Leh': {'lat': 34.1526, 'lon': 77.5771},
      'Kargil': {'lat': 34.5539, 'lon': 76.1349},
    },
    'Lakshadweep': {
      'Kavaratti': {'lat': 10.5669, 'lon': 72.6420},
    },
    'Puducherry': {
      'Puducherry': {'lat': 11.9416, 'lon': 79.8083},
      'Karaikal': {'lat': 10.9254, 'lon': 79.8380},
    },
  };


  // Fetch weather data from Open-Meteo API
  Future<WeatherModel> fetchWeather(String stateName, String cityName) async {
    final stateCities = indiaData[stateName];
    if (stateCities == null) throw Exception('State/UT not found');


    final coords = stateCities[cityName];
    if (coords == null) throw Exception('City coordinates not found');


    final url = Uri.parse(
      'https://api.open-meteo.com/v1/forecast?latitude=${coords['lat']}&longitude=${coords['lon']}&current_weather=true',
    );


    final response = await http.get(url);


    if (response.statusCode == 200) {
      final Map<String, dynamic> data = jsonDecode(response.body);
      return WeatherModel.fromJson(data, '$cityName, $stateName');
    } else {
      throw Exception('Failed to load weather data (${response.statusCode})');
    }
  }


  // Save selected state and city locally
  Future<void> saveDefaultLocation(String stateName, String cityName) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('saved_state', stateName);
    await prefs.setString('saved_city', cityName);
  }


  // Retrieve saved location or default to Maharashtra -> Mumbai
  Future<Map<String, String>> getSavedLocation() async {
    {
      final prefs = await SharedPreferences.getInstance();
      String state = prefs.getString('saved_state') ?? 'Maharashtra';
      String city = prefs.getString('saved_city') ?? 'Mumbai';


      if (!indiaData.containsKey(state) ||
          !indiaData[state]!.containsKey(city)) {
        state = 'Maharashtra';
        city = 'Mumbai';
      }


      return {'state': state, 'city': city};
    }
  }
}
