import {configureStore} from '@reduxjs/toolkit';
import authReducer from '../features/authSlice';
import customerReducer from '../features/customerSlice';
import accountReducer from '../features/accountSlice';
import transactionReducer from '../features/transactionSlice';
import moneyTransferReducer from '../features/moneyTransferSlice';

export const store = configureStore({
    reducer: {
        auth: authReducer,
        customer: customerReducer,
        account: accountReducer,
        transaction: transactionReducer,
        moneyTransfer: moneyTransferReducer
    }
});