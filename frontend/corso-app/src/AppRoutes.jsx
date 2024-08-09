import { Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import Login from './components/Auth/Login';
import DashboardPage from './pages/DashboardPage';
import PrivateRoute from './components/PrivateRoute';


const AppRoutes = () => {
    return (
        <Routes>
            <Route exact path="/" element={<HomePage />} />
            <Route path="/login" element={<Login />} />
            <Route path="/dashboard" element={
                <PrivateRoute>
                    <DashboardPage />
                </PrivateRoute>
            } />
            <Route path="*" element={<HomePage />} />
        </Routes>
    );
};

export default AppRoutes;