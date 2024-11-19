import React, { useEffect, useState } from 'react';
import { LodgingService } from './api/LodgingService';
import CustomerService from './api/CustomerService';
import LodgingForm from './components/LodgingForm';
import LodgingList from './components/LodgingList';
import CustomerForm from './components/CustomerForm';
import CustomerList from './components/CustomerList';
import { ChevronDown, ChevronUp, Plus } from 'lucide-react';

const TabButton = ({ active, children, onClick }) => (
    <button
        onClick={onClick}
        className={`px-4 py-2 font-medium text-sm rounded-t-lg transition-colors duration-200
      ${active
            ? 'bg-white text-blue-600 border-t border-x border-gray-200'
            : 'bg-gray-50 text-gray-600 hover:bg-gray-100'
        }`}
    >
        {children}
    </button>
);

const Section = ({ title, children, isFormOpen, setIsFormOpen, form }) => (
    <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
        <div className="flex justify-between items-center mb-6">
            <h3 className="text-xl font-semibold text-gray-900">{title}</h3>
            <button
                onClick={() => setIsFormOpen(!isFormOpen)}
                className="flex items-center gap-2 px-4 py-2 rounded-lg bg-blue-50 text-blue-600
          hover:bg-blue-100 transition-colors duration-200"
            >
                {isFormOpen ? (
                    <>
                        <ChevronUp size={20} />
                        Cerrar formulario
                    </>
                ) : (
                    <>
                        <Plus size={20} />
                        Agregar nuevo
                    </>
                )}
            </button>
        </div>

        {/* Collapsible Form */}
        <div className={`transition-all duration-300 ease-in-out overflow-hidden
      ${isFormOpen ? 'max-h-[1000px] opacity-100 mb-6' : 'max-h-0 opacity-0'}`}>
            {form}
        </div>

        {/* Content */}
        {children}
    </div>
);

function Admin() {
    const [lodgings, setLodgings] = useState([]);
    const [customers, setCustomers] = useState([]);
    const [selectedLodging, setSelectedLodging] = useState(null);
    const [selectedCustomer, setSelectedCustomer] = useState(null);
    const [activeTab, setActiveTab] = useState('lodgings');
    const [isLodgingFormOpen, setIsLodgingFormOpen] = useState(false);
    const [isCustomerFormOpen, setIsCustomerFormOpen] = useState(false);

    useEffect(() => {
        fetchLodgings();
        fetchCustomers();
    }, []);

    const fetchLodgings = async () => {
        const data = await LodgingService.getAllLodgings();
        setLodgings(data.lodgings);
    };

    const fetchCustomers = async () => {
        const data = await CustomerService.getAllCustomers();
        setCustomers(data.customers);
    };

    const handleLodgingSubmit = async (lodging) => {
        if (selectedLodging) {
            await LodgingService.updateLodging(lodging);
        } else {
            await LodgingService.createLodging(lodging);
        }
        fetchLodgings();
        setSelectedLodging(null);
        setIsLodgingFormOpen(false);
    };

    const handleCustomerSubmit = async (customer) => {
        if (selectedCustomer) {
            await CustomerService.updateCustomer(customer);
        } else {
            await CustomerService.createCustomer(customer);
        }
        fetchCustomers();
        setSelectedCustomer(null);
        setIsCustomerFormOpen(false);
    };

    const handleLodgingDelete = async (id) => {
        await LodgingService.deleteLodging(id);
        fetchLodgings();
    };

    const handleCustomerDelete = async (id) => {
        await CustomerService.deleteCustomer(id);
        fetchCustomers();
    };

    return (
        <div className="max-w-7xl mx-auto px-4 py-8">
            <h2 className="text-2xl font-bold text-gray-900 mb-8">
                Panel De Administración
            </h2>

            {/* Tabs */}
            <div className="flex gap-2 mb-6">
                <TabButton
                    active={activeTab === 'lodgings'}
                    onClick={() => setActiveTab('lodgings')}
                >
                    Hospedajes
                </TabButton>
                <TabButton
                    active={activeTab === 'customers'}
                    onClick={() => setActiveTab('customers')}
                >
                    Clientes
                </TabButton>
            </div>

            {/* Tab Content */}
            <div className="bg-white rounded-lg">
                {activeTab === 'lodgings' && (
                    <Section
                        title="Hospedajes"
                        isFormOpen={isLodgingFormOpen}
                        setIsFormOpen={setIsLodgingFormOpen}
                        form={
                            <LodgingForm
                                onSubmit={handleLodgingSubmit}
                                lodging={selectedLodging}
                            />
                        }
                    >
                        <LodgingList
                            lodgings={lodgings}
                            onEdit={(lodging) => {
                                setSelectedLodging(lodging);
                                setIsLodgingFormOpen(true);
                            }}
                            onDelete={handleLodgingDelete}
                        />
                    </Section>
                )}

                {activeTab === 'customers' && (
                    <Section
                        title="Clientes"
                        isFormOpen={isCustomerFormOpen}
                        setIsFormOpen={setIsCustomerFormOpen}
                        form={
                            <CustomerForm
                                onSubmit={handleCustomerSubmit}
                                customer={selectedCustomer}
                            />
                        }
                    >
                        <CustomerList
                            customers={customers}
                            onEdit={(customer) => {
                                setSelectedCustomer(customer);
                                setIsCustomerFormOpen(true);
                            }}
                            onDelete={handleCustomerDelete}
                        />
                    </Section>
                )}
            </div>
        </div>
    );
}

export default Admin;