import { useState, useEffect } from 'react';
import './ServiceCreationPage.css';
import { useNavigate } from "react-router-dom";

// SAFE IMPORT: Keep your fallback image inside your frontend's src/assets folder!
import DefaultProfilePic from '../../assets/tempProfilePicture.jpeg';

function ServiceCreationPage() {
    const navigate = useNavigate();

    // Cookie name must be written here instead, which holds real userId
    const userIdCookieName = 'userId';

    // Description string for the service
    const [description, setDescription] = useState('');

    // Title string for the service
    const [titleString,setTitleString] = useState('');

    // State to hold the dynamic image URL
    const [profilePictureUrl, setProfilePictureUrl] = useState(DefaultProfilePic);

    // 2. Handle the submit action
    const handleSubmit = () => {
        // The string is already saved in the 'description' variable!
        console.log("Ready to send:", description);
        console.log("Ready to send:", titleString);
        // Call your API function here
        sendToBackend({
                description : description,
                titleString : titleString,
                userId : userIdCookieName
        }).then(() => {
            console.log("Service submitted successfully!");
        })
    };

    async function sendToBackend(jsonData) {
        try {
            const response = await fetch('http://localhost:8080/api/service/information', {
                method: 'POST', // Usually POST when creating new data
                headers: {
                    'Content-Type': 'application/json',
                },
                // We turn your jsonData object into a JSON string here
                body: JSON.stringify(jsonData)
            });

            if (response.ok) {
                console.log("Service created successfully!");
            } else {
                console.error("Failed to create service");
            }
        } catch (error) {
            console.error("Error connecting to backend:", error);
        }
    }

    // Helper function to read a cookie by its name
    const getCookie = (name) => {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
        return null;
    };

    // Run this once when the page loads
    useEffect(() => {
        // 1. Variable to hold the memory URL so we can delete it later
        let localImageUrl = null;

        const loggedInUserId = getCookie(userIdCookieName);

        if (loggedInUserId) {
            // 2. Fetch the raw image data (blob) from the backend
            fetch(`http://localhost:8080/api/users/${loggedInUserId}/profile-image`)
                .then((response) => {
                    if (response.ok) {
                        return response.blob();
                    }
                    throw new Error('Image not found');
                })
                .then((imageBlob) => {
                    // 3. Save it to memory and display it
                    localImageUrl = URL.createObjectURL(imageBlob);
                    setProfilePictureUrl(localImageUrl);
                })
                .catch((error) => {
                    console.warn("Could not load backend image, keeping default.", error);
                });

        }

        // 4. CLEANUP: This runs when you leave the page, deleting the image from RAM to stop lag
        return () => {
            if (localImageUrl) {
                URL.revokeObjectURL(localImageUrl);
            }
        };
    }, []); // Empty array means this only runs on mount and unmount

    return (
        <div className="service-creation-page">
            <header className="service-creation-upper">
                <div className="profile-section">
                    <img
                        src={profilePictureUrl}
                        alt="Profile"
                        className="profile-picture"
                        onClick={() => window.location.href = '/user-profile'}
                        onError={(e) => {
                            // Ultimate fallback just in case the blob fails to render
                            e.target.src = DefaultProfilePic;
                        }}
                    />
                </div>

                <div className="fields-section">
                    <div className="field-group">
                        <label htmlFor="service-title" className="field-label">
                            Service Title
                        </label>
                        <input
                            onChange={(e) => setTitleString(e.target.value)}
                            id="service-title"
                            type="text"
                            className="service-title-input"
                            placeholder="Enter a name for your service"
                        />
                    </div>

                    <div className="field-group">
                        <label htmlFor="readme" className="field-label">
                            ReadMe
                        </label>
                        <textarea
                            value = {description}
                            onChange={(e) => setDescription(e.target.value)}
                            id="readme"
                            className="readme-textarea"
                            placeholder="Describe what this service does, how to use it, and any other relevant details..."
                        />
                    </div>
                </div>
            </header>

            <section className="service-creation-lower">
                {/* Intentionally left empty for now */}
            </section>

            <div className="service-creation-post">
                <button
                    className="service-creation-post-button"
                    onClick={() => {
                        navigate('/service');
                        handleSubmit();
                }}>
                    Create
                </button>
            </div>
        </div>
    );
}

export default ServiceCreationPage;