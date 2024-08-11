import React from 'react';
import { useTheme } from '../../context/ThemeProvider';
import AccordionNavs from './AccordionNavs';

const Sidebar = () => {
    const { theme } = useTheme();

    return (
        <nav id="sidebar" className="h-100 flex-column align-items-stretch shadow">
            <nav className="nav nav-pills flex-column h-100">
                <ul className="navbar-nav ms-auto">
                    <AccordionNavs variant={'sidebar'} />
                </ul>

            </nav>
        </nav>
    );
};

export default Sidebar;