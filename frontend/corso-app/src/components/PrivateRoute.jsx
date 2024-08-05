import React from 'react';
import { Navigate } from 'react-router-dom';
import useAuth from '../hooks/useAuth';
import { GeneralSpinner } from './Common/GeneralSpinner';
import { Unauthorized } from './Auth/Unauthorized';

const PrivateRoute = ({ children }) => {
    const { user, fetch_status, status } = useAuth();

    console.log("fetchStatus ", fetch_status);

    return user ? children : fetch_status === 'loading'? <GeneralSpinner /> : <Navigate to="/login" /> ;
    // fetchStatus === 'loading'? <GeneralSpinner /> : <Unauthorized />;

    // return user ? children : fetchStatus==='failed'? <Navigate to="/login" /> : 
    // fetchStatus === 'loading'? <GeneralSpinner /> : <Unauthorized />;
};

export default PrivateRoute;
