import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

/**
 * mini-search functionality with live search dropdown
 * fetches all listings and does filtering logic
 */
export function useMiniSearch() {
    const [query, setQuery] = useState('');
    const [allListings, setAllListings] = useState([]);
    const [filteredResults, changeFilteredResults] = useState([]);
    const navigate = useNavigate();

    // Fetch all listings to do filtering
    useEffect(() => {
        fetch('http://localhost:8080/api/listings').then(res => {
            if (!res.ok) throw new Error("error fetching listing list");
            return res.json();
        }).then(data => {
            console.log("came from backend", data);
            if (Array.isArray(data)) {
                setAllListings(data);
            }
        }).catch(error => console.error("Error fetching initial listings:", error));
    }, []);

    /**
     * Handles input changes. Updates the query and filters based on title or category.
     */
    const inputChange = (e) => {
        const value = e.target.value;
        setQuery(value);

        // Clear results if input is empty
        if (value.trim() === '') {
            changeFilteredResults([]);
            return;
        }

        // Perform clientside filter
        const filteredList = allListings.filter((item) =>
            (item.title && item.title.toLowerCase().includes(value.toLowerCase())) ||
            (item.category && item.category.toLowerCase().includes(value.toLowerCase()))
        );
        changeFilteredResults(filteredList);
    };

    /**
     * redirects the user to a dedicated search
     * page with the query as a URL parameter.
     */
    const submitSearch = (e) => {
        e.preventDefault();
        changeFilteredResults([]);
        navigate(`/search?query=${encodeURIComponent(query.trim())}`);
    };

    /**
     * Handles clicking on an item in search dropdown.
     * clears search and navigates to listing page.
     */
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