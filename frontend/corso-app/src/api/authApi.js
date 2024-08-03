import {axiosInstance} from './interceptor'
import { AUTH_URL, CURRENT_USER_URL, LOGOUT_URL } from '../constants/apiUrl'

export const login = async (credentials) => {
    
    return axiosInstance.post(AUTH_URL, {
        email: credentials.email,
        password: credentials.password
    }).then((response) => {
        return response.data;
    }).catch((error) => {
        console.error(error);
        throw error;
    });

}

export const fetchUser = async () => { // role kullanılmıyor
    // const response = await axiosInstance.get(`${AUTH_URL}/currentUser?${role}`);
    // return response.data;
    return axiosInstance.get(CURRENT_USER_URL)
    .then((response) => {
        return response.data;
    })
    .catch((error) => {
        console.error(error);
        throw new Error(error.response.data);
    });
}

export const logoutUser = async () => {
    const response = await axiosInstance.delete(LOGOUT_URL);
    return response.data;
};