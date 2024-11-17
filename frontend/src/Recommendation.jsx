import React, { useEffect, useState } from 'react';
import { LodgingService } from './api/lodgingService';
import { MapPin, Heart } from 'lucide-react';
import './Recommendation.css';

function Recommendation() {
    const [lodgings, setLodgings] = useState([]);

    useEffect(() => {
        LodgingService.getAllLodgings()
            .then(data => {
                const sortedLodgings = data.lodgings.sort((a, b) => b.stars - a.stars);
                setLodgings(sortedLodgings.slice(0, 10));
            })
            .catch(error => console.error('Error fetching lodgings:', error));
    }, []);

    const renderStars = (it) => {
        const stars = [];
        for (let i = 0; i < 5; i++) {
            stars.push(
                <span key={i} className={i < it ? 'star filled' : 'star'}>&#9733;</span>
            );
        }
        return stars;
    };

    return (
        <div className="recommendation">
            <h2>Recomendaciones</h2>
            <div className="grid-container">
                {lodgings.map(lodging => (
                    <div key={lodging.id} className="recommendation-card">
                        <div className="photo-container">
                            <img src={lodging.displayPhoto} alt={lodging.name} className="lodging-photo"/>
                            <Heart
                                className={`heart-icon ${lodging.isFavorite ? 'favorite' : ''}`}
                                size={24}
                            />
                        </div>
                        <div className="lodging-details">
                            <div className="lodging-header">
                                <span>{lodging.category}</span>
                                <div className="stars">{renderStars(lodging.stars)}</div>
                            </div>
                            <h3>{lodging.name}</h3>
                            <p>
                                <MapPin size={16}/>
                                <a href={`https://maps.google.com/?q=${lodging.address}`} target="_blank" rel="noopener noreferrer">
                                    {lodging.distanceFromDowntown}
                                </a>
                            </p>
                            <div className="rating-badge">{lodging.averageCustomerRating}</div>
                            <div className="lodging-grade">{lodging.grade}</div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default Recommendation;