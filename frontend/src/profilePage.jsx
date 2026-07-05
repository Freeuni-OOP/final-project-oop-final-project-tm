import React from 'react';
import { useNavigate } from 'react-router-dom';

export default function profilePage() {
    const navigate = useNavigate();

    return (
        <div>
            <h2>Fake Profile</h2>
            <div>
                <button onClick={() => navigate('/profile/hires/current')}>
                    Active
                </button>
                <button onClick={() => navigate('/profile/hires/past')}>
                    Completed
                </button>
            </div>
        </div>
    );
}