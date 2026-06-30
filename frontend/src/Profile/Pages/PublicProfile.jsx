import 'react';
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import ProfileBase from "./ProfileBase.jsx";

function PublicProfile() {
    const userId = useParams().userId;
    const [data, setData] = useState(null);

    useEffect(() => {
        fetch(`http://localhost:8080/api/profile/${userId}`)
            .then(res => res.json())
            .then(data => setData(data));
    }, [userId]);

    if(!data) return <div> Loading ... </div>

    return <ProfileBase profileData={data} isPublic={true} />
}

export default PublicProfile;