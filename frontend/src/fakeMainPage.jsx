import React from 'react';
import { useNavigate } from 'react-router-dom';

export default function FakeMainPage() {
    const navigate = useNavigate();

    return (
        <div>

            <button
                onClick={() => navigate('/profile')}
            >
                Go to Profile
            </button>

            <button
                onClick={() => navigate('/search')}
            >
                Go to Search
            </button>
        </div>
    );
}