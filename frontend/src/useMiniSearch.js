import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export function useMiniSearch() {
    const [query, setQuery] = useState('');
    const [allListings, setAllListings] = useState([]);
    const [filteredResults, changeFilteredResults] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        fetch('http://localhost:8080/api/listings').then(res => {
                if (!res.ok) throw new Error("error fetching listing list");
                return res.json();
            }).then(data => {
                if (Array.isArray(data)) {
                    setAllListings(data);
                }
            }).catch(error => console.error("Error fetching initial listings:", error));
    }, []);

    const inputChange = (e) => {
        const value = e.target.value;
        setQuery(value);

        if (value.trim() === '') {
            changeFilteredResults([]);
            return;
        }

        const filteredList = allListings.filter((item) =>
            (item.title && item.title.toLowerCase().includes(value.toLowerCase())) ||
            (item.category && item.category.toLowerCase().includes(value.toLowerCase()))
        );
        changeFilteredResults(filteredList);
    };

    const submitSearch = (e) => {
        e.preventDefault();
        changeFilteredResults([]);
        navigate(`/search?query=${encodeURIComponent(query.trim())}`);
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