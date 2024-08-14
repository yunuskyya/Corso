import AddCustomerPage from '../pages/customer/AddCustomerPage';
import ListUser from '../pages/user/ListUser';
import ListCustomerPage from '../pages/customer/ListCustomerPage';
import ListAccountsForManagerPage from '../pages/account/ListAccountsForManagerPage';
import ActivateUser from '../pages/user/ActivateUser';
import ListTransactionHistoryPage from '../pages/transaction/ListTransactionHistoryPage';
import TransactionOperationsPage from '../pages/transaction/TransactionOperationsPage';
import ListCashFlowPage from '../pages/cashflow/ListCashFlowPage';
import EndOfDayPage from '../pages/endofday/EndOfDayPage';
import IbanPage from '../pages/iban/IbanPage';
import AddAccountPage from '../pages/account/AddAccountPage';
import AddCashPage from '../pages/cashflow/AddCashPage';
import SendCashPage from '../pages/cashflow/SendCashPage';
import AddUser from '../pages/user/AddUser';
import ChangePassword from '../pages/user/ChangePassword';
import AddBrokerPage from '../pages/broker/AddBrokerPage';
import ListBrokerPage from './../pages/broker/ListBrokerPage';
import ListAccountsForBrokerPage from './../pages/account/ListAccountsForBrokerPage';
import ListTransactionHistoryPageForManager  from './../pages/transaction/ListTransactionHistoryPageForManager';

// -------------- ADMIN --------------
export const ADMIN = '/admin';
export const ADMIN_DASHBOARD = '/admin/dashboard';

// dashboard operations

// User operations
export const ADMIN_ADD_USER = '/dashboard/admin/add-user'; // register broker or manager
export const ADMIN_LIST_BROKERS = '/dashboard/admin/list-user'; // user listeleniyor
export const ADMIN_ACTVATE_USER = '/dashboard/admin/activate-user'; // authorize broker or manager by email.
export const ADMIN_CHANGE_PASSWORD = '/dashboard/admin/change-password'

export const ADMIN_OPERATIONS = [
    {
        title: 'Kullanıcı İşlemleri',
        title_link: null,
        self_page: null,
        operations: [
            {
                title: 'Yönetici Ekle',
                link: ADMIN_ADD_USER,
                page: <AddUser />,
            },
            {
                title: 'Listele',
                link: ADMIN_LIST_BROKERS,
                page: <ListUser />,
            },
        ]
    },
    {
        title: 'Şifre Değiştir',
        title_link: ADMIN_CHANGE_PASSWORD,
        self_page: <ChangePassword />,
        operations: []
    }
];

// -------------- MANAGER --------------

export const MANAGER = '/manager';
export const MANAGER_DASHBOARD = '/manager/dashboard';

// dashboard operations
export const MANAGER_LIST_CUSTOMER = '/dashboard/manager/list-customer'; // list customers
export const MANAGER_LIST_ACCOUNTS = '/dashboard/manager/list-accounts'; // list accounts
export const MANAGER_TRANSACTION_HISTORY = '/dashboard/manager/list-transaction-history'; // list transaction history
export const MANAGER_LIST_CASH_FLOW = '/dashboard/manager/cash-flow'; // cash flow history
export const MANAGER_END_OF_DAY = '/dashboard/manager/end-of-day'; // end of day
export const MANAGER_CHANGE_PASSWORD = '/dashboard/manager/change-password';
export const MANAGER_ADD_BROKER = '/dashboard/manager/add-broker';
export const MANAGER_LIST_BROKER = '/dashboard/manager/list-broker';

export const MANAGER_OPERATIONS = [
    {
        title: 'Müşteri İşlemleri',
        title_link: null,
        self_page: null,
        operations: [
            {
                title: 'Müşteri İşlemleri',
                link: MANAGER_LIST_CUSTOMER,
                page: <ListCustomerPage />,
            },
        ]
    },
    {
        title: 'Hesap İşlemleri',
        title_link: null,
        self_page: null,
        operations: [
            {
                title: 'Hesap Listele',
                link: MANAGER_LIST_ACCOUNTS,
                page: <ListAccountsForManagerPage />,
            },
        ]
    },
    {
        title: 'Alım-Satım İşlemleri',
        title_link: null,
        self_page: null,
        operations: [
            {
                title: 'Geçmiş İşlemler',
                link: MANAGER_TRANSACTION_HISTORY,
                page: <ListTransactionHistoryPageForManager/>,
            },
        ]

    },
    {
        title: 'Personel İşlemleri',
        title_link: null,
        self_page: null,
        operations: [
            {
                title: 'Ekle',
                link: MANAGER_ADD_BROKER,
                page: <AddBrokerPage />,
            },
            {
                title: 'Listele',
                link: MANAGER_LIST_BROKER,
                page: <ListBrokerPage />
            }
        ]

    },
    {
        title: 'Nakit Akışı',
        title_link: MANAGER_LIST_CASH_FLOW,
        self_page: <ListCashFlowPage />,
        operations: []

    },
    {
        title: 'Gün Sonu',
        title_link: MANAGER_END_OF_DAY,
        self_page: <EndOfDayPage />,
        operations: []

    },
    {
        title: 'Şifre Değiştir',
        title_link: MANAGER_CHANGE_PASSWORD,
        self_page: <ChangePassword />,
        operations: []
    }
];

// ---------------- BROKER ----------------

export const BROKER = '/broker';
export const BROKER_DASHBOARD = '/broker/dashboard';

// dashboard operations
// musteri start
export const BROKER_ADD_CUSTOMER = '/dashboard/broker/add-customer'; // add customer
export const BROKER_LIST_CUSTOMER = '/dashboard/broker/list-customer'; // list customers
export const BROKER_IBAN = '/dashboard/broker/iban-operations'; // iban operations
// musteri end

// hesap start
export const BROKER_ADD_ACCOUNT = '/dashboard/broker/add-account'; // add account
export const BROKER_LIST_ACCOUNTS = '/dashboard/broker/list-accounts'; // list accounts
// hesap end

// transactions start
export const BROKER_TRANSACTION = '/dashboard/broker/transaction'; // transaction
export const BROKER_TRANSACTION_HISTORY = '/dashboard/broker/transaction-history'; // transaction history
// transactions end

// nakit akışı start
export const BROKER_LIST_CASH_FLOW = '/dashboard/broker/list-cashflow'; // cash flow
export const BROKER_ADD_CASH = '/dashboard/broker/add-cash'; // add cash
export const BROKER_SEND_CASH = '/dashboard/broker/send-cash'; // add cash
// nakit akışı end

export const BROKER_CHANGE_PASSWORD = '/dashboard/broker/change-password';

export const BROKER_OPERATIONS = [
    {
        title: 'Müşteri Yönetimi',
        title_link: null,
        self_page: null,
        operations: [
            {
                title: 'Müşteri Ekle',
                link: BROKER_ADD_CUSTOMER,
                page: <AddCustomerPage />,
            },
            {
                title: 'Müşteri İşlemleri',
                link: BROKER_LIST_CUSTOMER,
                page: <ListCustomerPage />,
            },
            // {
            //     title: 'IBAN İşlemleri',
            //     link: BROKER_IBAN,
            //     page: <IbanPage />,
            // }
        ]
    },
    {
        title: 'Hesap İşlemleri',
        title_link: null,
        self_page: null,
        operations: [
            {
                title: 'Hesap Ekle',
                link: BROKER_ADD_ACCOUNT,
                page: <AddAccountPage />,
            },
            {
                title: 'Hesap Listele',
                link: BROKER_LIST_ACCOUNTS,
                page: <ListAccountsForBrokerPage />,
            }
        ]
    },
    {
        title: 'Alım-Satım İşlemleri',
        title_link: null,
        self_page: null,
        operations: [
            {
                title: 'Alım-Satım',
                link: BROKER_TRANSACTION,
                page: <TransactionOperationsPage />,
            },
            {
                title: 'İşlem Geçmişi',
                link: BROKER_TRANSACTION_HISTORY,
                page: <ListTransactionHistoryPage />,
            }
        ]
    },
    {
        title: 'Nakit Akışı',
        title_link: null,
        self_page: null,
        operations: [
            {
                title: 'Listele',
                link: BROKER_LIST_CASH_FLOW,
                page: <ListCashFlowPage />,
            },
            {
                title: 'Para Yatır',
                link: BROKER_ADD_CASH,
                page: <AddCashPage />,
            },
            {
                title: 'Para Gönder',
                link: BROKER_SEND_CASH,
                page: <SendCashPage />,
            }
        ]
    },
    {
        title: 'Şifre Değiştir',
        title_link: BROKER_CHANGE_PASSWORD,
        self_page: <ChangePassword />,
        operations: []
    }
];