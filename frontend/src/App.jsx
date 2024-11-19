import React from 'react';
import { BrowserRouter as Router, Route, Routes, useLocation } from 'react-router-dom';
import Header from './Header';
import SearchBar from './SearchBar';
import Categories from './Categories';
import LodgingCard from './components/LodgingCard';
import LodgingDetail from './components/LodgingDetail';
import Footer from './Footer';
import Admin from "./Admin";

// Layout wrapper component for consistent page structure
const Layout = ({ children, showSearch = true }) => {
    const location = useLocation();
    const isAdminRoute = location.pathname === '/admin';
    const isLodgingDetail = location.pathname.includes('/lodging/');

    return (
        <div className="min-h-screen flex flex-col bg-slate-50 font-['Fira_Sans']">
            <header className="w-full bg-white shadow-sm">
                <Header />
                {showSearch && !isLodgingDetail && !isAdminRoute && (
                    <div className="w-full bg-sky-50">
                        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
                            <SearchBar />
                        </div>
                    </div>
                )}
            </header>

            <main className="flex-grow w-full">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    {children}
                </div>
            </main>

            <Footer />
        </div>
    );
};

// Routes configuration for cleaner organization
const AppRoutes = () => {
    return (
        <Routes>
            <Route
                path="/"
                element={
                    <Layout>
                        <Categories />
                    </Layout>
                }
            />
            <Route
                path="/lodgings"
                element={
                    <Layout>
                        <LodgingCard />
                    </Layout>
                }
            />
            <Route
                path="/lodging/:id"
                element={
                    <Layout>
                        <LodgingDetail />
                    </Layout>
                }
            />
            <Route
                path="/admin"
                element={
                    <Layout showSearch={false}>
                        <Admin />
                    </Layout>
                }
            />
        </Routes>
    );
};

const App = () => {
    return (
        <Router>
            <AppRoutes />
        </Router>
    );
};

export default App;