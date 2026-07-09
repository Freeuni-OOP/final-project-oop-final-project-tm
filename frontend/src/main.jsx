import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import PageNotFound from "./PageNotFound/PageNotFound.jsx"
import Login from './components/Register_Login/Login.jsx'
import Register from './components/Register_Login/Register.jsx'
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import LandingPage from "./Pages/LandingPage.jsx";
import MainLayout from './Pages/MainLayout.jsx';
import ServiceBase from './serviceUrl/pages/ServiceBase.jsx';
import PublicProfile from "./Profile/Pages/PublicProfile.jsx";
import PrivateProfile from "./Profile/Pages/PrivateProfile.jsx";
import ProfileEdit from "./Profile/Pages/ProfileEdit.jsx";
import ServiceCreation from "./service-creation/pages/ServiceCreation.jsx";
import SettingsCalendar from "./features/calendar/SettingsCalendar.jsx";
import ForgotPassword from './components/Register_Login/ForgotPassword.jsx';
import ServiceCalendarPage from "./features/calendar/ServiceCalendarPage.jsx";
import SearchPage from "./searchPage";

const routers = createBrowserRouter([
    {
        path: "/",
        element: <App />,
        children: [
            {
                element: <MainLayout />,
                children: [
                    { path: "", element: <LandingPage /> },
                    { path: "search", element: <SearchPage /> }, // 👈 აი, ეს დაამატე!
                    { path: "services/:serviceId", element: <ServiceBase /> },
                    { path: "profile/:userId", element: <PublicProfile /> },
                    { path: "profile/", element: <PrivateProfile /> },
                    { path: "profile/edit", element: <ProfileEdit />},
                    { path: "profile/edit", element: <ProfileEdit />},
                    { path: "service-creation", element: <ServiceCreation /> },
                    { path: "calendar/:serviceId", element: <ServiceCalendarPage />},
                    { path: "calendar/:serviceId", element: <SettingsCalendar />},
                ]
            },
            { path: "login", element: <Login /> },
            { path: "register", element: <Register /> },
            { path: "forgot-password", element: <ForgotPassword /> },
        ]
    },
    { path: "*", element: <PageNotFound /> }
]);

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <RouterProvider router={routers} />
    </StrictMode>,
)
