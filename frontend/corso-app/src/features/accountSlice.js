import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { createAccount, getAllAccountsSelectedCustomer, deleteAccount, getAllAccounts, getAllAccountsForBroker } from '../api/accountApi';

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
export const getAllAccountsForManager = createAsyncThunk(
    'account/getAllAccountsForManager',
    async () => {
        const response = await getAllAccounts();
        return response;
    }
);

export const getAllAccountsForBrokerById = createAsyncThunk(
    'account/getAllAccountsForBrokerById',
    async (userId) => {
        const response = await getAllAccountsForBroker(userId);
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
            .addCase(getAllAccountsForManager.pending, (state) => {
                state.status = 'loading';
                state.error = null;
            })
            .addCase(getAllAccountsForManager.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.accounts = action.payload;
                state.error = null;
            })
            .addCase(getAllAccountsForManager.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            })
            // Fetch Accounts for Customer
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
            })
            .addCase(getAllAccountsForBrokerById.pending, (state) => {
                state.status = 'loading';
                state.error = null;
            })
            .addCase(getAllAccountsForBrokerById.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.accounts = action.payload;
                state.error = null;
            })
            .addCase(getAllAccountsForBrokerById.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            });
    },
});

export const { resetAccountStatus } = accountSlice.actions;
export default accountSlice.reducer;
