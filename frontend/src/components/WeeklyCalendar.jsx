import { useState, useEffect, useCallback } from 'react';
import BookingModal from './BookingModal';
import './WeeklyCalendar.css';

// ─────────────────────────────────────────────────────────────────────────────
// Constants
// ─────────────────────────────────────────────────────────────────────────────

// Vite proxies /api → http://localhost:8080  (see vite.config.js)
const API_BASE = '';

const DAY_NAMES_FULL  = ['Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday'];
const DAY_NAMES_SHORT = ['Mon','Tue','Wed','Thu','Fri','Sat','Sun'];

// Hours displayed in the grid (9 AM – 5 PM inclusive)
const HOUR_RANGE = [9, 10, 11, 12, 13, 14, 15, 16, 17];

// ─────────────────────────────────────────────────────────────────────────────
// Utility helpers
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Formats an ISO-8601 date-time string as "9:00 AM".
 * Spring Boot serialises LocalDateTime as "2026-06-15T09:00:00" (no Z),
 * which JS Date() correctly parses as local time.
 */
function formatTime(iso) {
  return new Date(iso).toLocaleTimeString('en-US', {
    hour: 'numeric', minute: '2-digit', hour12: true,
  });
}

/** Formats an ISO-8601 date-time string as "Monday, June 15, 2026". */
function formatDate(iso) {
  return new Date(iso).toLocaleDateString('en-US', {
    weekday: 'long', year: 'numeric', month: 'long', day: 'numeric',
  });
}

/**
 * Returns the Monday of the week that contains `date`.
 * Result is midnight local time.
 */
function getWeekMonday(date = new Date()) {
  const d   = new Date(date);
  const dow = d.getDay();                     // 0 = Sun … 6 = Sat
  const diff = dow === 0 ? -6 : 1 - dow;     // shift so week starts Monday
  d.setDate(d.getDate() + diff);
  d.setHours(0, 0, 0, 0);
  return d;
}

/**
 * Converts a JS day-of-week (0=Sun) to a Mon-based index (0=Mon … 6=Sun).
 * Used to place slots in the correct grid column.
 */
function dayColIndex(iso) {
  const dow = new Date(iso).getDay();
  return dow === 0 ? 6 : dow - 1;
}

/** Extracts the hour (0-23) from an ISO string. */
function hourOf(iso) {
  return new Date(iso).getHours();
}

// ─────────────────────────────────────────────────────────────────────────────
// Component
// ─────────────────────────────────────────────────────────────────────────────

/**
 * WeeklyCalendar
 * ─────────────────────────────────────────────────────────────────────────────
 * Renders a weekly grid (Mon–Sun × 9 AM–5 PM) of appointment slots fetched
 * from the Spring Boot backend.  FREE slots are clickable and open a
 * BookingModal for the user to submit a request.
 */
function WeeklyCalendar() {

  // ── State ───────────────────────────────────────────────────────────────────
  const [slots,        setSlots]        = useState([]);   // BookingSlotDTO[]
  const [loading,      setLoading]      = useState(true);
  const [fetchError,   setFetchError]   = useState(null);
  const [selectedSlot, setSelectedSlot] = useState(null); // clicked FREE slot
  const [modalOpen,    setModalOpen]    = useState(false);

  // ── Fetch slots from backend on mount ──────────────────────────────────────
  useEffect(() => {
    const loadSlots = async () => {
      setLoading(true);
      setFetchError(null);

      try {
        const res = await fetch(`${API_BASE}/api/slots`);
        if (!res.ok) throw new Error(`Server responded with status ${res.status}`);
        const data = await res.json();
        setSlots(data);
      } catch (err) {
        console.error('[WeeklyCalendar] Failed to load slots:', err);
        setFetchError('Could not load the schedule. Please check your connection and try again.');
      } finally {
        setLoading(false);
      }
    };

    loadSlots();
  }, []);

  // ── Handlers ────────────────────────────────────────────────────────────────

  /** Opens the booking modal for the clicked FREE slot. */
  const handleSlotClick = useCallback((slot) => {
    if (slot.status !== 'FREE') return;
    setSelectedSlot(slot);
    setModalOpen(true);
  }, []);

  /**
   * Called by BookingModal when the user submits their details.
   * POSTs to /api/bookings/request and updates the local slot state
   * so the calendar reflects the new PENDING status immediately.
   */
  const handleBookingConfirm = useCallback(async (clientName, clientEmail) => {
    if (!selectedSlot) return;

    const res = await fetch(`${API_BASE}/api/bookings/request`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        slotId:      selectedSlot.id,
        clientName,
        clientEmail,
      }),
    });

    if (!res.ok) {
      const body = await res.json().catch(() => ({}));
      throw new Error(body.error || `Request failed (${res.status})`);
    }

    // Server returns the updated slot DTO – use it to update React state
    const updatedSlot = await res.json();
    setSlots((prev) =>
      prev.map((s) => (s.id === updatedSlot.id ? updatedSlot : s))
    );
  }, [selectedSlot]);

  const handleModalClose = useCallback(() => {
    setModalOpen(false);
    setSelectedSlot(null);
  }, []);

  // ── Build 2-D grid (rows = hours, cols = days) ──────────────────────────────
  //   grid[rowIdx][colIdx] = matching BookingSlotDTO | null
  const grid = HOUR_RANGE.map((hour) =>
    DAY_NAMES_FULL.map((_, dayIdx) =>
      slots.find((s) => dayColIndex(s.slotDateTime) === dayIdx && hourOf(s.slotDateTime) === hour) ?? null
    )
  );

  // Build day-header metadata (name + date number + today flag)
  const weekMonday  = getWeekMonday();
  const dayHeaders  = DAY_NAMES_FULL.map((_, i) => {
    const d = new Date(weekMonday);
    d.setDate(d.getDate() + i);
    return {
      shortName: DAY_NAMES_SHORT[i],
      dateNum:   d.getDate(),
      isToday:   d.toDateString() === new Date().toDateString(),
    };
  });

  // ── Human-readable hour label ───────────────────────────────────────────────
  const hourLabel = (h) =>
    h < 12 ? `${h} AM` : h === 12 ? '12 PM' : `${h - 12} PM`;

  // ── Early-return states ─────────────────────────────────────────────────────
  if (loading) {
    return (
      <div className="calendar-wrapper">
        <div className="calendar-loading">
          <div className="spinner" aria-hidden="true" />
          <p>Loading schedule…</p>
        </div>
      </div>
    );
  }

  if (fetchError) {
    return (
      <div className="calendar-wrapper">
        <div className="calendar-error" role="alert">
          <span className="error-icon" aria-hidden="true">⚠️</span>
          <p>{fetchError}</p>
        </div>
      </div>
    );
  }

  // ── Main render ─────────────────────────────────────────────────────────────
  return (
    <div className="calendar-wrapper">

      {/* ── Page header + legend ─────────────────────────────────────────── */}
      <div className="calendar-header">
        <h2 className="calendar-title">Weekly Schedule</h2>
        <div className="calendar-legend" aria-label="Slot status legend">
          <span className="legend-item free">Available</span>
          <span className="legend-item pending">Pending</span>
          <span className="legend-item booked">Booked</span>
        </div>
      </div>

      {/* ── Calendar grid ────────────────────────────────────────────────── */}
      <div className="calendar-grid" role="grid" aria-label="Weekly appointment calendar">

        {/* ── Day-column header row ───────────────────────────────────────── */}
        <div className="grid-row header-row" role="row">
          {/* Empty corner above the time-label column */}
          <div className="time-label-cell" aria-hidden="true" />

          {dayHeaders.map((d) => (
            <div
              key={d.shortName}
              className={`day-header-cell ${d.isToday ? 'today' : ''}`}
              role="columnheader"
              aria-current={d.isToday ? 'date' : undefined}
            >
              <span className="day-short">{d.shortName}</span>
              <span className={`day-num ${d.isToday ? 'today-num' : ''}`}>
                {d.dateNum}
              </span>
            </div>
          ))}
        </div>

        {/* ── One row per hour ────────────────────────────────────────────── */}
        {HOUR_RANGE.map((hour, rowIdx) => (
          <div key={hour} className="grid-row" role="row">

            {/* Time label */}
            <div className="time-label-cell" aria-label={hourLabel(hour)}>
              {hourLabel(hour)}
            </div>

            {/* Slot cells */}
            {grid[rowIdx].map((slot, colIdx) => (
              <div key={colIdx} className="grid-cell" role="gridcell">
                {slot ? (
                  <button
                    className={`slot-block ${slot.status.toLowerCase()}`}
                    onClick={() => handleSlotClick(slot)}
                    disabled={slot.status !== 'FREE'}
                    aria-label={
                      `${hourLabel(hour)} ${dayHeaders[colIdx].shortName} – ${slot.status}`
                    }
                    title={
                      slot.status === 'FREE'    ? 'Click to request this slot' :
                      slot.status === 'PENDING' ? 'Awaiting clinic confirmation' :
                                                  'Already booked'
                    }
                  >
                    <span className="slot-status-label">
                      {slot.status === 'FREE'    ? '+ Available'  :
                       slot.status === 'PENDING' ? '⏳ Pending'   :
                                                   '✓ Booked'}
                    </span>
                  </button>
                ) : (
                  /* No slot seeded for this day/hour combination */
                  <div className="slot-empty" aria-hidden="true" />
                )}
              </div>
            ))}

          </div>
        ))}

      </div>{/* end .calendar-grid */}

      {/* ── Booking modal ─────────────────────────────────────────────────── */}
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
