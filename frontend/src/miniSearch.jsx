import React from 'react';
import { useMiniSearch } from './useMiniSearch';

export default function MiniSearch() {
    const {
        query,
        filteredResults,
        inputChange,
        itemClick,
        submitSearch
    } = useMiniSearch();

    return (
        <div
            style={{ position: 'relative', width: '227px' }}>
            <form onSubmit={submitSearch}>
                <input
                    type="text"
                    value={query}
                    onChange={inputChange}
                    placeholder="enter text"
                />
                <button type="submit">search</button>
            </form>

            {filteredResults.length > 0 && (
                <ul
                    style={dropdownListStyle}>
                    {filteredResults.map((item) => (
                        <li
                            key={item.id}
                            onClick={() => itemClick(item.id)}
                            style={itemStyle}
                        >
                            {item.name} - ({item.subject})
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

const dropdownListStyle = {
    position: 'absolute',
    background: '#fff',
    listStyle: 'none',
    margin: 0
};

const itemStyle = {
    padding: '5px',
    cursor: 'pointer',
    color: '#000'
};