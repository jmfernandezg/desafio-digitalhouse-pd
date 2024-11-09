import React from 'react';
import './SearchBar.css';

function SearchBar() {
    return (
        <div className="search-bar">
            <input type="text" placeholder="Location" />
            <input type="date" />
            <button>Buscar</button>
        </div>
    );
}

export default SearchBar;