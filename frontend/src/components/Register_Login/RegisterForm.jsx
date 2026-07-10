const RegisterForm = ({first_name, setFirst_name,
                          last_name, setLast_name,
                          email, setEmail,
                          password, setPassword, isSubmitting,
                          confirmPassword, setConfirmPassword,
                          onSubmit
                      }) => {
    return (
        <form onSubmit={onSubmit}>
            <h2>Create Account</h2>
            <div className="input-group">
                <label htmlFor="first_name">First Name</label>
                <input
                    type="text"
                    id="first_name"
                    value={first_name}
                    onChange={(e) => setFirst_name(e.target.value)}
                    required
                    placeholder="Enter your First name"
                />
            </div>
            <div className="input-group">
                <label htmlFor="last_name">Last Name</label>
                <input
                    type="text"
                    id="last_name"
                    value={last_name}
                    onChange={(e) => setLast_name(e.target.value)}
                    required
                    placeholder="Enter your Last name"
                />
            </div>
            <div className="input-group">
                <label htmlFor="email">Email Address</label>
                <input
                    type="email"
                    id="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                    placeholder="Enter your email"
                />
            </div>
            <div className="input-group">
                <label htmlFor="password">Password</label>
                <input
                    type="password"
                    id="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                    placeholder="Create a password"
                />
            </div>
            <div className="input-group">
                <label htmlFor="confirmPassword">Confirm Password</label>
                <input
                    type="password"
                    id="confirmPassword"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    required
                    placeholder="Confirm your password"
                />
            </div>
            <button type="submit" className="register-btn" disabled={isSubmitting}>
                {isSubmitting ? 'Registering...' : 'Register'}
            </button>
        </form>
    );
};

export default RegisterForm;