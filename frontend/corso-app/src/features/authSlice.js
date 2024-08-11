import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { login, fetchUser, logoutUser } from '../api/authApi';

const initialState = {
    user: null,
    status: 'idle',
    fetch_status: 'idle',
    error: null,
    fetch_error: null,
    isLoginSuccess: false,
};

export const loginUser = createAsyncThunk('auth/loginUser', async (credentials) => {
    const response = await login(credentials);
    return response;
});

// delete role here. it is for mock request
export const fetchCurrentUser = createAsyncThunk('auth/fetchCurrentUser', async () => {
    const response = await fetchUser();
    return response;
});

export const logout = createAsyncThunk('auth/logout', async () => {
    console.log("LOGOUT slice..")
    await logoutUser();
});

const authSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        resetLoginStatus: (state) => {
            state.status = 'idle';
            state.error = null;
            state.isLoginSuccess = false;
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(loginUser.pending, (state) => {
                state.status = 'loading';
                state.error = null;
            })
            .addCase(loginUser.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.isLoginSuccess = true;
                state.error = null;
            })
            .addCase(loginUser.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
                state.isLoginSuccess = false;
            })
            .addCase(fetchCurrentUser.pending, (state) => {
                state.fetch_status = 'loading';
            })
            .addCase(fetchCurrentUser.fulfilled, (state, action) => {
                state.fetch_status = 'succeeded';
                state.isLoginSuccess = true;
                state.user = action.payload;
                state.fetch_error = null;
                state.status = 'succeeded';
            })
            .addCase(fetchCurrentUser.rejected, (state, action) => {
                state.fetch_status = 'failed';
                state.user = null;
                state.isLoginSuccess = false;
                state.fetch_error = action.error.message;
            })
            .addCase(logout.fulfilled, (state) => {
                state.user = null;
                state.status = 'idle';
                state.isLoginSuccess = false;
                state.fetch_error = null;
                state.fetch_status = 'idle';
                state.error = null;
            });
    },
});

export const { resetLoginStatus } = authSlice.actions;
export default authSlice.reducer;
