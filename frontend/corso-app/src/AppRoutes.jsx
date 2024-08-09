import { Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import Login from './components/Auth/Login';
import DashboardPage from './pages/DashboardPage';
import PrivateRoute from './components/PrivateRoute';
import AddCustomerPage from './pages/AddCustomerPage';
import AccountsPage from './pages/AccountsPage';


const AppRoutes = () => {
    return (
        <Routes>
            <Route exact path="/" element={<HomePage />} />
            <Route path="/login" element={<Login />} />
            <Route path="/addCustomer" element={<AddCustomerPage />} />
            <Route path="/accounts" element={<AccountsPage />} />
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