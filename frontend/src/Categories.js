import React, {useEffect, useState} from 'react';
import axios from 'axios';
import './Categories.css';

const BACKEND_URL = process.env.REACT_APP_BACKEND_URL || 'http://backend:8080';

function Categories() {
    const [categories, setCategories] = useState([]);

    useEffect(() => {
        axios.get(`${BACKEND_URL}/v1/lodging/categories`)
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
            <div className="categories-grid-container">
                {categories.map((category, index) => (
                    <div key={index} className="category-card">
                        <img src={category.imageUrl} alt={category.name}/>
                        <h3>{category.name}</h3>
                        <p>{category.numberOfLodgings} lodgings available</p>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default Categories;