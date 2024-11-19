import React, {useEffect, useState} from 'react';
import {Calendar, Loader, MapPin, Search} from 'lucide-react';
import {DateRange} from 'react-date-range';
import {es} from 'date-fns/locale';
import 'react-date-range/dist/styles.css';
import 'react-date-range/dist/theme/default.css';
import {LodgingService} from './api/LodgingService';

const TextField = ({startIcon, ...props}) => (<div className="relative flex-1 min-w-[240px]">
        <div className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400">
            {startIcon}
        </div>
        <input
            className={`w-full h-14 pl-10 pr-4 rounded-md border border-gray-300 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none
        ${props.error ? 'border-red-500' : 'border-gray-300'}`}
            {...props}
        />
    </div>);

const Suggestions = ({suggestions, onSelect, onClickAway}) => (<div className="absolute z-50 w-full max-w-[400px] mt-1">
        <div
            className="bg-white rounded-md shadow-lg max-h-[240px] overflow-auto"
            onClick={onClickAway}
        >
            {suggestions.map((suggestion, index) => (<div
                    key={index}
                    className="flex items-center gap-2 px-3 py-2 hover:bg-blue-50 cursor-pointer"
                    onClick={() => onSelect(suggestion)}
                    role="option"
                >
                    <MapPin className="text-blue-500" size={16}/>
                    <span>{suggestion}</span>
                </div>))}
        </div>
    </div>);

function SearchBar() {
    const [query, setQuery] = useState('');
    const [suggestions, setSuggestions] = useState([]);
    const [cities, setCities] = useState([]);
    const [dateState, setDateState] = useState([{
        startDate: new Date(), endDate: new Date(), key: 'selection'
    }]);
    const [showDatePicker, setShowDatePicker] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
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

    return (<div className="w-full mx-auto">
            <div className="m-0 mb-4 p-4 bg-gradient-to-br from-blue-900 to-gray-400 rounded-lg">
                <h2 className="text-2xl md:text-3xl font-bold text-center text-white mb-8">
                    Buscar ofertas en hoteles, casas y mucho más
                </h2>

                <div className="flex flex-col md:flex-row gap-4 p-6 bg-white rounded-lg shadow-md">
                    <div className="flex flex-col md:flex-row gap-4 flex-1">
                        {/* Destination Input */}
                        <div className="relative flex-1">
                            <TextField
                                placeholder="¿A dónde vamos?"
                                value={query}
                                onChange={handleInputChange}
                                startIcon={<MapPin size={20}/>}
                                error={Boolean(error && !query)}
                                aria-label="Destino"
                            />

                            {suggestions.length > 0 && (<Suggestions
                                    suggestions={suggestions}
                                    onSelect={handleSuggestionClick}
                                    onClickAway={() => setSuggestions([])}
                                />)}
                        </div>

                        {/* Date Range Input */}
                        <div className="relative flex-1">
                            <TextField
                                value={formatDateRange()}
                                onClick={() => setShowDatePicker(!showDatePicker)}
                                startIcon={<Calendar size={20}/>}
                                placeholder="Seleccionar fechas"
                                readOnly
                            />

                            {showDatePicker && (<div className="absolute z-50 mt-2">
                                    <div className="bg-white rounded-lg shadow-lg overflow-hidden">
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
                                    </div>
                                </div>)}
                        </div>
                    </div>

                    {/* Search Button */}
                    <button
                        onClick={handleSearch}
                        disabled={loading || !query || !dateState[0].startDate || !dateState[0].endDate}
                        className="h-14 px-8 whitespace-nowrap bg-blue-500 hover:bg-blue-600 text-white rounded-md
              disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
                    >
                        {loading ? (<>
                                <Loader className="animate-spin" size={20}/>
                                Buscando...
                            </>) : (<>
                                <Search size={20}/>
                                Buscar
                            </>)}
                    </button>
                </div>

                {/* Error Message */}
                {error && (<div className="mt-4 p-3 bg-red-100 text-red-700 rounded-lg" role="alert">
                        {error}
                    </div>)}

                {/* Loading Cities Indicator */}
                {isLoadingCities && (
                    <div className="mt-4 p-3 bg-blue-100 text-blue-700 rounded-lg flex items-center gap-2">
                        <Loader className="animate-spin" size={16}/>
                        <span>Cargando ciudades...</span>
                    </div>)}
            </div>
        </div>);
}

export default SearchBar;