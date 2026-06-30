import 'react'
import {Link} from "react-router-dom";
import {useEffect} from "react";

function PageNotFound() {

    useEffect(() => {
        fetch('http://localhost:8080/api/login', {
            method: 'Post',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include'
        })
    }, []);
    return (
        <div>
        <h1> Page Not Found </h1>
        <Link to={"/"}>
            <button>Go to Home Page</button>
        </Link>
        </div>
    );
}
export default PageNotFound