import React, { useCallback, useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import { fetchIbanListByCustomerThunk, createMoneyTransferThunk, fetchCustomerListForMoneyTransferThunk } from '../../features/moneyTransferSlice';

import useAuth from '../../hooks/useAuth';
import { fetchCustomerList } from '../../api/transactionApi';

const AddCashPage = () => {
    const dispatch = useAppDispatch();
    const { user } = useAuth();
    const [selectedCustomer, setSelectedCustomer] = useState('');
    const [selectedIban, setSelectedIban] = useState('');
    const [amount, setAmount] = useState('');
    const [currencyType, setCurrencyType] = useState('');
    const [customerList, setCustomerList] = useState({});

    const { customers, ibanList, transferStatus, successMessage, error } = useAppSelector((state) => state.moneyTransfer);

    /* useEffect(() => {
         
         dispatch(fetchCustomerListForMoneyTransferThunk(user.id));
     }, [dispatch, user.id]);
 */

    const getCustomerList = useCallback(async (page) => {
        try {
            const response = await fetchCustomerList(user.id);
            setCustomerList(response);
            console.log("getCustomerList methodu içi: " + response);
        } catch (error) {
            //toast.error("Hata,",error);
        }
    }, [user.id]);

    useEffect(() => {
        getCustomerList();
    }, [getCustomerList]);

    useEffect(() => {
        if (selectedCustomer) {
            // Seçilen müşteri değiştiğinde IBAN listesini çek
            dispatch(fetchIbanListByCustomerThunk(selectedCustomer));
        }
    }, [selectedCustomer, dispatch]);

    const handleCustomerChange = (e) => {
        setSelectedCustomer(e.target.value);
        setSelectedIban(''); // IBAN'ı sıfırla
        setCurrencyType(''); // Döviz türünü sıfırla
    };

    const handleIbanChange = (e) => {
        const iban = e.target.value;
        setSelectedIban(iban);
        // Seçilen IBAN'ın döviz türünü ayarla
        const selectedIbanDetails = ibanList.find((item) => item.ibanNo === iban);
        if (selectedIbanDetails) {
            setCurrencyType(selectedIbanDetails.currencyCode);
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const transferRequest = {
            customer_id: selectedCustomer,
            ibanNo: selectedIban,
            amount: parseFloat(amount),
        };
        dispatch(createMoneyTransferThunk(transferRequest));
    };

    return (
        <div style={styles.container}>
            <h2 style={styles.header}>Add Cash</h2>
            <form onSubmit={handleSubmit} style={styles.form}>
                {/* Müşteri Seçimi */}
                <div style={styles.formGroup}>
                    <label htmlFor="customerSelect" style={styles.label}>Customer</label>
                    <select
                        id="customerSelect"
                        value={selectedCustomer}
                        onChange={handleCustomerChange}
                        style={styles.select}
                        required
                    >
                        <option value="">Müşteri Seçiniz</option>
                        {
                            customerList?.content?.map((customer) => (
                                <option key={customer.id} value={customer.id}>
                                    {customer.name} {customer.surname}
                                </option>
                            ))}

                    </select>
                </div>

                {/* IBAN Seçimi */}
                <div style={styles.formGroup}>
                    <label htmlFor="ibanSelect" style={styles.label}>IBAN</label>
                    <select
                        id="ibanSelect"
                        value={selectedIban}
                        onChange={handleIbanChange}
                        style={styles.select}
                        required
                    >
                        <option value="">Select IBAN</option>
                        {ibanList.map((iban) => (
                            <option key={iban.ibanNo} value={iban.ibanNo}>
                                {iban.ibanNo}
                            </option>
                        ))}
                    </select>
                </div>

                {/* Döviz Türü */}
                <div style={styles.formGroup}>
                    <label htmlFor="currencyType" style={styles.label}>Currency Type</label>
                    <input
                        type="text"
                        id="currencyType"
                        value={currencyType}
                        readOnly
                        style={styles.input}
                    />
                </div>

                {/* Miktar Girişi */}
                <div style={styles.formGroup}>
                    <label htmlFor="amount" style={styles.label}>Amount</label>
                    <input
                        type="number"
                        id="amount"
                        step="0.01"
                        min="0"
                        value={amount}
                        onChange={(e) => setAmount(e.target.value)}
                        style={styles.input}
                        required
                    />
                </div>

                {/* Gönder Butonu */}
                <button
                    type="submit"
                    className="btn btn-primary"
                    style={styles.button}
                    disabled={transferStatus === 'loading'}
                >
                    {transferStatus === 'loading' ? 'Submitting...' : 'Submit'}
                </button>
            </form>

            {successMessage && <div style={styles.alertSuccess}>{successMessage}</div>}
            {error && <div style={styles.alertError}>{error}</div>}
        </div>
    );
};

// CSS-in-JS stiller
const styles = {
    container: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        marginTop: '2rem'
    },
    header: {
        marginBottom: '1.5rem',
        textAlign: 'center'
    },
    form: {
        width: '100%',
        maxWidth: '500px'
    },
    formGroup: {
        marginBottom: '1rem'
    },
    label: {
        display: 'block',
        marginBottom: '0.5rem'
    },
    select: {
        width: '100%'
    },
    input: {
        width: '100%'
    },
    button: {
        width: '100%',
        marginTop: '1rem'
    },
    alertSuccess: {
        color: 'green',
        marginTop: '1rem'
    },
    alertError: {
        color: 'red',
        marginTop: '1rem'
    }
};

export default AddCashPage;
