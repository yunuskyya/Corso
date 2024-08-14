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
export const CUSTOMER_URL_FILTER = CUSTOMER_URL + '/filter'
export const CUSTOMER_URL_SOFT_DELETE = CUSTOMER_URL + '/{customerId}';

// account urls:

export const ACCOUNT_URL = BASE_URL + '/accounts';
export const ACCOUNT_URL_CUSTOMER = BASE_URL + '/accounts/faketest';
export const ACCOUNT_URL_LIST_FOR_BROKER = BASE_URL + '/accounts/customer/transaction/{customerId}';
export const ACCOUNT_URL_CREATE_ACCOUT = ACCOUNT_URL + '/{customerId}';
export const ACCOUNT_URL_LIST_FOR_MANAGER = BASE_URL + '/accounts';
export const ACCOUNT_URL_LIST_FOR_BROKER_BY_ID = ACCOUNT_URL + '/broker/{userId}';

// currency urls:

// user urls:
export const USER_REGISTER_BROKER_URL = USER_URL + '/register/broker';  // Broker kaydı
export const USER_REGISTER_MANAGER_URL = USER_URL + '/register/manager'; // Manager kaydı
export const USER_CHANGE_PASSWORD_URL = USER_URL + '/change-password';   // Şifre değişikliği
export const USER_RESET_PASSWORD_URL = USER_URL + '/reset-password';     // Şifre sıfırlama
export const USER_ACTIVATE_URL = USER_URL + '/activate';                 // Kullanıcı aktif etme
export const USER_UNBLOCK_URL = USER_URL + '/unBlock';                   // Kullanıcı blok kaldırma
export const USER_DELETE_URL = USER_URL + '';
export const USER_LIST_URL = USER_URL + '/all';
export const USER_LIST_BROKER_URL = USER_URL + '/allBrokers';

export const CURRENCY_URL_COST = BASE_URL + '/currencies/cost';

// transaction urls:

export const TRANSACTION_URL_CREATE = BASE_URL + '/transaction/create';
export const TRANSACTION_URL_GET_ALL_BY_BROKER = BASE_URL + '/transaction/get-all/{userId}';
export const TRANSACTION_URL_GET_ALL_FOR_CUSTOMER = BASE_URL + '/transaction/get-all';

// money transfer urls:

export const MONEY_TRANSFER_CREATE = BASE_URL + '/money-transfer';
export const MONEY_TRANSFER_FILTRED_LIST = BASE_URL + '/money-transfer/get-filtered';

// iban urls:

export const IBAN_URL_FETCH_LIST_BY_CUSTOMER_ID = BASE_URL + '/iban/customer/{customerId}';

// end of day - reports ursl:

export const END_OF_DAY_CLOSE_DAY = BASE_URL + '/system-date/close-day';
export const END_OF_DAY_START_CLOSE_DAY = BASE_URL + '/system-date/start-close-day';
export const END_OF_DAY_IS_DAY_CLOSED = BASE_URL + '/system-date/is-day-close-started';
export const END_OF_DAY_IS_DAY_CLOSE_STARTED = BASE_URL + '/system-date/is-day-close-started';
export const REPORTS_MONEY_TRANSFER_FETCH_ALL_EXCEL = BASE_URL + '/reports/export-money-transfers/excel';
export const REPORTS_MONEY_TRANSFER_FETCH_ALL_PDF = BASE_URL + '/reports/export-money-transfers/pdf';
export const REPORTS_CUSTOMERS_FETCH_ALL_EXCEL = BASE_URL + '/reports/export-customers/excel';
export const REPORTS_CUSTOMERS_TRANSFER_FETCH_ALL_PDF = BASE_URL + '/reports/export-customers/pdf';
export const REPORTS_ACCOUNTS_FETCH_ALL_EXCEL = BASE_URL + '/reports/export-accounts/excel';
export const REPORTS_ACCOUNTS_TRANSFER_FETCH_ALL_PDF = BASE_URL + '/reports/export-accounts/pdf';
export const REPORTS_TRANSACTIONS_FETCH_ALL_EXCEL = BASE_URL + '/reports/export-transactions/excel';
export const REPORTS_TRANSACTIONS_TRANSFER_FETCH_ALL_PDF = BASE_URL + '/reports/export-transactions/pdf';
export const GET_SYSTEM_DATE = BASE_URL + '/system-date/';

