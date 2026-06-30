const VerifyForm = ({ email, verificationCode, setVerificationCode, onSubmit }) => {
    return (
        <form onSubmit={onSubmit}>
            <h2>Verify Your Email</h2>
            <p style={{ fontSize: '14px', color: '#698dac', marginBottom: '20px' }}>
                We sent a 6-digit confirmation code to <strong>{email}</strong>.
            </p>
            <div className="input-group">
                <label htmlFor="verificationCode">Verification Code</label>
                <input
                    type="text"
                    id="verificationCode"
                    value={verificationCode}
                    onChange={(e) => setVerificationCode(e.target.value)}
                    required
                    maxLength="6"
                    placeholder="000000"
                />
            </div>
            <button type="submit" className="register-btn">Verify Account</button>
        </form>
    );
};

export default VerifyForm;