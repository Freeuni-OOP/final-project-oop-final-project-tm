import { useState, useEffect, useMemo, useCallback } from 'react';
import { useLocation } from 'react-router-dom';

export function useSearch() {
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const initialQuery = queryParams.get('query') || '';

    const [searchQuery, setSearchQuery] = useState(initialQuery);
    const [selectedCategory, setSelectedCategory] = useState("All");
    const [minPrice, setMinPrice] = useState("");
    const [maxPrice, setMaxPrice] = useState("");
    const [allListings, setAllListings] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 50;
    const [sortBy, setSortBy] = useState("serviceId");
    const [direction, setDirection] = useState("DESC");


    const fetchListings = useCallback(() => {
        const url = `http://localhost:8080/api/listings?sortType=${sortBy}&direction=${direction}`;

        fetch(url).then(res => {
                if (!res.ok){
                    throw new Error(`http error`);
                }
                return res.json();
            }).then(data => {
                if (Array.isArray(data)){
                    setAllListings(data);
                }else{
                    setAllListings([]);
                }
            }).catch(err => setAllListings([]));
    }, [sortBy, direction]);

    useEffect(() => {
        fetchListings();
    }, [fetchListings]);

    useEffect(() => {
        setSearchQuery(initialQuery);
    }, [initialQuery]);

    useEffect(() => {
        fetch('http://localhost:8080/api/listings')
            .then(res => {
                if (!res.ok) throw new Error(`http error`);
                return res.json();
            })
            .then(data => {
                if (Array.isArray(data)) setAllListings(data);
                else setAllListings([]);
            })
            .catch(err => setAllListings([]));
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

        if (minPrice !== "" && !isNaN(minPrice)) {
            result = result.filter(item => item.price >= parseFloat(minPrice));
        }

        if (maxPrice !== "" && !isNaN(maxPrice)) {
            result = result.filter(item => item.price <= parseFloat(maxPrice));
        }

        return result;
    }, [searchQuery, selectedCategory, minPrice, maxPrice, allListings]);

    useEffect(() => {
        setCurrentPage(1);
    }, [searchQuery, selectedCategory, minPrice, maxPrice]);

    const paginatedListings = useMemo(() => {
        const startIndex = (currentPage - 1) * itemsPerPage;
        return filteredListings.slice(startIndex, startIndex + itemsPerPage);
    }, [filteredListings, currentPage]);

    const totalPages = Math.ceil(filteredListings.length / itemsPerPage);

    const clearFilters = () => {
        setSearchQuery("");
        setSelectedCategory("All");
        setMinPrice("");
        setMaxPrice("");
        setSortBy("serviceId");
        setDirection("DESC");
        setCurrentPage(1);
    };

    return {
        searchQuery,
        setSearchQuery,
        selectedCategory,
        setSelectedCategory,
        minPrice,
        setMinPrice,
        maxPrice,
        setMaxPrice,
        sortBy,
        setSortBy,
        direction,
        setDirection,
        filteredListings,
        paginatedListings,
        currentPage,
        setCurrentPage,
        totalPages,
        clearFilters
    };
}