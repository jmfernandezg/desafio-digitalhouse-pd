import React from 'react';
import {BrowserRouter as Router, Route, Routes, useLocation} from 'react-router-dom';
import Header from './Header';
import SearchBar from './SearchBar';
import Categories from './Categories';
import Lodgings from './Lodgings';
import LodgingDetail from './components/LodgingDetail';
import Footer from './Footer';
import './App.css';
import Admin from "./Admin";

function App() {
    return (
        <Router>
            <MainContent />
        </Router>
    );
}

function MainContent() {
    const location = useLocation();
    const isAdminRoute = location.pathname === '/admin';

    return (
        <div>
            <div className="mainHeader">
                <Header />
                {!isAdminRoute && <SearchBar />}
            </div>
            <div className="main">
                <Routes>
                    <Route path="/" element={<Categories />} />
                    <Route path="/lodgings" element={<Lodgings />} />
                    <Route path="/lodging/:id" element={<LodgingDetail />} />
                    <Route path="/admin" element={<Admin />} />
                </Routes>
            </div>
            <Footer />
        </div>
    );
}

export default App;