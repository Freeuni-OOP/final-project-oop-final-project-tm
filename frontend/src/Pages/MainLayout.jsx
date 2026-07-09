import { Outlet, useOutletContext } from 'react-router-dom';
import NavigationBar from '../components/NavigationBar/NavigationBar';

function MainLayout() {
    const context = useOutletContext();
    return (
        <div>
            {/*handles navbar state by monitoring if the user is logged in*/}
            <NavigationBar user={context.currentUser} onLogout={context.handleLogout} />
            <main>
                <Outlet context={context} />
            </main>
        </div>
    );
}

export default MainLayout;