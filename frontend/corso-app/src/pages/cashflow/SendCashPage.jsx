import React, { useEffect, useState } from 'react';
import useAuth from '../../hooks/useAuth';
import { fetchCustomerList } from '../../api/transactionApi';
import { fetchIbanListByCustomer, createMoneyTransfer } from '../../api/moneyTransferApi';

const SendCashPage = () => {
    const { user } = useAuth();
    const [selectedCustomer, setSelectedCustomer] = useState('');
    const [selectedIban, setSelectedIban] = useState('');
    const [amount, setAmount] = useState('');
    const [currencyType, setCurrencyType] = useState('');
    const [customerList, setCustomerList] = useState([]);
    const [ibanList, setIbanList] = useState([]);
    const [alertMessage, setAlertMessage] = useState('');
    const [alertType, setAlertType] = useState('');

    // Müşteri listesini API'den çek
    useEffect(() => {
        const getCustomerList = async () => {
            try {
                const response = await fetchCustomerList(user.id);
                setCustomerList(response.content);  // API'den gelen müşteri listesini set ediyoruz
            } catch (error) {
                console.error('Error fetching customer list:', error);
            }
        };
        getCustomerList();
    }, [user.id]);

    // Seçilen müşteri değiştiğinde IBAN listesini çek
    useEffect(() => {
        if (selectedCustomer) {
            const getIbanList = async () => {
                try {
                    const response = await fetchIbanListByCustomer(selectedCustomer);
                    setIbanList(response);  // API'den gelen IBAN listesini set ediyoruz
                } catch (error) {
                    console.error('Error fetching IBAN list:', error);
                }
            };
            getIbanList();
        } else {
            setIbanList([]);  // Müşteri seçilmediğinde IBAN listesini sıfırla
        }
    }, [selectedCustomer]);

    const handleCustomerChange = (e) => {
        setSelectedCustomer(e.target.value);
        setSelectedIban(''); // IBAN'ı sıfırla
        setCurrencyType(''); // Döviz türünü sıfırla
    };

    const handleIbanChange = (e) => {
        const iban = e.target.value;
        setSelectedIban(iban);

        const selectedIbanDetails = ibanList.find((item) => item.iban === iban);
        if (selectedIbanDetails) {
            setCurrencyType(selectedIbanDetails.currencyCode);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (parseFloat(amount) >= 1000000000) {
            setAlertMessage('Girilen miktar 1 milyarı geçemez.');
            setAlertType('danger');
            return;
        }
        const transferRequest = {
            customer_id: selectedCustomer,
            currencyCode: currencyType,
            amount: parseFloat(amount),
            receiver: selectedIban,
            sender: null 
        };
        try {
            const response = await createMoneyTransfer(transferRequest);
            console.log('Transfer successful:', response);
            // Success handling here
            setAlertMessage('Para gönderme işlemi başarılı!');
            setAlertType('success');
        } catch (error) {
            console.error('Error creating money transfer:', error);
            // Error handling here
            setAlertMessage('Para gönderme sırasında bir hata oluştu.');
            setAlertType('danger');
        }
    };


    const handleReset = () => {
        setSelectedCustomer('');
        setSelectedIban('');
        setAmount('');
        setCurrencyType('');
        setAlertMessage('');
        setAlertType('');
    };

    return (
        <div style={styles.container}>
            <h2 style={styles.header}>Para Gönder</h2>
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
                        {customerList?.map((customer) => (
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
                        <option value="">IBAN Seçiniz</option>
                        {ibanList.map((iban) => (
                            <option key={iban.id} value={iban.iban}>
                                {iban.ibanName} {iban.iban}
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

                {/* Hata/Success Mesajı */}
                {alertMessage && (
                    <div className={`alert alert-${alertType}`} role="alert">
                        {alertMessage}
                    </div>
                )}

                {/* Gönder ve Temizle Butonları */}
                <button
                    type="submit"
                    className="btn btn-primary"
                    style={styles.button}
                >
                    Onayla
                </button>
                <button
                    type="button"
                    className="btn btn-secondary"
                    style={styles.button}
                    onClick={handleReset}
                >
                    Temizle
                </button>
            </form>
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
    }
};

export default SendCashPage;
