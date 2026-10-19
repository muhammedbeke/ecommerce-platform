import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:google_fonts/google_fonts.dart';
import 'services/api_service.dart';
import 'screens/auth/login_screen.dart';
import 'screens/home_screen.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  final prefs = await SharedPreferences.getInstance();
  final String? savedToken = prefs.getString('jwt_token');

  bool validLogin = false;
  if (savedToken != null && savedToken.isNotEmpty) {
    if (!ApiService.isTokenExpired(savedToken)) {
      ApiService.token = savedToken;
      validLogin = true;
    } else {
      await prefs.remove('jwt_token');
    }
  }

  runApp(MyApp(isLoggedIn: validLogin));
}

class MyApp extends StatelessWidget {
  final bool isLoggedIn;

  const MyApp({super.key, required this.isLoggedIn});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'SmartShop E-Commerce',
      debugShowCheckedModeBanner: false,
      navigatorKey: ApiService.navigatorKey,
      routes: {'/login': (context) => const LoginScreen()},

      theme: ThemeData(
        useMaterial3: true,
        brightness: Brightness.light,
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF3F51B5),
          primary: const Color(0xFF3F51B5),
          secondary: const Color(0xFF64748B),
          surface: const Color(0xFFF8FAFC),
        ),
        textTheme: GoogleFonts.interTextTheme(
          ThemeData(brightness: Brightness.light).textTheme,
        ),

        appBarTheme: const AppBarTheme(
          backgroundColor: Color(0xFFF8FAFC),
          foregroundColor: Color(0xFF1E293B),
          elevation: 0,
          centerTitle: false,
        ),
        cardTheme: const CardThemeData(color: Colors.white, elevation: 2),
      ),

      // Dark Theme Design
      darkTheme: ThemeData(
        useMaterial3: true,
        brightness: Brightness.dark,
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF3F51B5),
          brightness: Brightness.dark,
          primary: const Color(0xFF7986CB),
          secondary: const Color(0xFF94A3B8),
          surface: const Color(0xFF0F172A),
        ),
        textTheme: GoogleFonts.interTextTheme(
          ThemeData(brightness: Brightness.dark).textTheme,
        ),

        appBarTheme: const AppBarTheme(
          backgroundColor: Color(0xFF0F172A),
          foregroundColor: Colors.white,
          elevation: 0,
          centerTitle: false,
        ),
        cardTheme: const CardThemeData(color: Color(0xFF1E293B), elevation: 2),
      ),

      themeMode: ThemeMode.system, // Dynamically follow the system settings
      home: isLoggedIn ? const HomeScreen() : const LoginScreen(),
    );
  }
}
