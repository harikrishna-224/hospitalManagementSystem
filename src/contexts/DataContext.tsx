import React, { createContext, useContext, useState, ReactNode } from 'react';

export interface Patient {
  id: string;
  name: string;
  email: string;
  phone: string;
  dateOfBirth: string;
  gender: 'male' | 'female' | 'other';
  address: string;
  emergencyContact: string;
  bloodType: string;
  allergies: string[];
  medications: string[];
  createdAt: string;
}

export interface Appointment {
  id: string;
  patientId: string;
  patientName: string;
  doctorId: string;
  doctorName: string;
  date: string;
  time: string;
  duration: number;
  type: 'consultation' | 'follow-up' | 'emergency' | 'surgery';
  status: 'scheduled' | 'completed' | 'cancelled' | 'no-show';
  notes: string;
}

export interface EHRRecord {
  id: string;
  patientId: string;
  date: string;
  type: 'diagnosis' | 'treatment' | 'test-result' | 'prescription';
  title: string;
  description: string;
  doctorId: string;
  doctorName: string;
  attachments: string[];
}

export interface InventoryItem {
  id: string;
  name: string;
  category: 'medication' | 'equipment' | 'supplies';
  quantity: number;
  minStock: number;
  unit: string;
  supplier: string;
  expiryDate?: string;
  cost: number;
  location: string;
}

export interface Bill {
  id: string;
  patientId: string;
  patientName: string;
  date: string;
  items: {
    description: string;
    quantity: number;
    unitPrice: number;
    total: number;
  }[];
  subtotal: number;
  tax: number;
  total: number;
  status: 'pending' | 'paid' | 'overdue';
  dueDate: string;
}

interface DataContextType {
  patients: Patient[];
  appointments: Appointment[];
  ehrRecords: EHRRecord[];
  inventory: InventoryItem[];
  bills: Bill[];
  addPatient: (patient: Omit<Patient, 'id' | 'createdAt'>) => void;
  updatePatient: (id: string, patient: Partial<Patient>) => void;
  deletePatient: (id: string) => void;
  addAppointment: (appointment: Omit<Appointment, 'id'>) => void;
  updateAppointment: (id: string, appointment: Partial<Appointment>) => void;
  deleteAppointment: (id: string) => void;
  addEHRRecord: (record: Omit<EHRRecord, 'id'>) => void;
  updateInventory: (id: string, item: Partial<InventoryItem>) => void;
  addBill: (bill: Omit<Bill, 'id'>) => void;
  updateBillStatus: (id: string, status: Bill['status']) => void;
}

const DataContext = createContext<DataContextType | undefined>(undefined);

export function DataProvider({ children }: { children: ReactNode }) {
  const [patients, setPatients] = useState<Patient[]>([
    {
      id: '1',
      name: 'John Smith',
      email: 'john.smith@email.com',
      phone: '+1-555-0123',
      dateOfBirth: '1985-06-15',
      gender: 'male',
      address: '123 Main St, City, State 12345',
      emergencyContact: '+1-555-0124',
      bloodType: 'O+',
      allergies: ['Penicillin', 'Peanuts'],
      medications: ['Lisinopril 10mg'],
      createdAt: '2024-01-15'
    },
    {
      id: '2',
      name: 'Emily Johnson',
      email: 'emily.johnson@email.com',
      phone: '+1-555-0125',
      dateOfBirth: '1990-03-22',
      gender: 'female',
      address: '456 Oak Ave, City, State 12345',
      emergencyContact: '+1-555-0126',
      bloodType: 'A-',
      allergies: ['Latex'],
      medications: [],
      createdAt: '2024-01-16'
    }
  ]);

  const [appointments, setAppointments] = useState<Appointment[]>([
    {
      id: '1',
      patientId: '1',
      patientName: 'John Smith',
      doctorId: '2',
      doctorName: 'Dr. Michael Chen',
      date: '2024-01-25',
      time: '10:00',
      duration: 30,
      type: 'consultation',
      status: 'scheduled',
      notes: 'Regular checkup'
    },
    {
      id: '2',
      patientId: '2',
      patientName: 'Emily Johnson',
      doctorId: '1',
      doctorName: 'Dr. Sarah Johnson',
      date: '2024-01-25',
      time: '14:30',
      duration: 45,
      type: 'follow-up',
      status: 'scheduled',
      notes: 'Follow-up for previous consultation'
    }
  ]);

  const [ehrRecords, setEHRRecords] = useState<EHRRecord[]>([
    {
      id: '1',
      patientId: '1',
      date: '2024-01-20',
      type: 'diagnosis',
      title: 'Hypertension',
      description: 'Patient diagnosed with stage 1 hypertension. Blood pressure readings consistently above 140/90.',
      doctorId: '2',
      doctorName: 'Dr. Michael Chen',
      attachments: []
    },
    {
      id: '2',
      patientId: '1',
      date: '2024-01-20',
      type: 'prescription',
      title: 'Lisinopril Prescription',
      description: 'Prescribed Lisinopril 10mg once daily for hypertension management.',
      doctorId: '2',
      doctorName: 'Dr. Michael Chen',
      attachments: []
    }
  ]);

  const [inventory, setInventory] = useState<InventoryItem[]>([
    {
      id: '1',
      name: 'Paracetamol 500mg',
      category: 'medication',
      quantity: 150,
      minStock: 50,
      unit: 'tablets',
      supplier: 'PharmaCorp',
      expiryDate: '2025-12-31',
      cost: 0.15,
      location: 'Pharmacy-A1'
    },
    {
      id: '2',
      name: 'Digital Thermometer',
      category: 'equipment',
      quantity: 25,
      minStock: 10,
      unit: 'pieces',
      supplier: 'MedEquip Ltd',
      cost: 45.00,
      location: 'Equipment-B2'
    },
    {
      id: '3',
      name: 'Disposable Gloves',
      category: 'supplies',
      quantity: 500,
      minStock: 100,
      unit: 'boxes',
      supplier: 'SafeSupply Co',
      cost: 12.50,
      location: 'Supplies-C1'
    }
  ]);

  const [bills, setBills] = useState<Bill[]>([
    {
      id: '1',
      patientId: '1',
      patientName: 'John Smith',
      date: '2024-01-20',
      items: [
        { description: 'Consultation Fee', quantity: 1, unitPrice: 150.00, total: 150.00 },
        { description: 'Blood Pressure Test', quantity: 1, unitPrice: 25.00, total: 25.00 },
        { description: 'Prescription', quantity: 1, unitPrice: 30.00, total: 30.00 }
      ],
      subtotal: 205.00,
      tax: 20.50,
      total: 225.50,
      status: 'pending',
      dueDate: '2024-02-20'
    }
  ]);

  const addPatient = (patientData: Omit<Patient, 'id' | 'createdAt'>) => {
    const newPatient: Patient = {
      ...patientData,
      id: Date.now().toString(),
      createdAt: new Date().toISOString().split('T')[0]
    };
    setPatients(prev => [...prev, newPatient]);
  };

  const updatePatient = (id: string, patientData: Partial<Patient>) => {
    setPatients(prev => prev.map(patient => 
      patient.id === id ? { ...patient, ...patientData } : patient
    ));
  };

  const deletePatient = (id: string) => {
    setPatients(prev => prev.filter(patient => patient.id !== id));
  };

  const addAppointment = (appointmentData: Omit<Appointment, 'id'>) => {
    const newAppointment: Appointment = {
      ...appointmentData,
      id: Date.now().toString()
    };
    setAppointments(prev => [...prev, newAppointment]);
  };

  const updateAppointment = (id: string, appointmentData: Partial<Appointment>) => {
    setAppointments(prev => prev.map(appointment => 
      appointment.id === id ? { ...appointment, ...appointmentData } : appointment
    ));
  };

  const deleteAppointment = (id: string) => {
    setAppointments(prev => prev.filter(appointment => appointment.id !== id));
  };

  const addEHRRecord = (recordData: Omit<EHRRecord, 'id'>) => {
    const newRecord: EHRRecord = {
      ...recordData,
      id: Date.now().toString()
    };
    setEHRRecords(prev => [...prev, newRecord]);
  };

  const updateInventory = (id: string, itemData: Partial<InventoryItem>) => {
    setInventory(prev => prev.map(item => 
      item.id === id ? { ...item, ...itemData } : item
    ));
  };

  const addBill = (billData: Omit<Bill, 'id'>) => {
    const newBill: Bill = {
      ...billData,
      id: Date.now().toString()
    };
    setBills(prev => [...prev, newBill]);
  };

  const updateBillStatus = (id: string, status: Bill['status']) => {
    setBills(prev => prev.map(bill => 
      bill.id === id ? { ...bill, status } : bill
    ));
  };

  return (
    <DataContext.Provider value={{
      patients,
      appointments,
      ehrRecords,
      inventory,
      bills,
      addPatient,
      updatePatient,
      deletePatient,
      addAppointment,
      updateAppointment,
      deleteAppointment,
      addEHRRecord,
      updateInventory,
      addBill,
      updateBillStatus
    }}>
      {children}
    </DataContext.Provider>
  );
}

export function useData() {
  const context = useContext(DataContext);
  if (context === undefined) {
    throw new Error('useData must be used within a DataProvider');
  }
  return context;
}