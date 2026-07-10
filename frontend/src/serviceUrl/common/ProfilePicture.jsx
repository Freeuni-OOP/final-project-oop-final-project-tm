import React from 'react';

// Handles the display and fetching of a user's profile picture
function ProfilePicture({ image , service}) {
    // 2. Construct the full URL to your Spring Boot backend.
    let picUrl;
    // 1. Determine the filename. If no valid image string is passed, default it.
    // If the image string is a valid URL, extract the filename.
    // If the image is back end file, extract the filename.
    if(typeof image === 'string' && image.length > 5 && image.substring(0, 5) === "https"){
        console.log(image);
        picUrl = image;
    } else {
        const imageName = (image && typeof image === 'string' && image.trim() !== "")
            ? image.split(/[/\\]/).pop()
            : "default.png";
        picUrl = `http://localhost:8080/api/images/${imageName}`;
        console.log(picUrl);
    }

    return (
        <img
            src={picUrl}
            alt="User Profile"
            className="profile-picture"

            // 3. The Fallback: If the browser fails to load the image,
            // this safely catches the error and displays the default image.
            onError={(e) => {
                e.target.onerror = null; // Prevents an infinite loop if default.png is ALSO missing
                e.target.src = "http://localhost:8080/api/images/default.png";
            }}
        />
    );
}

export default ProfilePicture;