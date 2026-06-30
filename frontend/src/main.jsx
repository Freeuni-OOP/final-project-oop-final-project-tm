import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import PrivateProfile from './Profile/Pages/PrivateProfile.jsx'
import PageNotFound from "./PageNotFound/PageNotFound.jsx";
import PublicProfile from "./Profile/Pages/PublicProfile.jsx";


import {createBrowserRouter, RouterProvider} from "react-router-dom";
import ProfileEdit from "./Profile/Pages/ProfileEdit.jsx";

const routers = createBrowserRouter([
    {path: "/", element: <App/>},
    {path: "/profile/:userId", element: <PublicProfile/>},
    {path: "*", element: <PageNotFound/>},
    {path: "/profile", element:<PrivateProfile/>},
    {path: "/profile/edit", element:<ProfileEdit/>}
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={routers} />
  </StrictMode>,
)