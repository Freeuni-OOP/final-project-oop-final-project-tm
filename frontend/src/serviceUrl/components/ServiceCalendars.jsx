import MinPrivateCalendar from '../../features/calendar/MiniPrivateCalendar.jsx';
import MinServiceCalendar from '../../features/calendar/MiniServiceCalendar.jsx';

function ServiceCalendars({ serviceId, userId, isRegistered }) {
    return (
        <div className={`service-bottom-half ${isRegistered ? 'single-calendar' : ''}`}>
            <div className="calendar-container">
                <h3 className="calendar-title">Availability</h3>
                <div className="calendar-placeholder">
                    <MinServiceCalendar serviceId={serviceId} />
                </div>
            </div>

            {isRegistered && (
                <div className="calendar-container">
                    <h3 className="calendar-title">Your Calendar</h3>
                    <div className="calendar-placeholder">
                        <MinPrivateCalendar userId={userId} />
                    </div>
                </div>
            )}
        </div>
    );
}

export default ServiceCalendars;