import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { createMoneyTransfer} from '../api/moneyTransferApi';
import { fetchCustomerList as fetchCustomerListApi } from '../api/transactionApi';
import { fetchIbanListByCustomer as fetchIbanListByCustomerApi} from '../api/moneyTransferApi';

const initialState = {
    transferStatus: 'idle',
    error: null,
    customerError: null,
    successMessage: null,
    ibanList: [],
    customers:[],
    ibanStatus: 'idle',
    customerStatus: 'idle'
};

// Para transferi oluşturma thunk'ı
export const createMoneyTransferThunk = createAsyncThunk(
    'moneyTransfer/createMoneyTransfer',
    async (moneyTransferRequest, { rejectWithValue }) => {
        try {
            const response = await moneyTransferApi.createMoneyTransfer(moneyTransferRequest);
            return response;
        } catch (error) {
            return rejectWithValue(error.response.data);
        }
    }
);

//NOT***
export const fetchCustomerListForMoneyTransferThunk = createAsyncThunk('moneyTransfer/fetchCustomerList', async (userId) => {
    const response = await fetchCustomerListApi(userId);
    return response;
});


// IBAN listesi çekme thunk'ı
export const fetchIbanListByCustomerThunk = createAsyncThunk(
    'moneyTransfer/fetchIbanListByCustomer',
    async (customerId, { rejectWithValue }) => {
        try {
            const response = await moneyTransferApi.fetchIbanListByCustomerApi(customerId);
            console.log("slice içi method denemesi erkal **: "+response);
            return response.data;
        } catch (error) {
            return rejectWithValue(error.response.data);
        }
    }
);

const moneyTransferSlice = createSlice({
    name: 'moneyTransfer',
    initialState,
    reducers: {
        resetTransferStatus: (state) => {
            state.transferStatus = 'idle';
            state.error = null;
            state.successMessage = null;
        },
        resetIbanStatus: (state) => {
            state.ibanStatus = 'idle';
            state.error = null;
        },
    },
    extraReducers: (builder) => {
        builder

            .addCase(fetchCustomerListForMoneyTransferThunk.pending, (state) => {
                state.customerStatus = 'loading';
                state.customerError= null;
            })
            .addCase(fetchCustomerListForMoneyTransferThunk.fulfilled, (state, action) => {
                state.customerStatus = 'succeeded';
                state.customers = action.payload;
                state.customerError = null;
            })
            .addCase(fetchCustomerListForMoneyTransferThunk.rejected, (state, action) => {
                state.customerStatus = 'failed';
                state.customerError = action.error.message;
            })
            .addCase(createMoneyTransferThunk.pending, (state) => {
                state.transferStatus = 'loading';
                state.error = null;
                state.successMessage = null;
            })
            .addCase(createMoneyTransferThunk.fulfilled, (state, action) => {
                state.transferStatus = 'succeeded';
                state.successMessage = 'Transfer başarıyla gerçekleştirildi!';
                state.error = null;
            })
            .addCase(createMoneyTransferThunk.rejected, (state, action) => {
                state.transferStatus = 'failed';
                state.error = action.payload || action.error.message;
            })
            .addCase(fetchIbanListByCustomerThunk.pending, (state) => {
                state.ibanStatus = 'loading';
                state.error = null;
            })
            .addCase(fetchIbanListByCustomerThunk.fulfilled, (state, action) => {
                state.ibanStatus = 'succeeded';
                state.ibanList = action.payload; // Gelen IBAN listesini state'e ekliyoruz
                state.error = null;
            })
            .addCase(fetchIbanListByCustomerThunk.rejected, (state, action) => {
                state.ibanStatus = 'failed';
                state.error = action.payload || action.error.message;
            });
    },
});

export const { resetTransferStatus, resetIbanStatus } = moneyTransferSlice.actions;
export default moneyTransferSlice.reducer;
