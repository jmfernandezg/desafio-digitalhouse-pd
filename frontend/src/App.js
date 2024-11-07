// frontend/src/App.js
import axios from 'axios';
import React, { useEffect, useState } from 'react';
import './App.css';

function App() {
  const [hotels, setHotels] = useState([]);

  useEffect(() => {
    const fetchHotels = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/hotels');
        setHotels(response.data);
      } catch (error) {
        console.error('Error fetching hotels:', error);
      }
    };

    fetchHotels();
  }, []);

  return (
    <div className="App">
      <header className="App-header">
        <h1>Hotels</h1>
      </header>
      <main>
        <div className="hotels-grid">
          {hotels.map(hotel => (
            <div key={hotel.id} className="hotel-card">
              <h2>{hotel.name}</h2>
              <p>{hotel.address}</p>
              <p>Rating: {hotel.rating} / 5</p>
            </div>
          ))}
        </div>
      </main>
    </div>
  );
}

export default App;
