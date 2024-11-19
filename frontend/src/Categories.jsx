import React, {useEffect, useState} from 'react';
import {LodgingService} from './api/LodgingService';
import {Card, CardContent, CardMedia, Typography} from '@mui/material';
import LodgingCard from './components/LodgingCard';
import './Categories.css';

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
                    <Card key={index} sx={{maxWidth: 345}}>
                        <CardMedia
                            component="img"
                            height="140"
                            image={category.imageUrl}
                            alt={category.name}
                            style={{cursor: 'pointer'}}
                            onClick={() => handleCategoryClick(category)}
                        />
                        <CardContent>
                            <Typography gutterBottom variant="h5" component="div">
                                {category.name}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                <span onClick={() => handleCategoryClick(category)} style={{cursor: 'pointer'}}>
                                    {category.numberOfLodgings} disponibles
                                </span>
                            </Typography>
                        </CardContent>
                    </Card>
                ))}
            </div>
            <LodgingCard category={selectedCategory}/>
        </div>
    );
}

export default Categories;