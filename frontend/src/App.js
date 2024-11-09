import React from 'react';
import Header from './Header';
import SearchBar from './SearchBar';
import Categories from './Categories';
import Recommendation from './Recommendation';
import './App.css';

function App() {
    return (
        <div>
            <Header />
            <SearchBar />
            <Categories />
            <Recommendation />
        </div>
    );
}

export default App;