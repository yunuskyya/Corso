
const AdminDashboard = () => {
    return (
        <main className='row main m-0 p-0'>
            <div className='d-none d-lg-block col-lg-auto m-0 p-0'>
                <Sidebar />
            </div>
            <div className='col'>
                <h1>Admin Dashboard</h1>
                <Outlet />
            </div>
        </main>
    );
};

export default AdminDashboard;
