import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { LodgingService } from '../api/lodgingService';
import { Star } from 'lucide-react';
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

    return (
        <div className="container">
            <button className="back-button" onClick={() => navigate(-1)}>Back</button>
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
                </div>
            </div>

            <div className="splide">


            </div>

            <div className="details-grid">
                <div className="left-column">
                    <h2>About this place</h2>
                    <p>{lodging.description}</p>
                </div>

                <div className="right-column">
                    <div className="price">
                        ${lodging.price.toFixed(2)}<span>/night</span>
                    </div>
                    <div className="additional-details">
                        <div className="detail">
                            <span>Location:</span>
                            <span>{lodging.address}</span>
                        </div>
                        <div className="detail">
                            <span>Rating:</span>
                            <div className="stars">
                                {Array.from({ length: lodging.stars }).map((_, index) => (
                                    <Star key={index} className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                                ))}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default LodgingDetail;