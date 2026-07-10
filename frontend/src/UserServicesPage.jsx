import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

export default function UserServicesPage() {
    const { userId, status } = useParams();
    const navigate = useNavigate();
    const [filteredListings, setFilteredListings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchServices = async () => {
            setLoading(true);
            setError(null);
            try {
                if (status === 'offered') {
                    const response = await fetch(`/api/profile/services/offered/${userId}`);
                    if (!response.ok) throw new Error('Failed to fetch offered services');
                    const data = await response.json();
                    setFilteredListings(data);
                } else {
                    const response = await fetch('/api/profile/services/registered', {
                        credentials: 'include'
                    });
                    if (!response.ok) throw new Error('Failed to fetch registered services');
                    const data = await response.json();

                    if (status === 'registered' || status === 'current') {
                        const active = data.filter(item => item.active !== false);
                        setFilteredListings(active);
                    } else if (status === 'past') {
                        const completed = data.filter(item => item.active === false);
                        setFilteredListings(completed);
                    }
                }
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchServices();
    }, [status, userId]);

    const getPageName = () => {
        if (status === 'offered') return 'Offered Services';
        if (status === 'current' || status === 'registered') return 'Active Registered Services';
        if (status === 'past') return 'Completed Services';
        return 'Services';
    };

    if (loading) return <div style={styles.container}>Loading services...</div>;
    if (error) return <div style={{...styles.container, color: 'red'}}>Error: {error}</div>;

    return (
        <div style={styles.container}>
            <h2 style={styles.title}>{getPageName()}</h2>

            <div style={styles.tabContainer}>
                <button onClick={() => navigate(`/profile/${userId}/services/offered`)}
                        style={{...styles.tabButton, fontWeight: status === 'offered' ? 'bold' : 'normal'}}
                > Offered
                </button>
                <button onClick={() => navigate(`/profile/${userId}/services/registered`)}
                        style={{...styles.tabButton, fontWeight: (status === 'registered' || status === 'current') ? 'bold' : 'normal'}}
                > Registered
                </button>
                <button onClick={() => navigate(`/profile/${userId}/services/past`)}
                        style={{...styles.tabButton, fontWeight: status === 'past' ? 'bold' : 'normal'}}
                > Past
                </button>
            </div>

            <div style={styles.listContainer}>
                {filteredListings.length > 0 ? (
                    filteredListings.map((listing) => {
                        const serviceId = listing.id || listing.serviceId;
                        return (
                            <div key={serviceId} onClick={() => navigate(`/services/${serviceId}`)} style={styles.card}>
                                <div style={styles.cardHeader}>
                                    <h3 style={styles.cardTitle}>{listing.title}</h3>
                                    <span style={{
                                        ...styles.statusTag,
                                        backgroundColor: listing.active !== false ? '#e6f4ea' : '#f3e8ff',
                                        color: listing.active !== false ? 'green' : 'purple'
                                    }}>
                                        {listing.active !== false ? 'ACTIVE' : 'COMPLETED'}
                                    </span>
                                </div>
                                <p style={styles.cardCategory}>Category: {listing.category}</p>
                                <p style={styles.cardPrice}>Price: <strong>${listing.price}</strong></p>
                            </div>
                        );
                    })
                ) : (
                    <p style={styles.emptyText}>No services found.</p>
                )}
            </div>

            <button onClick={() => navigate(-1)} style={styles.backButton}> Back </button>
        </div>
    );
}

const styles = {
    container: { maxWidth: '800px', margin: '30px auto', padding: '20px', fontFamily: 'sans-serif' },
    title: { color: '#333', marginBottom: '20px' },
    tabContainer: { display: 'flex', gap: '10px', marginBottom: '25px', borderBottom: '1px solid #ddd', paddingBottom: '10px' },
    tabButton: { background: 'none', border: 'none', cursor: 'pointer', color: '#555', fontSize: '16px' },
    listContainer: { display: 'flex', flexDirection: 'column', gap: '15px' },
    card: { border: '1px solid #e0e0e0', padding: '20px', borderRadius: '10px', cursor: 'pointer', backgroundColor: '#fff', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' },
    cardHeader: { display: 'flex', justifyContent: 'space-between', alignItems: 'center' },
    cardTitle: { margin: 0, fontSize: '18px', color: '#222' },
    cardCategory: { color: '#666', fontSize: '14px', margin: '5px 0' },
    cardPrice: { margin: '5px 0 0 0', color: '#333' },
    statusTag: { padding: '4px 10px', borderRadius: '15px', fontSize: '12px', fontWeight: 'bold' },
    emptyText: { color: '#888', fontStyle: 'italic' },
    backButton: { marginTop: '20px', padding: '10px 15px', backgroundColor: '#f0f0f0', border: 'none', borderRadius: '5px', cursor: 'pointer' }
};