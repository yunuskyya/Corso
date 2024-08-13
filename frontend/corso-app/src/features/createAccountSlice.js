// src/redux/slices/createAccountSlice.js
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { createAccount } from '../api/customerApi';

export const createNewAccount = createAsyncThunk(
    'account/createNewAccount',
    async ({ customerId, createAccountRequest }, thunkAPI) => {
        try {
            const response = await createAccount(customerId, createAccountRequest);
            return response;
        } catch (error) {
            return thunkAPI.rejectWithValue(error.response.data);
        }
    }
);

const createAccountSlice = createSlice({
    name: 'createAccount',
    initialState: {
        loading: false,
        success: false,
        error: null,
    },
    reducers: {
        resetState: (state) => {
            state.loading = false;
            state.success = false;
            state.error = null;
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(createNewAccount.pending, (state) => {
                state.loading = true;
                state.error = null;
                state.success = false;
            })
            .addCase(createNewAccount.fulfilled, (state) => {
                state.loading = false;
                state.success = true;
            })
            .addCase(createNewAccount.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
                state.success = false;
            });
    },
});

export const { resetState } = createAccountSlice.actions;
export default createAccountSlice.reducer;
