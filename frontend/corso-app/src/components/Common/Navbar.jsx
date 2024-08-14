
import React from 'react';
import { Link } from 'react-router-dom';
import { useAppSelector } from '../../redux/hooks';
import { useTheme } from '../../context/ThemeProvider';
import AccordionNavs from './AccordionNavs';
import useAuth from '../../hooks/useAuth';
import logo from '../../assets/LogoCorso.png';
import SystemDate from './SystemDate';

const Navbar = ({ onLogout }) => {
    const { theme, toggleTheme } = useTheme();
    const isLoginSuccess = useAppSelector((state) => state.auth.isLoginSuccess);
    const { user } = useAuth();

    const handleLogout = async () => {
        await onLogout();
    };
    const getRoleDisplayName = (role) => {
        switch (role) {
            case 'broker':
                return 'Personel';
            case 'manager':
                return 'Yönetici';
            case 'admin':
                return 'Admin';
            default:
                return 'Bilinmeyen Rol';
        }
    };
    return (
        <nav className={`navbar navbar-expand-lg border-bottom shadow`}>
            <div className="container-fluid">
                <Link className="navbar-brand" to="/">
                    <img className="" src={logo} alt="logo" height="24" /></Link>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav me-auto">
                        {/* <li className="nav-item">
                            <Link className="nav-link" to="/dashboard">Ana Sayfa</Link>
                        </li> */}

                    </ul>
                    <ul className="navbar-nav ms-auto">
                        {user && isLoginSuccess && (<li className=''><SystemDate /></li>)}
                        <li className="nav-item dropdown">
                            <a className="nav-link dropdown-toggle" href="#" id="themeDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                Tema
                            </a>
                            <ul className="dropdown-menu" aria-labelledby="themeDropdown">
                                <li><button className="dropdown-item" onClick={() => toggleTheme('default')}>Varsayılan</button></li>
                                <li><button className="dropdown-item" onClick={() => toggleTheme('light')}>Aydınlık</button></li>
                                <li><button className="dropdown-item" onClick={() => toggleTheme('dark')}>Koyu</button></li>
                            </ul>
                        </li>
                        {user && isLoginSuccess && (<div className='d-block d-lg-none'>
                            <AccordionNavs variant={'navs'} />
                        </div>)}
                        {user && isLoginSuccess && (
                            <div className='row'>
                                <div className='col-12 fs-6 fw-bold text-info text-center'>{user.username}</div>
                                <div className='col-12 fs-6 fw-italic text-info text-center'>{getRoleDisplayName(user.role.split('_')[1].toLowerCase())}</div>
                            </div>

                        )}
                        {user && isLoginSuccess && (
                            <li className="nav-item">
                                <button className="btn btn-outline-danger my-2 my-sm-0" onClick={handleLogout}>Çıkış Yap</button>
                            </li>
                        )}
                    </ul>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;
