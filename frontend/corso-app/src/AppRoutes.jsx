import { Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import Login from './pages/authentication/Login';
import DashboardPage from './pages/DashboardPage';
import PrivateRoute from './components/PrivateRoute';
import IbanPage from './pages/iban/IbanPage';
import { ADMIN_OPERATIONS, MANAGER_OPERATIONS, BROKER_OPERATIONS } from './constants/routes';
import useAuth from './hooks/useAuth';
import TransactionOperationsPage from './pages/transaction/TransactionOperationsPage';
import ResetPassword from './pages/user/ResetPassword';

const AppRoutes = () => {
    const { user, role } = useAuth();
    console.log('role: ', role);

    const dashboardRoutes = () => {
        console.log('determining subroutes for: ', role);
        switch (role) {
            case 'ROLE_ADMIN':
                return getAdminSubRoutes();
            case 'ROLE_MANAGER':
                return getManagerSubRoutes();
            case 'ROLE_BROKER':
                return getBrokerSubRoutes();
            default:
                return null;
        }
    }

    return (
        <Routes>
            <Route exact path="/" element={<HomePage />} />
            <Route path="/login" element={<Login />} />
            <Route path="/iban" element={<IbanPage />} />
            <Route path='/transaction' element={<TransactionOperationsPage/>}/>
            <Route path="/set-password" element={<ResetPassword />} />
            <Route path="/dashboard" element={
                <PrivateRoute>
                    <DashboardPage />
                </PrivateRoute>
            }>
                {user ? dashboardRoutes() : null}

            </Route>
            <Route path="*" element={<HomePage />} />
        </Routes>
    );
};

function getAdminSubRoutes() {

    const adminRoutes = ADMIN_OPERATIONS.map((op) => {
        if (op.title_link === null) {
            return op.operations.map((subOp) => {
                return <Route key={subOp.link} path={subOp.link} element={subOp.page} />
            });
        } else {
            return <Route key={op.title_link} path={op.title_link} element={op.self_page} />
        }
    });

    return (
        adminRoutes
    );
}

function getManagerSubRoutes() {
    console.log("calling getManagerSubRoutes");

    const managerRoutes = MANAGER_OPERATIONS.map((op) => {
        if (op.title_link === null) {
            return op.operations.map((subOp) => {
                return <Route key={subOp.link} path={subOp.link} element={subOp.page} />
            });
        } else {
            return <Route key={op.title_link} path={op.title_link} element={op.self_page} />
        }
    });

    console.log('managerRoutes: ', managerRoutes);

    return (
        managerRoutes
    );
}

function getBrokerSubRoutes() {

    const brokerRoutes = BROKER_OPERATIONS.map((op) => {
        if (op.title_link === null) {
            return op.operations.map((subOp) => {
                return <Route key={subOp.link} path={subOp.link} element={subOp.page} />
            });
        } else {
            return <Route key={op.title_link} path={op.title_link} element={op.self_page} />
        }
    });

    return (
        brokerRoutes
    );
}

export default AppRoutes;