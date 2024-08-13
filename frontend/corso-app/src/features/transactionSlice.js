import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { fetchCustomerList as fetchCustomerListApi, 
         fetchAccountsForCustomerBalanceHigherThanZero as fetchAccountsForCustomerBalanceHigherThanZeroApi,
         fetchCurrencyCost as fetchCurrencyCostApi, 
         createTransaction as createTransactionApi,
         fetchTransactionListForBroker as fetchTransactionListForBrokerApi } from '../api/transactionApi';

const initialState = {
    customers: [],
    accounts: [],
    transactions: [], // Yeni eklenen state alanı
    status: 'idle',
    error: null,
    maxBuying: null,
    currencyStatus: 'idle',
    transactionStatus: 'idle', 
    createErrorTransaction: null
};

export const fetchCustomerListThunk = createAsyncThunk('transaction/fetchCustomerList', async (userId) => {
    const response = await fetchCustomerListApi(userId);
    console.log("deneme erkal: "+ response);
    return response;
});

export const fetchAccountsForCustomerThunk = createAsyncThunk('transaction/fetchAccountsForCustomer', async (customerId) => {
    const response = await fetchAccountsForCustomerBalanceHigherThanZeroApi(customerId);
    return response;
});

export const fetchCurrencyCostThunk = createAsyncThunk(
    'transaction/fetchCurrencyCost',
    async ({ purchasedCurrencyCode, soldCurrencyCode, selectedAccountBalance}) => {
        const response = await fetchCurrencyCostApi(purchasedCurrencyCode, soldCurrencyCode, selectedAccountBalance);
        return response;
    }
); 

export const createTransactionThunk = createAsyncThunk(
    'transaction/createTransaction',
    async ({ account_id, purchasedCurrency, soldCurrency, amount, user_id }) => {
        console.log("slice  içindeyiz mi ?******");
        const response = await createTransactionApi(account_id, purchasedCurrency, soldCurrency, amount, user_id);
        console.log("Response log : " +response);
        return response;
    }
);

export const fetchTransactionListForBrokerThunk = createAsyncThunk(
    'transaction/fetchTransactionListForBroker',
    async (userId) => {
        const response = await fetchTransactionListForBrokerApi(userId);
        return response;
    }
);

const transactionSlice = createSlice({
    name: 'transaction',
    initialState,
    reducers: {
        resetTransactionStatus: (state) => {
            state.status = 'idle';
            state.error = null;
        },
        resetCreateTransactionStatus: (state) => {
            state.transactionStatus = 'idle';
            state.createErrorTransaction = null;
            
        },

        resetMaxBuying: (state) => {
            state.maxBuying = 0;
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(fetchCustomerListThunk.pending, (state) => {
                state.status = 'loading';
                state.error = null;
            })
            .addCase(fetchCustomerListThunk.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.customers = action.payload;
                state.error = null;
            })
            .addCase(fetchCustomerListThunk.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            })
            .addCase(fetchAccountsForCustomerThunk.pending, (state) => {
                state.status = 'loading';
                state.error = null;
            })
            .addCase(fetchAccountsForCustomerThunk.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.accounts = action.payload;
                state.error = null;
            })
            .addCase(fetchAccountsForCustomerThunk.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            })
            .addCase(fetchCurrencyCostThunk.pending, (state) => {
                state.currencyStatus = 'loading';
                state.error = null;
            })
            .addCase(fetchCurrencyCostThunk.fulfilled, (state, action) => {
                console.log('Action Payload:', action.payload);
                state.currencyStatus = 'succeeded';
                state.maxBuying = action.payload.maxBuying; 
            })
            .addCase(fetchCurrencyCostThunk.rejected, (state, action) => {
                state.currencyStatus = 'failed';
                state.error = action.error.message;
            })
            .addCase(createTransactionThunk.pending, (state) => {
                state.transactionStatus = 'loading';
                state.createErrorTransaction = null;
            })
            .addCase(createTransactionThunk.fulfilled, (state, action) => {
                state.transactionStatus = 'succeeded';
                state.createErrorTransaction = null;
            })
            .addCase(createTransactionThunk.rejected, (state, action) => {
                state.transactionStatus = 'failed';
                state.createErrorTransaction = action.error.message;
            })
            .addCase(fetchTransactionListForBrokerThunk.pending, (state) => {
                state.status = 'loading';
                state.error = null;
            })
            .addCase(fetchTransactionListForBrokerThunk.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.transactions = action.payload; // Gelen veriyi state'e ekliyoruz
                state.error = null;
            })
            .addCase(fetchTransactionListForBrokerThunk.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            });
    },
});

export const { resetTransactionStatus, resetCreateTransactionStatus, resetMaxBuying} = transactionSlice.actions;
export default transactionSlice.reducer;
