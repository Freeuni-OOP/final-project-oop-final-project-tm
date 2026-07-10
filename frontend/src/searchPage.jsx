import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useSearch } from "./useSearch";

export default function SearchPage() {
    const {
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
    } = useSearch();

    return (
        <div style={styles.page}>
            <div style={styles.searchSection}>
                <h1 style={styles.title}>Find Service</h1>
                <input
                    type="text"
                    placeholder={`Search by ${searchField}...`}
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    style={styles.searchInput}
                />

                <div style={styles.filters}>
                    <select
                        value={searchField}
                        onChange={(e) => setSearchField(e.target.value)}
                        style={styles.select}
                    >
                        <option value="title">Search by Title</option>
                        <option value="category">Search by Category</option>
                        <option value="description">Search by Description</option>
                    </select>

                    <input
                        type="number"
                        placeholder="Min price ($)"
                        value={minPrice}
                        onChange={(e) => setMinPrice(e.target.value)}
                        style={styles.priceInput}
                    />

                    <input
                        type="number"
                        placeholder="Max price ($)"
                        value={maxPrice}
                        onChange={(e) => setMaxPrice(e.target.value)}
                        style={styles.priceInput}
                    />

                    <select
                        value={sortBy}
                        onChange={(e) => setSortBy(e.target.value)}
                        style={styles.select}
                    >
                        <option value="id">Newest</option>
                        <option value="price">Sort by Price</option>
                        <option value="title">Sort by Title</option>
                    </select>

                    <select
                        value={direction}
                        onChange={(e) => setDirection(e.target.value)}
                        style={styles.select}
                    >
                        <option value="ASC">Low to High (ASC)</option>
                        <option value="DESC">High to Low (DESC)</option>
                    </select>

                    <select
                        value={filterMode}
                        onChange={(e) => setFilterMode(e.target.value)}
                        style={styles.select}
                    >
                        <option value="all">All Listings</option>
                        <option value="favorites">Liked ❤️</option>
                        <option value="others">Not Liked 🤍</option>
                    </select>

                    <button onClick={clearFilters} style={styles.clearButton}>
                        Clear
                    </button>
                </div>
            </div>

            {/* Results count */}
            <p style={styles.resultsCount}>
                Found: <strong>{filteredListings.length}</strong> listing{filteredListings.length !== 1 ? "s" : ""}
            </p>

            {/* Listings Grid */}
            {filteredListings.length === 0 ? (
                <div style={styles.empty}>
                    <p>No listings found. Try a different search term.</p>
                </div>
            ) : (
                <>
                    <div style={styles.grid}>
                        {paginatedListings.map((listing, index) => (
                            <ListingCard key={index} listing={listing} />
                        ))}
                    </div>

                    {totalPages > 1 && (
                        <div style={styles.paginationContainer}>
                            <button
                                style={{ ...styles.pageButton, ...(currentPage === 1 ? styles.disabledButton : {}) }}
                                onClick={() => setCurrentPage(p => Math.max(1, p - 1))}
                                disabled={currentPage === 1}
                            >
                                <span style={styles.icon}>&#x23EE;</span> Prev
                            </button>

                            {[...Array(totalPages)].map((_, i) => {
                                const pageNumber = i + 1;
                                return (
                                    <button
                                        key={pageNumber}
                                        style={{
                                            ...styles.pageNumberButton,
                                            ...(currentPage === pageNumber ? styles.activePageButton : {})
                                        }}
                                        onClick={() => setCurrentPage(pageNumber)}
                                    >
                                        {pageNumber}
                                    </button>
                                );
                            })}

                            <button
                                style={{ ...styles.pageButton, ...(currentPage === totalPages ? styles.disabledButton : {}) }}
                                onClick={() => setCurrentPage(p => Math.min(totalPages, p + 1))}
                                disabled={currentPage === totalPages}
                            >
                                Next <span style={styles.icon}>&#x23ED;</span>
                            </button>

                        </div>
                    )}
                </>
            )}
        </div>
    );
}

function ListingCard({ listing }) {
    const [isExpanded, setIsExpanded] = React.useState(false);
    const [isLiked, setIsLiked] = React.useState(false);

    const currUserId = 1;
    const serviceId = listing.serviceId || listing.id;
    const navigate = useNavigate();

    React.useEffect(() => {
        fetch(`http://localhost:8080/api/favorites/check/${currUserId}/${serviceId}`)
            .then(res => res.json()).then(data => setIsLiked(data))
            .catch(err => console.error("Error:", err));
    }, [serviceId]);

    const handleLikeToggle = () => {
        const previousState = isLiked;
        setIsLiked(!isLiked);
        fetch(`http://localhost:8080/api/favorites/${currUserId}/${serviceId}`, { method: "POST" })
            .catch(err => { setIsLiked(previousState); });
    };

    const text = listing.bio || listing.description || "No description provided.";
    const isLongText = text.length > 100;
    const displayText = isExpanded ? text : text.substring(0, 100) + (isLongText ? "..." : "");

    return (
        <div style={styles.card}>

            <div style={styles.cardHeader}>
                <div style={styles.avatar}>
                    {listing.title ? listing.title.charAt(0).toUpperCase() : "T"}
                </div>
                <div style={{ flex: 1 }}>
                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                        <h3 style={styles.cardName}>{listing.title || "Untitled"}</h3>
                        <button onClick={handleLikeToggle}
                                style={styles.likeButton}
                        >
                                {isLiked ? "❤️" : "🤍"}
                        </button>
                    </div>
                </div>
            </div>

            {listing.category && (
                <div style={styles.categoryTag}>
                    {listing.category}
                </div>
            )}

            <p style={styles.description}>
                {displayText}
            </p>

            {isLongText && (
                <button onClick={() => setIsExpanded(!isExpanded)}
                        style={styles.readMoreButton}
                >
                    {isExpanded ? "Read Less" : "Read More"}
                </button>
            )}

            <div style={styles.cardFooter}>
                <div style={styles.ratingPrice}>
                    <span style={styles.price}>
                        {listing.price ? `$${listing.price}/hr` : "Price not set"}
                    </span>
                </div>

                <button
                    onClick={() => navigate(`/services/${serviceId}`)}
                    style={styles.viewDetailsButton}
                >
                    View Details
                </button>
            </div>
        </div>
    );
}

const styles = {
    page: {
        maxWidth: "1100px",
        margin: "0 auto",
        padding: "24px 16px",
        fontFamily: "'Segoe UI', sans-serif"
    },
    searchSection: {
        background: "#f8f9fa",
        borderRadius: "12px",
        padding: "24px",
        marginBottom: "24px"
    },
    title: {
        fontSize: "28px",
        fontWeight: "700",
        marginBottom: "16px",
        color: "#1a1a2e"
    },
    searchInput: {
        width: "100%",
        padding: "12px 16px",
        fontSize: "16px",
        border: "2px solid #e0e0e0",
        borderRadius: "8px",
        outline: "none",
        boxSizing: "border-box",
        marginBottom: "12px"
    },
    filters: {
        display: "flex",
        gap: "12px",
        flexWrap: "wrap"
    },
    select: {
        padding: "10px 14px",
        fontSize: "14px",
        border: "1px solid #ced4da",
        borderRadius: "8px",
        background: "white",
        color: "#333",
        cursor: "pointer",
        boxShadow: "0 1px 3px rgba(0,0,0,0.1)",
        outline: "none"
    },
    priceInput: {
        padding: "10px 14px",
        fontSize: "14px",
        border: "2px solid #e0e0e0",
        borderRadius: "8px",
        width: "150px",
        background: "white",
        color: "#333"
    },
    clearButton: {
        padding: "10px 18px",
        background: "#6c757d",
        color: "white",
        border: "none",
        borderRadius: "8px",
        cursor: "pointer",
        fontSize: "14px" },
    resultsCount: {
        color: "#555",
        marginBottom: "16px",
        fontSize: "14px"
    },
    grid: {
        display: "grid",
        gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))",
        gap: "20px"
    },
    empty: {
        textAlign: "center",
        padding: "60px",
        color: "#888",
        fontSize: "16px"
    },
    card: {
        background: "white",
        border: "1px solid #e8e8e8",
        borderRadius: "12px",
        padding: "20px",
        boxShadow: "0 2px 8px rgba(0,0,0,0.06)",
        position: "relative"
    },
    cardHeader: {
        display: "flex",
        alignItems: "center",
        gap: "12px",
        marginBottom: "12px"
    },
    avatar: {
        width: "48px",
        height: "48px",
        borderRadius: "50%",
        background: "#4a90d9",
        color: "white",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        fontSize: "20px",
        fontWeight: "bold",
        flexShrink: 0
    },
    cardName: {
        margin: 0,
        fontSize: "16px",
        fontWeight: "600",
        color: "#1a1a2e"
    },
    description: {
        fontSize: "13px",
        color: "#666",
        lineHeight: "1.5",
        marginBottom: "14px"
    },
    cardFooter: {
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        flexWrap: "wrap",
        gap: "8px"
    },
    ratingPrice: {
        display: "flex",
        gap: "12px",
        alignItems: "center"
    },
    price: {
        fontWeight: "700",
        fontSize: "16px",
        color: "#2d6a4f"
    },

    paginationContainer: {
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        gap: "8px",
        marginTop: "40px",
        padding: "16px",
        background: "#f8f9fa",
        border: "1px solid #e0e0e0",
        borderRadius: "8px",
    },
    pageButton: {
        display: "flex",
        alignItems: "center",
        gap: "4px",
        background: "transparent",
        border: "none",
        color: "#6b8ab0",
        fontSize: "14px",
        cursor: "pointer",
        padding: "8px 12px",
    },
    pageNumberButton: {
        background: "transparent",
        border: "none",
        color: "#6b8ab0",
        fontSize: "14px",
        cursor: "pointer",
        width: "32px",
        height: "32px",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        borderRadius: "50%",
        transition: "all 0.2s",
    },
    activePageButton: {
        background: "#007bff",
        color: "white",
        fontWeight: "bold",
    },
    disabledButton: {
        opacity: 0.5,
        cursor: "not-allowed",
    },
    icon: {
        fontSize: "10px",
    },
    readMoreButton: {
        background: "none",
        border: "none",
        color: "#007bff",
        cursor: "pointer",
        fontSize: "12px",
        padding: "0",
        fontWeight: "600",
        marginBottom: "10px"
    },
    likeButton: {
        border: "none",
        background: "none",
        cursor: "pointer",
        fontSize: "20px",
        padding: "5px"
    },
    viewDetailsButton: {
        background: "#007bff",
        color: "white",
        border: "none",
        padding: "6px 12px",
        borderRadius: "6px",
        cursor: "pointer",
        fontSize: "13px",
        fontWeight: "600",
        transition: "background 0.2s"
    },
    categoryTag: {
        display: "inline-block",
        background: "#e0f2fe",
        color: "#0284c7",
        padding: "4px 10px",
        borderRadius: "6px",
        fontSize: "12px",
        fontWeight: "700",
        marginBottom: "12px",
        textTransform: "uppercase",
        letterSpacing: "0.5px"
    }
};