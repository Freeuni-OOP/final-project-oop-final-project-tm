import NavigationBar from "../../components/NavigationBar/NavigationBar.jsx";
import './ProfileEdit.css'
import {Link, useLocation} from "react-router-dom";
import {useEffect, useState} from "react";
import {UpdateUserProfile} from "../Services/UpdateUserProfile.js";
import {UpdateProfilePicture} from "../Services/UpdateProfilePicture.js";

function ProfileEdit() {

    const [profileData, setProfileData] = useState(null);
    useEffect(() => {
        fetch('http://localhost:8080/api/profile/', {credentials: 'include'})
            .then(res => res.json())
            .then(data => setProfileData(data))
    }, []);

    if (!profileData) return <div> Loading ...</div>;

    return <ProfileEditBody profileData={profileData}/>
}
function ProfileEditBody({profileData}) {

    const [savedChanges, setSavedChanges] = useState(false);
    const [firstName, setFirstName] = useState(profileData?.firstName || "");
    const [lastName, setLastName] = useState(profileData?.lastName || "");
    const [aboutMe, setAboutMe] = useState(profileData?.aboutMe || "");
    const [picUrl, setPicUrl] = useState(profileData?.picUrl || "");


    console.log("Full profileData: ", profileData);


    const handleSave = async () => {

        const updateData = {
            id: profileData.id,
            firstName: firstName,
            lastName: lastName,
            aboutMe: aboutMe,
        }

        try {
            await UpdateUserProfile(updateData);
            if(picUrl instanceof File) {
                await UpdateProfilePicture(picUrl);
            }


            setSavedChanges(true);
            setTimeout(() => {
                setSavedChanges(false);
            }, 2000);
6
        } catch(error) {
            console.error("Could not save changes: ", error);
            alert("Failed to save changes. Please try again");
        }
    }

    return (
        <div className={"edit-block"}>
            <div className={"go-back-div"}>
                <Link to={"/profile"} className={"go-back-link"}>
                    <button className={"go-back-button"}> Go Back </button>
                </Link>
            </div>
            <div className="form-container">
                <div className="form-card">
                    <h2 className = {"title-text"}> EDIT YOUR PROFILE </h2>
                    <div className={"first-name-div"}>
                        <label className={"first-name-label"}> Change First Name </label>
                        <input type= "text" id="first-name-input" name = "first-name-input"
                        value = {firstName}
                        onChange={(e) =>setFirstName(e.target.value)}
                        />
                    </div>

                <div className={"last-name-div"}>
                    <label className={"last-name-label"}> Change Last Name </label>
                    <input type= "text" id="last-name-input" name = "last-name-input"
                           value = {lastName}
                           onChange={(e) =>setLastName(e.target.value)}
                    />
                </div>

                <div className={"aboutMe-div"}>
                    <label className={"aboutMe-label"}> Change aboutMe  </label>
                    <textarea  id="aboutMe-input" name = "aboutMe-input"
                               value = {aboutMe}
                               onChange={(e) =>setAboutMe(e.target.value)}
                    />
                </div>

                <div className={"profile-picture-div"}>
                    <label className={"profile-picture-label"}> Change Profile Picture </label>
                    <input type= "file" accept="image/*" id="pic-input" name = "pic-input"
                    onChange={(e) => setPicUrl(e.target.files[0])}
                    />
                </div>

                <div className={"save-section"}>
                    {savedChanges && (
                        <div className="success-message">
                            Successfully changed your profile!
                        </div>
                    )}
                    <button className={"save-changes"} onClick={handleSave}> Save Changes </button>
                </div>
            </div>

        </div>

    </div>

    );
}

export default ProfileEdit