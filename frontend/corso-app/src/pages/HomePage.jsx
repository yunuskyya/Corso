import { Link } from 'react-router-dom';

const HomePage = () => {
    return (
        <div className="container">
            <h1>Home Page</h1>
            <Link to="/login" className="btn btn-primary">Login</Link>
        </div>
    );
};

export default HomePage;
