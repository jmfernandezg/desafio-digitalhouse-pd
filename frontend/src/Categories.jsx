import React, { useEffect, useState } from 'react';
import { LodgingService } from './api/lodgingService';
import './Categories.css';

function Categories() {
    const [categories, setCategories] = useState([]);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

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

    if (error) {
        return <div className="error-message">{error}</div>;
    }

    if (isLoading) {
        return <div className="loading">Cargando categorías...</div>;
    }

    return (
        <div className="categories">
            <h2>Buscar por tipo de alojamiento</h2>
            <div className="categories-grid-container">
                {categories.map((category, index) => (
                    <div key={index} className="category-card">
                        <img src={category.imageUrl} alt={category.name} />
                        <h3>{category.name}</h3>
                        <p>{category.numberOfLodgings} lodgings available</p>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default Categories;