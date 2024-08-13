import { axiosInstance } from './interceptor';
import { 
    USER_REGISTER_BROKER_URL, 
    USER_REGISTER_MANAGER_URL, 
    USER_CHANGE_PASSWORD_URL, 
    USER_RESET_PASSWORD_URL, 
    USER_ACTIVATE_URL, 
    USER_UNBLOCK_URL ,
    USER_DELETE_URL,
    USER_LIST_URL,
    USER_LIST_BROKER_URL
} from '../constants/apiUrl';

// Broker kullanıcısı oluşturma
export const registerBroker = async (brokerRequest) => {
    return axiosInstance.post(USER_REGISTER_BROKER_URL, {
        username: brokerRequest.username,
        firstName: brokerRequest.firstName,
        lastName: brokerRequest.lastName,
        email: brokerRequest.email,
        phone: brokerRequest.phone
    }).then(response => {
        return response.data;
    }).catch(error => {
        console.error(error);
        throw error;
    });
}

// Manager kullanıcısı oluşturma
export const registerManager = async (managerRequest) => {
    return axiosInstance.post(USER_REGISTER_MANAGER_URL, {
        username: managerRequest.username,
        firstName: managerRequest.firstName,
        lastName: managerRequest.lastName,
        email: managerRequest.email,
        phone: managerRequest.phone,
        password: managerRequest.password // Manager kayıt işlemi için şifre gerekli
    }).then(response => {
        return response.data;
    }).catch(error => {
        console.error(error);
        throw error;
    });
}

// Kullanıcı şifresini değiştirme
export const changePassword = async (changePasswordRequest) => {
    return axiosInstance.put(USER_CHANGE_PASSWORD_URL, {
        email: changePasswordRequest.email,
        oldPassword: changePasswordRequest.oldPassword,
        newPassword: changePasswordRequest.newPassword
    }).then(response => {
        return response.data;
    }).catch(error => {
        console.error(error);
        throw error;
    });
}

// Şifre sıfırlama talebi
export const resetPassword = async (resetPasswordRequest) => {
    return axiosInstance.put(USER_RESET_PASSWORD_URL, {
        email: resetPasswordRequest.email
    }).then(response => {
        return response.data;
    }).catch(error => {
        console.error(error);
        throw error;
    });
}

// Kullanıcıyı aktifleştirme
export const activateUser = async (activateUserRequest) => {
    return axiosInstance.put(USER_ACTIVATE_URL, {
        email: activateUserRequest.email
    }).then(response => {
        return response.data;
    }).catch(error => {
        console.error(error);
        throw error;
    });
}
// Kullanıcı silme
export const deleteUser = async (deleteUserRequest) => {
    return axiosInstance.delete(`${USER_DELETE_URL}/${deleteUserRequest.userId}`)
        .then(response => {
            return response.data;
        })
        .catch(error => {
            console.error(error);
            throw error;
        });
};

// Kullaıcı listesi
export const userList = async () => {
    return axiosInstance.get(USER_LIST_URL).then(response => {
        return response.data;
    }).catch(error => {
        console.error(error);
        throw error;
    });
}

// Kullanıcı blok kaldırma
export const unblockUser = async (unblockUserRequest) => {
    return axiosInstance.put(USER_UNBLOCK_URL, {
        email: unblockUserRequest.email
    }).then(response => {
        return response.data;
    }).catch(error => {
        console.error(error);
        throw error;
    });
    
}
export const userListBroker = async () => {
    console.log("userListBroker");
    return axiosInstance.get(USER_LIST_BROKER_URL).then(response => {
        return response.data;
    }).catch(error => {
        console.error(error);
        throw error;
    });
}
