import WeeklyCalendar from './components/WeeklyCalendar';
import './App.css';

/**
 * Root application component.
 * Renders the clinic's weekly booking calendar as the main page content.
 */
function App() {
  return (
    <div className="app">
      <header className="app-header">
        <h1 className="app-brand">🔧 Service Clinic</h1>
        <p className="app-tagline">Book your appointment in seconds</p>
      </header>

      <main className="app-main">
        <WeeklyCalendar />
      </main>

      <footer className="app-footer">
        <p>© {new Date().getFullYear()} Service Clinic · All rights reserved</p>
      </footer>
    </div>
  );
}

export default App;
