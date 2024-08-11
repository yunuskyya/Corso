import {axiosInstance} from './interceptor'
import { CUSTOMER_URL_LIST_FOR_BROKER } from '../constants/apiUrl';
import { ACCOUNT_URL_LIST_FOR_BROKER } from '../constants/apiUrl';




export const fetchCustomerList = async (userId) => {
    try {
        console.log("deneme : : : "+CUSTOMER_URL_LIST_FOR_BROKER.replace('{userId}', parseInt(userId)));
        const response = await axiosInstance.get(CUSTOMER_URL_LIST_FOR_BROKER.replace('{userId}', parseInt(userId)));
        return response.data; // Bu, CustomerByBrokerResponseTransactionPage sınıfının listesi olacak.
    } catch (error) {
        console.error('Error fetching customer list:', error);
        throw error;
    }
};

export const fetchAccountsForCustomerBalanceHigherThanZero = async (customerId) => {
    try {
        const response = await axiosInstance.get(ACCOUNT_URL_LIST_FOR_BROKER.replace('{customerId}', customerId));
        return response.data; // Bu, GetCustomerAccountsForTransactionPage sınıfının listesi olacak.
    } catch (error) {
        console.error('Error fetching accounts for customer:', error);
        throw error;
    }
};