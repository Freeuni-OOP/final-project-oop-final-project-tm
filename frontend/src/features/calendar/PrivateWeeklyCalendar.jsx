import { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import PageNotFound from '../../PageNotFound/PageNotFound';
import './WeeklyCalendar.css';

const API_BASE = 'http://localhost:8080';
const DAY_NAMES_SHORT = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];

function toMinutes(hhmm) {
  const [h, m] = hhmm.split(':').map(Number);
  return h * 60 + m;
}

function labelHour(h) {
  if (h === 0) return '12 AM';
  return h < 12 ? `${h} AM` : h === 12 ? '12 PM' : `${h - 12} PM`;
}

function labelTime(hhmm) {
  const [h, m] = hhmm.split(':').map(Number);
  const suffix = h < 12 ? 'AM' : 'PM';
  const hour12 = h % 12 === 0 ? 12 : h % 12;
  return `${hour12}:${String(m).padStart(2, '0')} ${suffix}`;
}

function formatRange(weekStart, weekEnd) {
  const opts = { month: 'long', day: 'numeric' };
  const s = new Date(weekStart).toLocaleDateString('en-US', opts);
  const e = new Date(weekEnd).toLocaleDateString('en-US', { day: 'numeric' });
  return `${s} - ${e}, ${new Date(weekEnd).getFullYear()}`;
}

function PrivateWeeklyCalendar() {
  const { userId } = useParams();

  const [authorized, setAuthorized] = useState(null);
  const [weekOffset, setWeekOffset] = useState(0);
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [fetchError, setFetchError] = useState(null);

  useEffect(() => {
    let cancelled = false;
    fetch(`${API_BASE}/api/profile/`, { credentials: 'include' })
      .then(res => (res.ok ? res.json() : null))
      .then(profile => {
        if (cancelled) return;
        setAuthorized(profile != null && String(profile.id) === String(userId));
      })
      .catch(() => { if (!cancelled) setAuthorized(false); });
    return () => { cancelled = true; };
  }, [userId]);

  const loadWeek = useCallback(async (showSpinner) => {
    if (showSpinner) setLoading(true);
    try {
      const res = await fetch(`${API_BASE}/api/calendar/user/${userId}?weekOffset=${weekOffset}`, { cache: 'no-store', credentials: 'include' });
      if (!res.ok) throw new Error(`Server responded with status ${res.status}`);
      setData(await res.json());
      setFetchError(null);
    } catch (err) {
      console.error('[PrivateWeeklyCalendar] Failed to load calendar:', err);
      setFetchError('Could not load your schedule. Please try again.');
    } finally {
      if (showSpinner) setLoading(false);
    }
  }, [userId, weekOffset]);

  useEffect(() => {
    if (authorized !== true) return;
    loadWeek(true);
  }, [authorized, loadWeek]);

  useEffect(() => {
    if (authorized !== true) return;
    const timer = setInterval(() => loadWeek(false), 5000);
    return () => clearInterval(timer);
  }, [authorized, loadWeek]);

  if (authorized === null) return null;
  if (authorized === false) return <PageNotFound />;

  const openMin = data ? toMinutes(data.openTime) : 0;
  const closeMin = data ? toMinutes(data.closeTime) : 0;
  const totalMin = closeMin - openMin;

  const hourMarks = [];
  if (data) {
    for (let h = Math.ceil(openMin / 60); h * 60 <= closeMin; h++) hourMarks.push(h);
  }

  const segStyle = (seg) => {
    const top = ((toMinutes(seg.start) - openMin) / totalMin) * 100;
    const height = ((toMinutes(seg.end) - toMinutes(seg.start)) / totalMin) * 100;
    return { top: `${top}%`, height: `${height}%` };
  };

  return (
    <div className="wc-wrapper">
      <div className="wc-header">
        <h2 className="wc-title">My Schedule</h2>
        <div className="wc-header-right">
          <div className="wc-legend">
            <span className="wc-legend-item free">Available</span>
            <span className="wc-legend-item pending">Pending</span>
            <span className="wc-legend-item booked">Booked</span>
          </div>
        </div>
      </div>

      <div className="wc-nav">
        <button className="wc-nav-btn" onClick={() => setWeekOffset(w => w - 1)}>Prev</button>
        <div className="wc-nav-center">
          <span className="wc-week-label">{data ? formatRange(data.weekStart, data.weekEnd) : ''}</span>
          {weekOffset !== 0 && (
            <button className="wc-today-btn" onClick={() => setWeekOffset(0)}>Today</button>
          )}
        </div>
        <button className="wc-nav-btn" onClick={() => setWeekOffset(w => w + 1)}>Next</button>
      </div>

      {loading && (
        <div className="wc-loading"><div className="wc-spinner" /><p>Loading schedule...</p></div>
      )}
      {fetchError && !loading && (
        <div className="wc-error" role="alert"><p>{fetchError}</p></div>
      )}

      {!loading && !fetchError && data && (
        <div className="wc-grid">
          <div className="wc-head-row">
            <div className="wc-axis-spacer" />
            {data.days.map((day, i) => (
              <div key={day.date} className="wc-day-head">
                <span className="wc-day-short">{DAY_NAMES_SHORT[i]}</span>
                <span className="wc-day-num">{new Date(day.date).getDate()}</span>
              </div>
            ))}
          </div>

          <div className="wc-body-row">
            <div className="wc-axis">
              {hourMarks.map(h => (
                <span
                  key={h}
                  className="wc-axis-label"
                  style={{ top: `${((h * 60 - openMin) / totalMin) * 100}%` }}
                >
                  {labelHour(h)}
                </span>
              ))}
            </div>

            {data.days.map(day => (
              <div key={day.date} className="wc-track">
                {hourMarks.map(h => (
                  <div
                    key={h}
                    className="wc-gridline"
                    style={{ top: `${((h * 60 - openMin) / totalMin) * 100}%` }}
                  />
                ))}
                {day.segments.map((seg, idx) => (
                  <div
                    key={idx}
                    className={`wc-seg ${seg.status.toLowerCase()}`}
                    style={segStyle(seg)}
                    title={`${labelTime(seg.start)} - ${labelTime(seg.end)} (${seg.status})`}
                  >
                    <span className="wc-seg-label">{labelTime(seg.start)} - {labelTime(seg.end)}</span>
                  </div>
                ))}
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

export default PrivateWeeklyCalendar;
