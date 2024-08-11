import { Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import Login from './pages/authentication/Login';
import DashboardPage from './pages/DashboardPage';
import PrivateRoute from './components/PrivateRoute';
import AddCustomerPage from './pages/customer/AddCustomerPage';
import IbanPage from './pages/iban/IbanPage';
import ListCustomerPage from './pages/customer/ListCustomerPage';


const AppRoutes = () => {

    return (
        <Routes>
            <Route exact path="/" element={<HomePage />} />
            <Route path="/login" element={<Login />} />
            <Route path="/iban" element={<IbanPage />} />
            <Route path="/dashboard" element={
                <PrivateRoute>
                    <DashboardPage />
                </PrivateRoute>
            }>
                <Route path="/dashboard/addCustomer" element={<AddCustomerPage />} />
                <Route path="/dashboard/listCustomer" element={<ListCustomerPage />} />

            </Route>
            <Route path="*" element={<HomePage />} />
        </Routes>
    );
};

export default AppRoutes;