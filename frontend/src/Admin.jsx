import React, {useEffect, useState} from 'react';
import {LodgingService} from './api/LodgingService';
import CustomerService from './api/CustomerService';
import LodgingForm from './components/LodgingForm';
import LodgingList from './components/LodgingList';
import CustomerForm from './components/CustomerForm';
import CustomerList from './components/CustomerList';
import * as Collapsible from "@radix-ui/react-collapsible";
import {Cross2Icon, RowSpacingIcon} from "@radix-ui/react-icons";
import './Admin.css';

function Admin() {
    const [lodgings, setLodgings] = useState([]);
    const [customers, setCustomers] = useState([]);
    const [selectedLodging, setSelectedLodging] = useState(null);
    const [selectedCustomer, setSelectedCustomer] = useState(null);
    const [open, setOpen] = React.useState(false);

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

    return (<div className="admin-container">
        <h1>Panel De Administración</h1>
        <div className="admin-section">
            <h2>Hospedajes</h2>
            <Collapsible.Root open={open} onOpenChange={setOpen} className="CollapsibleRoot">
                <Collapsible.Content>
                    <LodgingForm onSubmit={handleLodgingSubmit} lodging={selectedLodging}/>
                </Collapsible.Content>

                <Collapsible.Trigger asChild>
                    <button className="IconButton">
                        {open ? <Cross2Icon/> : <RowSpacingIcon/>}
                    </button>
                </Collapsible.Trigger>
            </Collapsible.Root>

            <LodgingList lodgings={lodgings} onEdit={setSelectedLodging} onDelete={handleLodgingDelete}/>
        </div>
        <div className="admin-section">
            <h2>Clientes</h2>

            <Collapsible.Root open={open} onOpenChange={setOpen}>
                <Collapsible.Content>
                    <CustomerForm onSubmit={handleCustomerSubmit} customer={selectedCustomer}/>
                </Collapsible.Content>
                <Collapsible.Trigger asChild>
                    <button className="IconButton">
                        {open ? <Cross2Icon/> : <RowSpacingIcon/>}
                    </button>
                </Collapsible.Trigger>
            </Collapsible.Root>
            <CustomerList customers={customers} onEdit={setSelectedCustomer} onDelete={handleCustomerDelete}/>
        </div>
    </div>);
}

export default Admin;