import { useState, useEffect, useMemo, useCallback } from 'react';
import { useLocation } from 'react-router-dom';

export function useSearch(currentUser) {
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const initialQuery = queryParams.get('query') || '';

    const [searchQuery, setSearchQuery] = useState(initialQuery);
    const [searchField, setSearchField] = useState("title");
    const [minPrice, setMinPrice] = useState("");
    const [maxPrice, setMaxPrice] = useState("");
    const [allListings, setAllListings] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 50;
    const [sortBy, setSortBy] = useState("id");
    const [direction, setDirection] = useState("DESC");
    const [filterMode, setFilterMode] = useState("all");

    const currUserId = currentUser ? currentUser.id : null;

    const fetchListings = useCallback(() => {
        let url = `http://localhost:8080/api/listings?sortType=${sortBy}&direction=${direction}`;

        if (currUserId) {
            if (filterMode === "favorites") {
                url += `&favoriteUserId=${currUserId}`;
            } else if (filterMode === "others") {
                url += `&excludeFavUserId=${currUserId}`;
            }
        } else if (filterMode !== "all") {
            setFilterMode("all");
        }

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
    }, [sortBy, direction, filterMode, currUserId]);

    useEffect(() => {
        fetchListings();
    }, [fetchListings]);

    useEffect(() => {
        setSearchQuery(initialQuery);
    }, [initialQuery]);

    const filteredListings = useMemo(() => {
        let result = allListings;

        if (searchQuery.trim() !== "") {
            const query = searchQuery.toLowerCase();
            result = result.filter(item => {
                if (searchField === "title") {
                    return item.title?.toLowerCase().includes(query);
                } else if (searchField === "category") {
                    return item.category?.toLowerCase().includes(query);
                } else if (searchField === "description") {
                    const text = item.bio || item.description || "";
                    return text.toLowerCase().includes(query);
                }
                return true;
            });
        }

        if (minPrice !== "" && !isNaN(minPrice)) {
            result = result.filter(item => item.price >= parseFloat(minPrice));
        }

        if (maxPrice !== "" && !isNaN(maxPrice)) {
            result = result.filter(item => item.price <= parseFloat(maxPrice));
        }

        return result;
    }, [searchQuery, searchField, minPrice, maxPrice, allListings]);

    useEffect(() => {
        setCurrentPage(1);
    }, [searchQuery, searchField, minPrice, maxPrice]);

    const paginatedListings = useMemo(() => {
        const startIndex = (currentPage - 1) * itemsPerPage;
        return filteredListings.slice(startIndex, startIndex + itemsPerPage);
    }, [filteredListings, currentPage]);

    const totalPages = Math.ceil(filteredListings.length / itemsPerPage);

    const clearFilters = () => {
        setSearchQuery("");
        setSearchField("title");
        setMinPrice("");
        setMaxPrice("");
        setSortBy("id");
        setDirection("DESC");
        setCurrentPage(1);
        setFilterMode("all");
    };

    return {
        searchQuery,
        setSearchQuery,
        searchField,
        setSearchField,
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
        clearFilters,
        filterMode,
        setFilterMode
    };
}