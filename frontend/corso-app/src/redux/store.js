import {configureStore} from '@reduxjs/toolkit';
import authReducer from '../features/authSlice';
import customerReducer from '../features/customerSlice';
import accountReducer from '../features/accountSlice';

export const store = configureStore({
    reducer: {
        auth: authReducer,
        customer: customerReducer,
        account: accountReducer
    }
});