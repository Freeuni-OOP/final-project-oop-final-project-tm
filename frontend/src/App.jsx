import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import FakeMainPage from './fakeMainPage';
import SearchPage from './searchPage';
import ProfilePage from './profilePage';
import UserHiresPage from './userHiresPage';
import './App.css';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<FakeMainPage />} />
                <Route path="/search" element={<SearchPage />} />
                <Route path="/profile" element={<ProfilePage />} />
                <Route path="/profile/hires/:status" element={<UserHiresPage />} />
            </Routes>
        </Router>
    );
}

export default App;