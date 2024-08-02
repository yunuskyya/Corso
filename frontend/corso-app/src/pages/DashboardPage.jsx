import useAuth from '../hooks/useAuth';
import AdminDashboard from '../components/Dashboard/AdminDashboard.jsx';
import UserDashboard from '../components/Dashboard/UserDashboard.jsx';

const DashboardPage = () => {
    const { user, isAdmin } = useAuth();

    if (isAdmin) {
        return <AdminDashboard />;
    }

    return <UserDashboard />;
};

export default DashboardPage;
