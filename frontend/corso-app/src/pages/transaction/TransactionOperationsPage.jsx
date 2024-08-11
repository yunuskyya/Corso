import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import { fetchCustomerListThunk, fetchAccountsForCustomerThunk, fetchCurrencyCostThunk, createTransactionThunk } from '../../features/transactionSlice'; // Doğru importlar
import { currencies } from '../../constants/currencies';
import useAuth from '../../hooks/useAuth';

const TransactionOperationsPage = () => {
  const [selectedCustomer, setSelectedCustomer] = useState('');
  const [selectedSellCurrency, setSelectedSellCurrency] = useState('');
  const [selectedBuyCurrency, setSelectedBuyCurrency] = useState('');
  const [amount, setAmount] = useState('');
  const [isSellCurrencyDisabled, setSellCurrencyDisabled] = useState(true);
  const [isBuyCurrencyDisabled, setBuyCurrencyDisabled] = useState(true);
  const [isAmountInputDisabled, setAmountInputDisabled] = useState(true);
  const [isConfirmDisabled, setConfirmDisabled] = useState(true);
  const { user } = useAuth();
  const dispatch = useAppDispatch();

  const customers = useAppSelector((state) => state.transaction.customers);
  const accounts = useAppSelector((state) => state.transaction.accounts);
  const currencyCost = useAppSelector((state) => state.transaction.currencyCost);
  const currencyStatus = useAppSelector((state) => state.transaction.currencyStatus);
  const transactionStatus = useAppSelector((state) => state.transaction.transactionStatus);

  useEffect(() => {
    dispatch(fetchCustomerListThunk(user.id));
  }, [dispatch, user.id]);

  useEffect(() => {
    if (selectedCustomer) {
      dispatch(fetchAccountsForCustomerThunk(selectedCustomer));
    }
  }, [selectedCustomer, dispatch]);

  useEffect(() => {
    if (amount && selectedSellCurrency && selectedBuyCurrency) {
      dispatch(fetchCurrencyCostThunk({
        purchasedCurrencyCode: selectedSellCurrency,
        soldCurrencyCode: selectedBuyCurrency,
        amount: parseFloat(amount),
      }));
    }
  }, [amount, selectedSellCurrency, selectedBuyCurrency, dispatch]);

  const handleCustomerChange = (event) => {
    setSelectedCustomer(event.target.value);
    setSellCurrencyDisabled(false);
  };

  const handleSellCurrencyChange = (event) => {
    const selectedCurrency = event.target.value;
    setSelectedSellCurrency(selectedCurrency);
    setBuyCurrencyDisabled(false);
    setAmountInputDisabled(false);
  };

  const filteredCurrencies = currencies.filter(
    (currency) => currency.code !== selectedSellCurrency
  );

  const handleBuyCurrencyChange = (event) => {
    setSelectedBuyCurrency(event.target.value);
  };

  const handleAmountChange = (event) => {
    const amountValue = event.target.value;
    setAmount(amountValue);
    setConfirmDisabled(false);

    if (amountValue && selectedSellCurrency && selectedBuyCurrency) {
        dispatch(fetchCurrencyCostThunk({
            purchasedCurrencyCode: selectedBuyCurrency,
            soldCurrencyCode: selectedSellCurrency,
            amount: parseFloat(amountValue) 
        }));
    }
  };

  const handleConfirmClick = () => {
    if (selectedCustomer && selectedSellCurrency && selectedBuyCurrency && amount) {
      // Seçilen döviz türüne göre hesabı bul
      const selectedAccount = accounts.find(account => account.currency === selectedSellCurrency);
      console.log(selectedAccount);

      if (selectedAccount) {
        const account_id = selectedAccount.id;
        dispatch(createTransactionThunk({
          account_id,
          purchasedCurrency: selectedBuyCurrency,
          soldCurrency: selectedSellCurrency,
          amount: parseFloat(amount),
          user_id: user.id
        }));
      } else {
        console.error('Hesap bulunamadı!');
      }
    } else {
      console.error('Tüm alanları doldurmanız gerekiyor!');
    }
  };

  return (
    <div style={styles.container}>
      {/* Müşteri Seçimi */}
      <div style={styles.formGroup}>
        <label htmlFor="customerSelect" style={styles.label}>Müşteri Seçiniz</label>
        <select className="form-select" id="customerSelect" value={selectedCustomer} onChange={handleCustomerChange} style={styles.select}>
          <option value="" disabled>Bir Müşteri Seçiniz</option>
          {customers?.content?.map((customer) => (
            <option key={customer.id} value={customer.id}>{customer.name} {customer.surname}</option>
          ))}
        </select>
      </div>

      {/* Satılacak Döviz Seçimi */}
      <div style={styles.formGroup}>
        <label htmlFor="sellCurrencySelect" style={styles.label}>Satılacak Döviz Tipini Seçiniz</label>
        <select className="form-select" id="sellCurrencySelect" value={selectedSellCurrency} onChange={handleSellCurrencyChange} disabled={isSellCurrencyDisabled} style={styles.select}>
          <option value="" disabled>Döviz Tipi Seçiniz</option>
          {accounts.map((account) => (
            <option key={account.id} value={account.currency}>{account.currency}</option>
          ))}
        </select>
      </div>

      {/* Alınacak Döviz Seçimi */}
      <div style={styles.formGroup}>
        <label htmlFor="buyCurrencySelect" style={styles.label}>Alınacak Döviz Tipini Seçiniz</label>
        <select className="form-select" id="buyCurrencySelect" value={selectedBuyCurrency} onChange={handleBuyCurrencyChange} disabled={isBuyCurrencyDisabled} style={styles.select}>
          <option value="" disabled>Döviz Tipi Seçiniz</option>
          {filteredCurrencies.map((currency) => (
            <option key={currency.code} value={currency.code}>{currency.code}</option>
          ))}
        </select>
      </div>

      {/* Miktar Girişi */}
      <div style={styles.formGroup}>
        <label htmlFor="amountInput" style={styles.label}>Miktar</label>
        <input type="number" className="form-control" id="amountInput" placeholder="Miktar girin" value={amount} onChange={handleAmountChange} disabled={isAmountInputDisabled} style={styles.input} />
      </div>

      {/* Maliyet Alanı */}
      <div style={styles.transactionPreview}>
        <div style={styles.previewContent}>
          <h5 style={styles.previewTitle}>Maliyet</h5>
          <p style={styles.previewText}>{currencyStatus === 'loading' ? 'Hesaplanıyor...' : `Maliyet: ${currencyCost ? currencyCost.toFixed(2) : 'Bilgi Yok'}`}</p>
          <button className="btn btn-primary" disabled={isConfirmDisabled || transactionStatus === 'loading'} onClick={handleConfirmClick}>
            {transactionStatus === 'loading' ? 'İşlem Yapılıyor...' : 'İşlemi Onayla'}
          </button>
        </div>
      </div>
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
  formGroup: {
    marginBottom: '1rem',
    width: '100%',
    maxWidth: '400px'
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
  transactionPreview: {
    display: 'flex',
    justifyContent: 'center',
    marginTop: '1rem'
  },
  previewContent: {
    backgroundColor: '#ffffff',
    padding: '1rem',
    borderRadius: '8px',
    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
    width: '100%',
    maxWidth: '400px',
    textAlign: 'center'
  },
  previewTitle: {
    marginBottom: '0.5rem'
  },
  previewText: {
    margin: '0.5rem 0'
  }
};

export default TransactionOperationsPage;
