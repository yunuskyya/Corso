import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { fetchCustomerList as fetchCustomerListApi, fetchAccountsForCustomerBalanceHigherThanZero as fetchAccountsForCustomerBalanceHigherThanZeroApi } from '../api/transactionApi';

const initialState = {
    customers: [],
    accounts: [],
    status: 'idle',
    error: null,
};

export const fetchCustomerListThunk = createAsyncThunk('transaction/fetchCustomerList', async (userId) => {
    const response = await fetchCustomerListApi(userId);
    return response;
});

export const fetchAccountsForCustomerThunk = createAsyncThunk('transaction/fetchAccountsForCustomer', async (customerId) => {
    const response = await fetchAccountsForCustomerBalanceHigherThanZeroApi(customerId);
    return response;
});

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
            });
    },
});

export const { resetTransactionStatus } = transactionSlice.actions;
export default transactionSlice.reducer;
