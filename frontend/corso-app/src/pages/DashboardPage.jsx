import useAuth from '../hooks/useAuth';
import AdminDashboard from '../components/Dashboard/AdminDashboard.jsx';
import UserDashboard from '../components/Dashboard/UserDashboard.jsx';

const DashboardPage = () => {
    const { user, isAdmin } = useAuth();

    console.log("isAdmin: ", isAdmin);
    console.log("user: ", user);

    if (isAdmin) {
        return <AdminDashboard />;
    }

    return <UserDashboard />;
};

export default DashboardPage;
