import React from 'react';
import { useParams } from 'react-router-dom';
import './MinPrivateCalendar.css';

// Mock data only — no real logic, no API calls.
// Replace with real fetched availability once the backend endpoint is ready.
const MOCK_MONTH_LABEL = 'July 2026';
const MOCK_WEEKDAYS = ['S', 'M', 'T', 'W', 'T', 'F', 'S'];

// 6 rows x 7 cols mock grid. `null` = empty cell (padding day).
// status: 'booked' | 'blocked' | 'empty'
const MOCK_DAYS = [
    [null, null, null, 1, 2, 3, 4],
    [5, 6, 7, 8, 9, 10, 11],
    [12, 13, 14, 15, 16, 17, 18],
    [19, 20, 21, 22, 23, 24, 25],
    [26, 27, 28, 29, 30, 31, null],
    [null, null, null, null, null, null, null],
];

const MOCK_STATUS_BY_DAY = {
    8: 'booked',
    9: 'booked',
    15: 'blocked',
    22: 'booked',
    23: 'blocked',
    24: 'blocked',
};

function MinServiceCalendar() {
    // serviceId comes from the route, e.g. /services/24
    const { serviceId } = useParams();

    return (
        <div className="mpc-wrapper">
            <div className="mpc-header">
                <button className="mpc-nav-btn" type="button" aria-label="Previous month">
                    ‹
                </button>
                <div className="mpc-month-label">{MOCK_MONTH_LABEL}</div>
                <button className="mpc-nav-btn" type="button" aria-label="Next month">
                    ›
                </button>
            </div>

            <div className="mpc-service-tag">Service #{serviceId ?? '—'}</div>

            <div className="mpc-grid mpc-weekdays">
                {MOCK_WEEKDAYS.map((day, idx) => (
                    <div className="mpc-weekday-cell" key={`weekday-${idx}`}>
                        {day}
                    </div>
                ))}
            </div>

            <div className="mpc-grid mpc-days">
                {MOCK_DAYS.flat().map((day, idx) => {
                    if (day === null) {
                        return <div className="mpc-day-cell mpc-day-empty" key={`empty-${idx}`} />;
                    }

                    const status = MOCK_STATUS_BY_DAY[day];

                    return (
                        <div
                            className={`mpc-day-cell ${status ? `mpc-day-${status}` : ''}`}
                            key={`day-${day}`}
                        >
                            <span className="mpc-day-number">{day}</span>
                        </div>
                    );
                })}
            </div>

            <div className="mpc-legend">
                <div className="mpc-legend-item">
                    <span className="mpc-legend-dot mpc-legend-booked" />
                    Booked
                </div>
                <div className="mpc-legend-item">
                    <span className="mpc-legend-dot mpc-legend-blocked" />
                    Blocked
                </div>
            </div>
        </div>
    );
}

export default MinServiceCalendar;