import axios from 'axios';
import { BASE_URL } from '../constants/apiUrl'

const instance = axios.create({
    baseURL: BASE_URL,
    withCredentials: true, // for cookies
});

instance.interceptors.response.use(
    
    (response) => {


        return response;
    },

    (error) => {

        return Promise.reject(error);
    }
);

export const axiosInstance = instance;