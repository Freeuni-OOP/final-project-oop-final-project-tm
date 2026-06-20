import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import ServiceCreationPage from './components/ServiceCreation/ServiceCreationPage';
import './App.css';
import ProtectedRoute from "./ProtectedRouter/ProtectedRoute.jsx";
import LoginPage from './components/mock_login/LoginPage.jsx';

function App() {
    return (
        <BrowserRouter>
            <div className="App">
                <Routes>
                    {/* If they go to localhost:5173/login, show the Login page */}
                    <Route path="/login" element={<LoginPage />} />

                    {/* If they go to localhost:5173/create-service, show your page */}
                    <Route path="/create-service" element={
                        <ProtectedRoute>
                            <ServiceCreationPage/>
                       </ProtectedRoute>
                    } />
                    {/* If they go to the base URL (localhost:5173/), redirect them somewhere */}
                    <Route path="/" element={<Navigate to="/create-service" />} />

                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App;