import { axiosInstance } from './interceptor';
import { MONEY_TRANSFER_CREATE, IBAN_URL_FETCH_LIST_BY_CUSTOMER_ID } from '../constants/apiUrl';


export const moneyTransferApi = {
    createMoneyTransfer: async ({ customer_id, currencyCode, ibanNo, amount, receiver, sender }) => {
        try {
            const response = await axiosInstance.post(MONEY_TRANSFER_CREATE, {
                customer_id,
                currencyCode,
                ibanNo,
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
    }
};

export const fetchIbanListByCustomer = async (customerId) => {
    try {
        const response = await axiosInstance.get(IBAN_URL_FETCH_LIST_BY_CUSTOMER_ID.replace('{customerId}', customerId));
        return response.data;
    } catch (error) {
        console.error('Error fetching IBAN list:', error);
        throw error;
    }
};