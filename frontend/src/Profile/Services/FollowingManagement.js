export const FollowingManagement = async (isFollowing, publicId)=> {
    if(isFollowing) {

        console.log("dgiudgfiudfg " + isFollowing + "   ");
        const response = await fetch(`http://localhost:8080/api/profile/follow/new/${publicId}`,
            {
                method: 'POST',
                credentials: 'include'
            }
            )
        if(!response.ok) {
            console.log("Error with follow")
            throw new Error("Failed to follow");
        }
        const k = await response.json();
        console.log("k:    " + k);
        return k;
    } else {
        console.log("dgiudgfiudfg " + isFollowing);
        const response = await fetch(`http://localhost:8080/api/profile/follow/delete/${publicId}`,
            {
                method: 'POST',
                credentials: 'include'
            }
        )
        if(!response.ok) {
            console.log("Error with follow")

            throw new Error("Failed to unfollow");
        }
        const k = await response.json();
        console.log("k:    " + k);
        return k;
    }

}