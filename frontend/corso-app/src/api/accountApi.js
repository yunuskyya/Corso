import { axiosInstance } from './interceptor'
import { ACCOUNT_URL, ACCOUNT_URL_CUSTOMER, ACCOUNT_URL_LIST_FOR_BROKER_BY_ID, ACCOUNT_URL_LIST_FOR_MANAGER } from '../constants/apiUrl'

export const createAccount = async (accountRequest) => {

    return axiosInstance.post(ACCOUNT_URL, {
        customerId: accountRequest.customerId,
        currencyType: accountRequest.currencyType,

    }).then((response) => {
        return response.data;
    }).catch((error) => {
        console.error(error);
        throw error;
    });

}

export const getAllAccountsSelectedCustomer = async (accountRequest) => {

    return axiosInstance.get(ACCOUNT_URL_CUSTOMER, {
        customerId: accountRequest.customerId,
        currencyType: accountRequest.currencyType,

    }).then((response) => {
        return response.data;
    }).catch((error) => {
        console.error(error);
        throw error;
    });

}

export const deleteAccount = async (accountRequest) => {

    return axiosInstance.delete(ACCOUNT_URL, {
        customerId: accountRequest.customerId,
        currencyType: accountRequest.currencyType,

    }).then((response) => {
        return response.data;
    }).catch((error) => {
        console.error(error);
        throw error;
    });

}

export const getAllAccounts = async () => {

    return axiosInstance.get(ACCOUNT_URL_LIST_FOR_MANAGER).then((response) => {
        return response.data;
    }).catch((error) => {
        console.error(error);
        throw error;
    });

}

export const getAllAccountsForBroker = async (userId) => {

    return axiosInstance.get(ACCOUNT_URL_LIST_FOR_BROKER_BY_ID.replace('{userId}', userId)).then((response) => {
        return response.data; s
    }).catch((error) => {
        console.error(error);
        throw error;
    });

}

