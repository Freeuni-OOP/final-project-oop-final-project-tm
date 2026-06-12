import { useState } from 'react';
import './BookingModal.css';

/**
 * BookingModal
 * ─────────────────────────────────────────────────────────────────────────────
 * Confirmation dialog shown when a user clicks a FREE slot.
 *
 * Props:
 *   slot        – BookingSlotDTO object (id, slotDateTime, status)
 *   onConfirm   – async fn(clientName: string, clientEmail: string)
 *                 called when the user submits the form
 *   onClose     – fn() called when user cancels or closes
 *   formatDate  – fn(isoString) → "Monday, June 15, 2026"
 *   formatTime  – fn(isoString) → "9:00 AM"
 */
function BookingModal({ slot, onConfirm, onClose, formatDate, formatTime }) {

  // ── Local state ─────────────────────────────────────────────────────────────
  const [clientName,  setClientName]  = useState('');
  const [clientEmail, setClientEmail] = useState('');
  const [submitting,  setSubmitting]  = useState(false);
  const [error,       setError]       = useState(null);
  const [success,     setSuccess]     = useState(false);

  // ── Form submission ─────────────────────────────────────────────────────────
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!clientName.trim() || !clientEmail.trim()) {
      setError('Please fill in both your name and email.');
      return;
    }

    setSubmitting(true);
    setError(null);

    try {
      // Fire POST /api/bookings/request (handled by parent WeeklyCalendar)
      await onConfirm(clientName.trim(), clientEmail.trim());
      setSuccess(true);
      // Auto-close after a brief success message
      setTimeout(() => onClose(), 2000);
    } catch (err) {
      setError(err.message || 'Something went wrong. Please try again.');
    } finally {
      setSubmitting(false);
    }
  };

  // ── Close when clicking outside the card ───────────────────────────────────
  const handleBackdropClick = (e) => {
    if (e.target === e.currentTarget) onClose();
  };

  // ── Render ──────────────────────────────────────────────────────────────────
  return (
    <div
      className="modal-backdrop"
      onClick={handleBackdropClick}
      role="dialog"
      aria-modal="true"
      aria-labelledby="modal-title"
    >
      <div className="modal-card">

        {/* Close (×) button */}
        <button
          className="modal-close-btn"
          onClick={onClose}
          aria-label="Close booking dialog"
        >
          ×
        </button>

        {success ? (
          /* ── Success state ───────────────────────────────────────────────── */
          <div className="modal-success">
            <div className="success-icon" aria-hidden="true">✅</div>
            <h3>Request Sent!</h3>
            <p>
              Your booking request for <strong>{formatDate(slot.slotDateTime)}</strong> at{' '}
              <strong>{formatTime(slot.slotDateTime)}</strong> has been submitted.
              <br />The clinic will confirm your appointment shortly.
            </p>
          </div>
        ) : (
          /* ── Request form ────────────────────────────────────────────────── */
          <>
            <div className="modal-icon" aria-hidden="true">📅</div>

            <h3 className="modal-title" id="modal-title">Request a Booking</h3>

            <p className="modal-subtitle">
              Would you like to request a booking for
              <br />
              <strong>{formatDate(slot.slotDateTime)}</strong> at{' '}
              <strong>{formatTime(slot.slotDateTime)}</strong>?
            </p>

            <form className="modal-form" onSubmit={handleSubmit} noValidate>

              <div className="form-group">
                <label htmlFor="modal-name">Your Name</label>
                <input
                  id="modal-name"
                  type="text"
                  placeholder="e.g. Jane Doe"
                  value={clientName}
                  onChange={(e) => setClientName(e.target.value)}
                  autoComplete="name"
                  required
                  disabled={submitting}
                />
              </div>

              <div className="form-group">
                <label htmlFor="modal-email">Your Email</label>
                <input
                  id="modal-email"
                  type="email"
                  placeholder="e.g. jane@example.com"
                  value={clientEmail}
                  onChange={(e) => setClientEmail(e.target.value)}
                  autoComplete="email"
                  required
                  disabled={submitting}
                />
              </div>

              {/* Inline error banner */}
              {error && (
                <div className="modal-error" role="alert">
                  ⚠️ {error}
                </div>
              )}

              <div className="modal-actions">
                <button
                  type="button"
                  className="btn btn-cancel"
                  onClick={onClose}
                  disabled={submitting}
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="btn btn-confirm"
                  disabled={submitting}
                >
                  {submitting ? 'Sending…' : 'Yes, Request Booking'}
                </button>
              </div>

            </form>
          </>
        )}

      </div>
    </div>
  );
}

export default BookingModal;
