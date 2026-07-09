import 'react';
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import ProfileBase from "./ProfileBase.jsx";

function PublicProfile() {
    const userId = useParams().userId;
    const [dataP, setDataP] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        fetch(`http://localhost:8080/api/profile/${userId}`, {
            method: 'GET',
            credentials:"include"
        })
            .then(res => res.json())
            .then(data => setDataP(data));
    }, [userId]);

    useEffect(() => {
        if(dataP && dataP.first && dataP.second && dataP.first.id === dataP.second) {
            navigate('/profile');
        }
    }, [dataP, navigate]);

    if(!dataP) return <div> Loading ... </div>

    console.log(dataP);

    // We don't allow users access to their public profiles
    if(dataP && dataP.first && dataP.second && dataP.first.id === dataP.second) {
        navigate('/profile');
        return null;
    }

    return <ProfileBase profileData={dataP.first} isPublic={true} />
}


export default PublicProfile;