import { useEffect, useState } from 'react';
import './MiniCalendar.css';

const API_BASE = 'http://localhost:8080';

const STATUS_COLORS = {
    FREE: '#bfe3bf',
    PENDING: '#f5d98a',
    BOOKED: '#eaa9a9',
};

function formatDay(iso) {
    return new Date(iso).toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
}

function MiniCalendar({ userId }) {
    const [calendar, setCalendar] = useState(null);
    const [weekOffset, setWeekOffset] = useState(0);

    useEffect(() => {
        fetch(`${API_BASE}/api/calendar/user/${userId}?weekOffset=${weekOffset}`)
            .then(res => res.json())
            .then(setCalendar)
            .catch(() => setCalendar(null));
    }, [userId, weekOffset]);

    if (!calendar) return <div className="mini-calendar-loading">Loading calendar...</div>;

    const hours = calendar.days[0]?.slots.map(slot => slot.time) ?? [];

    return (
        <div className="mini-calendar">
            <div className="mini-calendar-title">Weekly availability</div>

            <div className="mini-calendar-header">
                <button className="mini-calendar-nav" onClick={() => setWeekOffset(weekOffset - 1)}>&lt;</button>
                <span className="mini-calendar-range">{formatDay(calendar.weekStart)} - {formatDay(calendar.weekEnd)}</span>
                <button className="mini-calendar-nav" onClick={() => setWeekOffset(weekOffset + 1)}>&gt;</button>
            </div>

            <table className="mini-calendar-grid">
                <thead>
                    <tr>
                        <th></th>
                        {calendar.days.map(day => (
                            <th key={day.date}>{day.dayOfWeek.slice(0, 3)}</th>
                        ))}
                    </tr>
                </thead>
                <tbody>
                    {hours.map((hour, rowIndex) => (
                        <tr key={hour}>
                            <td className="mini-calendar-time">{hour}</td>
                            {calendar.days.map(day => {
                                const cell = day.slots[rowIndex];
                                return (
                                    <td
                                        key={day.date + hour}
                                        className="mini-calendar-cell"
                                        style={{ backgroundColor: STATUS_COLORS[cell.status] }}
                                        title={`${day.date} ${hour} - ${cell.status}`}
                                    />
                                );
                            })}
                        </tr>
                    ))}
                </tbody>
            </table>

            <div className="mini-calendar-legend">
                <span><i style={{ backgroundColor: STATUS_COLORS.FREE }} /> Free</span>
                <span><i style={{ backgroundColor: STATUS_COLORS.PENDING }} /> Pending</span>
                <span><i style={{ backgroundColor: STATUS_COLORS.BOOKED }} /> Booked</span>
            </div>
        </div>
    );
}

export default MiniCalendar;
