import React from 'react';
import Header from './Header';
import SearchBar from './SearchBar';
import Categories from './Categories';
import Recommendation from './Recommendation';

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