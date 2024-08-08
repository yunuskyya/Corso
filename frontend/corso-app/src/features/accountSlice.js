import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { createAccount, getAllAccountsSelectedCustomer, deleteAccount } from '../api/accountApi';

const initialState = {
    accounts: [],
    account: null,
    status: 'idle',
    error: null,
};

export const createNewAccount = createAsyncThunk(
    'account/createNewAccount',
    async (accountRequest) => {
        const response = await createAccount(accountRequest);
        return response;
    }
);

export const fetchAccountsForCustomer = createAsyncThunk(
    'account/fetchAccountsForCustomer',
    async (accountRequest) => {
        const response = await getAllAccountsSelectedCustomer(accountRequest);
        return response;
    }
);

export const removeAccount = createAsyncThunk(
    'account/removeAccount',
    async (accountRequest) => {
        const response = await deleteAccount(accountRequest);
        return response;
    }
);

const accountSlice = createSlice({
    name: 'account',
    initialState,
    reducers: {
        resetAccountStatus: (state) => {
            state.status = 'idle';
            state.error = null;
        },
    },
    extraReducers: (builder) => {
        builder
            // Create Account
            .addCase(createNewAccount.pending, (state) => {
                state.status = 'loading';
                state.error = null;
            })
            .addCase(createNewAccount.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.account = action.payload;
                state.accounts.push(action.payload);
                state.error = null;
            })
            .addCase(createNewAccount.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            })
            // Fetch Accounts
            .addCase(fetchAccountsForCustomer.pending, (state) => {
                state.status = 'loading';
                state.error = null;
            })
            .addCase(fetchAccountsForCustomer.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.accounts = action.payload;
                state.error = null;
            })
            .addCase(fetchAccountsForCustomer.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            })
            // Delete Account
            .addCase(removeAccount.pending, (state) => {
                state.status = 'loading';
                state.error = null;
            })
            .addCase(removeAccount.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.accounts = state.accounts.filter(
                    account => account.id !== action.payload.id
                );
                state.error = null;
            })
            .addCase(removeAccount.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            });
    },
});

export const { resetAccountStatus } = accountSlice.actions;
export default accountSlice.reducer;
