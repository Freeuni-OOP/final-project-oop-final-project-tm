import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import WeeklyCalendar from './WeeklyCalendar';
import SettingsCalendar from './SettingsCalendar';

const API_BASE = 'http://localhost:8080';

function ServiceCalendarPage() {
  const { serviceId } = useParams();
  const [isOwner, setIsOwner] = useState(null);

  useEffect(() => {
    let cancelled = false;

    async function checkOwnership() {
      try {
        const ownerRes = await fetch(`${API_BASE}/api/calendar/service/${serviceId}/owner`);
        if (!ownerRes.ok) {
          if (!cancelled) setIsOwner(false);
          return;
        }
        const { ownerId } = await ownerRes.json();

        const profileRes = await fetch(`${API_BASE}/api/profile/`, { credentials: 'include' });
        if (!profileRes.ok) {
          if (!cancelled) setIsOwner(false);
          return;
        }
        const profile = await profileRes.json();
        if (!cancelled) setIsOwner(profile.id === ownerId);
      } catch {
        if (!cancelled) setIsOwner(false);
      }
    }

    checkOwnership();
    return () => { cancelled = true; };
  }, [serviceId]);

  if (isOwner === null) return null;
  return isOwner ? <SettingsCalendar serviceId={serviceId} /> : <WeeklyCalendar serviceId={serviceId} />;
}

export default ServiceCalendarPage;
