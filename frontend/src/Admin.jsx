import React, {useEffect, useState} from 'react';
import { LodgingService } from './api/LodgingService';
import CustomerService from './api/CustomerService';
import LodgingForm from './components/LodgingForm';
import LodgingList from './components/LodgingList';
import CustomerForm from './components/CustomerForm';
import CustomerList from './components/CustomerList';

import './Admin.css';

function Admin() {
    const [lodgings, setLodgings] = useState([]);
    const [customers, setCustomers] = useState([]);
    const [selectedLodging, setSelectedLodging] = useState(null);
    const [selectedCustomer, setSelectedCustomer] = useState(null);

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
    };

    const handleCustomerSubmit = async (customer) => {
        if (selectedCustomer) {
            await CustomerService.updateCustomer(customer);
        } else {
            await CustomerService.createCustomer(customer);
        }
        fetchCustomers();
        setSelectedCustomer(null);
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
        <div className="admin-container">
            <h1>Admin Panel</h1>
            <div className="admin-section">
                <h2>Lodgings</h2>
                <LodgingForm onSubmit={handleLodgingSubmit} lodging={selectedLodging}/>
                <LodgingList lodgings={lodgings} onEdit={setSelectedLodging} onDelete={handleLodgingDelete}/>
            </div>
            <div className="admin-section">
                <h2>Customers</h2>
                <CustomerForm onSubmit={handleCustomerSubmit} customer={selectedCustomer}/>
                <CustomerList customers={customers} onEdit={setSelectedCustomer} onDelete={handleCustomerDelete}/>
            </div>
        </div>
    );
}

export default Admin;