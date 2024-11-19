import React, { useEffect, useState } from 'react';
import { LodgingService } from '../api/LodgingService';
import ReactPaginate from 'react-paginate';
import LodgingGrid from './LodgingGrid';
import SortDropdown from './SortDropdown';
import './LodgingCard.css';

function LodgingCard({ category }) {
    console.log('category:', category);
    const [lodgings, setLodgings] = useState([]);
    const [sortOption, setSortOption] = useState('price');
    const [currentPage, setCurrentPage] = useState(0);
    const lodgingsPerPage = 10;

    useEffect(() => {
        const fetchLodgings = async () => {
            try {
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

export default LodgingCard;