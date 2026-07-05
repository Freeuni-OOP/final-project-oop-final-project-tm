import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { mockUserHires } from './mockHiresData';

export default function userHiresPage() {
    const { status } = useParams();
    const navigate = useNavigate();
    const [filteredListings, setFilteredListings] = useState([]);

    useEffect(() => {
        const listings = mockUserHires.filter(listing => {
            if (status === 'current') {
                return listing.status === 'ACTIVE';
            } else if (status === 'past') {
                return listing.status === 'COMPLETED';
            }
            return false;
        });

        setFilteredListings(listings);
    }, [status]);

    return (
        <div>
            <h2>{status === 'current' ? 'active services' : 'completed services'}</h2>

            <div>
                {filteredListings.length > 0 ? (
                    filteredListings.map((listing) => (
                        <div
                            key={listing.serviceId}
                            onClick={() => navigate(`/listing/${listing.serviceId}`)}
                            style={{
                                border: '1px solid #ccc',
                                padding: '15px',
                                borderRadius: '8px',
                                cursor: 'pointer'
                            }}
                        >
                            <h3>{listing.title}</h3>
                            <p>price: {listing.price} $</p>
                            <span style={{ color: listing.status === 'ACTIVE' ? 'green' : 'PURPLE' }}>
                                status: {listing.status}
                            </span>
                        </div>
                    ))
                ) : (
                    <p>empty</p>
                )}
            </div>
        </div>
    );
}