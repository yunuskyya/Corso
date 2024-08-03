import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { login, fetchUser, logoutUser } from '../api/authApi';

const initialState = {
    user: null,
    status: 'idle',
    error: null,
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
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(loginUser.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(loginUser.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.user = action.payload.user;
                state.isLoginSuccess = true;
            })
            .addCase(loginUser.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
                state.isLoginSuccess = false;
            })
            .addCase(fetchCurrentUser.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(fetchCurrentUser.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.user = action.payload;
            })
            .addCase(fetchCurrentUser.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            })
            .addCase(logout.fulfilled, (state) => {
                state.user = null;
                state.status = 'idle';
                state.isLoginSuccess = false;
            });
    },
});

export default authSlice.reducer;
