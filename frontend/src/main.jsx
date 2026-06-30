import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import Profile from './Profile/Pages/Profile.jsx'
import PageNotFound from "./PageNotFound/PageNotFound.jsx";


import {createBrowserRouter, RouterProvider} from "react-router-dom";

const routers = createBrowserRouter([
    {path: "/", element: <App/>},
    {path: "/profile/:userId", element: <Profile/>},
    {path: "*", element: <PageNotFound/>}
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={routers} />
  </StrictMode>,
)