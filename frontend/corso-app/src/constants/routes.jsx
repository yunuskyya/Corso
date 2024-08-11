import AddCustomerPage from '../pages/customer/AddCustomerPage';
import ListUser from '../pages/user/ListUser';
import ListCustomerPage from '../pages/customer/ListCustomerPage';
import ListAccountPage from '../pages/account/ListAccountPage';
import ActivateUser from '../pages/user/ActivateUser';
import ListTransactionHistoryPage from '../pages/transaction/ListTransactionHistoryPage';
import TransactionOperationsPage from '../pages/transaction/TransactionOperationsPage';
import ListCashFlowPage from '../pages/cashflow/ListCashFlowPage';
import EndOfDayPage from '../pages/endofday/EndOfDayPage';
import IbanPage from '../pages/iban/IbanPage';
import AddAccountPage from '../pages/account/AddAccountPage';
import AddCashPage from '../pages/cashflow/AddCashPage';
import SendCashPage from '../pages/cashflow/SendCashPage';

// -------------- ADMIN --------------
export const ADMIN = '/admin';
export const ADMIN_DASHBOARD = '/admin/dashboard';

// dashboard operations

// User operations
export const ADMIN_ADD_USER = '/dashboard/admin/add-user'; // register broker or manager
export const ADMIN_LIST_BROKERS = '/dashboard/admin/list-user'; // user listeleniyor
export const ADMIN_ACTVATE_USER = '/dashboard/admin/activate-user'; // authorize broker or manager by email.

export const ADMIN_OPERATIONS = [
    {
        title: 'Kullanıcı İşlemleri',
        title_link: null,
        operations: [
            {
                title: 'Kullanıcı Eke',
                link: ADMIN_ADD_USER,
                page: <AddUser />,
            },
            {
                title: 'Broker Listele',
                link: ADMIN_LIST_BROKERS,
                page: <ListUser />,
            },
            {
                title: 'Kullanıcı Yetkilendir',
                link: ADMIN_UPDATE_USER,
                page: <ActivateUser />,
            }
        ]
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

export const MANAGER_OPERATIONS = [
    {
        title: 'Müşteri İşlemleri',
        title_link: null,
        self_page: null,
        operations: [
            {
                title: 'Müşteri Listele',
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
                page: <ListAccountPage />,
            },
        ]
    },
    {
        title: 'Alım-Satım İşlemleri',
        title_link: null,
        self_page: null,
        operations: [
            {
                title: 'Gemiş İşlemler',
                link: MANAGER_TRANSACTION_HISTORY,
                page: <ListTransactionPage />,
            },
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

export const BROKER_OPERATIONS = [
    {
        title: 'Müşteri İşlemleri',
        title_link: null,
        operations: [
            {
                title: 'Müşteri Ekle',
                link: BROKER_ADD_CUSTOMER,
                page: <AddCustomerPage />,
            },
            {
                title: 'Müşteri Listele',
                link: BROKER_LIST_CUSTOMER,
                page: <ListCustomerPage />,
            },
            {
                title: 'IBAN İşlemleri',
                link: BROKER_IBAN,
                page: <IbanPage />,
            }
        ]
    },
    {
        title: 'Hesap İşlemleri',
        title_link: null,
        operations: [
            {
                title: 'Hesap Ekle',
                link: BROKER_ADD_ACCOUNT,
                page: <AddAccountPage />,
            },
            {
                title: 'Hesap Listele',
                link: BROKER_LIST_ACCOUNTS,
                page: <ListAccountPage />,
            }
        ]
    },
    {
        title: 'Alım-Satım İşlemleri',
        title_link: null,
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
        operations: [
            {
                title: 'Listele',
                link: BROKER_LIST_CASH_FLOW,
                page: <ListCashFlowPage />,
            },
            {
                title: 'Nakit Ekle',
                link: BROKER_ADD_CASH,
                page: <AddCashPage />,
            },
            {
                title: 'Nakit Gönder',
                link: BROKER_SEND_CASH,
                page: <SendCashPage />,
            }
        ]
    }
];