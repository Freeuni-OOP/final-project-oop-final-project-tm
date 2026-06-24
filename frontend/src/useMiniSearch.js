import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { mockListings } from './mockData';

export function useMiniSearch() {
    const [query, setQuery] = useState('');
    const [filteredResults, changeFilteredResults] = useState([]);
    const navigate = useNavigate();

    const inputChange = (e) => {
        const value = e.target.value;
        setQuery(value);

        if (value.trim() === '') {
            changeFilteredResults([]);
            return;
        }

        const filteredList = mockListings.filter((item) =>
            item.name.toLowerCase().includes(value.toLowerCase()) ||
            item.subject.toLowerCase().includes(value.toLowerCase())
        );
        changeFilteredResults(filteredList);
    };

    const submitSearch = (e) => {
        e.preventDefault();
        changeFilteredResults([]);
        navigate(`/search?query=${query.trim()}`);
    };

    const itemClick = (id) => {
        setQuery('');
        changeFilteredResults([]);
        navigate(`/listing/${id}`);
    };

    return {
        query,
        filteredResults,
        inputChange,
        itemClick,
        submitSearch
    };
}