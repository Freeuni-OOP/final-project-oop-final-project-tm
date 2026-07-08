import 'react';
import {useEffect, useState} from "react";
import ProfileBase from "./ProfileBase.jsx";
import {Navigate} from "react-router-dom";
import MiniPrivateCalendar from "../../features/calendar/MiniPrivateCalendar.jsx";
function PrivateProfile() {
    const [data, setData] = useState(null);
    useEffect(() => {
        fetch('http://localhost:8080/api/profile/', {credentials: 'include'})
            .then(res => res.json())
            .then(data => setData(data))
    }, []);

    if(!data) return <div> Loading ... </div>

    if(data.id === null || data.id === undefined) {
        return <Navigate to="/register" replace />
    }


    return (
        <>
            <ProfileBase profileData={data} isPublic={false} />
            <MiniPrivateCalendar userId={data.id} />
        </>
    );
}

export default PrivateProfile;
