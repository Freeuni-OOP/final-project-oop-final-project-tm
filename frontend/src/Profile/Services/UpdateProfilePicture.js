
export const UpdateProfilePicture = async (picUrl) => {
    console.log("Changing Picture");
    const formData = new FormData();
    formData.append("picUrl", picUrl);



    const response = await fetch('http://localhost:8080/api/images',
        {method: 'POST',
            credentials: 'include',
            body: formData
        });

    if(!response.ok) {
        throw new Error("Failed to Upload Image");
    }
}