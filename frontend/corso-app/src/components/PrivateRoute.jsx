import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAppSelector } from '../redux/hooks';  

const PrivateRoute = ({ children }) => {
    const user = useAppSelector((state) => state.auth.user);

    console.log(user);

    return user ? children : <Navigate to="/login" />;
};

export default PrivateRoute;
