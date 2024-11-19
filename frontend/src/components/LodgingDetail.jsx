import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { LodgingService } from '../api/LodgingService';
import { Star, Heart, ArrowLeft, MapPin, Calendar, Info } from 'lucide-react';
import { Splide, SplideSlide } from '@splidejs/react-splide';
import '@splidejs/react-splide/css';

const LoadingState = () => (
    <div className="flex items-center justify-center h-64 space-x-2">
        <div className="w-4 h-4 bg-blue-600 rounded-full animate-bounce [animation-delay:-0.3s]" />
        <div className="w-4 h-4 bg-blue-600 rounded-full animate-bounce [animation-delay:-0.15s]" />
        <div className="w-4 h-4 bg-blue-600 rounded-full animate-bounce" />
    </div>
);

const ErrorState = ({ message }) => (
    <div className="flex flex-col items-center justify-center h-64 text-red-500 space-y-4">
        <Info className="w-12 h-12" />
        <p className="text-lg font-medium">{message}</p>
    </div>
);

const ImageGallery = ({ photos }) => (
    <div className="flex-1">
        <Splide
            options={{
                type: 'loop',
                perPage: 1,
                gap: '1rem',
                arrows: true,
                pagination: true,
            }}
            className="rounded-xl overflow-hidden shadow-lg"
        >
            {photos.map((photo, index) => (
                <SplideSlide key={index}>
                    <img
                        src={photo}
                        alt={`View ${index + 1}`}
                        className="w-full h-[500px] object-cover"
                    />
                </SplideSlide>
            ))}
        </Splide>
    </div>
);

const StarRating = ({ rating }) => (
    <div className="flex gap-1">
        {Array.from({ length: rating }).map((_, index) => (
            <Star key={index} className="w-5 h-5 fill-yellow-400 text-yellow-400" />
        ))}
    </div>
);

const RatingBadge = ({ rating, grade }) => (
    <div className="inline-flex flex-col items-center px-4 py-2 bg-blue-50 rounded-lg">
        <span className="text-2xl font-bold text-blue-600">{rating}</span>
        <span className="text-sm text-blue-600">{grade}</span>
    </div>
);

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

    if (error) return <ErrorState message={error} />;
    if (isLoading) return <LoadingState />;
    if (!lodging) return null;

    const formatDate = (date) => {
        const options = { day: '2-digit', month: '2-digit', year: 'numeric' };
        return new Date(date).toLocaleDateString('en-GB', options);
    };

    return (
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <button
                onClick={() => navigate(-1)}
                className="mb-6 flex items-center gap-2 px-4 py-2 text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
            >
                <ArrowLeft className="w-5 h-5" />
                <span>Regresar</span>
            </button>

            <div className="space-y-8">
                {/* Header Section */}
                <div className="flex justify-between items-start">
                    <div className="space-y-4">
                        <h1 className="text-4xl font-bold text-gray-900">{lodging.name}</h1>
                        <div className="flex items-center gap-4 text-gray-600">
                            <StarRating rating={lodging.stars} />
                            <span>•</span>
                            <div className="flex items-center gap-2">
                                <MapPin className="w-5 h-5" />
                                <span>{lodging.address}, {lodging.city}</span>
                            </div>
                        </div>
                    </div>

                    <div className="flex items-start gap-4">
                        <RatingBadge
                            rating={lodging.averageCustomerRating}
                            grade={lodging.grade}
                        />
                        <button
                            className={`p-2 rounded-full hover:bg-gray-100 transition-colors ${
                                lodging.isFavorite ? 'text-red-500' : 'text-gray-400'
                            }`}
                        >
                            <Heart className={`w-6 h-6 ${lodging.isFavorite ? 'fill-current' : ''}`} />
                        </button>
                    </div>
                </div>

                {/* Content Grid */}
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                    <ImageGallery photos={lodging.photos} />

                    <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 space-y-6">
                        <div className="space-y-4">
                            <h2 className="text-2xl font-semibold text-gray-900">Acerca de este lugar</h2>
                            <p className="text-gray-600 leading-relaxed">{lodging.description}</p>
                        </div>

                        <div className="pt-6 border-t border-gray-100">
                            <div className="flex items-baseline gap-2 mb-6">
                <span className="text-3xl font-bold text-gray-900">
                  ${lodging.price.toFixed(2)}
                </span>
                                <span className="text-gray-600">/noche</span>
                            </div>

                            <div className="space-y-4 text-gray-600">
                                <div className="flex items-center gap-2">
                                    <Calendar className="w-5 h-5 text-gray-400" />
                                    <span>Disponible desde: {formatDate(lodging.availableFrom)}</span>
                                </div>
                                <div className="flex items-center gap-2">
                                    <Calendar className="w-5 h-5 text-gray-400" />
                                    <span>Disponible hasta: {formatDate(lodging.availableTo)}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default LodgingDetail;