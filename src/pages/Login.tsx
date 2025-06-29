import React, { useState } from 'react';
import { Navigate } from 'react-router-dom';
import { Guitar as Hospital, Mail, Lock, AlertCircle } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login, isAuthenticated } = useAuth();

  if (isAuthenticated) {
    return <Navigate to="/" />;
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const success = await login(email, password);
      if (!success) {
        setError('Invalid email or password');
      }
    } catch (err) {
      setError('An error occurred during login');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-indigo-50 flex">
      {/* Left side - Hospital Image */}
      <div className="hidden lg:flex lg:w-1/2 relative overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-br from-blue-600/20 to-indigo-600/20 z-10"></div>
        <img
          src="https://images.pexels.com/photos/263402/pexels-photo-263402.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
          alt="Modern Hospital"
          className="w-full h-full object-cover"
        />
        <div className="absolute inset-0 z-20 flex flex-col justify-center items-center text-white p-12">
          <div className="text-center">
            <h1 className="text-5xl font-bold mb-6 drop-shadow-lg">
              Welcome to MedCare
            </h1>
            <p className="text-xl mb-8 drop-shadow-md max-w-md">
              Advanced Hospital Management System for modern healthcare facilities
            </p>
            <div className="flex items-center justify-center space-x-6 text-sm">
              <div className="flex items-center space-x-2">
                <div className="w-3 h-3 bg-white rounded-full"></div>
                <span>Patient Management</span>
              </div>
              <div className="flex items-center space-x-2">
                <div className="w-3 h-3 bg-white rounded-full"></div>
                <span>Appointment Scheduling</span>
              </div>
              <div className="flex items-center space-x-2">
                <div className="w-3 h-3 bg-white rounded-full"></div>
                <span>Electronic Health Records</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Right side - Login Form */}
      <div className="w-full lg:w-1/2 flex items-center justify-center p-8">
        <div className="max-w-md w-full space-y-8">
          <div className="bg-white rounded-2xl shadow-2xl p-8 border border-gray-100">
            <div className="text-center mb-8">
              <div className="flex justify-center mb-4">
                <div className="bg-blue-100 p-3 rounded-full">
                  <Hospital className="h-8 w-8 text-blue-600" />
                </div>
              </div>
              <h2 className="text-3xl font-bold text-gray-900">Sign In</h2>
              <p className="mt-2 text-gray-600">Access your MedCare account</p>
            </div>

            <form className="space-y-6" onSubmit={handleSubmit}>
              <div className="space-y-4">
                <div>
                  <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-2">
                    Email Address
                  </label>
                  <div className="relative">
                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                      <Mail className="h-5 w-5 text-gray-400" />
                    </div>
                    <input
                      id="email"
                      name="email"
                      type="email"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      required
                      className="block w-full pl-10 pr-3 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200 hover:border-gray-400"
                      placeholder="Enter your email"
                    />
                  </div>
                </div>

                <div>
                  <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-2">
                    Password
                  </label>
                  <div className="relative">
                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                      <Lock className="h-5 w-5 text-gray-400" />
                    </div>
                    <input
                      id="password"
                      name="password"
                      type="password"
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      required
                      className="block w-full pl-10 pr-3 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200 hover:border-gray-400"
                      placeholder="Enter your password"
                    />
                  </div>
                </div>
              </div>

              {error && (
                <div className="flex items-center space-x-2 text-red-600 bg-red-50 p-4 rounded-xl border border-red-200">
                  <AlertCircle className="h-5 w-5 flex-shrink-0" />
                  <span className="text-sm">{error}</span>
                </div>
              )}

              <button
                type="submit"
                disabled={loading}
                className="w-full flex justify-center py-3 px-4 border border-transparent rounded-xl shadow-sm text-white bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200 font-medium text-lg"
              >
                {loading ? (
                  <div className="flex items-center space-x-2">
                    <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                    <span>Signing in...</span>
                  </div>
                ) : (
                  'Sign In'
                )}
              </button>
            </form>

            {/* Demo Accounts Section */}
            <div className="mt-8 p-6 bg-gradient-to-r from-gray-50 to-blue-50 rounded-xl border border-gray-200">
              <h3 className="text-sm font-semibold text-gray-800 mb-4 flex items-center">
                <div className="w-2 h-2 bg-blue-500 rounded-full mr-2"></div>
                Demo Accounts
              </h3>
              <div className="space-y-3 text-xs">
                <div className="bg-white p-3 rounded-lg border border-gray-200 hover:shadow-sm transition-shadow">
                  <div className="flex items-center justify-between">
                    <div>
                      <div className="font-semibold text-gray-800">Administrator</div>
                      <div className="text-gray-600">gurijalaharikrishna4@gmail.com</div>
                    </div>
                    <div className="text-right">
                      <div className="text-gray-500">Password:</div>
                      <div className="font-mono text-gray-800">admin123</div>
                    </div>
                  </div>
                </div>
                
                <div className="bg-white p-3 rounded-lg border border-gray-200 hover:shadow-sm transition-shadow">
                  <div className="flex items-center justify-between">
                    <div>
                      <div className="font-semibold text-gray-800">Doctor</div>
                      <div className="text-gray-600">michael@hospital.com</div>
                    </div>
                    <div className="text-right">
                      <div className="text-gray-500">Password:</div>
                      <div className="font-mono text-gray-800">doctor123</div>
                    </div>
                  </div>
                </div>
                
                <div className="bg-white p-3 rounded-lg border border-gray-200 hover:shadow-sm transition-shadow">
                  <div className="flex items-center justify-between">
                    <div>
                      <div className="font-semibold text-gray-800">Nurse</div>
                      <div className="text-gray-600">lisa@hospital.com</div>
                    </div>
                    <div className="text-right">
                      <div className="text-gray-500">Password:</div>
                      <div className="font-mono text-gray-800">nurse123</div>
                    </div>
                  </div>
                </div>
              </div>
              
              <div className="mt-4 p-3 bg-blue-100 rounded-lg border border-blue-200">
                <p className="text-xs text-blue-800">
                  <strong>Quick Start:</strong> Use the Administrator account for full system access, or try the Doctor/Nurse accounts to see role-based features.
                </p>
              </div>
            </div>
          </div>

          {/* Footer */}
          <div className="text-center text-sm text-gray-500">
            <p>© 2024 MedCare Hospital Management System</p>
            <p className="mt-1">Secure • Reliable • Professional</p>
          </div>
        </div>
      </div>
    </div>
  );
}