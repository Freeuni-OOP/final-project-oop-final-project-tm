export const SetMessagesRead = async() => {
    const resp = await fetch(`http://localhost:8080/api/profile/notification/seen`, {
        method: 'POST',
        credentials: 'include'
    })

    if(!resp.ok) console.error("Cant unsee messages");
}