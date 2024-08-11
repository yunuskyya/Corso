import { Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import Login from './pages/authentication/Login';
import DashboardPage from './pages/DashboardPage';
import PrivateRoute from './components/PrivateRoute';
import AddCustomerPage from './pages/customer/AddCustomerPage';
import AccountsPage from './pages/account/AccountsPage';
import IbanPage from './pages/iban/IbanPage';
import UpdateCustomerPage from './pages/customer/UpdateCustomerPage';
import ListCustomerPage from './pages/customer/ListCustomerPage';


const AppRoutes = () => {

    return (
        <Routes>
            <Route exact path="/" element={<HomePage />} />
            <Route path="/login" element={<Login />} />
            <Route path="/iban" element={<IbanPage />} />
            <Route path="/accounts" element={<AccountsPage />} />
            <Route path="/dashboard" element={
                <PrivateRoute>
                    <DashboardPage />
                </PrivateRoute>
            }>
                <Route path="/dashboard/addCustomer" element={<AddCustomerPage />} />
                <Route path="/dashboard/listCustomer" element={<ListCustomerPage />} />
                <Route path="/dashboard/updateCustomer" element={<UpdateCustomerPage />} />

            </Route>
            <Route path="*" element={<HomePage />} />
        </Routes>
    );
};

export default AppRoutes;