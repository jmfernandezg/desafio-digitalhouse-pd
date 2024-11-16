import React from 'react';
import Header from './Header';
import SearchBar from './SearchBar';
import Categories from './Categories';
import Recommendation from './Recommendation';
import './App.css';

function App() {
    return (
        <div>
            <div className="mainHeader">
                <Header/>
                <SearchBar/>
            </div>
            <div className="main">
                <Categories/>
                <Recommendation/>
            </div>
        </div>
    );
}

export default App;