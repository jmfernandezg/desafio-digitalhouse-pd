import React, { useEffect, useState } from 'react';
import { LodgingService } from './api/LodgingService';
import ReactPaginate from 'react-paginate';
import LodgingGrid from './components/LodgingGrid';
import SortDropdown from './components/SortDropdown';
import './Lodgings.css';

function Lodgings({ category }) {
    const [lodgings, setLodgings] = useState([]);
    const [sortOption, setSortOption] = useState('price');
    const [currentPage, setCurrentPage] = useState(0);
    const lodgingsPerPage = 10;

    useEffect(() => {
        const fetchLodgings = async () => {
            try {
                const data = category
                    ? await LodgingService.getLodgingsByCategory(category)
                    : await LodgingService.getAllLodgings();
                const sortedLodgings = sortLodgings(data.lodgings, sortOption);
                setLodgings(sortedLodgings);
            } catch (error) {
                console.error('Error fetching lodgings:', error);
            }
        };

        fetchLodgings();
    }, [category, sortOption]);

    const sortLodgings = (lodgings, option) => {
        return lodgings.sort((a, b) => b[option] - a[option]);
    };

    const handlePageClick = (data) => {
        setCurrentPage(data.selected);
    };

    const startIndex = currentPage * lodgingsPerPage;
    const currentLodgings = lodgings.slice(startIndex, startIndex + lodgingsPerPage);

    return (
        <div className="recommendation">
            <div className="recommendation-title">
                <span>{category ? category : 'Recomendaciones'}</span>
                <SortDropdown sortOption={sortOption} setSortOption={setSortOption} />
            </div>

            <LodgingGrid lodgings={currentLodgings} />

            <ReactPaginate
                previousLabel={'Anterior'}
                nextLabel={'Siguiente'}
                breakLabel={'...'}
                pageCount={Math.ceil(lodgings.length / lodgingsPerPage)}
                marginPagesDisplayed={2}
                pageRangeDisplayed={5}
                onPageChange={handlePageClick}
                containerClassName={'pagination'}
                activeClassName={'active'}
            />
        </div>
    );
}

export default Lodgings;