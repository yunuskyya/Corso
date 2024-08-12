import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { createCustomer } from '../api/customerApi';

const initialState = {
    result: null,
    status: 'idle',
    error: null,
};

export const createNewCustomer = createAsyncThunk('customer/createNewCustomer', async (customerRequest) => {
    const response = await createCustomer(customerRequest);
    return response;
});

const customerSlice = createSlice({
    name: 'customer',
    initialState,
    reducers: {
        resetCustomerStatus: (state) => {
            state.status = 'idle';
            state.error = null;
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(createNewCustomer.pending, (state) => {
                state.status = 'loading';
                state.error = null;
            })
            .addCase(createNewCustomer.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.result = action.payload;
                state.error = null;
            })
            .addCase(createNewCustomer.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            });
    },
});

export const { resetCustomerStatus } = customerSlice.actions;
export default customerSlice.reducer;
