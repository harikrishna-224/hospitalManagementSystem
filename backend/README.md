# Hospital Management System - Java Backend

This is a pure Java backend implementation for the Hospital Management System without using Spring Boot or any external frameworks.

## Features

- **Authentication & Authorization**: User login/registration with role-based access
- **Patient Management**: CRUD operations for patient records
- **Appointment Management**: Schedule and manage appointments
- **Electronic Health Records (EHR)**: Medical records management
- **Inventory Management**: Track medical supplies and equipment
- **Billing System**: Generate and manage bills

## Architecture

### Models
- `User`: System users (doctors, nurses, admin, etc.)
- `Patient`: Patient information and medical history
- `Appointment`: Appointment scheduling and management
- `EHRRecord`: Electronic health records
- `InventoryItem`: Medical inventory items
- `Bill` & `BillItem`: Billing and invoice management

### Data Access Layer (DAO)
- `BaseDAO`: Base class for database operations
- `UserDAO`: User data access operations
- `PatientDAO`: Patient data access operations
- `AppointmentDAO`: Appointment data access operations

### Service Layer
- `AuthService`: Authentication and authorization logic
- `PatientService`: Patient business logic
- `AppointmentService`: Appointment business logic

### Web Layer (Controllers)
- `AuthController`: Authentication endpoints
- `PatientController`: Patient management endpoints
- `AppointmentController`: Appointment management endpoints
- `EHRController`: EHR management endpoints
- `InventoryController`: Inventory management endpoints
- `BillingController`: Billing management endpoints

### Database
- Uses H2 in-memory database for simplicity
- Database schema is automatically created on startup
- Sample data is inserted for testing

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Patients
- `GET /api/patients` - Get all patients
- `GET /api/patients/{id}` - Get patient by ID
- `POST /api/patients` - Create new patient
- `PUT /api/patients/{id}` - Update patient
- `DELETE /api/patients/{id}` - Delete patient

### Appointments
- `GET /api/appointments` - Get all appointments
- `GET /api/appointments/{id}` - Get appointment by ID
- `POST /api/appointments` - Create new appointment
- `PUT /api/appointments/{id}` - Update appointment
- `DELETE /api/appointments/{id}` - Delete appointment

## Running the Application

1. Compile the Java files:
```bash
javac -cp ".:lib/*" -d build src/main/java/com/medcare/**/*.java
```

2. Run the application:
```bash
java -cp "build:lib/*" com.medcare.HospitalManagementApplication
```

The server will start on port 8080.

## Sample Data

The application comes with sample data:

### Users
- **Admin**: gurijalaharikrishna4@gmail.com / admin123
- **Doctor**: michael@hospital.com / doctor123
- **Nurse**: lisa@hospital.com / nurse123

### Patients
- John Smith (with sample medical history)
- Emily Johnson

## Dependencies

This implementation uses only core Java libraries:
- `java.net.http` for HTTP server
- `java.sql` for database operations
- `java.time` for date/time handling
- H2 Database (embedded)

## CORS Support

The server includes CORS headers to allow cross-origin requests from the frontend application.

## Security Notes

This is a demonstration implementation. For production use, consider:
- Proper password hashing (bcrypt, etc.)
- JWT tokens for authentication
- Input validation and sanitization
- SQL injection prevention
- HTTPS/TLS encryption
- Rate limiting
- Proper error handling and logging