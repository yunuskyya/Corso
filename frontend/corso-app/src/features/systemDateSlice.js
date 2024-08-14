import { getSystemDate } from '../api/systemDateApi';
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';

const initialState = {
    systemDate: '',
    loading: false,
    error: null
};

export const fetchSystemDate = createAsyncThunk(
    'systemDate/fetchSystemDate',
    async () => {
        const response = await getSystemDate();
        return response;
    }
);

const systemDateSlice = createSlice({
    name: 'systemDate',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchSystemDate.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchSystemDate.fulfilled, (state, action) => {
                state.systemDate = action.payload;
                state.loading = false;
            })
            .addCase(fetchSystemDate.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
            });
    },
});

export default systemDateSlice.reducer;
