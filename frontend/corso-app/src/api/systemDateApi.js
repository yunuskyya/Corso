import { axiosInstance } from "./interceptor";

export const getSystemDate = async () => {
    try {
        const response = await axiosInstance.get('/system-date');
        return response.data;
    } catch (error) {
        console.error('Error fetching system date:', error);
        throw error;
    }
};
