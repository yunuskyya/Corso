import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/hooks';
import { fetchCustomerListThunk, fetchAccountsForCustomerThunk } from '../../features/transactionSlice'; // Doğru importlar
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
  const {user}=useAuth();

  const dispatch = useAppDispatch();
  const customers = useAppSelector((state) => state.transaction.customers);
  const accounts = useAppSelector((state) => state.transaction.accounts);
  const status = useAppSelector((state) => state.transaction.status);

  useEffect(() => {
    // Örneğin userId'yi buraya uygun bir şekilde eklemelisiniz
    dispatch(fetchCustomerListThunk(user.id));
  }, [dispatch]);

  useEffect(() => {
    if (selectedCustomer) {
      dispatch(fetchAccountsForCustomerThunk(selectedCustomer));
    }
  }, [selectedCustomer, dispatch]);

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

    // "Alınacak Döviz Tipi" listesini filtrele
  const filteredCurrencies = currencies.filter(
    (currency) => currency.code !== selectedSellCurrency
  );

  const handleBuyCurrencyChange = (event) => {
    setSelectedBuyCurrency(event.target.value);
  };

  const handleAmountChange = (event) => {
    setAmount(event.target.value);
    setConfirmDisabled(false);
  };

  const calculateConversion = () => {
    // Burada dönüşüm oranını ayarlayabilirsin
    return amount ? (amount * 1.1).toFixed(2) : '';
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
        <label htmlFor="buyCurrencySelect" style={styles.label}>Select Currency to Buy</label>
        <select className="form-select" id="buyCurrencySelect" value={selectedBuyCurrency} onChange={handleBuyCurrencyChange} disabled={isBuyCurrencyDisabled} style={styles.select}>
         <option value="" disabled>Select a currency</option>
           {filteredCurrencies.map((currency) => (
         <option key={currency.code} value={currency.code}>{currency.code}</option>
            ))}
       </select>
      </div>

      {/* Miktar Girişi */}
      <div style={styles.formGroup}>
        <label htmlFor="amountInput" style={styles.label}>Amount</label>
        <input type="number" className="form-control" id="amountInput" placeholder="Enter amount" value={amount} onChange={handleAmountChange} disabled={isAmountInputDisabled} style={styles.input} />
      </div>

      {/* Önizleme Alanı */}
      <div style={styles.transactionPreview}>
        <div style={styles.previewContent}>
          <h5 style={styles.previewTitle}>Transaction Preview</h5>
          <p style={styles.previewText}>Selling: {selectedSellCurrency ? `${selectedSellCurrency} ${amount}` : 'None'}</p>
          <p style={styles.previewText}>Buying: {selectedBuyCurrency ? `${selectedBuyCurrency} ${calculateConversion()}` : 'None'}</p>
          <button className="btn btn-primary" disabled={isConfirmDisabled}>Confirm Transaction</button>
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
