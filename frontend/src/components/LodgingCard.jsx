import React, {useEffect, useState} from 'react';
import {LodgingService} from '../api/LodgingService';
import {ChevronDown, ChevronLeft, ChevronRight, SlidersHorizontal} from 'lucide-react';
import LodgingGrid from './LodgingGrid';

const SortDropdown = ({value, onValueChange}) => {
    const [isOpen, setIsOpen] = useState(false);
    const options = [
        {value: 'price', label: 'Precio'},
        {value: 'stars', label: 'Estrellas'},
        {value: 'averageCustomerRating', label: 'Calificación'},
        {value: 'distanceFromDownTown', label: 'Distancia del Centro'}
    ];

    return (
        <div className="relative">
            <button
                onClick={() => setIsOpen(!isOpen)}
                className="flex items-center gap-2 px-4 py-2 rounded-lg bg-white border border-gray-200
                   hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
            >
                <SlidersHorizontal className="w-4 h-4"/>
                <span>{options.find(opt => opt.value === value)?.label || 'Ordenar por'}</span>
                <ChevronDown className="w-4 h-4 ml-2"/>
            </button>

            {isOpen && (
                <div
                    className="absolute right-0 mt-2 w-48 rounded-lg bg-white shadow-lg border border-gray-200 py-1 z-10">
                    {options.map((option) => (
                        <button
                            key={option.value}
                            onClick={() => {
                                onValueChange(option.value);
                                setIsOpen(false);
                            }}
                            className={`w-full text-left px-4 py-2 hover:bg-gray-50 ${
                                value === option.value ? 'text-blue-600 bg-blue-50' : 'text-gray-700'
                            }`}
                        >
                            {option.label}
                        </button>
                    ))}
                </div>
            )}
        </div>
    );
};

const Pagination = ({currentPage, totalPages, onPageChange}) => (
    <div className="flex justify-center items-center gap-2 mt-8">
        <button
            onClick={() => onPageChange(Math.max(0, currentPage - 1))}
            disabled={currentPage === 0}
            className="flex items-center gap-1 px-3 py-2 rounded-lg bg-white border border-gray-200 text-gray-700
                 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
        >
            <ChevronLeft className="w-4 h-4"/>
            Anterior
        </button>

        <div className="flex gap-1">
            {[...Array(totalPages)].map((_, idx) => (
                <button
                    key={idx}
                    onClick={() => onPageChange(idx)}
                    className={`w-10 h-10 rounded-lg flex items-center justify-center transition-colors
            ${currentPage === idx
                        ? 'bg-blue-600 text-white'
                        : 'bg-white border border-gray-200 text-gray-700 hover:bg-gray-50'
                    }`}
                >
                    {idx + 1}
                </button>
            ))}
        </div>

        <button
            onClick={() => onPageChange(Math.min(totalPages - 1, currentPage + 1))}
            disabled={currentPage === totalPages - 1}
            className="flex items-center gap-1 px-3 py-2 rounded-lg bg-white border border-gray-200 text-gray-700
                 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
        >
            Siguiente
            <ChevronRight className="w-4 h-4"/>
        </button>
    </div>
);

const LoadingSkeleton = () => (
    <div className="space-y-6">
        <div className="flex justify-between items-center">
            <div className="h-8 w-48 bg-gray-200 rounded-lg animate-pulse"/>
            <div className="h-10 w-40 bg-gray-200 rounded-lg animate-pulse"/>
        </div>
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {[...Array(4)].map((_, idx) => (
                <div key={idx} className="h-64 w-full bg-gray-200 rounded-lg animate-pulse"/>
            ))}
        </div>
    </div>
);

const LodgingCard = ({category}) => {
    const [lodgings, setLodgings] = useState([]);
    const [sortOption, setSortOption] = useState('price');
    const [currentPage, setCurrentPage] = useState(0);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    const LODGINGS_PER_PAGE = 10;

    useEffect(() => {
        const fetchLodgings = async () => {
            try {
                setIsLoading(true);
                setError(null);

                let data;
                if (category) {
                    data = await LodgingService.getLodgingsByCategory(category);
                } else {
                    data = await LodgingService.getAllLodgings();
                    data.lodgings = data.lodgings
                        .sort((a, b) => b.averageCustomerRating - a.averageCustomerRating || b.stars - a.stars)
                        .slice(0, 20)
                        .sort(() => Math.random() - 0.5);
                }

                const sortedLodgings = sortLodgings(data.lodgings, sortOption);
                setLodgings(sortedLodgings);
            } catch (error) {
                setError('Error al cargar los alojamientos. Por favor, intente nuevamente.');
                console.error('Error fetching lodgings:', error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchLodgings();
    }, [category, sortOption]);

    const sortLodgings = (lodgings, option) => {
        return [...lodgings].sort((a, b) => b[option] - a[option]);
    };

    if (isLoading) {
        return (
            <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
                <LoadingSkeleton/>
            </div>
        );
    }

    if (error) {
        return (
            <div className="bg-red-50 border-l-4 border-red-500 p-4 rounded-lg" role="alert">
                <p className="text-red-700">{error}</p>
            </div>
        );
    }

    const startIndex = currentPage * LODGINGS_PER_PAGE;
    const currentLodgings = lodgings.slice(startIndex, startIndex + LODGINGS_PER_PAGE);
    const totalPages = Math.ceil(lodgings.length / LODGINGS_PER_PAGE);

    return (
        <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
            <div className="flex justify-between items-center mb-6">
                <h2 className="text-2xl font-semibold text-gray-900">
                    {category || 'Recomendaciones'}
                </h2>
                <SortDropdown
                    value={sortOption}
                    onValueChange={setSortOption}
                />
            </div>

            <LodgingGrid lodgings={currentLodgings}/>

            <Pagination
                currentPage={currentPage}
                totalPages={totalPages}
                onPageChange={setCurrentPage}
            />
        </div>
    );
};

export default LodgingCard;