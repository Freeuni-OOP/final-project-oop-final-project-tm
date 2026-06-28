import 'react';
import {useParams} from "react-router-dom";

function Profile() {
    const {userId} = useParams();
    return (
        <h1> hello {userId}</h1>
    );
}

export default Profile;