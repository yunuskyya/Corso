// base url:
export const BASE_URL = 'http://localhost:8080/api/v1';

// auth urls:
export const AUTH_URL = BASE_URL + '/auth';
export const LOGOUT_URL = AUTH_URL;

// user urls:
export const USER_URL = BASE_URL + '/user';
export const CURRENT_USER_URL = USER_URL + '/role';

// customer urls:

export const CUSTOMER_URL = BASE_URL + '/customer';
export const CUSTOMER_URL_LIST_FOR_BROKER = BASE_URL + '/customer/transaction/broker/{userId}';

// account urls:

export const ACCOUNT_URL = BASE_URL + '/accounts';
export const ACCOUNT_URL_CUSTOMER = BASE_URL + '/accounts/faketest';
export const ACCOUNT_URL_LIST_FOR_BROKER = BASE_URL + '/accounts/customer/transaction/{customerId}';

// currency urls:

export const CURRENCY_URL_COST = BASE_URL + '/currencies/cost';

// transaction urls:

export const TRANSACTION_URL_CREATE = BASE_URL + '/transaction/create';
export const TRANSACTION_URL_GET_ALL_BY_BROKER = BASE_URL + '/transaction/get-all/{userId}';

// money transfer urls:

export const MONEY_TRANSFER_CREATE = BASE_URL + '/money-transfer';


// iban urls:

export const IBAN_URL_FETCH_LIST_BY_CUSTOMER_ID = BASE_URL + '/iban/customer/{customerId}'