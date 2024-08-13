import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { 
    registerBroker as apiRegisterBroker, 
    registerManager as apiRegisterManager, 
    changePassword as apiChangePassword, 
    resetPassword as apiResetPassword, 
    activateUser as apiActivateUser, 
    deleteUser as apiDeleteUser, 
    userList as apiUserList, 
    unblockUser as apiUnblockUser,
    userListBroker as apiUserListBroker
} from '../api/userApi'; // API işlevlerinizi içe aktarın

// Async Thunks
export const registerBroker = createAsyncThunk(
    'user/registerBroker',
    async (brokerRequest, { rejectWithValue }) => {
        try {
            const response = await apiRegisterBroker(brokerRequest);
            return response;
        } catch (error) {
            return rejectWithValue(error.response.data);
        }
    }
);

export const registerManager = createAsyncThunk(
    'user/registerManager',
    async (managerRequest, { rejectWithValue }) => {
        try {
            const response = await apiRegisterManager(managerRequest);
            return response;
        } catch (error) {
            return rejectWithValue(error.response.data);
        }
    }
);

export const changePassword = createAsyncThunk(
    'user/changePassword',
    async (changePasswordRequest, { rejectWithValue }) => {
        try {
            const response = await apiChangePassword(changePasswordRequest);
            return response;
        } catch (error) {
            return rejectWithValue(error.response.data);
        }
    }
);

export const resetPassword = createAsyncThunk(
    'user/resetPassword',
    async (resetPasswordRequest, { rejectWithValue }) => {
        try {
            const response = await apiResetPassword(resetPasswordRequest);
            return response;
        } catch (error) {
            return rejectWithValue(error.response.data);
        }
    }
);

export const activateUser = createAsyncThunk(
    'user/activateUser',
    async (activateUserRequest, { rejectWithValue }) => {
        try {
            const response = await apiActivateUser(activateUserRequest);
            return response;
        } catch (error) {
            return rejectWithValue(error.response.data);
        }
    }
);

export const deleteUser = createAsyncThunk(
    'user/deleteUser',
    async (deleteUserRequest, { rejectWithValue }) => {
        try {
            const response = await apiDeleteUser(deleteUserRequest);
            return response;
        } catch (error) {
            console.log('ASDJJKASJKDK');
            return rejectWithValue(error.response.data);
        }
    }
);

export const fetchUserList = createAsyncThunk(
    'user/fetchUserList',
    async (_, { rejectWithValue }) => {
        try {
            const response = await apiUserList();
            return response.content;
        } catch (error) {
            return rejectWithValue(error.response.data);
        }
    }
);

export const unblockUser = createAsyncThunk(
    'user/unblockUser',
    async (unblockUserRequest, { rejectWithValue }) => {
        try {
            const response = await apiUnblockUser(unblockUserRequest);
            return response;
        } catch (error) {
            return rejectWithValue(error.response.data);
        }
    }
);
export const userListBroker = createAsyncThunk(
    'user/userListBroker',
    async (_, { rejectWithValue }) => {
        console.log("XXXXXXXXX");
        try {
            const response = await apiUserListBroker();
            return response.content;
        } catch (error) {
            return rejectWithValue(error.response.data);
        }
    }
);

// Slice
const userSlice = createSlice({
    name: 'user',
    initialState: {
        userList: [],
        status: 'idle',
        error: null,
    },
    reducers: {},
    extraReducers: (builder) => {
        builder
            // Register Broker
            .addCase(registerBroker.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(registerBroker.fulfilled, (state) => {
                state.status = 'succeeded';
            })
            .addCase(registerBroker.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.payload;
            })
            // Register Manager
            .addCase(registerManager.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(registerManager.fulfilled, (state) => {
                state.status = 'succeeded';
            })
            .addCase(registerManager.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.payload;
            })
            // Change Password
            .addCase(changePassword.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(changePassword.fulfilled, (state) => {
                state.status = 'succeeded';
            })
            .addCase(changePassword.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.payload;
            })
            // Reset Password
            .addCase(resetPassword.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(resetPassword.fulfilled, (state) => {
                state.status = 'succeeded';
            })
            .addCase(resetPassword.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.payload;
            })
            // Activate User
            .addCase(activateUser.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(activateUser.fulfilled, (state) => {
                state.status = 'succeeded';
            })
            .addCase(activateUser.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.payload;
            })
            // Delete User
            .addCase(deleteUser.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(deleteUser.fulfilled, (state) => {
                state.status = 'succeeded';
            })
            .addCase(deleteUser.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.payload;
            })
            // Fetch User List
            .addCase(fetchUserList.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(fetchUserList.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.userList = action.payload;
            })
            .addCase(fetchUserList.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.payload;
            })
            // User List Broker
            .addCase(userListBroker.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(userListBroker.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.userList = action.payload;
            })
            .addCase(userListBroker.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.payload;
            })
            // Unblock User
            .addCase(unblockUser.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(unblockUser.fulfilled, (state) => {
                state.status = 'succeeded';
            })
            .addCase(unblockUser.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.payload;
            });
    }
});

export default userSlice.reducer;
