import React, {useEffect, useState} from 'react';
import './SearchBar.css';
import {Calendar, Loader, MapPin, Search} from 'lucide-react';
import {Button, ClickAwayListener, Paper, Popper, TextField} from "@mui/material";
import {DateRange} from 'react-date-range';
import {es} from 'date-fns/locale';
import 'react-date-range/dist/styles.css';
import 'react-date-range/dist/theme/default.css';
import {LodgingService} from './api/LodgingService';

function SearchBar() {
    const [query, setQuery] = useState('');
    const [suggestions, setSuggestions] = useState([]);
    const [cities, setCities] = useState([]);
    const [dateState, setDateState] = useState([{
        startDate: new Date(),
        endDate: new Date(),
        key: 'selection'
    }]);
    const [showDatePicker, setShowDatePicker] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [anchorEl, setAnchorEl] = useState(null);
    const [isLoadingCities, setIsLoadingCities] = useState(false);

    useEffect(() => {
        const fetchCities = async () => {
            setIsLoadingCities(true);
            setError('');
            try {
                const citiesData = await LodgingService.getCities();
                setCities(citiesData);
            } catch (error) {
                setError('Error al cargar las ciudades. Por favor, inténtelo de nuevo.');
                console.error('Error fetching cities:', error);
            } finally {
                setIsLoadingCities(false);
            }
        };

        fetchCities();
    }, []);

    const handleInputChange = (event) => {
        const value = event.target.value;
        setQuery(value);
        setAnchorEl(event.currentTarget);

        if (value.length > 0) {
            const filteredSuggestions = cities.filter(city =>
                city.toLowerCase().includes(value.toLowerCase())
            );
            setSuggestions(filteredSuggestions);
        } else {
            setSuggestions([]);
        }
    };

    const handleSuggestionClick = (suggestion) => {
        setQuery(suggestion);
        setSuggestions([]);
        setAnchorEl(null);
    };

    const handleClickAway = () => {
        setSuggestions([]);
        setAnchorEl(null);
    };

    const formatDateRange = () => {
        if (!dateState[0].startDate || !dateState[0].endDate) return '';
        return `${dateState[0].startDate.toLocaleDateString('es-ES')} - ${dateState[0].endDate.toLocaleDateString('es-ES')}`;
    };

    const handleSearch = async () => {
        if (!query) {
            setError('Por favor, seleccione un destino');
            return;
        }
        if (!dateState[0].startDate || !dateState[0].endDate) {
            setError('Por favor, seleccione las fechas');
            return;
        }

        setLoading(true);
        setError('');
        try {
            await LodgingService.search({
                destination: query,
                checkIn: dateState[0].startDate.toISOString(),
                checkOut: dateState[0].endDate.toISOString()
            });
        } catch (error) {
            setError('Error al realizar la búsqueda. Por favor, inténtelo de nuevo.');
            console.error('Search error:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleDateChange = (ranges) => {
        setDateState([ranges.selection]);
    };

    return (
        <div className="search-bar-wrapper">
            <div className="search-bar-inner">
                <h2 className="search-title">
                    Buscar ofertas en hoteles, casas y mucho más
                </h2>

                <div className="search-container">
                    {/* Search inputs container */}
                    <div className="inputs-container">
                        {/* Destination Input */}
                        <div className="input-field">
                            <TextField
                                fullWidth
                                placeholder="¿A dónde vamos?"
                                value={query}
                                onChange={handleInputChange}
                                InputProps={{
                                    startAdornment: <MapPin className="icon-prefix" size={20}/>,
                                    sx: { backgroundColor: 'white' }
                                }}
                                aria-label="Destino"
                                error={Boolean(error && !query)}
                            />

                            <Popper
                                open={Boolean(suggestions.length) && Boolean(anchorEl)}
                                anchorEl={anchorEl}
                                placement="bottom-start"
                                className="suggestions-popper"
                            >
                                <ClickAwayListener onClickAway={handleClickAway}>
                                    <Paper className="suggestions-paper" elevation={3}>
                                        {suggestions.map((suggestion, index) => (
                                            <div
                                                key={index}
                                                className="suggestion-item"
                                                onClick={() => handleSuggestionClick(suggestion)}
                                                role="option"
                                            >
                                                <MapPin className="suggestion-icon" size={16}/>
                                                <span>{suggestion}</span>
                                            </div>
                                        ))}
                                    </Paper>
                                </ClickAwayListener>
                            </Popper>
                        </div>

                        {/* Date Range Input */}
                        <div className="input-field">
                            <TextField
                                fullWidth
                                value={formatDateRange()}
                                onClick={() => setShowDatePicker(!showDatePicker)}
                                InputProps={{
                                    startAdornment: <Calendar className="icon-prefix" size={20}/>,
                                    readOnly: true,
                                    sx: { backgroundColor: 'white' }
                                }}
                                placeholder="Seleccionar fechas"
                            />

                            {showDatePicker && (
                                <div className="date-picker-container">
                                    <ClickAwayListener onClickAway={() => setShowDatePicker(false)}>
                                        <Paper elevation={3} className="date-picker-paper">
                                            <DateRange
                                                onChange={handleDateChange}
                                                moveRangeOnFirstSelection={false}
                                                ranges={dateState}
                                                months={2}
                                                direction="horizontal"
                                                locale={es}
                                                minDate={new Date()}
                                                rangeColors={['#2196f3']}
                                                showMonthAndYearPickers={true}
                                                showDateDisplay={true}
                                            />
                                        </Paper>
                                    </ClickAwayListener>
                                </div>
                            )}
                        </div>
                    </div>

                    {/* Search Button */}
                    <Button
                        variant="contained"
                        onClick={handleSearch}
                        disabled={loading || !query || !dateState[0].startDate || !dateState[0].endDate}
                        className="search-button"
                        startIcon={loading ? <Loader className="animate-spin"/> : <Search/>}
                        sx={{
                            backgroundColor: '#2196f3',
                            '&:hover': {
                                backgroundColor: '#1976d2'
                            }
                        }}
                    >
                        {loading ? 'Buscando...' : 'Buscar'}
                    </Button>
                </div>

                {/* Error Message */}
                {error && (
                    <div className="error-message" role="alert">
                        {error}
                    </div>
                )}

                {/* Loading Cities Indicator */}
                {isLoadingCities && (
                    <div className="loading-indicator">
                        <Loader className="animate-spin" size={16}/>
                        <span>Cargando ciudades...</span>
                    </div>
                )}
            </div>
        </div>
    );
}

export default SearchBar;