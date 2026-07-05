import { useState } from 'react';
import { Link } from 'react-router-dom';
import './ServiceCreation.css'; // Uses the same CSS we built previously!

function ServiceCreation() {
    // --- FORM STATES ---
    const [title, setTitle] = useState('');
    const [category, setCategory] = useState('');
    const [place, setPlace] = useState('');
    const [price, setPrice] = useState('');
    const [bio, setBio] = useState('');

    // --- IMAGE UPLOAD STATES ---
    const [selectedFile, setSelectedFile] = useState(null);
    const [imagePreview, setImagePreview] = useState(null);

    // --- HANDLE IMAGE SELECTION ---
    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setSelectedFile(file);
            const previewUrl = URL.createObjectURL(file);
            setImagePreview(previewUrl);
        }
    };

    // --- HANDLE INSTANT SUBMISSION ---
    const handleCreateClick = async () => {
        // We do NOT use e.preventDefault() here because we want the <Link> to navigate instantly.

        const formData = new FormData();
        formData.append('title', title);
        formData.append('category', category);
        formData.append('place', place);
        formData.append('price', price);
        formData.append('bio', bio);

        if (selectedFile) {
            formData.append('profilePicture', selectedFile);
        }

        try {
            // This fetch will fire in the background while the page navigates away!
            fetch('http://localhost:8080/api/services/create', {
                method: 'POST',
                body: formData,
            });
            // Note: We don't handle errors here because the component will likely
            // unmount before the server responds.
        } catch (error) {
            console.error("Failed to start upload:", error);
        }
    };

    return (
        <div className="service-page-container">
            <div className="creation-header">
                <h1 className="service-title">Create Your Service</h1>
                <p className="service-address">Fill out the details below to set up your new service.</p>
            </div>

            {/* Replaced <form> with a standard <div> since <Link> handles the click */}
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

                    <div className="input-row">
                        <div className="input-group flex-1">
                            <label>Category</label>
                            <input
                                type="text"
                                placeholder="e.g., Pet Care"
                                value={category}
                                onChange={(e) => setCategory(e.target.value)}
                            />
                        </div>
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

                    {/* NEW: The Link component replacing the submit button */}
                    <div className="submit-row">
                        <Link
                            to="/after_Profile_Creation_page"
                            className="action-btn btn-primary submit-btn"
                            onClick={handleCreateClick}
                            style={{ textDecoration: 'none' }} // Keeps it looking exactly like a button
                        >
                            Create Profile
                        </Link>
                    </div>

                </div>
            </div>
        </div>
    );
}

export default ServiceCreation;