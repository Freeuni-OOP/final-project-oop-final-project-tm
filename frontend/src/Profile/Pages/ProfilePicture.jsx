import './ProfilePicture.css'

function ProfilePicture({image}) {
    const name = (image && image !== "" && image !== undefined) ? image : "default";
    // const picUrl = `http://localhost:8080/api/images/${name}`;
    const picUrl = "http://localhost:8080/api/images/default";
    console.log("Attempting to set src to:", picUrl);

    return (
            <img
                src={picUrl}
                alt="PrivateProfile"
                className="profile-picture"
            />

    );
}

export default ProfilePicture;