import React, { useState } from 'react';
import Sidebar from './Sidebar';
import Navbar from './Navbar';
import AppRoutes from '../../AppRoutes';
import Footer from './Footer';

const Layout = ({ handleLogout }) => {

    return (
        <div className="">
            <Navbar onLogout={handleLogout} />
            <div className='app-container'>
                {/*  */}
                <AppRoutes />
            </div>
            <Footer />
        </div>
    );
};

export default Layout;

{/* <main className='row main m-0 p-0'>
    <div className='d-none d-lg-block col-lg-auto m-0 p-0'>
        <Sidebar />
    </div>
    <div className='col'>
        <AppRoutes />
    </div>
</main> */}