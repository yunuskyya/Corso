import {configureStore} from '@reduxjs/toolkit';
import authReducer from '../features/authSlice';
import customerReducer from '../features/customerSlice';
import accountReducer from '../features/accountSlice';
import transactionReducer from '../features/transactionSlice';

export const store = configureStore({
    reducer: {
        auth: authReducer,
        customer: customerReducer,
        account: accountReducer,
        transaction: transactionReducer
    }
});