import { useLocation } from 'react-router-dom';

function ServiceCalendar() {
    const location = useLocation();
    const { serviceId, maxCapacity } = location.state || {};

    return (
        <div className="service-creation-calendar">
            <div className="calendar-container">
                <h3 className="calendar-title">Availability</h3>
                <div className="calendar-placeholder">
                    <p>Calendar 1 will go here</p>
                </div>
            </div>
        </div>
    );
}

export default ServiceCalendar;