import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

class BaseApiClient {
  static const String baseUrl = 'http://YOUR_LOCAL_IP:9191/api/v1';

  static final GlobalKey<NavigatorState> navigatorKey =
      GlobalKey<NavigatorState>();

  static String? token;

  static void checkResponse(http.Response response) {
    if (response.statusCode == 401) {
      saveToken(null);
      navigatorKey.currentState?.pushNamedAndRemoveUntil(
        '/login',
        (route) => false,
      );
    }
  }

  static Future<void> saveToken(String? tokenValue) async {
    token = tokenValue;
    final prefs = await SharedPreferences.getInstance();
    if (tokenValue != null) {
      await prefs.setString('jwt_token', tokenValue);
    } else {
      await prefs.remove('jwt_token');
    }
  }

  static bool isTokenExpired(String token) {
    try {
      final parts = token.split('.');
      if (parts.length != 3) return true;

      String payload = parts[1];
      switch (payload.length % 4) {
        case 2:
          payload += '==';
          break;
        case 3:
          payload += '=';
          break;
      }
      final resp = utf8.decode(base64Url.decode(payload));
      final Map<String, dynamic> payloadMap = jsonDecode(resp);

      if (payloadMap.containsKey('exp')) {
        final exp = payloadMap['exp'] as int;
        final expDateTime = DateTime.fromMillisecondsSinceEpoch(exp * 1000);
        return DateTime.now().isAfter(expDateTime);
      }
      return false;
    } catch (e) {
      return true;
    }
  }

  static Map<String, String> getHeaders() {
    return {
      'Content-Type': 'application/json',
      if (token != null) 'Authorization': 'Bearer $token',
    };
  }

  static String getFullImageUrl(String relativeUrl) {
    if (relativeUrl.isEmpty) return '';
    if (relativeUrl.startsWith('http')) return relativeUrl;

    String host = baseUrl;
    if (baseUrl.endsWith('/api/v1')) {
      host = baseUrl.substring(0, baseUrl.length - 7);
    }

    final cleanRelative = relativeUrl.startsWith('/')
        ? relativeUrl
        : '/$relativeUrl';
    return '$host$cleanRelative';
  }

  static Future<http.Response> get(String path) async {
    final response = await http.get(
      Uri.parse('$baseUrl$path'),
      headers: getHeaders(),
    );
    checkResponse(response);
    return response;
  }

  static Future<http.Response> post(String path, {dynamic body}) async {
    final response = await http.post(
      Uri.parse('$baseUrl$path'),
      headers: getHeaders(),
      body: body != null ? jsonEncode(body) : null,
    );
    checkResponse(response);
    return response;
  }

  static Future<http.Response> put(String path, {dynamic body}) async {
    final response = await http.put(
      Uri.parse('$baseUrl$path'),
      headers: getHeaders(),
      body: body != null ? jsonEncode(body) : null,
    );
    checkResponse(response);
    return response;
  }

  static Future<http.Response> delete(String path) async {
    final response = await http.delete(
      Uri.parse('$baseUrl$path'),
      headers: getHeaders(),
    );
    checkResponse(response);
    return response;
  }
}
