import React from 'react';
import { Link } from 'react-router-dom';
import { useTheme } from '../../context/ThemeProvider';

const Footer = () => {
    const year = new Date().getFullYear();

    return (
        <footer className='container'>
            <div className="d-flex flex-wrap justify-content-between align-items-center py-3 my-4 border-top">
                <div className="col-md-4 d-flex align-items-center">
                    <Link to="/" className="mb-3 me-2 mb-md-0 text-decoration-none lh-1">
                        <svg className="bi" width="30" height="24">
                            <use xlinkHref="#bootstrap" />
                        </svg>
                    </Link>
                    <span className="mb-3 mb-md-0">
                        &copy; {year} Corso
                    </span>
                </div>

                <ul className="nav col-md-4 justify-content-end list-unstyled d-flex">
                    <li className="ms-3">
                        <a
                            href="mailto:helpdesk@corso.com"
                            className="text-decoration-none"
                        >
                            helpdesk@corso.com
                        </a>
                    </li>
                    <li className="ms-3">
                        <a
                            href="tel:+905435647745"
                            className="text-decoration-none"
                        >
                            +90 543 564 77 45
                        </a>
                    </li>
                </ul>
            </div>
        </footer>
    );
};

export default Footer;
