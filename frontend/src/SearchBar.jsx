import React, { useState, useEffect } from 'react';
import './SearchBar.css';
import { Calendar, MapPin, Search } from 'lucide-react';
import { Button } from "@mui/material";
import { LodgingService } from './api/LodgingService';
import { DatePicker, LocalizationProvider } from '@mui/x-date-pickers';

function SearchBar() {
    const [query, setQuery] = useState('');
    const [suggestions, setSuggestions] = useState([]);
    const [cities, setCities] = useState([]);

    useEffect(() => {
        const fetchCities = async () => {
            try {
                const citiesData = await LodgingService.getCities();
                setCities(citiesData);
            } catch (error) {
                console.error('Error fetching cities:', error);
            }
        };

        fetchCities();
    }, []);

    const handleInputChange = (event) => {
        const value = event.target.value;
        setQuery(value);

        if (value.length > 0) {
            const filteredSuggestions = cities.filter(city =>
                city.toLowerCase().includes(value.toLowerCase())
            );
            setSuggestions(filteredSuggestions);
        } else {
            setSuggestions([]);
        }
    };

    const handleSuggestionClick = (suggestion) => {
        setQuery(suggestion);
        setSuggestions([]);
    };

    return (
        <div className="search-bar-container">
            <h2>Buscar ofertas en hoteles, casas y mucho más</h2>
            <div className="search-bar">
                <div className="input-container">
                    <div className="input-icon">
                        <MapPin size={20} />
                    </div>
                    <input
                        type="text"
                        placeholder="¿A donde vamos?"
                        value={query}
                        onChange={handleInputChange}
                    />
                    {suggestions.length > 0 && (
                        <ul className="suggestions-list">
                            {suggestions.map((suggestion, index) => (
                                <li
                                    key={index}
                                    onClick={() => handleSuggestionClick(suggestion)}
                                >
                                    {suggestion}
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
                <div className="input-container">
                    <div className="input-icon">
                        <Calendar size={20} />
                    </div>
                    <LocalizationProvider>
                        <DatePicker label="Check in - Check out" />
                    </LocalizationProvider>
                </div>
                <div className="input-container">
                    <div className="input-icon">
                        <Button><Search size={16} /> Buscar</Button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default SearchBar;