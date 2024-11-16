import React from 'react';
import './SearchBar.css';
import { Search, Calendar, MapPin } from 'lucide-react';

function SearchBar() {
    return (
        <div className="search-bar-container">
            <h2>Buscar ofertas en hoteles, casas y mucho más</h2>
            <div className="search-bar">
                <div className="input-container">
                    <div className="input-icon">
                        <MapPin className="absolute left-3 top-1/2 -translate-y-1/2 text-teal-600" size={20}/>
                    </div>
                    <input type="text" placeholder="¿A donde vamos?"/>
                </div>
                <div className="input-container">
                    <div className="input-icon">
                        <Calendar className="absolute left-3 top-1/2 -translate-y-1/2 text-teal-600" size={20}/>
                    </div>
                    <input
                        type="date"
                        className="pl-10 pr-4 py-2 border border-gray-200 rounded focus:border-teal-500 focus:ring-1 focus:ring-teal-500"
                    />
                </div>
                <button>Buscar</button>
            </div>
        </div>
    );
}

export default SearchBar;