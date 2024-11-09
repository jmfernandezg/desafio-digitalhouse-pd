import React from 'react';

function Recommendation() {
    const recommendations = [
        { name: 'Hotel A', image: 'hotelA.jpg', rating: 4.5, distance: '500m' },
        { name: 'Hotel B', image: 'hotelB.jpg', rating: 4.0, distance: '1km' },
        { name: 'Hotel C', image: 'hotelC.jpg', rating: 3.5, distance: '2km' },
    ];

    return (
        <div className="recommendations">
            {recommendations.map((hotel, index) => (
                <div key={index} className="recommendation">
                    <img src={hotel.image} alt={hotel.name} />
                    <div>{hotel.name}</div>
                    <div>Rating: {hotel.rating}</div>
                    <div>Distance: {hotel.distance}</div>
                    <button>Mostrar en el mapa</button>
                </div>
            ))}
        </div>
    );
}

export default Recommendation;