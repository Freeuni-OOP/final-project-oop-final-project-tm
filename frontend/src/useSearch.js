import { useState, useEffect, useMemo } from 'react'; // დავამატეთ useMemo
import { useLocation } from 'react-router-dom';

export function useSearch() {
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const initialQuery = queryParams.get('query') || '';

    const [searchQuery, setSearchQuery] = useState(initialQuery);
    const [selectedCategory, setSelectedCategory] = useState("All");
    const [maxPrice, setMaxPrice] = useState("");
    const [allListings, setAllListings] = useState([]);

    useEffect(() => {
        setSearchQuery(initialQuery);
    }, [initialQuery]);

    useEffect(() => {
        fetch('http://localhost:8080/api/listings')
            .then(res => {
                if (!res.ok) {
                    throw new Error(`http error`);
                }
                return res.json();
            })
            .then(data => {
                if (Array.isArray(data)) {
                    setAllListings(data);
                } else {
                    console.error("api did not return an array", data);
                    setAllListings([]);
                }
            })
            .catch(err => {
                console.error("error fetching listings", err);
                setAllListings([]);
            });
    }, []);

    const filteredListings = useMemo(() => {
        let result = allListings;

        if (searchQuery.trim() !== "") {
            const query = searchQuery.toLowerCase();
            result = result.filter(item =>
                item.title?.toLowerCase().includes(query) ||
                item.description?.toLowerCase().includes(query)
            );
        }

        if (selectedCategory !== "All") {
            result = result.filter(item => item.category === selectedCategory);
        }

        if (maxPrice !== "" && !isNaN(maxPrice)) {
            result = result.filter(item => item.price <= parseFloat(maxPrice));
        }

        return result;
    }, [searchQuery, selectedCategory, maxPrice, allListings]);

    const clearFilters = () => {
        setSearchQuery("");
        setSelectedCategory("All");
        setMaxPrice("");
    };

    return {
        searchQuery,
        setSearchQuery,
        selectedCategory,
        setSelectedCategory,
        maxPrice,
        setMaxPrice,
        filteredListings,
        clearFilters
    };
}