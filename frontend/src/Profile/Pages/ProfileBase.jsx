import 'react';
import './ProfileBase.css'
import {useEffect, useState} from "react";
import NavigationBar from "../../components/NavigationBar/NavigationBar.jsx";
import ProfilePicture from "./ProfilePicture.jsx";
import {Link} from "react-router-dom";

function ProfileBase({profileData, isPublic}) {
    const [isExpanded, setIsExpanded] = useState(false);
    const {firstName, lastName, aboutMe, picUrl} = profileData;
    const fullBio = aboutMe || "";
    const fullName = firstName + " " + lastName;
    const shortBio = fullBio.substring(0, 450) + "...";

    console.log("Full profileData: ", profileData);
    console.log("Image Path from Backend: ", picUrl);

    return (
        <div>
            <div className="personal-profile-page">
                <NavigationBar />
                <div className= "profile-header">
                    <div className="profile-picture">
                        <ProfilePicture image={picUrl}/>
                    </div>
                    <div className="profile-text">
                        <h1 className="name"> {fullName}
                        </h1>
                        <h2 className="bio"> {isExpanded ? fullBio : shortBio}
                            <button className="read-more"
                                    onClick={() => setIsExpanded(!isExpanded)}>
                                {isExpanded ? "Show less" : "Read more"}
                            </button>
                        </h2>
                        {!isPublic && <Link to={"/profile/edit"}> <button className="edit-button" >Edit Profile</button> </Link>
                        }
                    </div>
                </div>

            </div>

            <div className={"dashboard-section"}>
                <div className={"buttons-div"}>
                    <button className={"offered-services"}> Offered Services </button>
                    {
                    !isPublic && (
                    <div className={"additional-buttons"}>
                        <button className={"registered-services"}> Registered Services</button>
                        <button className={"upload-new-services"}> Upload New Service</button>

                        <div className={"mock-calendar"}> calendar </div>
                    </div>
                        )
                    }
                </div>

            {
                !isPublic && (
                    <div className="calendar-column">
                        <h3 className="calendar-title">Availability Calendar</h3>
                        <div className="mock-calendar-grid">
                            {/* This creates 28 dummy squares for a 4-week grid */}
                            {[...Array(28)].map((_, i) => (
                                <div key={i} className="calendar-square"></div>
                            ))}
                        </div>
                    </div>
                )
            }
            </div>
        </div>
    );
}

export default ProfileBase;