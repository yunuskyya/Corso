// src/redux/slices/customerSlice.js
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { filterCustomersPaged } from '../api/customerApi';

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

const customerListSlice = createSlice({
    name: 'customersList',
    initialState: {
        customers: [],
        totalPages: 0,
        currentPage: 0,
        loading: false,
        error: null,
    },
    reducers: {},
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
            });
    },
});

export default customerListSlice.reducer;
