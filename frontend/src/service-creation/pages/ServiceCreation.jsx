import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './ServiceCreation.css'; // Uses the same CSS we built previously!

function ServiceCreation() {
    // --- FORM STATES ---
    const [title, setTitle] = useState('');
    const [category, setCategory] = useState('');
    const [price, setPrice] = useState('');
    const [maxCapacity, setMaxCapacity] = useState(''); // New State Variable
    const [place, setPlace] = useState('');
    const [bio, setBio] = useState('');

    // --- IMAGE UPLOAD STATES ---
    const [selectedFile, setSelectedFile] = useState(null);
    const [imagePreview, setImagePreview] = useState(null);

    // --- SUBMISSION STATES ---
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    const navigate = useNavigate();

    // --- HANDLE IMAGE SELECTION ---
    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setSelectedFile(file);
            const previewUrl = URL.createObjectURL(file);
            setImagePreview(previewUrl);
        }
    };

    // --- HANDLE CAPACITY CHANGE (Enforces Max 100 constraint safely) ---
    const handleCapacityChange = (e) => {
        const value = e.target.value;
        // Allows empty string (clearing input) or numbers strictly between 0 and 100
        if (value === '' || (Number(value) >= 0 && Number(value) <= 100)) {
            setMaxCapacity(value);
        }
    };

    // --- HANDLE SUBMISSION (waits for DB write before navigating) ---
    const handleCreateClick = async (e) => {
        e.preventDefault(); // stop <Link> from navigating instantly

        if (isSubmitting) return; // avoid double-submits from double-clicks

        setErrorMessage('');
        setIsSubmitting(true);

        const formData = new FormData();
        formData.append('title', title);
        formData.append('category', category);
        formData.append('price', price);
        formData.append('maxCapacity', maxCapacity);
        formData.append('place', place);
        formData.append('bio', bio);
        formData.append('date', new Date().toISOString());

        if (selectedFile) {
            formData.append('profilePicture', selectedFile);
        }

        try {
            const response = await fetch('http://localhost:8080/api/service-creation', {
                method: 'POST',
                body: formData,
                credentials: 'include',
            });

            if (!response.ok) {
                if (response.status === 400) {
                    const errorData = await response.json();
                    setErrorMessage(errorData.message || "Failed to create service. Please check your inputs.");
                    return;
                }

                if (response.status === 401) {
                    const errorData = await response.json();
                    switch (errorData.errorCode) {
                        case "AUTH_COOKIE_MISSING":
                            navigate('/login');
                            return;
                        case "AUTH_TOKEN_INVALID":
                            setErrorMessage("Your session has expired. Please log in again.");
                            return;
                        default:
                            setErrorMessage("Authorization failed. Please log in again.");
                            return;
                    }
                }

                throw new Error(`Server responded with status ${response.status}`);
            }

            const data = await response.json();

            navigate(`/calendar/${data.serviceId}`);
        } catch (error) {
            console.error("Failed to create service:", error);
            setErrorMessage("Something went wrong while creating your service. Please try again.");
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="service-page-container">
            <div className="creation-header">
                <h1 className="service-title">Create Your Service</h1>
                <p className="service-address">Fill out the details below to set up your new service.</p>
            </div>

            <div className="service-top-half creation-form-layout">

                {/* LEFT SIDE: Image Upload System */}
                <div className="service-profile-container">
                    <label htmlFor="imageUpload" className={`image-upload-box ${imagePreview ? 'has-image' : ''}`}>
                        {imagePreview ? (
                            <img src={imagePreview} alt="Preview" className="profile-picture preview-img" />
                        ) : (
                            <div className="upload-placeholder">
                                <span className="upload-icon">📸</span>
                                <span>Click to upload profile picture</span>
                                <span className="upload-subtext">JPEG, PNG, or WebP</span>
                            </div>
                        )}
                    </label>
                    <input
                        type="file"
                        id="imageUpload"
                        accept="image/*"
                        onChange={handleImageChange}
                        style={{ display: 'none' }}
                    />
                </div>

                {/* RIGHT SIDE: Text Inputs */}
                <div className="service-info-container creation-inputs">

                    <div className="input-group">
                        <label>Service Title</label>
                        <input
                            type="text"
                            placeholder="e.g., Professional Dog Walking"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                        />
                    </div>

                    <div className="input-group">
                        <label>Category</label>
                        <input
                            type="text"
                            placeholder="e.g., Pet Care"
                            value={category}
                            onChange={(e) => setCategory(e.target.value)}
                        />
                    </div>

                    {/* NEW ROW: Pairs Price and Max Capacity cleanly side-by-side */}
                    <div className="input-row">
                        <div className="input-group flex-1">
                            <label>Price ($)</label>
                            <input
                                type="number"
                                min="0"
                                step="0.01"
                                placeholder="0.00"
                                value={price}
                                onChange={(e) => setPrice(e.target.value)}
                            />
                        </div>
                        <div className="input-group flex-1">
                            <label>Max Capacity</label>
                            <input
                                type="number"
                                min="1"
                                max="100"
                                placeholder="Max 100 clients"
                                value={maxCapacity}
                                onChange={handleCapacityChange}
                            />
                        </div>
                    </div>

                    <div className="input-group">
                        <label>Location / Address</label>
                        <input
                            type="text"
                            placeholder="e.g., Remote or 123 Main St, City"
                            value={place}
                            onChange={(e) => setPlace(e.target.value)}
                        />
                    </div>


                    <div className="input-group flex-grow-textarea">
                        <label>About this service</label>
                        <textarea
                            placeholder="Describe what you offer, your experience, and what clients can expect..."
                            value={bio}
                            onChange={(e) => setBio(e.target.value)}
                        ></textarea>
                    </div>

                    {errorMessage && (
                        <p className="form-error-text" style={{ color: 'crimson', marginTop: '0.5rem' }}>
                            {errorMessage}
                        </p>
                    )}

                    <div className="submit-row">
                        <Link
                            to="/service-creation/service-calendar"
                            className={`action-btn btn-primary submit-btn ${isSubmitting ? 'is-disabled' : ''}`}
                            onClick={handleCreateClick}
                            aria-disabled={isSubmitting}
                            style={{
                                textDecoration: 'none',
                                pointerEvents: isSubmitting ? 'none' : 'auto',
                                opacity: isSubmitting ? 0.6 : 1,
                            }}
                        >
                            {isSubmitting ? 'Creating...' : 'Create Profile'}
                        </Link>
                    </div>

                </div>
            </div>
        </div>
    );
}

export default ServiceCreation;