import MinPrivateCalendar from '../../features/calendar/MiniPrivateCalendar.jsx';
import MinServiceCalendar from '../../features/calendar/MiniServiceCalendar.jsx';
import { useNavigate } from 'react-router-dom';

function ServiceCalendars({ serviceId, userId, isRegistered }) {
    const navigate = useNavigate();
    return (
        <div className={`service-bottom-half ${isRegistered ? 'single-calendar' : ''}`}>
            <div className="calendar-container">
                <h3 className="calendar-title">Availability</h3>
                <div
                    className="calendar-placeholder"
                    onClick={() => navigate(`/calendar/${serviceId}`)}
                    style={{ cursor: 'pointer' }}
                >
                    <MinServiceCalendar serviceId={serviceId} />
                </div>
            </div>

            {isRegistered && (
                <div className="calendar-container">
                    <h3 className="calendar-title">Your Calendar</h3>
                    <div
                        className="calendar-placeholder"
                        onClick={() => navigate(`/profile/calendar/${userId}`)}
                        style={{cursor: 'pointer'}}
                    >
                        <MinPrivateCalendar userId={userId} />
                    </div>
                </div>
            )}
        </div>
    );
}

export default ServiceCalendars;