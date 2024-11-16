import React from 'react';
import './SearchBar.css';
import {Calendar, MapPin, Search} from 'lucide-react';

function SearchBar() {
    return (
        <div className="search-bar-container">
            <h2>Buscar ofertas en hoteles, casas y mucho más</h2>
            <div className="search-bar">
                <div className="input-container">
                    <div className="input-icon">
                        <MapPin size={20}/>
                    </div>
                    <input type="text" placeholder="¿A donde vamos?"/>
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