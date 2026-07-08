import { Outlet, useOutletContext } from 'react-router-dom';
import NavigationBar from '../components/NavigationBar/NavigationBar';

function MainLayout() {
    const context = useOutletContext();
    return (
        <div>
            <NavigationBar user={context.currentUser} onLogout={context.handleLogout} />
            <main>
                <Outlet context={context} />
            </main>
        </div>
    );
}

export default MainLayout;