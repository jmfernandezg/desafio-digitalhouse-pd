import React, { useState, useEffect } from 'react';

function LodgingForm({ onSubmit, lodging }) {
    const [formData, setFormData] = useState({
        name: '',
        address: '',
        city: '',
        price: '',
        stars: '',
        averageCustomerRating: '',
        description: '',
        category: '',
        availableFrom: '',
        availableTo: '',
        isFavorite: false,
    });

    useEffect(() => {
        if (lodging) {
            setFormData(lodging);
        }
    }, [lodging]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(formData);
        setFormData({
            name: '',
            address: '',
            city: '',
            price: '',
            stars: '',
            averageCustomerRating: '',
            description: '',
            category: '',
            availableFrom: '',
            availableTo: '',
            isFavorite: false,
        });
    };

    return (
        <form className="admin-form" onSubmit={handleSubmit}>
            <input type="text" name="name" value={formData.name} onChange={handleChange} placeholder="Name" required />
            <input type="text" name="address" value={formData.address} onChange={handleChange} placeholder="Address" required />
            <input type="text" name="city" value={formData.city} onChange={handleChange} placeholder="City" required />
            <input type="number" name="price" value={formData.price} onChange={handleChange} placeholder="Price" required />
            <input type="number" name="stars" value={formData.stars} onChange={handleChange} placeholder="Stars" required />
            <input type="number" name="averageCustomerRating" value={formData.averageCustomerRating} onChange={handleChange} placeholder="Rating" required />
            <input type="text" name="description" value={formData.description} onChange={handleChange} placeholder="Description" required />
            <input type="text" name="category" value={formData.category} onChange={handleChange} placeholder="Category" required />
            <input type="datetime-local" name="availableFrom" value={formData.availableFrom} onChange={handleChange} required />
            <input type="datetime-local" name="availableTo" value={formData.availableTo} onChange={handleChange} required />
            <label>
                <input type="checkbox" name="isFavorite" checked={formData.isFavorite} onChange={(e) => setFormData({ ...formData, isFavorite: e.target.checked })} />
                Favorito
            </label>
            <button type="submit">Aceptar</button>
        </form>
    );
}

export default LodgingForm;