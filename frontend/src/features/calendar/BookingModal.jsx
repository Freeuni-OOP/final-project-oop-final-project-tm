import { useState } from 'react';
import './BookingModal.css';

function toMinutes(hhmm) {
  const [h, m] = hhmm.split(':').map(Number);
  return h * 60 + m;
}

function pad(n) {
  return String(n).padStart(2, '0');
}

function fromMinutes(min) {
  return `${pad(Math.floor(min / 60))}:${pad(min % 60)}`;
}

function dayLabel(day) {
  const d = new Date(day.date);
  return d.toLocaleDateString('en-US', { weekday: 'long', month: 'short', day: 'numeric' });
}

function BookingModal({
  days, openTime, closeTime, onSubmit, onClose,
  title = 'Book an Appointment',
  subtitle = 'Choose a day and the time range you would like to request.',
  submitLabel = 'Request Booking',
  successTitle = 'Request Sent!',
  successMessage = 'Your booking request has been submitted. The provider will confirm shortly.',
}) {
  const defaultEnd = fromMinutes(Math.min(toMinutes(openTime) + 60, toMinutes(closeTime)));

  const [date, setDate] = useState(days[0]?.date ?? '');
  const [startTime, setStartTime] = useState(openTime);
  const [endTime, setEndTime] = useState(defaultEnd);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const validate = () => {
    if (!date) return 'Please choose a day.';
    if (toMinutes(startTime) >= toMinutes(endTime)) return 'Start time must be before end time.';
    if (toMinutes(startTime) < toMinutes(openTime) || toMinutes(endTime) > toMinutes(closeTime)) {
      return `Please pick a time between ${openTime} and ${closeTime}.`;
    }
    return null;
  };

  const handleSubmit = async () => {
    const localError = validate();
    if (localError) { setError(localError); return; }

    setSubmitting(true);
    setError(null);
    try {
      await onSubmit({ date, startTime, endTime });
      setSuccess(true);
      setTimeout(() => onClose(), 1800);
    } catch (err) {
      setError(err.message || 'Something went wrong. Please try again.');
    } finally {
      setSubmitting(false);
    }
  };

  const handleBackdropClick = (e) => { if (e.target === e.currentTarget) onClose(); };

  return (
    <div className="modal-backdrop" onClick={handleBackdropClick} role="dialog" aria-modal="true">
      <div className="modal-card">
        <button className="modal-close-btn" onClick={onClose}>X</button>

        {success ? (
          <div className="modal-success">
            <h3>{successTitle}</h3>
            <p>{successMessage}</p>
          </div>
        ) : (
          <>
            <h3 className="modal-title">{title}</h3>
            <p className="modal-subtitle">{subtitle}</p>

            <div className="modal-form">
              <div className="form-group">
                <label htmlFor="bk-day">Day</label>
                <select id="bk-day" value={date} onChange={e => setDate(e.target.value)} disabled={submitting}>
                  {days.map(d => (
                    <option key={d.date} value={d.date}>{dayLabel(d)}</option>
                  ))}
                </select>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="bk-start">From</label>
                  <input id="bk-start" type="time" value={startTime} min={openTime} max={closeTime}
                         onChange={e => setStartTime(e.target.value)} disabled={submitting} />
                </div>
                <div className="form-group">
                  <label htmlFor="bk-end">To</label>
                  <input id="bk-end" type="time" value={endTime} min={openTime} max={closeTime}
                         onChange={e => setEndTime(e.target.value)} disabled={submitting} />
                </div>
              </div>
            </div>

            {error && <div className="modal-error" role="alert">{error}</div>}

            <div className="modal-actions">
              <button type="button" className="btn btn-cancel" onClick={onClose} disabled={submitting}>Cancel</button>
              <button type="button" className="btn btn-confirm" onClick={handleSubmit} disabled={submitting}>
                {submitting ? 'Sending...' : submitLabel}
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}

export default BookingModal;
