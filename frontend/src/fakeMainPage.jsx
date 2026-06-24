import React from 'react';
import MiniSearch from './miniSearch';

export default function FakeMainPage() {
    return (
        <div
            style={{ textAlign: 'right', marginRight: '30px'}}>
            <div
                style={{ display: 'inline-block', marginTop: '20px' }}>
                <MiniSearch />
            </div>
        </div>
    );
}