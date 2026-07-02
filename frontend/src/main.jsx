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
import ServiceBase from './serviceUrl/pages/ServiceBase.jsx'

const routers = createBrowserRouter([
    {
        path: "/",
        element: <App />,
        children: [
            {
                element: <MainLayout />,
                children: [
                    { path: "", element: <LandingPage /> },
                    { path: "services/:serviceId", element: <ServiceBase /> },
                ]
            },
            { path: "login", element: <Login /> },
            { path: "register", element: <Register /> },
        ]
    },
    { path: "*", element: <PageNotFound /> }
]);

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <RouterProvider router={routers} />
    </StrictMode>,
)