import React from 'react';
import { Link } from 'react-router-dom';
import logo from '../assets/LogoCorso.png';

const HomePage = () => {
    return (
        <div 
            className="container-fluid d-flex align-items-center justify-content-center min-vh-100 text-light p-5"
            style={{ backgroundColor: '#212529' }}
        >
            <div className="d-flex align-items-center">
                <div className="d-flex flex-column align-items-center mr-5">  {/* align-items-center ile hem yazı hem de image ortalanıyor */}
                    <img src={logo} alt="Corso Logo" style={{ width: '400px', marginBottom: '10px' }} />
                    <p className="mb-0 fs-4 text-center" style={{ fontFamily: 'Arial, sans-serif', fontWeight: 'bold',color: '#D1CCB9'}}>
                    Döviz alım satımının <br />kolay hali.
                    </p>
                </div>
                <div className="d-flex justify-content-end" style={{ marginLeft: '200px' }}>
                    <Link to="/login" className="btn btn-primary btn-lg">Login</Link>
                </div>
            </div>
        </div>
    );
};

export default HomePage;