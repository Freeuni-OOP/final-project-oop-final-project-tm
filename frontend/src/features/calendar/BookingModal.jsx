import { useState } from 'react';
import './BookingModal.css';

function BookingModal({ slot, onConfirm, onClose, formatDate, formatTime }) {
  const [clientName,  setClientName]  = useState('');
  const [clientEmail, setClientEmail] = useState('');
  const [submitting,  setSubmitting]  = useState(false);
  const [error,       setError]       = useState(null);
  const [success,     setSuccess]     = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!clientName.trim() || !clientEmail.trim()) { setError('Please fill in both your name and email.'); return; }
    setSubmitting(true); setError(null);
    try {
      await onConfirm(clientName.trim(), clientEmail.trim());
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
        <button className="modal-close-btn" onClick={onClose}>×</button>

        {success ? (
          <div className="modal-success">
            <div className="success-icon">✅</div>
            <h3>Request Sent!</h3>
            <p>Your booking for <strong>{formatDate(slot.slotDateTime)}</strong> at <strong>{formatTime(slot.slotDateTime)}</strong> has been submitted. The clinic will confirm shortly.</p>
          </div>
        ) : (
          <>
            <div className="modal-icon">📅</div>
            <h3 className="modal-title">Request a Booking</h3>
            <p className="modal-subtitle">
              Would you like to request a booking for<br />
              <strong>{formatDate(slot.slotDateTime)}</strong> at <strong>{formatTime(slot.slotDateTime)}</strong>?
            </p>
            <form className="modal-form" onSubmit={handleSubmit} noValidate>
              <div className="form-group">
                <label htmlFor="modal-name">Your Name</label>
                <input id="modal-name" type="text" placeholder="e.g. Jane Doe" value={clientName} onChange={(e) => setClientName(e.target.value)} autoComplete="name" required disabled={submitting} />
              </div>
              <div className="form-group">
                <label htmlFor="modal-email">Your Email</label>
                <input id="modal-email" type="email" placeholder="e.g. jane@example.com" value={clientEmail} onChange={(e) => setClientEmail(e.target.value)} autoComplete="email" required disabled={submitting} />
              </div>
              {error && <div className="modal-error" role="alert">⚠️ {error}</div>}
              <div className="modal-actions">
                <button type="button" className="btn btn-cancel" onClick={onClose} disabled={submitting}>Cancel</button>
                <button type="submit" className="btn btn-confirm" disabled={submitting}>{submitting ? 'Sending…' : 'Yes, Request Booking'}</button>
              </div>
            </form>
          </>
        )}
      </div>
    </div>
  );
}

export default BookingModal;
