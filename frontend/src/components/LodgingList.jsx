import React, { useState } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { IconButton } from "@mui/material";
import { Delete, Edit } from "@mui/icons-material";
import ConfirmDialog from './ConfirmDialog';

function LodgingList({ lodgings, onEdit, onDelete }) {
    const [currentPage, setCurrentPage] = useState(0);
    const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
    const [lodgingToDelete, setLodgingToDelete] = useState(null);
    const lodgingsPerPage = 5;

    const handlePageClick = (data) => {
        setCurrentPage(data.selected);
    };

    const handleDeleteClick = (lodging) => {
        setLodgingToDelete(lodging);
        setOpenConfirmDialog(true);
    };

    const handleConfirmDelete = () => {
        onDelete(lodgingToDelete.id);
        setOpenConfirmDialog(false);
        setLodgingToDelete(null);
    };

    const startIndex = currentPage * lodgingsPerPage;
    const currentLodgings = lodgings.slice(startIndex, startIndex + lodgingsPerPage);

    const columns = [
        { field: 'name', headerName: 'Nombre', flex: 1 },
        { field: 'city', headerName: 'Ciudad', flex: 1 },
        { field: 'price', headerName: 'Precio', flex: 1 },
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
                rows={currentLodgings}
                columns={columns}
                pageSize={lodgingsPerPage}
                onPageChange={handlePageClick}
                pagination
                paginationMode="server"
                rowCount={lodgings.length}
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

export default LodgingList;