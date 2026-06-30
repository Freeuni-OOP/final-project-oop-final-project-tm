import { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import BookingModal from './BookingModal';
import './WeeklyCalendar.css';

const API_BASE        = 'http://localhost:8080';
const DAY_NAMES_FULL  = ['Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday'];
const DAY_NAMES_SHORT = ['Mon','Tue','Wed','Thu','Fri','Sat','Sun'];
const HOUR_RANGE      = [9,10,11,12,13,14,15,16,17];

function formatTime(iso) {
  return new Date(iso).toLocaleTimeString('en-US', { hour: 'numeric', minute: '2-digit', hour12: true });
}
function formatDate(iso) {
  return new Date(iso).toLocaleDateString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
}

function getWeekMonday(offset = 0) {
  const d = new Date();
  const dow = d.getDay();
  d.setDate(d.getDate() + (dow === 0 ? -6 : 1 - dow) + offset * 7);
  d.setHours(0, 0, 0, 0);
  return d;
}

function dayColIndex(iso) {
  const dow = new Date(iso).getDay();
  return dow === 0 ? 6 : dow - 1;
}
function hourOf(iso) { return new Date(iso).getHours(); }

function formatWeekRange(monday) {
  const sunday = new Date(monday);
  sunday.setDate(monday.getDate() + 6);
  const opts = { month: 'long', day: 'numeric' };
  return `${monday.toLocaleDateString('en-US', opts)} - ${sunday.toLocaleDateString('en-US', { day: 'numeric' })}, ${sunday.getFullYear()}`;
}

function WeeklyCalendar({ serviceId: serviceIdProp }) {
  const params = useParams();
  const serviceId = serviceIdProp ?? params.serviceId ?? 1;

  const [weekOffset,   setWeekOffset]   = useState(0);
  const [slots,        setSlots]        = useState([]);
  const [loading,      setLoading]      = useState(true);
  const [fetchError,   setFetchError]   = useState(null);
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [modalOpen,    setModalOpen]    = useState(false);

  useEffect(() => {
    const loadSlots = async () => {
      setLoading(true);
      setFetchError(null);
      try {
        const res = await fetch(`${API_BASE}/api/slots?serviceId=${serviceId}&weekOffset=${weekOffset}`);
        if (!res.ok) throw new Error(`Server responded with status ${res.status}`);
        setSlots(await res.json());
      } catch (err) {
        console.error('[WeeklyCalendar] Failed to load slots:', err);
        setFetchError('Could not load the schedule. Please try again.');
      } finally {
        setLoading(false);
      }
    };
    loadSlots();
  }, [weekOffset, serviceId]);

  useEffect(() => {
    const poll = async () => {
      try {
        const res = await fetch(`${API_BASE}/api/slots?serviceId=${serviceId}&weekOffset=${weekOffset}`);
        if (res.ok) setSlots(await res.json());
      } catch {}
    };
    const timer = setInterval(poll, 5000);
    return () => clearInterval(timer);
  }, [weekOffset, serviceId]);

  const handleSlotClick = useCallback((slot) => {
    if (slot.status !== 'FREE') return;
    setSelectedSlot(slot);
    setModalOpen(true);
  }, []);

  const handleBookingConfirm = useCallback(async () => {
    if (!selectedSlot) return;
    const res = await fetch(`${API_BASE}/api/bookings/request`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({ slotId: selectedSlot.id }),
    });
    if (!res.ok) {
      const body = await res.json().catch(() => ({}));
      throw new Error(body.error || `Request failed (${res.status})`);
    }
    const updatedSlot = await res.json();
    setSlots((prev) => prev.map((s) => (s.id === updatedSlot.id ? updatedSlot : s)));
  }, [selectedSlot]);

  const handleModalClose = useCallback(() => {
    setModalOpen(false);
    setSelectedSlot(null);
  }, []);

  const grid = HOUR_RANGE.map((hour) =>
    DAY_NAMES_FULL.map((_, dayIdx) =>
      slots.find((s) => dayColIndex(s.slotDateTime) === dayIdx && hourOf(s.slotDateTime) === hour) ?? null
    )
  );

  const weekMonday = getWeekMonday(weekOffset);
  const dayHeaders = DAY_NAMES_FULL.map((_, i) => {
    const d = new Date(weekMonday);
    d.setDate(d.getDate() + i);
    return {
      shortName: DAY_NAMES_SHORT[i],
      dateNum:   d.getDate(),
      isToday:   d.toDateString() === new Date().toDateString(),
    };
  });

  const hourLabel = (h) => h < 12 ? `${h} AM` : h === 12 ? '12 PM' : `${h - 12} PM`;

  return (
    <div className="calendar-wrapper">

      <div className="calendar-header">
        <h2 className="calendar-title">Weekly Schedule</h2>
        <div className="calendar-legend">
          <span className="legend-item free">Available</span>
          <span className="legend-item pending">Pending</span>
          <span className="legend-item booked">Booked</span>
        </div>
      </div>

      <div className="week-nav">
        <button className="week-nav-btn" onClick={() => setWeekOffset(w => w - 1)}>
          Prev
        </button>

        <div className="week-nav-center">
          <span className="week-label">{formatWeekRange(weekMonday)}</span>
          {weekOffset !== 0 && (
            <button className="today-btn" onClick={() => setWeekOffset(0)}>
              Today
            </button>
          )}
        </div>

        <button className="week-nav-btn" onClick={() => setWeekOffset(w => w + 1)}>
          Next
        </button>
      </div>

      {loading && (
        <div className="calendar-loading"><div className="spinner" /><p>Loading schedule...</p></div>
      )}
      {fetchError && !loading && (
        <div className="calendar-error" role="alert"><p>{fetchError}</p></div>
      )}

      {!loading && !fetchError && (
        <div className="calendar-grid" role="grid">

          <div className="grid-row header-row" role="row">
            <div className="time-label-cell" aria-hidden="true" />
            {dayHeaders.map((d) => (
              <div key={d.shortName} className={`day-header-cell ${d.isToday ? 'today' : ''}`} role="columnheader">
                <span className="day-short">{d.shortName}</span>
                <span className={`day-num ${d.isToday ? 'today-num' : ''}`}>{d.dateNum}</span>
              </div>
            ))}
          </div>

          {HOUR_RANGE.map((hour, rowIdx) => (
            <div key={hour} className="grid-row" role="row">
              <div className="time-label-cell">{hourLabel(hour)}</div>
              {grid[rowIdx].map((slot, colIdx) => (
                <div key={colIdx} className="grid-cell" role="gridcell">
                  {slot ? (
                    <button
                      className={`slot-block ${slot.status.toLowerCase()}`}
                      onClick={() => handleSlotClick(slot)}
                      disabled={slot.status !== 'FREE'}
                      title={slot.status === 'FREE' ? 'Click to request' : slot.status === 'PENDING' ? 'Awaiting confirmation' : 'Already booked'}
                    >
                      <span className="slot-status-label">
                        {slot.status === 'FREE' ? 'Available' : slot.status === 'PENDING' ? 'Pending' : 'Booked'}
                      </span>
                    </button>
                  ) : (
                    <div className="slot-empty" />
                  )}
                </div>
              ))}
            </div>
          ))}
        </div>
      )}

      {modalOpen && selectedSlot && (
        <BookingModal
          slot={selectedSlot}
          onConfirm={handleBookingConfirm}
          onClose={handleModalClose}
          formatDate={formatDate}
          formatTime={formatTime}
        />
      )}
    </div>
  );
}

export default WeeklyCalendar;
