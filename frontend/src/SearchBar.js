import React from 'react';
import './SearchBar.css';

function SearchBar() {
    return (
        <div className="search-bar-container">
            <div className="search-bar">
                <div className="input-container">
                    <div className="input-icon">
                        <i className="fas fa-map-pin"></i>
                    </div>
                    <input type="text" placeholder="¿A donde vamos?" />
                </div>
                <div className="input-container">
                    <div className="input-icon">
                        <i className="fas fa-calendar-alt"></i>
                    </div>
                    <input type="date" />
                </div>
                <button>Buscar</button>
            </div>
        </div>
    );
}

export default SearchBar;