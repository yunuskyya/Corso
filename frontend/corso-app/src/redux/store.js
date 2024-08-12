import {configureStore} from '@reduxjs/toolkit';
import authReducer from '../features/authSlice';
import customerReducer from '../features/customerSlice';
import accountReducer from '../features/accountSlice';
import transactionReducer from '../features/transactionSlice';
import userReducer from '../features/userSlice';

export const store = configureStore({
    reducer: {
        auth: authReducer,
        customer: customerReducer,
        account: accountReducer,
        user: userReducer,
        transaction: transactionReducer
    }
});