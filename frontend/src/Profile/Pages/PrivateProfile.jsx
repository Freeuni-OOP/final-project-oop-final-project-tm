import 'react';
import {useEffect, useState} from "react";
import ProfileBase from "./ProfileBase.jsx";
import MiniCalendar from "../../features/calendar/MiniCalendar.jsx";
function PrivateProfile() {
    const [data, setData] = useState(null);
    useEffect(() => {
        fetch('http://localhost:8080/api/profile/', {credentials: 'include'})
            .then(res => res.json())
            .then(data => setData(data))
    }, []);

    if(!data) return <div> Loading ... </div>

    return (
        <>
            <ProfileBase profileData={data} isPublic={false} />
            <MiniCalendar userId={data.id} />
        </>
    );
}

export default PrivateProfile;