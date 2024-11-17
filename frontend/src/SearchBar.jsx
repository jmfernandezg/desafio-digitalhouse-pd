import React, {useState} from 'react';
import './SearchBar.css';
import {Calendar, MapPin, Search} from 'lucide-react';

function SearchBar() {
    const [query, setQuery] = useState('');
    const [suggestions, setSuggestions] = useState([]);

    const handleInputChange = async (event) => {
        const value = event.target.value;
        setQuery(value);

    };

    const handleSuggestionClick = (suggestion) => {
        setQuery(suggestion.city);
        setSuggestions([]);
    };

    return (
        <div className="search-bar-container">
            <h2>Buscar ofertas en hoteles, casas y mucho más</h2>
            <div className="search-bar">
                <div className="input-container">
                    <div className="input-icon">
                        <MapPin size={20}/>
                    </div>
                    <input
                        type="text"
                        placeholder="¿A donde vamos?"
                        value={query}
                        onChange={handleInputChange}
                    />
                    {suggestions.length > 0 && (
                        <ul className="suggestions-list">
                            {suggestions.map((suggestion) => (
                                <li
                                    key={suggestion.id}
                                    onClick={() => handleSuggestionClick(suggestion)}
                                >
                                    {suggestion.city}, {suggestion.country}
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
                <div className="input-container">
                    <div className="input-icon">
                        <Calendar size={20}/>
                    </div>
                    <input type="date"/>
                </div>
                <div className="input-container">
                    <div className="input-icon">
                        <button><Search size={16}/> Buscar</button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default SearchBar;