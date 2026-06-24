import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import FakeMainPage from './fakeMainPage';
import SearchPage from './searchPage';
import './App.css';

function App() {
  return (
      <Router>
        <Routes>
          <Route path="/" element={<FakeMainPage />} />
          <Route path="/search" element={<SearchPage />} />
        </Routes>
      </Router>
  );
}

export default App;