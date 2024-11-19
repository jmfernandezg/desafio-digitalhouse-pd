import { useState, useMemo } from 'react';

export function useLodgingFilters(lodgings, itemsPerPage = 5) {
    const [currentPage, setCurrentPage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');

    // Filter lodgings by name
    const filteredLodgings = useMemo(() => {
        if (!searchTerm.trim()) return lodgings;

        return lodgings.filter(lodging =>
            lodging.name.toLowerCase().includes(searchTerm.toLowerCase())
        );
    }, [lodgings, searchTerm]);

    // Pagination
    const totalPages = Math.ceil(filteredLodgings.length / itemsPerPage);
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const currentLodgings = filteredLodgings.slice(startIndex, endIndex);

    // Reset page when search changes
    const handleSearch = (value) => {
        setSearchTerm(value);
        setCurrentPage(1);
    };

    return {
        currentLodgings,
        filteredLodgings,
        currentPage,
        setCurrentPage,
        totalPages,
        startIndex,
        endIndex,
        searchTerm,
        handleSearch
    };
}
