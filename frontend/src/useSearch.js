import { useState, useMemo } from "react";
import { mockListings } from "./mockData";

function matchText(item, query) {
    if (query === "") {
        return true;
    }
    const lowerQuery = query.toLowerCase();
    return (
        item.name.toLowerCase().includes(lowerQuery) ||
        item.subject.toLowerCase().includes(lowerQuery) ||
        item.description.toLowerCase().includes(lowerQuery)
    );
}

function matchCategory(item, category) {
    if (category === "All") {
        return true;
    }
    return item.category === category;
}

function matchPrice(item, maxPrice) {
    if (maxPrice === "") {
        return true;
    }
    return item.price <= Number(maxPrice);
}

function filterListings(listings, { searchQuery, selectedCategory, maxPrice }) {
    return listings.filter((listing) =>
        matchText(listing, searchQuery) &&
        matchCategory(listing, selectedCategory) &&
        matchPrice(listing, maxPrice)
    );
}

export function useSearch() {
    const [searchQuery, setSearchQuery] = useState("");
    const [selectedCategory, setSelectedCategory] = useState("All");
    const [maxPrice, setMaxPrice] = useState("");

    const filteredListings = useMemo(() => {
        return filterListings(mockListings, { searchQuery, selectedCategory, maxPrice });
    }, [searchQuery, selectedCategory, maxPrice]);

    const clearFilters = () => {
        setMaxPrice("");
        setSearchQuery("");
        setSelectedCategory("All");
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