import 'react';
import {useEffect, useState} from "react";
import ProfileBase from "./ProfileBase.jsx";
import MiniPrivateCalendar from "../../features/calendar/MiniPrivateCalendar.jsx";
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
            <MiniPrivateCalendar userId={data.id} />
        </>
    );
}

export default PrivateProfile;