import {axiosInstance} from './interceptor'
import { CUSTOMER_URL } from '../constants/apiUrl'

export const createCustomer = async (customerRequest) => {
    
    return axiosInstance.post(CUSTOMER_URL, {
        name: customerRequest.name,
        surname: customerRequest.surname,
        customerType: customerRequest.type,
        tcKimlikNo: customerRequest.tcKimlikNo,
        companyName: customerRequest.companyName,
        vkn: customerRequest.vkn,
        phone: customerRequest.phone,
        email:customerRequest.email,
        userId: customerRequest.userId
    }).then((response) => {
        return response.data;
    }).catch((error) => {
        console.error(error);
        throw error;
    });

}