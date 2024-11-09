import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Categories.css';

function Categories() {
    const [categories, setCategories] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:8080/v1/lodging/categories')
            .then(response => {
                setCategories(response.data.categories);
            })
            .catch(error => {
                console.error('There was an error fetching the categories!', error);
            });
    }, []);

    return (
        <div className="categories">
            <h2>Buscar por tipo de alojamiento</h2>
            <div className="category-cards">
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