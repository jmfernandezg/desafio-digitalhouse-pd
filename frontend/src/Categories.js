import React from 'react';

function Categories() {
    const roomTypes = [
        { name: 'Hoteles', image: 'hotel.jpg', count: 10 },
        { name: 'Hostels', image: 'hostel.jpg', count: 5 },
        { name: 'Departamentos', image: 'apartment.jpg', count: 8 },
        { name: 'Bed and breakfast', image: 'bnb.jpg', count: 3 },
    ];

    return (
        <div className="room-types">
            {roomTypes.map((room, index) => (
                <div key={index} className="room-type">
                    <img src={room.image} alt={room.name} />
                    <div>{room.name}</div>
                    <div>{room.count} available</div>
                </div>
            ))}
        </div>
    );
}

export default Categories;