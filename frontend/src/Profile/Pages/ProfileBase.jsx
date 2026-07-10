import 'react';
import './ProfileBase.css'
import {useEffect, useState} from "react";
import ProfilePicture from "./ProfilePicture.jsx";
import {Link, useNavigate} from "react-router-dom";
import {FollowingManagement} from "../Services/FollowingManagement.js";
import {GetFollowerCount} from "../Services/GetFollowerCount.js";
import {GetFollowingCount} from "../Services/GetFollowingCount.js";
import {IsViewerFollowing} from "../Services/IsViewerFollowing.js";
import MiniPrivateCalendar from "../../features/calendar/MiniPrivateCalendar.jsx";

function ProfileBase({profileData, isPublic}) {
    const [isExpanded, setIsExpanded] = useState(false);
    const {firstName, lastName, aboutMe, imagePath} = profileData;
    const fullBio = aboutMe || "";
    const fullName = firstName + " " + lastName;
    const shortBio = fullBio.substring(0, 450) + "...";

    const[followerCount, setFollowerCount] = useState(0);
    const[followingCount, setFollowingCount] = useState(0);
    const [isFollowing, setIsFollowing] = useState(false);
    const publicId = profileData.id;
    const navigate = useNavigate();

    console.log("Full profileData: ", profileData);
    console.log("Image Path from Backend: ", imagePath);

    useEffect(() => {
        const fetchCount = async () => {
            try {
                const count = await GetFollowerCount(publicId);
                setFollowerCount(count);
                console.log("FOLLOWERS:   ", count);

                const count2 = await GetFollowingCount(publicId);
                setFollowingCount(count2);
                console.log("FOLLOWING:   ", count2);

                const isViewerFollowing = await IsViewerFollowing(publicId);
                if(isViewerFollowing) {
                    setIsFollowing(true);
                }


            } catch(error) {
                console.error("Error Fetching Followers: ", error);
            }
        }
        fetchCount();
    }, [publicId])

    const handleCalendarClick = () => {
        // 3. Define the path you want to navigate to
        navigate(`/profile/calendar/${publicId}`);
    };

    const handleFollowing = async() => {
        const nextIsFollowing = !isFollowing;
        setIsFollowing(!isFollowing);

        try {
            const count = await FollowingManagement(nextIsFollowing, publicId);
            console.log("FOLLOWERS:   ", count);
            setFollowerCount(count);
        } catch (error) {
            console.log("error in following: ", error);
        }


    }

    return (
        <div>
            <div className="personal-profile-page">
                <div className= "profile-header">
                    <div className="profile-picture">
                        <ProfilePicture image={imagePath}/>
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

                        <div className={"follow-requests"}>
                            <h2 className={"followers-text"}> Followers: {followerCount} </h2>
                            <h2 className={"following-text"}> Following: {followingCount}</h2>
                        </div>

                    </div>
                </div>

            </div>

            <div className={"dashboard-section"}>
                <div className={"buttons-div"}>
                    {
                        isPublic && (
                            <button className={"follow-button"}
                            onClick={
                                handleFollowing
                            }
                            > {isFollowing ? "Following" : "Follow" } </button>
                        )
                    }
                    <Link to={`/profile/${publicId}/services/offered`}>
                        <button className={"offered-services"}>Offered Services</button>
                    </Link>
                    {
                    !isPublic && (
                    <div className={"additional-buttons"}>
                        <Link to={`/profile/${publicId}/services/registered`}>
                            <button className={"registered-services"}>Registered Services</button>
                        </Link>
                        {
                            // SABA here is the link, url has to match
                            // thank you ELENE ;D
                        }
                        <Link to ="/service-creation" className = {"link-to-upload-serv"}>
                            <button className={"upload-new-services"}> Upload New Service</button>
                        </Link>


                        <div className={"mock-calendar"}> calendar </div>
                    </div>
                        )
                    }
                </div>
                <div className={"calend"}
                     onClick={handleCalendarClick}
                >

            {
                !isPublic && (
                    <MiniPrivateCalendar userId={profileData.id} />
                )
            }
                </div>
            </div>
        </div>
    );
}

export default ProfileBase;