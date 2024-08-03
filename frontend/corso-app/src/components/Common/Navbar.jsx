import { Link } from 'react-router-dom';
import { useAppSelector } from '../../redux/hooks';

const Navbar = ({ onLogout }) => {
    const isLoginSuccess = useAppSelector((state) => state.auth.isLoginSuccess);
    const handleLogout = async () => {
        await onLogout();
        //navigate('/login'); // Redirect to login page
    };

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <Link className="navbar-brand" to="/">MyApp</Link>
            <div className="collapse navbar-collapse">
                <ul className="navbar-nav mr-auto">
                    <li className="nav-item">
                        <Link className="nav-link" to="/dashboard">Dashboard</Link>
                    </li>
                </ul>
                {isLoginSuccess && (<button className="btn btn-outline-danger my-2 my-sm-0" onClick={handleLogout}>Logout</button>)}
            </div>
        </nav>
    );
};

export default Navbar;
