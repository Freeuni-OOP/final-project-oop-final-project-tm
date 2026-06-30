import { useState } from 'react';
import './BookingModal.css';

function BookingModal({ slot, onConfirm, onClose, formatDate, formatTime }) {
  const [submitting, setSubmitting] = useState(false);
  const [error,      setError]      = useState(null);
  const [success,    setSuccess]    = useState(false);

  const handleSubmit = async () => {
    setSubmitting(true); setError(null);
    try {
      await onConfirm();
      setSuccess(true);
      setTimeout(() => onClose(), 2000);
    } catch (err) {
      setError(err.message || 'Something went wrong. Please try again.');
    } finally { setSubmitting(false); }
  };

  const handleBackdropClick = (e) => { if (e.target === e.currentTarget) onClose(); };

  return (
    <div className="modal-backdrop" onClick={handleBackdropClick} role="dialog" aria-modal="true">
      <div className="modal-card">
        <button className="modal-close-btn" onClick={onClose}>X</button>

        {success ? (
          <div className="modal-success">
            <h3>Request Sent!</h3>
            <p>Your booking for <strong>{formatDate(slot.slotDateTime)}</strong> at <strong>{formatTime(slot.slotDateTime)}</strong> has been submitted. The clinic will confirm shortly.</p>
          </div>
        ) : (
          <>
            <h3 className="modal-title">Request a Booking</h3>
            <p className="modal-subtitle">
              Would you like to request a booking for<br />
              <strong>{formatDate(slot.slotDateTime)}</strong> at <strong>{formatTime(slot.slotDateTime)}</strong>?
            </p>
            {error && <div className="modal-error" role="alert">{error}</div>}
            <div className="modal-actions">
              <button type="button" className="btn btn-cancel" onClick={onClose} disabled={submitting}>Cancel</button>
              <button type="button" className="btn btn-confirm" onClick={handleSubmit} disabled={submitting}>{submitting ? 'Sending...' : 'Yes, Request Booking'}</button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}

export default BookingModal;
