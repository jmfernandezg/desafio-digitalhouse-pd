import React, { useState, useEffect } from 'react';
import axios from 'axios';

function Lodgings() {
    const [lodgings, setLodgings] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:8080/v1/lodgings')
            .then(response => setLodgings(response.data))
            .catch(error => console.error('Error fetching lodgings:', error));
    }, []);

    return (
        <div>
            <h1>Lodgings</h1>
            <ul>
                {lodgings.map(lodging => (
                    <li key={lodging.id}>{lodging.name}</li>
                ))}
            </ul>
        </div>
    );
}

export default Lodgings;