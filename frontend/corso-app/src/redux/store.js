import {configureStore} from '@reduxjs/toolkit';
import authReducer from '../features/authSlice';
import customerReducer from '../features/customerSlice';
import accountReducer from '../features/accountSlice';
import transactionReducer from '../features/transactionSlice';
import customerListReducer from '../features/customerListSlice';
import userReducer from '../features/userSlice';
import moneyTransferReducer from '../features/moneyTransferSlice';

export const store = configureStore({
    reducer: {
        auth: authReducer,
        customer: customerReducer,
        customerList: customerListReducer,
        account: accountReducer,
        user: userReducer,
        transaction: transactionReducer,
        moneyTransfer: moneyTransferReducer
    }
});