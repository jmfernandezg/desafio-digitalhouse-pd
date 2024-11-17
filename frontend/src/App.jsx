import React from 'react';
import Header from './Header';
import SearchBar from './SearchBar';
import Categories from './Categories';
import Recommendation from './Recommendation';
import Footer from './Footer';
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
            <Footer/>
        </div>
    );
}

export default App;