// src/redux/slices/customerSlice.js
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { filterCustomersPaged, softDeleteCustomer } from '../api/customerApi';

export const fetchFilteredCustomers = createAsyncThunk(
    'customers/fetchFilteredCustomers',
    async ({ filterRequest, page, size, sort }, thunkAPI) => {
        try {
            const response = await filterCustomersPaged(filterRequest, page, size, sort);
            return response;
        } catch (error) {
            return thunkAPI.rejectWithValue(error.response.data);
        }
    }
);

export const softDeleteCustomerById = createAsyncThunk(
    'customers/softDeleteCustomerById',
    async ({ customerId, customer }, thunkAPI) => {
        console.log('customerId:', customerId);
        console.log('customer:', customer);
        try {
            const response = await softDeleteCustomer(customerId, customer);
            return response.data; // Assuming the response contains the data in the `data` field
        } catch (error) {
            return thunkAPI.rejectWithValue(error.response?.data || 'An error occurred'); // Handle cases where `error.response` might be undefined
        }
    }
);

const customerListSlice = createSlice({
    name: 'customersList',
    initialState: {
        customers: [],
        totalPages: 0,
        currentPage: 0,
        loading: false,
        error: null,
        deleteError: null,
        deleteSuccess: false,
    },
    reducers: {
        resetDeleteState: (state) => {
            state.deleteError = null;
            state.deleteSuccess = false;
        }
    },
    extraReducers: (builder) => {
        builder
            .addCase(fetchFilteredCustomers.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchFilteredCustomers.fulfilled, (state, action) => {
                state.customers = action.payload.content;
                state.totalPages = action.payload.totalPages;
                state.currentPage = action.meta.arg.page;
                state.loading = false;
            })
            .addCase(fetchFilteredCustomers.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
            })
            .addCase(softDeleteCustomerById.pending, (state) => {
                state.loading = true;
                state.deleteError = null;
                state.deleteSuccess = false;
            })
            .addCase(softDeleteCustomerById.fulfilled, (state) => {
                state.loading = false;
                state.deleteSuccess = true;
            })
            .addCase(softDeleteCustomerById.rejected, (state, action) => {
                state.loading = false;
                state.deleteError = action.payload;
            });
    },
});

export const { resetDeleteState } = customerListSlice.actions;
export default customerListSlice.reducer;
