import { axiosInstance } from './interceptor'
import { CUSTOMER_URL } from '../constants/apiUrl'

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