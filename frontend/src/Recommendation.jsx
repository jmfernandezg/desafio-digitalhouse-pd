import React, {useEffect, useState} from 'react';
import {LodgingService} from './api/lodgingService';
import {Heart, MapPin} from 'lucide-react';
import ReactPaginate from 'react-paginate';
import './Recommendation.css';

function Recommendation() {
    const [lodgings, setLodgings] = useState([]);
    const [sortOption, setSortOption] = useState('price');
    const [currentPage, setCurrentPage] = useState(0);
    const lodgingsPerPage = 10;

    useEffect(() => {
        LodgingService.getAllLodgings()
            .then(data => {
                const sortedLodgings = sortLodgings(data.lodgings, sortOption);
                setLodgings(sortedLodgings);
            })
            .catch(error => console.error('Error fetching lodgings:', error));
    }, [sortOption]);

    const sortLodgings = (lodgings, option) => {
        return lodgings.sort((a, b) => b[option] - a[option]);
    };

    const renderStars = (it) => {
        const stars = [];
        for (let i = 0; i < 5; i++) {
            stars.push(
                <span key={i} className={i < it ? 'star filled' : 'star empty'}>&#9733;</span>
            );
        }
        return stars;
    };

    const handlePageClick = (data) => {
        setCurrentPage(data.selected);
    };

    const startIndex = currentPage * lodgingsPerPage;
    const currentLodgings = lodgings.slice(startIndex, startIndex + lodgingsPerPage);

    return (
        <div className="recommendation">
            <h2>Recomendaciones</h2>
            <div className="sort-dropdown">
                <label htmlFor="sort">Ordenar por: </label>
                <select id="sort" value={sortOption} onChange={(e) => setSortOption(e.target.value)}>
                    <option value="price">Precio</option>
                    <option value="stars">Estrellas</option>
                    <option value="averageCustomerRating">Calificación</option>
                    <option value="distanceFromDownTown">Distancia del Centro</option>
                </select>
            </div>
            <div className="grid-container">
                {currentLodgings.map(lodging => (
                    <div key={lodging.id} className="recommendation-card">
                        <div className="photo-container">
                            <img src={lodging.displayPhoto} alt={lodging.name} className="lodging-photo"/>
                            <Heart
                                className={`heart-icon ${lodging.isFavorite ? 'favorite' : ''}`}
                                size={24}
                            />
                        </div>
                        <div className="lodging-details">
                            <div className="lodging-price">
                                <span>&#36;{lodging.price.toFixed(2)}</span>
                            </div>
                            <div className="lodging-header">
                                <div className="rating-badge">
                                    {lodging.averageCustomerRating}
                                    <div className="rating-lodging-grade">{lodging.grade}</div>
                                </div>
                            </div>
                            <div className="lodging-category-row">
                                <div className="lodging-category">{lodging.category}</div>
                                <div className="stars">{renderStars(lodging.stars)}</div>
                            </div>
                            <h3>{lodging.name}</h3>
                            <div className="lodging-address">
                                <MapPin size={16}/>
                                {lodging.distanceFromDownTown.toFixed(2)} kms del centro
                                <a href={`https://maps.google.com/?q=${lodging.address}`} target="_blank"
                                   rel="noopener noreferrer">
                                    MOSTRAR MAPA
                                </a>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
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

export default Recommendation;