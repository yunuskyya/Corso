import { axiosInstance } from './interceptor'
import { ACCOUNT_URL_CREATE_ACCOUT, CUSTOMER_URL, CUSTOMER_URL_FILTER, CUSTOMER_URL_SOFT_DELETE } from '../constants/apiUrl'

export const createCustomer = async (customerRequest) => {

    if (customerRequest && customerRequest.customerType === 'KURUMSAL') {
        customerRequest.name = null;
        customerRequest.surname = null;
        customerRequest.tcKimlikNo = null;
    }

    if (customerRequest && customerRequest.customerType === 'BIREYSEL') {
        customerRequest.companyName = null;
        customerRequest.vkn = null
    }

    return axiosInstance.post(CUSTOMER_URL, customerRequest)
        .then((response) => {
            console.log("CREATE customer response: ", response);
            return response.data;
        }).catch((error) => {
            console.error(error);
            throw error;
        });

}

export const filterCustomersPaged = async (filterRequest, page = 0, size = 10, sort = 'id,asc') => {
    // Clean up the request based on the customer type
    let data_to_send;
    if (filterRequest) {
        data_to_send = {
            userId: filterRequest.userId,
            customerId: filterRequest.customerId,
            accountId: filterRequest.accountId,
            name: filterRequest.customerType === 'BIREYSEL' ? filterRequest.name + filterRequest.surname : filterRequest.customerName,
            tcKimlikNo: filterRequest.customerType === 'BIREYSEL' ? filterRequest.tcKimlikNo : null,
            vkn: filterRequest.customerType === 'BIREYSEL' ? null : filterRequest.vkn,
            customerType: filterRequest.customerType,
            status: filterRequest.status || null, // Set to null if empty
            currencyCode: filterRequest.currencyCode || null, // Set to null if empty
            phone: filterRequest.phone || null, // Set to null if empty
            email: filterRequest.email || null, // Set to null if empty
            dateStart: filterRequest.dateStart || null, // Set to null if empty
            dateEnd: filterRequest.dateEnd || null, // Set to null if empty
        };
    }

    // Prepare query params for pagination and sorting
    const queryParams = new URLSearchParams({
        page,
        size,
        sort,
    }).toString();

    try {
        // Send a POST request with filterRequest as the request body and pagination options in the query params
        const response = await axiosInstance.post(`${CUSTOMER_URL_FILTER}?${queryParams}`, data_to_send);

        console.log('FILTER customers response:', response);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};

export const createAccount = async (customerId, createAccountRequest) => {
    try {
        const response = await axiosInstance.post(ACCOUNT_URL_CREATE_ACCOUT.replace('{customerId}', customerId), createAccountRequest);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};

export const softDeleteCustomer = async (customerId, customer) => {
    console.log('softDeleteCustomer customerId:', customerId);
    console.log('softDeleteCustomer customer:', customer);
    try {
        const response = await axiosInstance.put(CUSTOMER_URL_SOFT_DELETE.replace('{customerId}', customerId), customer);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}