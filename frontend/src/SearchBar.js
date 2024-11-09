import React from 'react';
import './SearchBar.css';

function SearchBar() {
    return (
        <div className="search-bar">
            <input type="text" placeholder="¿A donde vamos?" />
            <input type="date" />
            <button>Buscar</button>
        </div>
    );
}

export default SearchBar;