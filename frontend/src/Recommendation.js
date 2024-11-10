import React, {useEffect, useState} from 'react';
import axios from "axios";
import './Recommendation.css';

const BACKEND_URL = process.env.REACT_APP_BACKEND_URL || 'http://backend:8080';

function Recommendation() {
    const [lodgings, setLodgings] = useState([]);

    useEffect(() => {
        axios.get(`${BACKEND_URL}/v1/lodging`)
            .then(response => {
                const sortedLodgings = response.data.sort((a, b) => b.rating - a.rating);
                setLodgings(sortedLodgings.slice(0, 6));
            })
            .catch(error => console.error('Error fetching lodgings:', error));
    }, []);

    return (
        <div className="recommendation">
            <h2>Recomendaciones</h2>
            <div className="grid-container">
                {lodgings.map(lodging => (
                    <div key={lodging.id} className="recommendation-card">
                        <img src={lodging.photos[0].url} alt={lodging.name} className="lodging-photo"/>
                        <h3>{lodging.name}</h3>
                        <p>Rating: {lodging.rating}</p>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default Recommendation;