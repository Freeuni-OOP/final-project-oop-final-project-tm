export const IsViewerFollowing = async(profileID) => {
    const response = await fetch(`http://localhost:8080/api/profile/follow/isfollowing/${profileID}`, {
        method: 'GET',
        credentials: 'include',
    });

    if(!response.ok) {
        console.error("UNABLE TO DETERMINE FOLLOWING STATUS");
    } else {
        return response.json();
    }
}