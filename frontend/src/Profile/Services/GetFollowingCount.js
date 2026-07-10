export const GetFollowingCount  = async(profileId) => {

    const response = await fetch(`http://localhost:8080/api/profile/follow/following/count/${profileId}`, {
        method: 'GET'
    });

    if(!response.ok) {
        console.error("Sth went wrong in BACKEND, not ok");
    }

    return response.json();

}