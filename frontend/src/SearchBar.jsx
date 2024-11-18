import React, {useEffect, useState} from 'react';
import './SearchBar.css';
import {Calendar, Loader, MapPin, Search} from 'lucide-react';
import {Button, ClickAwayListener, Paper, Popper, TextField} from "@mui/material";
import {DateRange} from 'react-date-range';
import {addDays} from 'date-fns';
import {es} from 'date-fns/locale';
import 'react-date-range/dist/styles.css';
import 'react-date-range/dist/theme/default.css';
import {LodgingService} from './api/LodgingService';

function SearchBar() {
    const [query, setQuery] = useState('');
    const [suggestions, setSuggestions] = useState([]);
    const [cities, setCities] = useState([]);
    const [dateState, setDateState] = useState([{
        startDate: new Date(), endDate: addDays(new Date(), 1), key: 'selection'
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
            const filteredSuggestions = cities.filter(city => city.toLowerCase().includes(value.toLowerCase()));
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

    // Handle date change
    const handleDateChange = (ranges) => {
        setDateState([ranges.selection]);
    };

    return (<div className="search-bar-container" role="search">
        <h2 className="text-2xl font-bold mb-4">
            Buscar ofertas en hoteles, casas y mucho más
        </h2>

        <div className="flex flex-col md:flex-row gap-4 p-4 bg-white rounded-lg shadow-md">
            {/* Destination Input */}
            <div className="flex-1 relative">
                <TextField
                    fullWidth
                    placeholder="¿A dónde vamos?"
                    value={query}
                    onChange={handleInputChange}
                    InputProps={{
                        startAdornment: <MapPin className="mr-2" size={20}/>,
                    }}
                    aria-label="Destino"
                    error={Boolean(error && !query)}
                />

                <Popper
                    open={Boolean(suggestions.length) && Boolean(anchorEl)}
                    anchorEl={anchorEl}
                    placement="bottom-start"
                >
                    <ClickAwayListener onClickAway={handleClickAway}>
                        <Paper className="mt-1 max-h-60 overflow-auto">
                            {suggestions.map((suggestion, index) => (<div
                                key={index}
                                className="p-2 hover:bg-gray-100 cursor-pointer"
                                onClick={() => handleSuggestionClick(suggestion)}
                                role="option"
                            >
                                <MapPin className="inline mr-2" size={16}/>
                                {suggestion}
                            </div>))}
                        </Paper>
                    </ClickAwayListener>
                </Popper>
            </div>

            {/* Date Range Picker */}
            <div className="flex-1 relative">
                <TextField
                    fullWidth
                    value={formatDateRange()}
                    onClick={() => setShowDatePicker(!showDatePicker)}
                    InputProps={{
                        startAdornment: <Calendar className="mr-2" size={20}/>, readOnly: true,
                    }}
                    placeholder="Seleccionar fechas"
                />

                {showDatePicker && (<div className="absolute z-50 mt-2">
                    <ClickAwayListener onClickAway={() => setShowDatePicker(false)}>
                        <Paper elevation={3}>
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
                </div>)}
            </div>

            {/* Search Button */}
            <Button
                variant="contained"
                onClick={handleSearch}
                disabled={loading || !query || !dateState[0].startDate || !dateState[0].endDate}
                className="h-14 px-8"
                startIcon={loading ? <Loader className="animate-spin"/> : <Search/>}
            >
                {loading ? 'Buscando...' : 'Buscar'}
            </Button>
        </div>

        {/* Error Message */}
        {error && (<div className="mt-2 text-red-600 text-sm" role="alert">
            {error}
        </div>)}

        {/* Loading Cities Indicator */}
        {isLoadingCities && (<div className="mt-2 text-gray-600 text-sm flex items-center">
            <Loader className="animate-spin mr-2" size={16}/>
            Cargando ciudades...
        </div>)}
    </div>);
}

export default SearchBar;