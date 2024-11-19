import React, { useEffect, useState } from 'react';
import { LodgingService } from './api/LodgingService';
import LodgingCard from './components/LodgingCard';

const CategoryCard = ({ category, onClick }) => (
    <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
        <div className="relative h-0 pb-[50%]"> {/* This creates a 2:1 aspect ratio */}
            <img
                src={category.imageUrl}
                alt={category.name}
                className="absolute top-0 left-0 w-full h-full object-cover cursor-pointer"
                onClick={() => onClick(category)}
            />
        </div>
        <div className="p-4">
            <h3 className="text-xl font-semibold text-blue-900 mb-2">
                {category.name}
            </h3>
            <p
                className="text-gray-600 cursor-pointer hover:text-blue-600 transition-colors duration-200"
                onClick={() => onClick(category)}
            >
                {category.numberOfLodgings} disponibles
            </p>
        </div>
    </div>
);

function Categories() {
    const [categories, setCategories] = useState([]);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [selectedCategory, setSelectedCategory] = useState(null);

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                setIsLoading(true);
                const data = await LodgingService.getCategories();
                setCategories(data.categories);
            } catch (err) {
                setError('Error cargando categorías. Por favor, intentá nuevamente.');
                console.error('Error cargando categorías:', err);
            } finally {
                setIsLoading(false);
            }
        };

        fetchCategories();
    }, []);

    const handleCategoryClick = (category) => {
        setSelectedCategory(category.name);
    };

    if (error) {
        return (
            <div className="max-w-7xl mx-auto px-4 py-6">
                <div className="bg-red-100 text-red-700 p-4 rounded-lg" role="alert">
                    {error}
                </div>
            </div>
        );
    }

    if (isLoading) {
        return (
            <div className="max-w-7xl mx-auto px-4 py-6">
                <div className="bg-blue-100 text-blue-700 p-4 rounded-lg flex items-center justify-center">
                    Cargando categorías...
                </div>
            </div>
        );
    }

    return (
        <div className="max-w-7xl mx-auto px-4 py-6">
            {/* Title */}
            <h2 className="text-xl md:text-2xl font-semibold text-blue-900 mb-6">
                Buscar por tipo de alojamiento
            </h2>

            {/* Categories Grid */}
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                {categories.map((category, index) => (
                    <CategoryCard
                        key={index}
                        category={category}
                        onClick={handleCategoryClick}
                    />
                ))}
            </div>

            {/* Lodging Card Component */}
            {<LodgingCard category={selectedCategory} />}
        </div>
    );
}

export default Categories;