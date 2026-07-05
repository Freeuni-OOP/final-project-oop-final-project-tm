export const GetFollowerCount = async (profileId) => {
    const response = await fetch(`http://localhost:8080/api/profile/follow/follower/count/${profileId}`,
        {
            method: 'GET'
        });

    if(!response.ok) {
        throw new Error("Failed to fetch follower count from backend");
    }
    return response.json();
}