import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './Header';
import SearchBar from './SearchBar';
import Categories from './Categories';
import Lodgings from './Lodgings';
import LodgingDetail from './components/LodgingDetail';
import Footer from './Footer';
import './App.css';

function App() {
    return (
        <Router>
            <div>
                <div className="mainHeader">
                    <Header/>
                    <SearchBar/>
                </div>
                <div className="main">
                    <Routes>
                        <Route path="/" element={<Categories/>} />
                        <Route path="/lodgings" element={<Lodgings/>} />
                        <Route path="/lodging/:id" element={<LodgingDetail/>} />
                    </Routes>
                </div>
                <Footer/>
            </div>
        </Router>
    );
}

export default App;