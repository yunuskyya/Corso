import useAuth from '../hooks/useAuth';
import AdminDashboard from './admin/AdminDashboard';
import BrokerDashboard from './broker/BrokerDashboard';
import ManagerDashboard from './manager/ManagerDashboard';
import HomePage from './HomePage';

const DashboardPage = () => {
    const { user, role } = useAuth();

    console.log("User: ", user);

    console.log("Role: ", role);

    const dashboard = () => {
        switch (role) {
            case "ROLE_ADMIN":
                return <AdminDashboard />;
            case "ROLE_MANAGER":
                return <ManagerDashboard />;
            case "ROLE_BROKER":
                return <BrokerDashboard />;
            default:
                return <HomePage />;
        }
    };

    return (
        <>{dashboard()}</>

    );
};

export default DashboardPage;
