import React, { useState } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { IconButton } from "@mui/material";
import { Delete, Edit } from "@mui/icons-material";
import ConfirmDialog from './ConfirmDialog';

function CustomerList({ customers, onEdit, onDelete }) {
    const [currentPage, setCurrentPage] = useState(0);
    const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
    const [customerToDelete, setCustomerToDelete] = useState(null);
    const customersPerPage = 5;

    const handlePageClick = (data) => {
        setCurrentPage(data.selected);
    };

    const handleDeleteClick = (customer) => {
        setCustomerToDelete(customer);
        setOpenConfirmDialog(true);
    };

    const handleConfirmDelete = () => {
        onDelete(customerToDelete.id);
        setOpenConfirmDialog(false);
        setCustomerToDelete(null);
    };

    const startIndex = currentPage * customersPerPage;
    const currentCustomers = customers.slice(startIndex, startIndex + customersPerPage);

    const columns = [
        { field: 'username', headerName: 'Usuario', flex: 1 },
        { field: 'email', headerName: 'Email', flex: 1 },
        {
            field: 'actions',
            headerName: 'Acciones',
            flex: 1,
            renderCell: (params) => (
                <>
                    <IconButton onClick={() => onEdit(params.row)}>
                        <Edit />
                    </IconButton>

                    <IconButton onClick={() => handleDeleteClick(params.row)}>
                        <Delete />
                    </IconButton>
                </>
            )
        }
    ];

    return (
        <div style={{ height: 400, width: '100%' }}>
            <DataGrid
                rows={currentCustomers}
                columns={columns}
                pageSize={customersPerPage}
                onPageChange={handlePageClick}
                pagination
                paginationMode="server"
                rowCount={customers.length}
            />
            {openConfirmDialog && (
                <ConfirmDialog
                    open={openConfirmDialog}
                    onClose={() => setOpenConfirmDialog(false)}
                    onConfirm={handleConfirmDelete}
                />
            )}
        </div>
    );
}

export default CustomerList;