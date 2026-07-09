const VerifyForm = ({ email, verificationCode, setVerificationCode, onSubmit,
                        title = "Verify Your Email",
                        buttonText = "Verify Account"}) => {
    return (
        <form onSubmit={onSubmit}>
            <h2>{title}</h2>
            <p className="verification-subtitle">
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
            <button type="submit" className="register-btn">{buttonText}</button>
        </form>
    );
};

export default VerifyForm;