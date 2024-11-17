import React, { useEffect, useState } from 'react';
import { LodgingService } from './api/lodgingService';
import { Card, CardContent, CardMedia, Typography } from '@mui/material';
import Lodgings from './Lodgings';
import './Categories.css';

function Categories() {
    const [categories, setCategories] = useState([]);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [selectedCategory, setSelectedCategory] = useState(null);
    const [lodgings, setLodgings] = useState([]);

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

    const handleCategoryClick = async (category) => {
        try {
            const data = await LodgingService.getLodgingsByCategory(category.name);
            setLodgings(data.lodgings);
            setSelectedCategory(category.name);
        } catch (err) {
            setError('Error cargando alojamientos. Por favor, intentá nuevamente.');
            console.error('Error cargando alojamientos:', err);
        }
    };

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
                    <Card key={index} sx={{ maxWidth: 345 }}>
                        <CardMedia
                            component="img"
                            height="140"
                            image={category.imageUrl}
                            alt={category.name}
                        />
                        <CardContent>
                            <Typography gutterBottom variant="h5" component="div">
                                {category.name}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                <span onClick={() => handleCategoryClick(category)} style={{ cursor: 'pointer', color: 'blue' }}>
                                    {category.numberOfLodgings} disponibles
                                </span>
                            </Typography>
                        </CardContent>
                    </Card>
                ))}
            </div>
            {selectedCategory && <Lodgings lodgings={lodgings} />}
        </div>
    );
}

export default Categories;