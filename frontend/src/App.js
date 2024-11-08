import React from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Lodgings from './Lodgings';
import Customers from './Customers';
import Navbar from './Navbar';

function App() {
    return (
        <Router>
            <div>
                <Navbar/>
                <Routes>
                    <Route path="/lodgings" element={<Lodgings/>}/>
                    <Route path="/customers" element={<Customers/>}/>
                </Routes>
            </div>
        </Router>
    );
}

export default App;