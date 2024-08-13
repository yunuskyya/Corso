import { configureStore } from '@reduxjs/toolkit';
import authReducer from '../features/authSlice';
import customerReducer from '../features/customerSlice';
import accountReducer from '../features/accountSlice';
import transactionReducer from '../features/transactionSlice';
import customerListReducer from '../features/customerListSlice';
import createAccountReducer from '../features/createAccountSlice';

export const store = configureStore({
    reducer: {
        auth: authReducer,
        customer: customerReducer,
        customerList: customerListReducer,
        account: accountReducer,
        transaction: transactionReducer,
        createAccount: createAccountReducer,
    }
});