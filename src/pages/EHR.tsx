import React, { useState } from 'react';
import { Plus, Search, FileText, User, Calendar, Filter } from 'lucide-react';
import { format, parseISO } from 'date-fns';
import { useData, EHRRecord } from '../contexts/DataContext';

const recordTypes = [
  'diagnosis',
  'treatment',
  'test-result',
  'prescription'
];

const typeColors = {
  diagnosis: 'bg-red-100 text-red-800',
  treatment: 'bg-blue-100 text-blue-800',
  'test-result': 'bg-green-100 text-green-800',
  prescription: 'bg-purple-100 text-purple-800'
};

const typeIcons = {
  diagnosis: '🩺',
  treatment: '⚕️',
  'test-result': '📋',
  prescription: '💊'
};

export default function EHR() {
  const { ehrRecords, patients, addEHRRecord } = useData();
  const [selectedPatient, setSelectedPatient] = useState<string>('');
  const [showModal, setShowModal] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [typeFilter, setTypeFilter] = useState<string>('all');
  const [formData, setFormData] = useState({
    patientId: '',
    type: 'diagnosis' as EHRRecord['type'],
    title: '',
    description: '',
    doctorName: 'Dr. Michael Chen'
  });

  const filteredRecords = ehrRecords.filter(record => {
    const patient = patients.find(p => p.id === record.patientId);
    const matchesSearch = record.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         record.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         patient?.name.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesType = typeFilter === 'all' || record.type === typeFilter;
    const matchesPatient = !selectedPatient || record.patientId === selectedPatient;
    return matchesSearch && matchesType && matchesPatient;
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    const recordData = {
      ...formData,
      date: new Date().toISOString().split('T')[0],
      doctorId: '2',
      attachments: []
    };

    addEHRRecord(recordData);
    resetForm();
  };

  const resetForm = () => {
    setFormData({
      patientId: '',
      type: 'diagnosis',
      title: '',
      description: '',
      doctorName: 'Dr. Michael Chen'
    });
    setShowModal(false);
  };

  const getPatientName = (patientId: string) => {
    const patient = patients.find(p => p.id === patientId);
    return patient?.name || 'Unknown Patient';
  };

  const getPatientRecords = (patientId: string) => {
    return ehrRecords.filter(record => record.patientId === patientId);
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Electronic Health Records</h1>
          <p className="text-gray-600">Manage patient medical records and history</p>
        </div>
        <button
          onClick={() => setShowModal(true)}
          className="mt-4 sm:mt-0 inline-flex items-center px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
        >
          <Plus className="h-5 w-5 mr-2" />
          Add Record
        </button>
      </div>

      {/* Filters */}
      <div className="flex flex-col lg:flex-row gap-4">
        <div className="relative flex-1 max-w-md">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
          <input
            type="text"
            placeholder="Search records..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="pl-10 pr-4 py-2 w-full border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
        </div>
        
        <select
          value={selectedPatient}
          onChange={(e) => setSelectedPatient(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        >
          <option value="">All Patients</option>
          {patients.map((patient) => (
            <option key={patient.id} value={patient.id}>{patient.name}</option>
          ))}
        </select>

        <select
          value={typeFilter}
          onChange={(e) => setTypeFilter(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        >
          <option value="all">All Types</option>
          {recordTypes.map((type) => (
            <option key={type} value={type}>{type.replace('-', ' ')}</option>
          ))}
        </select>
      </div>

      {/* Patient Summary Cards */}
      {!selectedPatient && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {patients.map((patient) => {
            const patientRecords = getPatientRecords(patient.id);
            const lastRecord = patientRecords.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())[0];
            
            return (
              <div key={patient.id} className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
                <div className="flex items-start justify-between">
                  <div className="flex items-center space-x-3">
                    <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
                      <User className="h-6 w-6 text-blue-600" />
                    </div>
                    <div>
                      <h3 className="text-lg font-semibold text-gray-900">{patient.name}</h3>
                      <p className="text-sm text-gray-500">{patientRecords.length} records</p>
                    </div>
                  </div>
                  <button
                    onClick={() => setSelectedPatient(patient.id)}
                    className="px-3 py-1 text-sm bg-blue-100 text-blue-700 rounded-lg hover:bg-blue-200 transition-colors"
                  >
                    View Records
                  </button>
                </div>
                
                {lastRecord && (
                  <div className="mt-4 p-3 bg-gray-50 rounded-lg">
                    <div className="flex items-center justify-between">
                      <span className={`px-2 py-1 rounded-full text-xs font-medium ${typeColors[lastRecord.type]}`}>
                        {typeIcons[lastRecord.type]} {lastRecord.type.replace('-', ' ')}
                      </span>
                      <span className="text-xs text-gray-500">
                        {format(parseISO(lastRecord.date), 'MMM d, yyyy')}
                      </span>
                    </div>
                    <p className="text-sm font-medium text-gray-900 mt-2">{lastRecord.title}</p>
                    <p className="text-sm text-gray-600 truncate">{lastRecord.description}</p>
                  </div>
                )}
              </div>
            );
          })}
        </div>
      )}

      {/* Records List */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <div className="p-6 border-b border-gray-200">
          <div className="flex items-center justify-between">
            <h2 className="text-lg font-semibold text-gray-900">
              {selectedPatient ? `${getPatientName(selectedPatient)} - Medical Records` : 'All Medical Records'}
            </h2>
            {selectedPatient && (
              <button
                onClick={() => setSelectedPatient('')}
                className="text-sm text-blue-600 hover:text-blue-800"
              >
                ← Back to all patients
              </button>
            )}
          </div>
        </div>

        <div className="divide-y divide-gray-200">
          {filteredRecords.map((record) => (
            <div key={record.id} className="p-6 hover:bg-gray-50">
              <div className="flex items-start justify-between">
                <div className="flex-1">
                  <div className="flex items-center space-x-3 mb-2">
                    <span className={`px-3 py-1 rounded-full text-sm font-medium ${typeColors[record.type]}`}>
                      {typeIcons[record.type]} {record.type.replace('-', ' ')}
                    </span>
                    <span className="text-sm text-gray-500">
                      {format(parseISO(record.date), 'MMM d, yyyy')}
                    </span>
                  </div>
                  
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">{record.title}</h3>
                  <p className="text-gray-700 mb-3">{record.description}</p>
                  
                  <div className="flex items-center space-x-4 text-sm text-gray-500">
                    <div className="flex items-center">
                      <User className="h-4 w-4 mr-1" />
                      {getPatientName(record.patientId)}
                    </div>
                    <div className="flex items-center">
                      <FileText className="h-4 w-4 mr-1" />
                      {record.doctorName}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>

        {filteredRecords.length === 0 && (
          <div className="text-center py-12">
            <FileText className="h-12 w-12 text-gray-400 mx-auto mb-4" />
            <p className="text-gray-500">No medical records found</p>
          </div>
        )}
      </div>

      {/* Add Record Modal */}
      {showModal && (
        <div className="fixed inset-0 z-50 overflow-y-auto">
          <div className="flex items-center justify-center min-h-screen px-4 pt-4 pb-20 text-center sm:block sm:p-0">
            <div className="fixed inset-0 transition-opacity bg-gray-500 bg-opacity-75" onClick={resetForm} />
            
            <div className="inline-block w-full max-w-2xl p-6 my-8 overflow-hidden text-left align-middle transition-all transform bg-white shadow-xl rounded-2xl">
              <h3 className="text-lg font-medium text-gray-900 mb-6">Add Medical Record</h3>

              <form onSubmit={handleSubmit} className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Patient</label>
                    <select
                      value={formData.patientId}
                      onChange={(e) => setFormData({ ...formData, patientId: e.target.value })}
                      className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                      required
                    >
                      <option value="">Select a patient</option>
                      {patients.map((patient) => (
                        <option key={patient.id} value={patient.id}>{patient.name}</option>
                      ))}
                    </select>
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Record Type</label>
                    <select
                      value={formData.type}
                      onChange={(e) => setFormData({ ...formData, type: e.target.value as EHRRecord['type'] })}
                      className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    >
                      {recordTypes.map((type) => (
                        <option key={type} value={type}>{type.replace('-', ' ')}</option>
                      ))}
                    </select>
                  </div>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Title</label>
                  <input
                    type="text"
                    value={formData.title}
                    onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    placeholder="e.g., Blood Pressure Check, X-Ray Results"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
                  <textarea
                    value={formData.description}
                    onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                    rows={4}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    placeholder="Detailed description of the medical record..."
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Doctor</label>
                  <input
                    type="text"
                    value={formData.doctorName}
                    onChange={(e) => setFormData({ ...formData, doctorName: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    required
                  />
                </div>

                <div className="flex justify-end space-x-3 pt-6">
                  <button
                    type="button"
                    onClick={resetForm}
                    className="px-4 py-2 text-gray-700 bg-gray-100 rounded-lg hover:bg-gray-200 transition-colors"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
                  >
                    Add Record
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}