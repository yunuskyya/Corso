import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import { fetchCustomerListThunk, fetchAccountsForCustomerThunk, fetchCurrencyCostThunk } from '../../features/transactionSlice'; 
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
  const [selectedAccountBalance, setSelectedAccountBalance] = useState(null);
  const [maxAmount, setMaxAmount] = useState(null); 


  const { user } = useAuth();
  const dispatch = useAppDispatch();
  const customers = useAppSelector((state) => state.transaction.customers);
  const accounts = useAppSelector((state) => state.transaction.accounts);
  const maxBuying = useAppSelector((state) => state.transaction.maxBuying);
  const currencyStatus = useAppSelector((state) => state.transaction.currencyStatus);

  useEffect(() => {
    dispatch(fetchCustomerListThunk(user.id));
  }, [dispatch, user.id]);

  useEffect(() => {
    if (selectedCustomer) {
      dispatch(fetchAccountsForCustomerThunk(selectedCustomer));
    }
  }, [selectedCustomer, dispatch]);

  useEffect(() => {
    if (selectedSellCurrency && selectedBuyCurrency) {
      dispatch(fetchCurrencyCostThunk({
        purchasedCurrencyCode: selectedBuyCurrency,
        soldCurrencyCode: selectedSellCurrency,
        selectedAccountBalance
      }));
    }
  }, [selectedSellCurrency, selectedBuyCurrency, selectedAccountBalance, dispatch]);
  

  useEffect(() => {
    if (selectedSellCurrency && accounts.length > 0) {
      const selectedAccount = accounts.find(account => account.currency === selectedSellCurrency);
      if (selectedAccount) {
        setSelectedAccountBalance(selectedAccount.balance);
      } else {
        setSelectedAccountBalance(null);
      }
    }
  }, [selectedSellCurrency, accounts]);

  useEffect(() => {
    if (maxBuying) {
        setMaxAmount(maxBuying.maxBuying); // maxBuying değerini kullan
    }
  }, [maxBuying]);

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
        amount: parseFloat(amountValue),
        selectedAccountBalance: selectedAccountBalance
      }));
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

      {/* Bakiye ve Maksimum Alınabilir Döviz Alanları */}
      <div style={styles.transactionPreview}>
        <div style={styles.previewContent}>
          <div style={styles.balanceContainer}>
            <div style={styles.balanceBox}>
              <p style={styles.balanceText}>Hesap Bakiyesi:</p>
              <p style={styles.balanceValue}>{selectedAccountBalance ? selectedAccountBalance.toFixed(2) : 'Bilgi Yok'}</p>
            </div>
            <div style={styles.maxAmountBox}>
              <p style={styles.maxAmountText}>Maks. Alınabilir Döviz Miktarı:</p>
              <p style={styles.maxAmountValue}>{maxBuying !== null ? maxBuying.toFixed(2) : 'Bilgi Yok'}</p>
            </div>
          </div>
          <h5 style={styles.previewTitle}>Maliyet</h5>
          <p style={styles.previewText}>{currencyStatus === 'loading' ? 'Hesaplanıyor...' : `Maliyet: ${maxBuying ? maxBuying.toFixed(2) : 'Bilgi Yok'}`}</p>
          <button className="btn btn-primary" disabled={isConfirmDisabled}>İşlemi Onayla</button>
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
  },
  balanceContainer: {
    display: 'flex',
    justifyContent: 'space-between',
    marginBottom: '1rem'
  },
  balanceBox: {
    backgroundColor: '#f1f1f1',
    padding: '0.5rem',
    borderRadius: '4px',
    flex: 1
  },
  balanceText: {
    fontSize: '0.875rem',
    color: '#555'
  },
  balanceValue: {
    fontSize: '1rem',
    fontWeight: 'bold'
  },
  maxAmountBox: {
    backgroundColor: '#e0f7fa',
    padding: '0.5rem',
    borderRadius: '4px',
    flex: 1
  },
  maxAmountText: {
    fontSize: '0.875rem',
    color: '#00796b'
  },
  maxAmountValue: {
    fontSize: '1rem',
    fontWeight: 'bold'
  }
};

export default TransactionOperationsPage;
