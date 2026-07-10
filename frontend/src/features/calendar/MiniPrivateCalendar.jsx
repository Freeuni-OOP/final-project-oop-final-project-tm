import { useEffect, useState } from 'react';
import './MiniPrivateCalendar.css';

const API_BASE = 'http://localhost:8080';
const DAY_NAMES_SHORT = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];

function toMinutes(hhmm) {
  const [h, m] = hhmm.split(':').map(Number);
  return h * 60 + m;
}

function labelTime(hhmm) {
  const [h, m] = hhmm.split(':').map(Number);
  const suffix = h < 12 ? 'AM' : 'PM';
  const hour12 = h % 12 === 0 ? 12 : h % 12;
  return `${hour12}:${String(m).padStart(2, '0')} ${suffix}`;
}

function formatDay(iso) {
  return new Date(iso).toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
}

function MiniPrivateCalendar({ userId }) {
  const [data, setData] = useState(null);
  const [weekOffset, setWeekOffset] = useState(0);

  useEffect(() => {
    fetch(`${API_BASE}/api/calendar/user/${userId}?weekOffset=${weekOffset}`, { cache: 'no-store', credentials: 'include' })
      .then(res => res.json())
      .then(setData)
      .catch(() => setData(null));
  }, [userId, weekOffset]);

  if (!data) return <div className="mini-calendar-loading">Loading calendar...</div>;

  const openMin = toMinutes(data.openTime);
  const closeMin = toMinutes(data.closeTime);
  const totalMin = closeMin - openMin;

  const segStyle = (seg) => ({
    top: `${((toMinutes(seg.start) - openMin) / totalMin) * 100}%`,
    height: `${((toMinutes(seg.end) - toMinutes(seg.start)) / totalMin) * 100}%`,
  });

  return (
    <div className="mini-calendar">
      <div className="mini-calendar-title">Weekly availability</div>

      <div className="mini-calendar-header">
        <button className="mini-calendar-nav" onClick={() => setWeekOffset(weekOffset - 1)}>&lt;</button>
        <span className="mini-calendar-range">{formatDay(data.weekStart)} - {formatDay(data.weekEnd)}</span>
        <button className="mini-calendar-nav" onClick={() => setWeekOffset(weekOffset + 1)}>&gt;</button>
      </div>

      <div className="mini-calendar-grid">
        {data.days.map((day, i) => (
          <div key={day.date} className="mini-calendar-col">
            <div className="mini-calendar-day">
              <span className="mini-calendar-dow">{DAY_NAMES_SHORT[i]}</span>
              <span className="mini-calendar-date">{new Date(day.date).getDate()}</span>
            </div>
            <div className="mini-calendar-track">
              {day.segments.map((seg, idx) => (
                <div
                  key={idx}
                  className={`mini-calendar-seg ${seg.status.toLowerCase()}`}
                  style={segStyle(seg)}
                  title={`${labelTime(seg.start)} - ${labelTime(seg.end)} (${seg.status})`}
                />
              ))}
            </div>
          </div>
        ))}
      </div>

      <div className="mini-calendar-legend">
        <span><i className="free" /> Free</span>
        <span><i className="pending" /> Pending</span>
        <span><i className="booked" /> Booked</span>
      </div>
    </div>
  );
}

export default MiniPrivateCalendar;
