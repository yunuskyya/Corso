import React, { useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import { Navigate } from 'react-router-dom';
import { loginUser } from '../../features/authSlice';
import { useEffect } from 'react';
import { resetLoginStatus } from '../../features/authSlice';
import './Loginstyle.css'
import logo from '../../assets/LogoCorso.png';
import { GeneralSpinner } from '../../components/Common/GeneralSpinner';

const Login = () => {
    const [showAlert, setShowAlert] = useState(true);

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const error = useAppSelector((state) => state.auth.error);
    const dispatch = useAppDispatch();
    const auth = useAppSelector((state) => state.auth);

    useEffect(() => {
        if (error) {
            setEmail('');
            setPassword('');
        }
    }, [error]);

    useEffect(() => {

        return () => {
            dispatch(resetLoginStatus());
        };
    }, []);

    const handleSubmit = (e) => {
        e.preventDefault();
        dispatch(loginUser({ email, password }));
    };

    if (auth.isLoginSuccess && auth.user) {
        console.log("Navigate to dashboard");
        return <Navigate to="/dashboard" />;
    }

    function handleResetState() {
        setShowAlert(false);
        dispatch(resetLoginStatus());
    }

    return (
        <div className="form-signin w-100 m-auto">
            <form onSubmit={handleSubmit}>
                <div className="text-center">
                    <img className="col mb-4 w-75" src={logo} alt="logo" />
                </div>
                <div className="form-group">
                    <label>Email</label>
                    <input
                        type="email"
                        className="form-control"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input
                        type="password"
                        className="form-control"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <button type="submit" className="btn btn-primary">Login</button>
                {auth.status === 'loading' && <GeneralSpinner />}
                {auth.status === 'failed' && auth.error && showAlert && (
                    <div className='alert alert-danger alert-dismissible' role='alert'>
                        <div>{auth.error}</div>
                        <button
                            type="button"
                            className="btn-close"
                            onClick={handleResetState}
                            aria-label="Close"
                        ></button>
                    </div>
                )}
            </form>
        </div>
    );
};

export default Login;
