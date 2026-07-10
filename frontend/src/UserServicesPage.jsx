import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { mockUserHires, mockOfferedServices } from './mockServicesData';

export default function UserServicesPage() {
    const { status } = useParams();
    const navigate = useNavigate();
    const [filteredListings, setFilteredListings] = useState([]);

    useEffect(() => {
        if (status === 'offered') {
            setFilteredListings(mockOfferedServices);
        } else if (status === 'registered' || status === 'current') {
            const active = mockUserHires.filter(item => item.status === 'ACTIVE');
            setFilteredListings(active);
        } else if (status === 'past') {
            const completed = mockUserHires.filter(item => item.status === 'COMPLETED');
            setFilteredListings(completed);
        }
    }, [status]);

    const getPageName = () => {
        if (status === 'offered'){
            return 'My Offered Services';
        }
        if (status === 'current'){
            return 'Active Registered Services';
        }
        if (status === 'past'){
            return 'Completed Services';
        }
        return 'Services';
    };

    return (
        <div style={styles.container}>
            <h2 style={styles.title}>{getPageName()}</h2>

            <div style={styles.tabContainer}>
                <button onClick={() => navigate('/profile/services/offered')}
                    style={{...styles.tabButton, fontWeight: status === 'offered' ? 'bold' : 'normal'}}
                > Offered
                </button>
                <button onClick={() => navigate('/profile/services/registered')}
                    style={{...styles.tabButton, fontWeight: status === 'registered' ? 'bold' : 'normal'}}
                > Registered
                </button>
                <button onClick={() => navigate('/profile/services/past')}
                    style={{...styles.tabButton, fontWeight: status === 'past' ? 'bold' : 'normal'}}
                > Past
                </button>
            </div>

            <div style={styles.listContainer}>
                {filteredListings.length > 0 ? (
                    filteredListings.map((listing) => (
                        <div
                            key={listing.serviceId}
                            onClick={() => navigate(`/services/${listing.serviceId}`)}
                            style={styles.card}
                        >
                            <div style={styles.cardHeader}>
                                <h3 style={styles.cardTitle}>{listing.title}</h3>
                                <span style={{
                                    ...styles.statusTag,
                                    backgroundColor: listing.status === 'ACTIVE' ? '#e6f4ea' : listing.status === 'COMPLETED' ? '#f3e8ff' : '#e8f0fe',
                                    color: listing.status === 'ACTIVE' ? 'green' : listing.status === 'COMPLETED' ? 'purple' : '#1a73e8'
                                }}>
                                    {listing.status}
                                </span>
                            </div>
                            <p style={styles.cardCategory}>Category: {listing.category}</p>
                            <p style={styles.cardPrice}>Price: <strong>${listing.price}</strong></p>
                        </div>
                    ))
                ) : (
                    <p style={styles.emptyText}>No services found in this category.</p>
                )}
            </div>

            <button onClick={() => navigate('/profile')} style={styles.backButton}> Back to Profile </button>
        </div>
    );
}

const styles = {
    container: { maxWidth: '800px', margin: '30px auto', padding: '20px', fontFamily: 'sans-serif' },
    title: { color: '#333', textTransform: 'capitalize', marginBottom: '20px' },
    tabContainer: { display: 'flex', gap: '10px', marginBottom: '25px', borderBottom: '1px solid #ddd', paddingBottom: '10px' },
    tabButton: { background: 'none', border: 'none', cursor: 'pointer', color: '#555', fontSize: '16px' },
    listContainer: { display: 'flex', flexDirection: 'column', gap: '15px' },
    card: { border: '1px solid #e0e0e0', padding: '20px', borderRadius: '10px', cursor: 'pointer', backgroundColor: '#fff', boxShadow: '0 2px 4px rgba(0,0,0,0.05)', transition: 'transform 0.2s' },
    cardHeader: { display: 'flex', justifyContent: 'space-between', alignItems: 'center' },
    cardTitle: { margin: 0, fontSize: '18px', color: '#222' },
    cardCategory: { color: '#666', fontSize: '14px', margin: '5px 0' },
    cardPrice: { margin: '5px 0 0 0', color: '#333' },
    statusTag: { padding: '4px 10px', borderRadius: '15px', fontSize: '12px', fontWeight: 'bold' },
    emptyText: { color: '#888', fontStyle: 'italic' },
    backButton: { marginTop: '20px', padding: '10px 15px', backgroundColor: '#f0f0f0', border: 'none', borderRadius: '5px', cursor: 'pointer' }
};