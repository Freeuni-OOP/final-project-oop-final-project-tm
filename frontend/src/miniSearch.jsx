import React from 'react';
import { useMiniSearch } from './useMiniSearch.js';

export default function MiniSearch() {
    const {
        query,
        filteredResults,
        inputChange,
        itemClick,
        submitSearch
    } = useMiniSearch();

    return (
        <div style={{ position: 'relative', width: '100%' }}>
            <form onSubmit={submitSearch} style={{ margin: 0, width: '100%' }}>
                <input
                    type="text"
                    value={query}
                    onChange={inputChange}
                    placeholder="Search..."
                    className="search-input"
                    style={{ width: '100%', boxSizing: 'border-box' }}
                />
            </form>

            {filteredResults?.length > 0 && (
                <ul style={dropdownListStyle}>
                    {filteredResults.map((item) => (
                        <li
                            key={item.id}
                            onClick={() => itemClick(item.id)}
                            style={itemStyle}
                            onMouseEnter={(e) => e.target.style.background = '#f8fafc'}
                            onMouseLeave={(e) => e.target.style.background = 'transparent'}
                        >
                            {item.title} - ({item.category})
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

const dropdownListStyle = {
    position: 'absolute',
    top: '100%',
    left: 0,
    width: '100%',
    background: '#fff',
    listStyle: 'none',
    margin: '8px 0 0 0',
    padding: '8px 0',
    border: '1px solid #cbd5e1',
    borderRadius: '16px',
    boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)',
    zIndex: 1000,
    maxHeight: '300px',
    overflowY: 'auto',
    textAlign: 'left',
    color: '#333'
};

const itemStyle = {
    padding: '10px 16px',
    cursor: 'pointer',
    borderBottom: '1px solid #f1f5f9',
    transition: 'background 0.2s ease',
    fontSize: '14px'
};