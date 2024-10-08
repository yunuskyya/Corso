import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import { fetchCustomerListThunk, fetchAccountsForCustomerThunk, fetchCurrencyCostThunk, createTransactionThunk, resetCreateTransactionStatus, resetMaxBuying } from '../../features/transactionSlice';
import { currencies } from '../../constants/currencies';
import { GeneralSpinner } from './../../components/Common/GeneralSpinner';
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
  const [exchangeRate, setExchangeRate] = useState(null);
  const [rate, setRate] = useState(null);
  const [cost, setCost] = useState(null);
  const [alertMessage, setAlertMessage] = useState('');
  const [alertType, setAlertType] = useState('success');
  const [showAlert, setShowAlert] = useState(false); // Uyarı için state

  const { user } = useAuth();
  const dispatch = useAppDispatch();
  const createTransactionStatus = useAppSelector((state) => state.transaction.transactionStatus);
  const createTransactionError = useAppSelector((state) => state.transaction.createErrorTransaction);
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

  const handleResetForm = () => {
    dispatch(resetCreateTransactionStatus());
    dispatch(resetMaxBuying());
    setRate(null);
    setCost(0);
    setSelectedAccountBalance('');
    setAmount(0);
    setSelectedBuyCurrency('');
    setSelectedSellCurrency('');
    setSelectedCustomer('');
    setAmountInputDisabled(true);
    setBuyCurrencyDisabled(true);
    setConfirmDisabled(true);
    setSellCurrencyDisabled(true);
  };


  useEffect(() => {
    if (selectedAccountBalance !== null && maxAmount !== null) {
      const rate = selectedAccountBalance / maxAmount;
      setExchangeRate(rate);
    }
  }, [selectedAccountBalance, maxAmount]);


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
      setMaxAmount(maxBuying.maxBuying);
    }
  }, [maxBuying]);


  //DÖVİZ KURU HESAPLAMASI
  useEffect(() => {
    console.log('selectedAccountBalance:', selectedAccountBalance);
    console.log('maxBuying:', maxBuying);

    if (selectedAccountBalance !== null && maxBuying !== null && maxBuying !== 0) {
      const computedRate = selectedAccountBalance / maxBuying;
      console.log('Computed Rate:', computedRate); // Rate hesaplamasını logla
      setRate(computedRate);
    } else {
      setRate(null);
    }
  }, [selectedAccountBalance, maxBuying]);

  //MALİYET HESABI 
  useEffect(() => {
    if (rate !== null && amount) {
      const computedCost = rate * parseFloat(amount);
      console.log("maliyet hesabı usereffect içi : " + computedCost);
      setCost(computedCost);
    } else {
      setCost(null);
    }
  }, [rate, amount]);

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

  const onConfirmTransaction = () => {
    const selectedAccount = accounts.find(account => account.currency === selectedSellCurrency);
    if (amount > maxBuying) {
      setAlertMessage('Girdiğiniz miktar maksimum alınabilir dövizi aşıyor.');
      setAlertType('warning');
      setShowAlert(true);
      return alert;
    }
    if (selectedAccount && selectedBuyCurrency && selectedSellCurrency && amount) {
      dispatch(createTransactionThunk({
        account_id: selectedAccount.id,
        purchasedCurrency: selectedBuyCurrency,
        soldCurrency: selectedSellCurrency,
        amount: parseFloat(amount),
        user_id: user.id,
      }))
    }
  };

  return (
    <div style={styles.container}>
      {createTransactionStatus === 'succeeded' && <div className='bg-success'>
        <div>{"İşlem Tamamlandı"} <button className='m-1 p-1 rounded' onClick={handleResetForm}>Tamam</button></div>
      </div>}
      {createTransactionStatus === 'failed' && <div className='bg-warning'>
        <div>{`Hata: ${createTransactionError}`} <button className='m-1 p-1 rounded' onClick={handleResetForm}>Kapat</button></div>
      </div>}
      {createTransactionStatus === 'loading' && <GeneralSpinner />}
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
            <div style={styles.rateBox}>
              <p style={styles.rateText}>Kur Fiyatı:</p>
              <p style={styles.rateValue}>{rate !== null ? rate.toFixed(2) : 'Bilgi Yok'}</p>
            </div>
            <div style={styles.costBox}>
              <p style={styles.costText}>Maliyet:</p>
              <p style={styles.costValue}>{cost !== null ? cost.toFixed(2) : 'Bilgi Yok'}</p>
            </div>
          </div>
          <div style={styles.buttonContainer}>
            <button className="btn btn-primary m-1" onClick={onConfirmTransaction} disabled={isConfirmDisabled}>İşlemi Onayla</button>
            <button className="btn btn-secondary m-1" onClick={handleResetForm}>Temizle</button>
          </div>

          {showAlert && <div className={`alert alert-${alertType} ${styles.alert}`} role="alert">
            {alertMessage}
            <button className='btn btn-primary m-1' onClick={() => setShowAlert(false)}>Tamam</button>
          </div>}
        
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
    flexDirection: 'column',
    alignItems: 'center',
    marginTop: '1rem',
    width: '100%',
  },
  previewContent: {
    backgroundColor: '#ffffff',
    padding: '1rem',
    borderRadius: '8px',
    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
    width: '100%',
    maxWidth: '600px',
    textAlign: 'center',
  },
  balanceContainer: {
    display: 'flex',
    justifyContent: 'space-between',
    marginBottom: '1rem',
    width: '100%',
  },
  balanceBox: {
    backgroundColor: '#f1f1f1',
    padding: '0.5rem',
    borderRadius: '4px',
    flex: 1,
    marginRight: '0.5rem',
    textAlign: 'center', // Metni ortalar
  },
  maxAmountBox: {
    backgroundColor: '#e0f7fa',
    padding: '0.5rem',
    borderRadius: '4px',
    flex: 1,
    marginRight: '0.5rem',
    textAlign: 'center', // Metni ortalar
  },
  rateBox: {
    backgroundColor: '#f1f1f1',
    padding: '0.5rem',
    borderRadius: '4px',
    flex: 1,
    textAlign: 'center', // Metni ortalar
    marginRight: '0.5rem'
  },
  balanceText: {
    fontSize: '0.875rem',
    color: '#555',
    margin: '0',
  },
  balanceValue: {
    fontSize: '1rem',
    fontWeight: 'bold',
    margin: '0',
  },
  maxAmountText: {
    fontSize: '0.875rem',
    color: '#00796b',
    margin: '0',
  },
  maxAmountValue: {
    fontSize: '1rem',
    fontWeight: 'bold',
    margin: '0',
  },
  rateText: {
    fontSize: '0.875rem',
    color: '#555',
    margin: '0',
  },
  rateValue: {
    fontSize: '1rem',
    fontWeight: 'bold',
    margin: '0',
  },
  confirmButton: {
    marginTop: '1rem',
  },
  costBox: {
    backgroundColor: '#f1f1f1',
    padding: '0.5rem',
    borderRadius: '4px',
    flex: 1,
    textAlign: 'center',
  },
  costText: {
    fontSize: '0.875rem',
    color: '#555',
    margin: '0',
  },
  costValue: {
    fontSize: '1rem',
    fontWeight: 'bold',
    margin: '0',
  }
};

export default TransactionOperationsPage;
