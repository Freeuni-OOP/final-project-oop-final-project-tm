export const GetNotifications = async () => {
    const response = await fetch(`http://localhost:8080/api/profile/notification/get`, {
        method: 'GET',
        credentials: 'include'
    });

    if(!response.ok) console.error("Unable to get notifications")
    return response.json();
}