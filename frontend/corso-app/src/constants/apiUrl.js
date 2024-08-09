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
