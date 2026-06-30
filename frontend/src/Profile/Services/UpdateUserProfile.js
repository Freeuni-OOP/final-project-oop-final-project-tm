
export const UpdateUserProfile = async (userData) => {
    console.error(userData.getAboutMe);
    const response = await fetch('http://localhost:8080/api/profile/update',
        {method: 'POST',
            credentials: 'include',
            headers: {
            'Content-Type':'application/json'},
            body: JSON.stringify(userData)})

    if(!response.ok) {
        console.error("Server could not update: ", response.status);
        throw new Error(response.status);
    }
    
}