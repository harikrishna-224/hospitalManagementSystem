# MedCare - Hospital Management System

A comprehensive hospital management system built with React, TypeScript, and Tailwind CSS. This application provides a complete solution for managing hospital operations including patient records, appointments, electronic health records, inventory, and billing.

## Features

### 🏥 Core Modules
- **Dashboard**: Overview of hospital statistics and recent activities
- **Patient Management**: Complete patient records with medical history
- **Appointment Scheduling**: Calendar and list views for appointment management
- **Electronic Health Records (EHR)**: Digital medical records and patient history
- **Inventory Management**: Track medical supplies, equipment, and medications
- **Billing & Invoicing**: Generate and manage patient bills

### 🔐 Authentication & Security
- Role-based access control (Admin, Doctor, Nurse, Receptionist)
- Secure login system with demo accounts
- Protected routes and user session management

### 📊 Analytics & Reporting
- Interactive charts and graphs using Recharts
- Real-time statistics and KPIs
- Department-wise patient distribution
- Revenue tracking and financial reports

### 🎨 Modern UI/UX
- Clean, professional design with Tailwind CSS
- Responsive layout for all device sizes
- Intuitive navigation and user experience
- Apple-level design aesthetics with attention to detail

## Demo Accounts

### Admin Access
- **Email**: gurijalaharikrishna4@gmail.com
- **Password**: admin123
- **Role**: Administrator (Full system access)

### Doctor Access
- **Email**: michael@hospital.com
- **Password**: doctor123
- **Role**: Doctor

### Nurse Access
- **Email**: lisa@hospital.com
- **Password**: nurse123
- **Role**: Nurse

## Technology Stack

- **Frontend**: React 18 with TypeScript
- **Styling**: Tailwind CSS
- **Icons**: Lucide React
- **Charts**: Recharts
- **Routing**: React Router DOM
- **Date Handling**: date-fns
- **Build Tool**: Vite
- **Backend**: Pure Java (no frameworks)
- **Database**: H2 in-memory database

## Getting Started

### Prerequisites
- Node.js 16+ and npm
- Java 11+ (for backend)

### Frontend Setup
1. Clone the repository
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```

### Backend Setup
1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Compile and run the Java backend:
   ```bash
   javac -cp ".:lib/*" -d build src/main/java/com/medcare/**/*.java
   java -cp "build:lib/*" com.medcare.HospitalManagementApplication
   ```

The backend server will start on port 8080, and the frontend will be available at http://localhost:5173

## Project Structure

```
src/
├── components/          # Reusable UI components
│   └── Layout.tsx      # Main application layout
├── contexts/           # React contexts for state management
│   ├── AuthContext.tsx # Authentication state
│   └── DataContext.tsx # Application data state
├── pages/              # Main application pages
│   ├── Dashboard.tsx   # Dashboard overview
│   ├── Patients.tsx    # Patient management
│   ├── Appointments.tsx # Appointment scheduling
│   ├── EHR.tsx         # Electronic health records
│   ├── Inventory.tsx   # Inventory management
│   ├── Billing.tsx     # Billing and invoicing
│   └── Login.tsx       # Authentication page
└── App.tsx             # Main application component

backend/
├── src/main/java/com/medcare/
│   ├── config/         # Database configuration
│   ├── dao/            # Data access objects
│   ├── model/          # Entity models
│   ├── server/         # HTTP server and controllers
│   ├── service/        # Business logic services
│   └── util/           # Utility classes
└── lib/                # Dependencies (H2 database)
```

## Key Features Explained

### Patient Management
- Complete patient profiles with personal and medical information
- Medical history tracking including allergies and current medications
- Search and filter capabilities
- CRUD operations with form validation

### Appointment System
- Calendar view for visual appointment scheduling
- List view with filtering and search
- Multiple appointment types (consultation, follow-up, emergency, surgery)
- Status tracking (scheduled, completed, cancelled, no-show)

### Electronic Health Records
- Digital medical records organized by patient
- Multiple record types (diagnosis, treatment, test results, prescriptions)
- Doctor attribution and timestamps
- Patient-specific medical history timeline

### Inventory Management
- Track medications, equipment, and supplies
- Low stock alerts and expiry date monitoring
- Category-based organization
- Stock level adjustments and supplier information

### Billing System
- Generate detailed invoices with line items
- Tax calculations and payment tracking
- Multiple bill statuses (pending, paid, overdue)
- Patient billing history

## API Integration

The application is designed to work with a RESTful backend API. The current implementation uses mock data stored in React context, but can be easily adapted to work with real API endpoints.

### API Endpoints Structure
```
/api/auth/login          # User authentication
/api/patients           # Patient CRUD operations
/api/appointments       # Appointment management
/api/ehr               # Electronic health records
/api/inventory         # Inventory management
/api/billing           # Billing operations
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions, please contact:
- **Admin**: gurijalaharikrishna4@gmail.com

## Deployment

The application is deployed and accessible at: https://peppy-starship-2f416e.netlify.app

You can use the demo accounts listed above to explore the system functionality.