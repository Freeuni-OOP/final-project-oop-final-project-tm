import { useParams } from 'react-router-dom';
import './ServiceBase.css';

import { useServiceData } from '../hooks/useServiceData';
import { useProviderImage } from '../hooks/useProviderImage';
import { useRegistrationStatus } from '../hooks/useRegistrationStatus';
import { useStarToggle } from '../hooks/useStarToggle';

import ServiceHeader from '../components/ServiceHeader';
import ServiceCalendars from '../components/ServiceCalendars';
import { useCurrentUser } from '../common/getProfileId.jsx';

function ServiceBase() {
    const { serviceId } = useParams();

    const { service, loading, errorMessage } = useServiceData(serviceId);
    const providerImage = useProviderImage(serviceId);
    const isRegistered = useRegistrationStatus();
    const { userId, loading: userLoading } = useCurrentUser();
    const [isStarred, toggleStar] = useStarToggle(serviceId);


    if (loading || userLoading) return <div>Loading...</div>;
    if (errorMessage) return <div>{errorMessage}</div>;

    return (
        <div className="service-page-container">
            <ServiceHeader
                service={service}
                providerImage={providerImage}
                isStarred={isStarred}
                onStarClick={toggleStar}
            />
            <ServiceCalendars
                serviceId={serviceId}
                userId = {userId}
                isRegistered={isRegistered}
            />
        </div>
    );
}

export default ServiceBase;