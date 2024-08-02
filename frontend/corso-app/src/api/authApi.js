import {axiosInstance} from './interceptor'
import { AUTH_URL, LOGOUT_URL } from '../constants/apiUrl'

export const authenicate = async (username, password) => {
    
    const response = await axiosInstance.post(AUTH_URL, {
        username: username,
        password: password
    });

    return response.data;

}

export const logoutUser = async () => {
    const response = await axios.delete(LOGOUT_URL);
    return response.data;
};