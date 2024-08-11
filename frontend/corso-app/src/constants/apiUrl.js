// base url:
export const BASE_URL = 'http://localhost:8080/api/v1';

// auth urls:
export const AUTH_URL = BASE_URL + '/auth';
export const LOGOUT_URL = AUTH_URL;

// user urls:
export const USER_URL = BASE_URL + '/user';
export const CURRENT_USER_URL = USER_URL + '/role';

// customer urls:

export const CUSTOMER_URL = BASE_URL + 'customer';

// account urls:

export const ACCOUNT_URL = BASE_URL + 'accounts';
export const ACCOUNT_URL_CUSTOMER = BASE_URL + 'accounts/faketest';

// user urls:
export const USER_REGISTER_BROKER_URL = USER_URL + '/register/broker';  // Broker kaydı
export const USER_REGISTER_MANAGER_URL = USER_URL + '/register/manager'; // Manager kaydı
export const USER_CHANGE_PASSWORD_URL = USER_URL + '/change-password';   // Şifre değişikliği
export const USER_RESET_PASSWORD_URL = USER_URL + '/reset-password';     // Şifre sıfırlama
export const USER_ACTIVATE_URL = USER_URL + '/activate';                 // Kullanıcı aktif etme
export const USER_UNBLOCK_URL = USER_URL + '/unBlock';                   // Kullanıcı blok kaldırma
export const USER_DELETE_URL = USER_URL + '/{id}';
export const USER_LIST_URL = USER_URL + '/all';
