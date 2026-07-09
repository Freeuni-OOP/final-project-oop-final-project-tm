import { useEffect, useState } from 'react';
import './MiniServiceCalendar.css';

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

function MiniServiceCalendar({ serviceId }) {
  const [data, setData] = useState(null);
  const [weekOffset, setWeekOffset] = useState(0);

  useEffect(() => {
    fetch(`${API_BASE}/api/calendar/service/${serviceId}?weekOffset=${weekOffset}`, { cache: 'no-store' })
      .then(res => res.json())
      .then(setData)
      .catch(() => setData(null));
  }, [serviceId, weekOffset]);

  if (!data) return <div className="mini-service-calendar-loading">Loading calendar...</div>;

  const openMin = toMinutes(data.openTime);
  const closeMin = toMinutes(data.closeTime);
  const totalMin = closeMin - openMin;

  const segStyle = (seg) => ({
    top: `${((toMinutes(seg.start) - openMin) / totalMin) * 100}%`,
    height: `${((toMinutes(seg.end) - toMinutes(seg.start)) / totalMin) * 100}%`,
  });

  return (
    <div className="mini-service-calendar">
      <div className="mini-service-calendar-title">Weekly availability</div>

      <div className="mini-service-calendar-header">
        <button className="mini-service-calendar-nav" onClick={() => setWeekOffset(weekOffset - 1)}>&lt;</button>
        <span className="mini-service-calendar-range">{formatDay(data.weekStart)} - {formatDay(data.weekEnd)}</span>
        <button className="mini-service-calendar-nav" onClick={() => setWeekOffset(weekOffset + 1)}>&gt;</button>
      </div>

      <div className="mini-service-calendar-grid">
        {data.days.map((day, i) => (
          <div key={day.date} className="mini-service-calendar-col">
            <div className="mini-service-calendar-day">
              <span className="mini-service-calendar-dow">{DAY_NAMES_SHORT[i]}</span>
              <span className="mini-service-calendar-date">{new Date(day.date).getDate()}</span>
            </div>
            <div className="mini-service-calendar-track">
              {day.segments.map((seg, idx) => (
                <div
                  key={idx}
                  className={`mini-service-calendar-seg ${seg.status.toLowerCase()}`}
                  style={segStyle(seg)}
                  title={`${labelTime(seg.start)} - ${labelTime(seg.end)} (${seg.status})`}
                />
              ))}
            </div>
          </div>
        ))}
      </div>

      <div className="mini-service-calendar-legend">
        <span><i className="free" /> Free</span>
        <span><i className="pending" /> Pending</span>
        <span><i className="booked" /> Booked</span>
      </div>
    </div>
  );
}

export default MiniServiceCalendar;
