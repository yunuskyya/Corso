import React, { useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import { Navigate } from 'react-router-dom';
import { loginUser } from '../../features/authSlice';
import { useEffect } from 'react';

const Login = () => {
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

    const handleSubmit = (e) => {
        e.preventDefault();
        dispatch(loginUser({ email, password }));
    };

    if (auth.isLoginSuccess && auth.user) {
        console.log("Navigate to dashboard");
        return <Navigate to="/dashboard" />;
    }

    return (
        <div className="container">
            <form onSubmit={handleSubmit}>
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
                {auth.status === 'loading' && <p>Loading...</p>}
                {auth.status === 'failed' && auth.error && <p>{auth.error}</p>}
            </form>
        </div>
    );
};

export default Login;
