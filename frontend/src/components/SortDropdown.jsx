import React from 'react';
import { Select, MenuItem, FormControl, InputLabel } from '@mui/material';

function SortDropdown({ sortOption, setSortOption }) {
    return (
        <FormControl fullWidth variant="outlined">
            <InputLabel id="sort-label">Ordenar por</InputLabel>
            <Select
                labelId="sort-label"
                value={sortOption}
                onChange={(e) => setSortOption(e.target.value)}
                label="Ordenar por"
            >
                <MenuItem value="price">Precio</MenuItem>
                <MenuItem value="stars">Estrellas</MenuItem>
                <MenuItem value="averageCustomerRating">Calificación</MenuItem>
                <MenuItem value="distanceFromDownTown">Distancia del Centro</MenuItem>
            </Select>
        </FormControl>
    );
}

export default SortDropdown;