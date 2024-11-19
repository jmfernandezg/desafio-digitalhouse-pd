import React from 'react';
import { Link } from 'react-router-dom';
import { Heart, MapPin, Star } from 'lucide-react';

const RatingBadge = ({ rating, reviewCount, grade }) => (
    <div className="absolute top-4 right-4 bg-white px-3 py-1 rounded-lg shadow-md">
        <div className="flex items-center gap-1">
            <Star className="w-4 h-4 fill-yellow-400 text-yellow-400" />
            <span className="font-semibold">{rating}</span>
            <span className="text-gray-600 text-sm">{`(${reviewCount})`}</span>
        </div>
        <div className="text-sm text-gray-600 text-center mt-1">
            {grade}
        </div>
    </div>
);

const StarRating = ({ rating }) => (
    <div className="flex gap-1">
        {[...Array(5)].map((_, index) => (
            <Star
                key={index}
                className={`w-4 h-4 ${
                    index < rating
                        ? "fill-yellow-400 text-yellow-400"
                        : "fill-gray-200 text-gray-200"
                }`}
            />
        ))}
    </div>
);

function LodgingGrid({ lodgings }) {
    return (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {lodgings.map(lodging => (
                <div
                    key={lodging.id}
                    className="bg-white rounded-lg shadow-md overflow-hidden flex flex-col md:flex-row border border-gray-200"
                >
                    {/* Image Section */}
                    <div className="relative md:w-1/2">
                        <img
                            src={lodging.displayPhoto}
                            alt={lodging.name}
                            className="w-full h-full object-cover"
                        />
                        <button
                            className={`absolute top-4 left-4 p-2 rounded-full bg-white shadow-md
                ${lodging.isFavorite ? 'text-red-500' : 'text-gray-400'}`}
                        >
                            <Heart className={`w-5 h-5 ${lodging.isFavorite ? 'fill-current' : ''}`} />
                        </button>
                        <RatingBadge
                            rating={lodging.averageCustomerRating}
                            reviewCount={lodging.reviewCount || 123}
                            grade={lodging.grade || "Excelente"}
                        />
                    </div>

                    {/* Content Section */}
                    <div className="p-6 md:w-1/2 flex flex-col">
                        <div className="flex justify-between items-start mb-2">
                            <div>
                                <h3 className="text-xl font-semibold text-gray-900 hover:text-blue-600">
                                    <Link to={`/lodging/${lodging.id}`}>
                                        {lodging.name}
                                    </Link>
                                </h3>
                                <p className="text-sm text-gray-600 mt-1">
                                    {lodging.city}, {lodging.country}
                                </p>
                            </div>
                            <p className="text-xl font-bold text-blue-600">
                                ${lodging.price.toFixed(2)}
                            </p>
                        </div>

                        <div className="flex items-center gap-2 mb-4">
                            <span className="text-sm text-gray-600">{lodging.category}</span>
                            <StarRating rating={lodging.stars} />
                        </div>

                        <div className="flex items-center gap-2 text-sm text-gray-600 mb-4">
                            <MapPin className="w-4 h-4" />
                            <span>{lodging.distanceFromDownTown.toFixed(2)} kms del centro</span>
                            <a
                                href={`https://maps.google.com/?q=${lodging.address}`}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="ml-2 text-blue-600 hover:underline"
                            >
                                MOSTRAR MAPA
                            </a>
                        </div>

                        {/* Availability Dates */}
                        <div className="mt-auto">
                            <p className="text-sm text-gray-600 mb-1">Disponibilidad:</p>
                            <p className="text-sm font-medium">
                                {new Date(lodging.availableFrom || Date.now()).toLocaleDateString('es-ES')} -
                                {' '}
                                {new Date(lodging.availableTo || Date.now() + 7776000000).toLocaleDateString('es-ES')}
                            </p>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
}

export default LodgingGrid;