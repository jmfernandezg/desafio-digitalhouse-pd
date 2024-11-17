import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { LodgingService } from '../api/LodgingService';
import { Star, Heart } from 'lucide-react';
import { Splide, SplideSlide } from '@splidejs/react-splide';
import '@splidejs/react-splide/css';
import './LodgingDetail.css';

function LodgingDetail() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [lodging, setLodging] = useState(null);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchLodging = async () => {
            try {
                setIsLoading(true);
                const data = await LodgingService.getLodgingById(id);
                setLodging(data);
            } catch (err) {
                setError('Error loading lodging details. Please try again.');
                console.error('Error loading lodging details:', err);
            } finally {
                setIsLoading(false);
            }
        };

        fetchLodging();
    }, [id]);

    if (error) {
        return (
            <div className="flex items-center justify-center h-64 text-red-500">
                {error}
            </div>
        );
    }

    if (isLoading) {
        return (
            <div className="flex items-center justify-center h-64">
                Loading lodging details...
            </div>
        );
    }

    if (!lodging) return null;

    const formatDate = (date) => {
        const options = { day: '2-digit', month: '2-digit', year: 'numeric' };
        return new Date(date).toLocaleDateString('en-GB', options);
    };

    return (
        <div className="container">
            <button className="back-button" onClick={() => navigate(-1)}>Regresar</button>
            <div className="header">
                <h1>{lodging.name}</h1>
                <div className="details">
                    <div className="stars">
                        {Array.from({ length: lodging.stars }).map((_, index) => (
                            <Star key={index} className="w-5 h-5 fill-yellow-400 text-yellow-400" />
                        ))}
                    </div>
                    <span>•</span>
                    <span>{lodging.address}</span>
                    <span>{lodging.city}</span>
                </div>
                <div className="rating-badge">
                    <span>{lodging.averageCustomerRating}</span>
                    <span>{lodging.grade}</span>
                </div>
                <div className="favorite-icon">
                    {lodging.isFavorite ? <Heart className="heart-icon filled" /> : <Heart className="heart-icon" />}
                </div>
            </div>

            <div className="content">
                <div className="details-grid">
                    <div className="left-column">
                        <h2>Acerca de este lugar</h2>
                        <p>{lodging.description}</p>
                    </div>

                    <div className="right-column">
                        <div className="price">
                            ${lodging.price.toFixed(2)}<span>/noche</span>
                        </div>
                        <div className="additional-details">
                            <div className="detail">
                                <span>Location:</span>
                                <span>{lodging.address}</span>
                            </div>
                            <div className="detail">
                                <span>Disponible desde:</span>
                                <span>{formatDate(lodging.availableFrom)}</span>
                            </div>
                            <div className="detail">
                                <span>Disponible hasta :</span>
                                <span>{formatDate(lodging.availableTo)}</span>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="splide">
                    <Splide>
                        {lodging.photos.map((photo, index) => (
                            <SplideSlide key={index}>
                                <img src={photo} alt={`Slide ${index}`} />
                            </SplideSlide>
                        ))}
                    </Splide>
                </div>
            </div>
        </div>
    );
}

export default LodgingDetail;