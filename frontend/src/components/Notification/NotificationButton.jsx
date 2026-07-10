import {useEffect, useRef, useState} from "react";
import './NotificationButton.css';
import bellIcon from '../../assets/bell.png';
import {GetNotifications} from "./Services/GetNotifications.js";
import {SetMessagesRead} from "./Services/SetMessagesRead.js";

export default function NotificationButton() {
    const [isOpen, setIsOpen] = useState(false);
    // this ref is used to determine if click was outside the box
    const refNotDiv = useRef(null);
    const [notifications, setNotifications] = useState(null);

    useEffect(() => {
        function handleClickOutside(event) {
            if(isOpen && refNotDiv.current && !refNotDiv.current.contains(event.target)) {
                setIsOpen(false);
                SetMessagesRead();
            }
        }

        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        }
    }, [isOpen]);

    const handleNotificationOpening = async() =>  {
        const l = await GetNotifications();
        setNotifications(l);
        setIsOpen(!isOpen);
        // console.log(notifications);

        SetMessagesRead();

    }


    return (
        <div className={"notification-main-div"}  ref={refNotDiv}>
            <button className={"bell-button"}
            onClick={handleNotificationOpening}
            >
                <img src={bellIcon} alt="BELL" className={"bell-icon"} />

            </button>
            {
                isOpen && (
                    <div className={"notification-dropdown"}>
                        <div className={"notification-title"}>
                            <h3> Notifications </h3>
                        </div>
                        <ol className={"notif-ordered-list"}>
                            {
                                notifications != null &&
                                notifications.map(notif => (
                                    notif.seen ?  (
                                    <li key={notif.id} className={"notification-item-seen"}>
                                        {notif.text}
                                    </li>
                                )    : (
                                    <li key={notif.id} className={"notification-item-unseen"}>
                                        {notif.text}
                                    </li>
                                )))

                            }
                        </ol>
                    </div>
                )
            }
        </div>
    )
}