import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_animate/flutter_animate.dart';
import '../controllers/bangla_controller.dart';

class AuthView extends StatefulWidget {
  const AuthView({Key? key}) : super(key: key);

  @override
  State<AuthView> createState() => _AuthViewState();
}

class _AuthViewState extends State<AuthView> {
  final _emailController = TextEditingController();
  final _passController = TextEditingController();
  bool _isSignUp = false;

  @override
  void dispose() {
    _emailController.dispose();
    _passController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final controller = Provider.of<BanglaController>(context);

    return Scaffold(
      body: Center(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              // Logo
              Icon(Icons.school, size: 64, color: Theme.of(context).primaryColor).animate().scale(duration: 400.ms),
              const SizedBox(height: 12),
              const Text("বাংলা স্কুল", style: TextStyle(fontSize: 22, fontWeight: FontWeight.black)),
              const SizedBox(height: 4),
              const Text("ধ্রুপদী ও আধুনিক বাংলা সাহিত্য চর্চা কেন্দ্র", style: TextStyle(fontSize: 12, color: Colors.grey)),
              const SizedBox(height: 32),

              Card(
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                elevation: 4,
                child: Padding(
                  padding: const EdgeInsets.all(20.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      Text(
                        _isSignUp ? "নতুন অ্যাকাউন্ট তৈরি করুন" : "অ্যাকাউন্টে লগইন করুন",
                        textAlign: TextAlign.center,
                        style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(height: 20),
                      TextField(
                        controller: _emailController,
                        style: const TextStyle(fontSize: 13),
                        decoration: const InputDecoration(
                          labelText: "ইমেইল অ্যাড্রেস",
                          prefixIcon: Icon(Icons.email_outlined, size: 18),
                          border: OutlineInputBorder(),
                        ),
                      ),
                      const SizedBox(height: 12),
                      TextField(
                        controller: _passController,
                        obscureText: true,
                        style: const TextStyle(fontSize: 13),
                        decoration: const InputDecoration(
                          labelText: "পাসওয়ার্ড",
                          prefixIcon: Icon(Icons.lock_outline, size: 18),
                          border: OutlineInputBorder(),
                        ),
                      ),
                      const SizedBox(height: 20),
                      ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Theme.of(context).primaryColor,
                          padding: const EdgeInsets.symmetric(vertical: 14),
                          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                        ),
                        onPressed: () {
                          final email = _emailController.text.trim();
                          final pass = _passController.text.trim();
                          if (email.isEmpty || pass.isEmpty) {
                            ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("দয়া করে ইমেইল ও পাসওয়ার্ড প্রদান করুন!")));
                            return;
                          }
                          controller.userLogin(email, pass);
                          controller.navigateTo(ActiveScreen.dashboard);
                          ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("স্বাগতম, ${controller.userProfile.username}!")));
                        },
                        child: Text(_isSignUp ? "সাইন আপ করুন" : "প্রবেশ করুন", style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                      ),
                      const SizedBox(height: 12),
                      TextButton(
                        onPressed: () {
                          setState(() {
                            _isSignUp = !_isSignUp;
                          });
                        },
                        child: Text(
                          _isSignUp ? "ইতিমধ্যে অ্যাকাউন্ট আছে? লগইন করুন" : "নতুন অ্যাকাউন্ট প্রয়োজন? সাইন আপ করুন",
                          style: TextStyle(color: Theme.of(context).primaryColor, fontSize: 12),
                        ),
                      )
                    ],
                  ),
                ).animate().slideY(begin: 0.1, end: 0, duration: 300.ms),
              )
            ],
          ),
        ),
      ),
    );
  }
}
