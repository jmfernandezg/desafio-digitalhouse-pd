import React, {useEffect, useState} from 'react';
import {LodgingService} from './api/LodgingService';
import CustomerService from './api/CustomerService';
import LodgingForm from './components/LodgingForm';
import LodgingList from './components/LodgingList';
import CustomerForm from './components/CustomerForm';
import CustomerList from './components/CustomerList';
import * as Collapsible from "@radix-ui/react-collapsible";
import {Cross2Icon, RowSpacingIcon} from "@radix-ui/react-icons";
import * as Tabs from "@radix-ui/react-tabs";
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

    return (
        <div className="admin-container">
            <h2>Panel De Administración</h2>
            <Tabs.Root defaultValue="tab1">
                <Tabs.List>
                    <Tabs.Trigger value="tab1">Hospedajes</Tabs.Trigger>
                    <Tabs.Trigger value="tab2">Clientes</Tabs.Trigger>
                </Tabs.List>
                <Tabs.Content value="tab1">
                    <div className="admin-section">
                        <h3>Hospedajes</h3>
                        <Collapsible.Root open={open} onOpenChange={setOpen} className="CollapsibleRoot">
                            <Collapsible.Trigger asChild>
                                <button className="IconButton">
                                    {open ? <Cross2Icon/> : <RowSpacingIcon/>}
                                </button>
                            </Collapsible.Trigger>
                            <Collapsible.Content>
                                <LodgingForm onSubmit={handleLodgingSubmit} lodging={selectedLodging}/>
                            </Collapsible.Content>
                        </Collapsible.Root>
                        <LodgingList lodgings={lodgings} onEdit={setSelectedLodging} onDelete={handleLodgingDelete}/>
                    </div>
                </Tabs.Content>
                <Tabs.Content value="tab2">
                    <div className="admin-section">
                        <h3>Clientes</h3>
                        <Collapsible.Root open={open} onOpenChange={setOpen}>
                            <Collapsible.Trigger asChild>
                                <button className="IconButton">
                                    {open ? <Cross2Icon/> : <RowSpacingIcon/>}
                                </button>
                            </Collapsible.Trigger>
                            <Collapsible.Content>
                                <CustomerForm onSubmit={handleCustomerSubmit} customer={selectedCustomer}/>
                            </Collapsible.Content>
                        </Collapsible.Root>
                        <CustomerList customers={customers} onEdit={setSelectedCustomer} onDelete={handleCustomerDelete}/>
                    </div>
                </Tabs.Content>
            </Tabs.Root>
        </div>
    );
}

export default Admin;