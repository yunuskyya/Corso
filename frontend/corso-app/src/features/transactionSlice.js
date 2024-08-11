import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { fetchCustomerList as fetchCustomerListApi, 
         fetchAccountsForCustomerBalanceHigherThanZero as fetchAccountsForCustomerBalanceHigherThanZeroApi,
         fetchCurrencyCost as fetchCurrencyCostApi } from '../api/transactionApi'; // API çağrısını farklı isimle import ettik

const initialState = {
    customers: [],
    accounts: [],
    status: 'idle',
    error: null,
    currencyCost: null, // Döviz maliyetini saklayacak alan
    currencyStatus: 'idle', // Döviz maliyetinin durumu
};

export const fetchCustomerListThunk = createAsyncThunk('transaction/fetchCustomerList', async (userId) => {
    const response = await fetchCustomerListApi(userId);
    return response;
});

export const fetchAccountsForCustomerThunk = createAsyncThunk('transaction/fetchAccountsForCustomer', async (customerId) => {
    const response = await fetchAccountsForCustomerBalanceHigherThanZeroApi(customerId);
    return response;
});

export const fetchCurrencyCostThunk = createAsyncThunk(
    'transaction/fetchCurrencyCost',
    async ({ purchasedCurrencyCode, soldCurrencyCode, amount }) => {
        // Döviz maliyetini hesaplayan API çağrısını yap
        const response = await fetchCurrencyCostApi(purchasedCurrencyCode, soldCurrencyCode, amount);
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
                state.currencyStatus = 'succeeded';
                state.currencyCost = action.payload.cost; // API'den gelen maliyeti sakla
            })
            .addCase(fetchCurrencyCostThunk.rejected, (state, action) => {
                state.currencyStatus = 'failed';
                state.error = action.error.message;
            });
    },
});

export const { resetTransactionStatus } = transactionSlice.actions;
export default transactionSlice.reducer;
