import { axiosInstance } from './interceptor';
import { MONEY_TRANSFER_CREATE, IBAN_URL_FETCH_LIST_BY_CUSTOMER_ID, MONEY_TRANSFER_FILTRED_LIST } from '../constants/apiUrl';



export const createMoneyTransfer = async ({ customer_id, currencyCode, amount, receiver, sender }) => {
    try {
        const response = await axiosInstance.post(MONEY_TRANSFER_CREATE, {
            customer_id,
            currencyCode,
            amount,
            receiver,
            sender
        }, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return response.data;
    } catch (error) {
        console.error('Error creating money transfer:', error);
        throw error;
    }
};

export const fetchFilteredMoneyTransferList = async ({ customerId, startDate, endDate, currencyCode, direction }) => {
    try {
        const response = await axiosInstance.post(MONEY_TRANSFER_FILTRED_LIST, {
            customerId,
            startDate,
            endDate,
            currencyCode,
            direction,
        }, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching filtered money transfer list:', error);
        throw error;
    }
};


export const fetchIbanListByCustomer = async (customerId) => {
    try {
        const response = await axiosInstance.get(IBAN_URL_FETCH_LIST_BY_CUSTOMER_ID.replace('{customerId}', customerId));
        console.log('useEffect method içi erkal : ', JSON.stringify(response, null, 2));
        return response.data;
    } catch (error) {
        console.error('Error fetching IBAN list:', error);
        throw error;
    }
};

