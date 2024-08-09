// src/components/Common/Navbar.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import { useAppSelector } from '../../redux/hooks';
import { useTheme } from '../../context/ThemeProvider';

const Navbar = ({ onLogout }) => {
    const { theme, toggleTheme } = useTheme();
    const isLoginSuccess = useAppSelector((state) => state.auth.isLoginSuccess);

    const handleLogout = async () => {
        await onLogout();
    };

    return (
        <nav className={`navbar navbar-expand-lg ${theme === 'dark' ? 'navbar-dark bg-dark' : 'navbar-light bg-light'} border-bottom`}>
            <div className="container-fluid">
                <Link className="navbar-brand" to="/">MyApp</Link>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav me-auto">
                        <li className="nav-item">
                            <Link className="nav-link" to="/dashboard">Ana Sayfa</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/addCustomer">Müşteri Ekle</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/accounts">Hesap Listeleme</Link>
                        </li>
                    </ul>
                    <ul className="navbar-nav ms-auto">
                        <li className="nav-item dropdown">
                            <a className="nav-link dropdown-toggle" href="#" id="themeDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                Theme
                            </a>
                            <ul className="dropdown-menu" aria-labelledby="themeDropdown">
                                <li><button className="dropdown-item" onClick={() => toggleTheme('default')}>Default</button></li>
                                <li><button className="dropdown-item" onClick={() => toggleTheme('light')}>Light</button></li>
                                <li><button className="dropdown-item" onClick={() => toggleTheme('dark')}>Dark</button></li>
                            </ul>
                        </li>
                        {isLoginSuccess && (
                            <li className="nav-item">
                                <button className="btn btn-outline-danger my-2 my-sm-0" onClick={handleLogout}>Logout</button>
                            </li>
                        )}
                    </ul>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;
